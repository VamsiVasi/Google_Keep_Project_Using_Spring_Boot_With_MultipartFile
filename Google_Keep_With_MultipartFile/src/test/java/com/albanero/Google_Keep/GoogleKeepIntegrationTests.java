package com.albanero.Google_Keep;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.albanero.Google_Keep.Model.Notes_Keep;
import com.albanero.Google_Keep.Model.User_Keep;
import com.albanero.Google_Keep.Repository.Notes_Keep_Repository;
import com.albanero.Google_Keep.Repository.User_Keep_Repository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoogleKeepIntegrationTests implements Constants{

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private User_Keep_Repository user_Keep_Repo;

	@Autowired
	private Notes_Keep_Repository notes_Keep_Repo;

	ObjectMapper om = new ObjectMapper();

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void createUserKeep() throws Exception {
		MockMultipartFile file1 = new MockMultipartFile("file", "Java%session.txt", "text/plain",
				"Session on Java".getBytes());
		ArrayList<String> NOTES_IDs = new ArrayList<String>();
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		byte[] FILE1 = file1.getBytes();
		Notes_Keep notes_Keep = new Notes_Keep(NOTES_NAME1, NOTES1, FILE1, URL1);
		notes_Keep_Repo.save(notes_Keep);
		NOTES_IDs.add(notes_Keep.getId());
		String jsonRequest = om.writeValueAsString(user_Keep);
		MvcResult result = mockMvc.perform(
				post("/keep/create/user-notes").content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isInternalServerError()).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		System.out.println("Create user and note : " + resultContent);
		notes_Keep_Repo.deleteAll();
		user_Keep_Repo.deleteAll();
	}

	@Test
	public void createNotesKeepByUserName() throws Exception {
		MockMultipartFile file1 = new MockMultipartFile("file", "Java%session.txt", "text/plain",
				"Session on Java".getBytes());
		MockMultipartFile file2 = new MockMultipartFile("file", "Spring%session.txt", "text/plain",
				"Session on Spring".getBytes());
		ArrayList<String> NOTES_IDs = new ArrayList<String>();
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		byte[] FILE1 = file1.getBytes();
		Notes_Keep notes1 = new Notes_Keep(NOTES_NAME1, NOTES1, FILE1, URL1);
		notes_Keep_Repo.save(notes1);
		NOTES_IDs.add(notes1.getId());
		user_Keep_Repo.save(user_Keep);
		String userName = USERNAME;
		Optional<User_Keep> optionalUserKeep = user_Keep_Repo.findByUserName(userName);
		User_Keep user_Keep1 = optionalUserKeep.get();
		ArrayList<String> ua = user_Keep.getNotesIds();
		byte[] FILE2 = file2.getBytes();
		Notes_Keep notes2 = new Notes_Keep(NOTES_NAME2, NOTES2, FILE2, URL2);
		ua.add(notes2.getId());
		user_Keep_Repo.save(user_Keep1);
		String jsonRequest = om.writeValueAsString(notes2);
		MvcResult result = mockMvc.perform(post("/keep/create/notes/" + userName).content(jsonRequest)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isInternalServerError()).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		System.out.println("Create notes by userName : " + resultContent);
		notes_Keep_Repo.deleteAll();
		user_Keep_Repo.deleteAll();
	}

	@Test
	public void getAllUserKeep() throws Exception {
		MockMultipartFile file1 = new MockMultipartFile("file", "Java%session.txt", "text/plain",
				"Session on Java".getBytes());
		ArrayList<String> NOTES_IDs = new ArrayList<String>();
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		byte[] FILE1 = file1.getBytes();
		Notes_Keep notes1 = new Notes_Keep(NOTES_NAME1, NOTES1, FILE1, URL1);
		notes_Keep_Repo.save(notes1);
		NOTES_IDs.add(notes1.getId());
		user_Keep_Repo.save(user_Keep);
		MvcResult result = mockMvc.perform(get("/keep/get/user-notes").content(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		System.out.println("Get all users : " + resultContent);
		notes_Keep_Repo.deleteAll();
		user_Keep_Repo.deleteAll();
	}

	@Test
	public void getUserKeepByUserName() throws Exception {
		MockMultipartFile file1 = new MockMultipartFile("file", "Java%session.txt", "text/plain",
				"Session on Java".getBytes());
		ArrayList<String> NOTES_IDs = new ArrayList<String>();
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		byte[] FILE1 = file1.getBytes();
		Notes_Keep notes1 = new Notes_Keep(NOTES_NAME1, NOTES1, FILE1, URL1);
		notes_Keep_Repo.save(notes1);
		NOTES_IDs.add(notes1.getId());
		user_Keep_Repo.save(user_Keep);
		String userName =USERNAME;
		MvcResult result = mockMvc
				.perform(get("/keep/get/user-notes/" + userName).content(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		System.out.println("Get user details by userName : " + resultContent);
		notes_Keep_Repo.deleteAll();
		user_Keep_Repo.deleteAll();
	}

	@Test
	public void getAllNotesKeepByName() throws Exception {
		MockMultipartFile file1 = new MockMultipartFile("file", "Java%session.txt", "text/plain",
				"Session on Java".getBytes());
		ArrayList<String> NOTES_IDs = new ArrayList<String>();
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		byte[] FILE1 = file1.getBytes();
		Notes_Keep notes1 = new Notes_Keep(NOTES_NAME1, NOTES1, FILE1, URL1);
		notes_Keep_Repo.save(notes1);
		NOTES_IDs.add(notes1.getId());
		user_Keep_Repo.save(user_Keep);
			String userName = USERNAME;
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.get("/keep/get/notes/" + userName).content(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		System.out.println("Get all notes of the user by userName : " + resultContent);
		notes_Keep_Repo.deleteAll();
		user_Keep_Repo.deleteAll();
	}

	@Test
	public void getNotesKeepByName() throws Exception {
		MockMultipartFile file1 = new MockMultipartFile("file", "Java%session.txt", "text/plain",
				"Session on Java".getBytes());
		ArrayList<String> NOTES_IDs = new ArrayList<String>();
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		byte[] FILE1 = file1.getBytes();
		Notes_Keep notes1 = new Notes_Keep(NOTES_NAME1, NOTES1, FILE1, URL1);
		notes_Keep_Repo.save(notes1);
		NOTES_IDs.add(notes1.getId());
		user_Keep_Repo.save(user_Keep);
		String userName = USERNAME, name = NOTES_NAME1;
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/keep/get/notes/" + userName + "/" + name)
				.content(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		System.out.println("Get a particular notes of the user by notes name : " + resultContent);
		notes_Keep_Repo.deleteAll();
		user_Keep_Repo.deleteAll();
	}

	@Test
	public void updateUserKeepByUserName() throws Exception {
		MockMultipartFile file1 = new MockMultipartFile("file", "Java%session.txt", "text/plain",
				"Session on Java".getBytes());
		ArrayList<String> NOTES_IDs = new ArrayList<String>();
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		byte[] FILE1 = file1.getBytes();
		Notes_Keep notes1 = new Notes_Keep(NOTES_NAME1, NOTES1, FILE1, URL1);
		notes_Keep_Repo.save(notes1);
		NOTES_IDs.add(notes1.getId());
		user_Keep_Repo.save(user_Keep);
		String userName = USERNAME;
		user_Keep.setPassword(UPDATED_PASSWORD);
		String jsonRequest = om.writeValueAsString(user_Keep);
		MvcResult result = mockMvc.perform(put("/keep/put/userdetails/" + userName).content(jsonRequest)
				.contentType(MediaType.MULTIPART_FORM_DATA)).andExpect(status().isInternalServerError()).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		System.out.println("Update user details by userName : " + resultContent);
		notes_Keep_Repo.deleteAll();
		user_Keep_Repo.deleteAll();
	}

	@Test
	public void updateNotesKeepByName() throws Exception {
		MockMultipartFile file1 = new MockMultipartFile("file", "Java%session.txt", "text/plain",
				"Session on Java".getBytes());
		MockMultipartFile updateFile = new MockMultipartFile("file", "Spring%Boot%session.txt", "text/plain",
				"Session on Spring Boot".getBytes());
		ArrayList<String> NOTES_IDs = new ArrayList<String>();
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		byte[] FILE1 = file1.getBytes();
		byte[] UPDATED_FILE = updateFile.getBytes();
		Notes_Keep notes1 = new Notes_Keep(NOTES_NAME1, NOTES1, FILE1, URL1);
		notes_Keep_Repo.save(notes1);
		NOTES_IDs.add(notes1.getId());
		user_Keep_Repo.save(user_Keep);
		String userName = USERNAME, name = NOTES_NAME1;
		notes1.setName(NOTES_NAME1);
		notes1.setNotes(UPDATED_NOTES);
		notes1.setFile(UPDATED_FILE);
		notes1.setFileURL(UPDATED_URL);
		String jsonRequest = om.writeValueAsString(notes1);
		MvcResult result = mockMvc.perform(put("/keep/put/notesdetails/" + userName + "/" + name).content(jsonRequest)
				.contentType(MediaType.MULTIPART_FORM_DATA)).andExpect(status().isInternalServerError()).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		System.out.println("Update a particular notes of the user by notes name : " + resultContent);
		notes_Keep_Repo.deleteAll();
		user_Keep_Repo.deleteAll();
	}

	@Test
	public void deleteNotesKeepByUserName() throws Exception {
		MockMultipartFile file1 = new MockMultipartFile("file", "Java%session.txt", "text/plain",
				"Session on Java".getBytes());
		ArrayList<String> NOTES_IDs = new ArrayList<String>();
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		byte[] FILE1 = file1.getBytes();
		Notes_Keep notes1 = new Notes_Keep(NOTES_NAME1, NOTES1, FILE1, URL1);
		notes_Keep_Repo.save(notes1);
		NOTES_IDs.add(notes1.getId());
		user_Keep_Repo.save(user_Keep);
		String userName = USERNAME, name = NOTES_NAME1;
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
				.delete("/keep/delete/notesdetails/" + userName + "/" + name).content(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		System.out.println("Delete a particular notes of the user by notes name : " + resultContent);
		notes_Keep_Repo.deleteAll();
		user_Keep_Repo.deleteAll();
	}

	@Test
	public void deleteUserKeepByUserName() throws Exception {
		MockMultipartFile file1 = new MockMultipartFile("file", "Java%session.txt", "text/plain",
				"Session on Java".getBytes());
		ArrayList<String> NOTES_IDs = new ArrayList<String>();
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		byte[] FILE1 = file1.getBytes();
		Notes_Keep notes1 = new Notes_Keep(NOTES_NAME1, NOTES1, FILE1, URL1);
		notes_Keep_Repo.save(notes1);
		NOTES_IDs.add(notes1.getId());
		user_Keep_Repo.save(user_Keep);
		String userName = USERNAME;
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/keep/delete/userdetails/" + userName)
				.content(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
		String resultContent = result.getResponse().getContentAsString();
		System.out.println("Delete user by userName and all the notes of that particular user : " + resultContent);
		notes_Keep_Repo.deleteAll();
		user_Keep_Repo.deleteAll();
	}

}
