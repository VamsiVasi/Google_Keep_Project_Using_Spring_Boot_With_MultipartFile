package com.albanero.Google_Keep;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import com.albanero.Google_Keep.Exceptions.ResourceNotFoundException;
import com.albanero.Google_Keep.Model.Notes_Keep;
import com.albanero.Google_Keep.Model.User_Keep;
import com.albanero.Google_Keep.Repository.Notes_Keep_Repository;
import com.albanero.Google_Keep.Repository.User_Keep_Repository;
import com.albanero.Google_Keep.Service.Keep_Service;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoogleKeepUnitTests implements Constants {

	@Autowired
	private Keep_Service keep_Service;

	@MockBean
	private User_Keep_Repository user_Keep_Repo;

	@MockBean
	private Notes_Keep_Repository notes_Keep_Repo;

	private ArrayList<String> NOTES_IDs;

	// GET API Test case for getting all users
	@Test
	public void getAllUserKeepTest() {
		NOTES_IDs = new ArrayList<String>();
		NOTES_IDs.add(OBJECT_ID1);
		NOTES_IDs.add(OBJECT_ID2);
		ArrayList<String> vd = new ArrayList<String>();
		vd.add(OBJECT_ID3);
		vd.add(OBJECT_ID4);
		when(user_Keep_Repo.findAll()).thenReturn(Stream
				.of(new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs),
						new User_Keep("Venky432", "venkatesh348@gmail.com", "Venky4345@", vd))
				.collect(Collectors.toList()));
		assertEquals(2, keep_Service.getUserKeep().size());
	}

	// GET API Test case for getting user details by existing userName
	@Test
	public void getUserKeepByUserName() throws ResourceNotFoundException {
		String userName = USERNAME;
		NOTES_IDs = new ArrayList<String>();
		NOTES_IDs.add(OBJECT_ID1);
		NOTES_IDs.add(OBJECT_ID2);
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.of(user_Keep));
		assertEquals(user_Keep, keep_Service.getUserKeepByName(userName));
	}

	// GET API Test case for getting user details by non-existing userName
	@Test
	public void getUserKeepByNonExistingUserName() {
		String userName = USERNAME;
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> keep_Service.getUserKeepByName(userName));
	}

	// GET API Test case for getting all notes of the user by existing userName
	@Test
	public void getAllNotesKeepByUserName() throws ResourceNotFoundException, IOException {
		MockMultipartFile file1 = new MockMultipartFile("file", "Java%session.txt", "text/plain",
				"Session on Java".getBytes());
		MockMultipartFile file2 = new MockMultipartFile("file", "Spring%session.txt", "text/plain",
				"Session on Spring".getBytes());
		String userName = USERNAME;
		NOTES_IDs = new ArrayList<String>();
		NOTES_IDs.add(OBJECT_ID1);
		NOTES_IDs.add(OBJECT_ID2);
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		byte[] FILE1 = file1.getBytes();
		byte[] FILE2 = file2.getBytes();
		Notes_Keep notes1 = new Notes_Keep(NOTES_NAME1, NOTES1, FILE1, URL1);
		Notes_Keep notes2 = new Notes_Keep(NOTES_NAME2, NOTES2, FILE2, URL2);
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.of(user_Keep));
		assertEquals(user_Keep, keep_Service.getUserKeepByName(userName));
		ArrayList<String> l = user_Keep.getNotesIds();
		ArrayList<Notes_Keep> allNotesOfUser = new ArrayList<Notes_Keep>();
		allNotesOfUser.add(notes1);
		allNotesOfUser.add(notes2);
		for (int i = 0; i < l.size(); i++) {
			when(notes_Keep_Repo.findById(l.get(i))).thenReturn(Optional.of(allNotesOfUser.get(i)));
		}
		assertEquals(allNotesOfUser, keep_Service.getAllNotesKeepByName(userName));
	}

	// GET API Test case for getting all notes of the user by non-existing userName
	@Test
	public void getAllNotesKeepByNonExistingUserName() {
		String userName = USERNAME;
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> keep_Service.getAllNotesKeepByName(userName));
	}

	// GET API Test case for getting a particular notes of the existing user by existing notes name
	@Test
	public void getNotesKeepByName() throws ResourceNotFoundException, IOException {
		String userName = USERNAME, name = NOTES_NAME1;
		MockMultipartFile file1 = new MockMultipartFile("file", "Java%session.txt", "text/plain",
				"Session on Java".getBytes());
		MockMultipartFile file2 = new MockMultipartFile("file", "Spring%session.txt", "text/plain",
				"Session on Spring".getBytes());
		NOTES_IDs = new ArrayList<String>();
		NOTES_IDs.add(OBJECT_ID1);
		NOTES_IDs.add(OBJECT_ID2);
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		byte[] FILE1 = file1.getBytes();
		byte[] FILE2 = file2.getBytes();
		Notes_Keep notes1 = new Notes_Keep(NOTES_NAME1, NOTES1, FILE1, URL1);
		Notes_Keep notes2 = new Notes_Keep(NOTES_NAME2, NOTES2, FILE2, URL2);
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.of(user_Keep));
		assertEquals(user_Keep, keep_Service.getUserKeepByName(userName));
		ArrayList<String> l = user_Keep.getNotesIds();
		ArrayList<Notes_Keep> allNotesOfUser = new ArrayList<Notes_Keep>();
		allNotesOfUser.add(notes1);
		allNotesOfUser.add(notes2);
		for (int i = 0; i < l.size(); i++) {
			when(notes_Keep_Repo.findById(l.get(i))).thenReturn(Optional.of(allNotesOfUser.get(i)));
		}
		assertEquals(allNotesOfUser, keep_Service.getAllNotesKeepByName(userName));
		for (int i = 0; i < allNotesOfUser.size(); i++) {
			String s1 = allNotesOfUser.get(i).getName();
			if (s1.equals(name)) {
				when(notes_Keep_Repo.findByName(name)).thenReturn(Optional.of(allNotesOfUser.get(i)));
			}
		}
		assertEquals(1, keep_Service.getNotesKeepByName(userName, name).size());
	}

	// GET API Test case for getting a particular notes of the non-existing user
	@Test
	public void getNotesKeepByNonExistingUserName() {
		String userName = USERNAME, name = NOTES_NAME1;
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> keep_Service.getNotesKeepByName(userName, name));
	}

	// POST API Test case for creating a user and note where user contains only the object id of the notes
	@Test
	public void createUserKeep() throws IOException {
		MockMultipartFile file1 = new MockMultipartFile("file", "Java%session.txt", "text/plain",
				"Session on Java".getBytes());
		NOTES_IDs = new ArrayList<String>();
		NOTES_IDs.add(OBJECT_ID1);
		byte[] FILE1 = file1.getBytes();
		Notes_Keep notes_Keep = new Notes_Keep(NOTES_NAME1, NOTES1, FILE1, URL1);
		notes_Keep_Repo.save(notes_Keep);
		verify(notes_Keep_Repo, times(1)).save(notes_Keep);
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		user_Keep_Repo.save(user_Keep);
		verify(user_Keep_Repo, times(1)).save(user_Keep);
	}

	// POST API Test case for creating a notes by existing userName
	@Test
	public void createNotesKeep() throws ResourceNotFoundException, IOException {
		MockMultipartFile file2 = new MockMultipartFile("file", "Spring%session.txt", "text/plain",
				"Session on Spring".getBytes());
		String userName = USERNAME;
		NOTES_IDs = new ArrayList<String>();
		NOTES_IDs.add(OBJECT_ID1);
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.of(user_Keep));
		assertEquals(user_Keep, keep_Service.getUserKeepByName(userName));
		ArrayList<String> ua = user_Keep.getNotesIds();
		byte[] FILE2 = file2.getBytes();
		Notes_Keep notes_Keep = new Notes_Keep(NOTES_NAME2, NOTES2, FILE2, URL2);
		when(notes_Keep_Repo.save(notes_Keep)).thenReturn(notes_Keep);
		ua.add(notes_Keep.getId());
		user_Keep.setNotesIds(ua);
		when(user_Keep_Repo.save(user_Keep)).thenReturn(user_Keep);
		assertEquals(user_Keep, keep_Service.createNotesKeep(file2, NOTES2, NOTES_NAME2, userName));
	}

	// POST API Test case for creating a notes by non-existing userName
	@Test
	public void createNotesKeepByNonExistingUserName() throws IOException {
		MockMultipartFile file2 = new MockMultipartFile("file", "Spring%session.txt", "text/plain",
				"Session on Spring".getBytes());
		String userName = USERNAME;
		byte[] FILE2 = file2.getBytes();
		Notes_Keep notes_Keep = new Notes_Keep(NOTES_NAME2, NOTES2, FILE2, URL2);
		when(notes_Keep_Repo.save(notes_Keep)).thenReturn(notes_Keep);
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class,
				() -> keep_Service.createNotesKeep(file2, NOTES2, NOTES_NAME2, userName));
	}

	// PUT API Test case for updating user details by existing userName
	@Test
	public void updateUserKeepByUserName() throws ResourceNotFoundException {
		String userName = USERNAME;
		NOTES_IDs = new ArrayList<String>();
		NOTES_IDs.add(OBJECT_ID1);
		NOTES_IDs.add(OBJECT_ID2);
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.of(user_Keep));
		assertEquals(user_Keep, keep_Service.getUserKeepByName(userName));
		user_Keep.setUserName(UPDATED_USERNAME);
		user_Keep.setPassword(UPDATED_PASSWORD);
		when(user_Keep_Repo.save(user_Keep)).thenReturn(user_Keep);
		assertEquals(user_Keep, keep_Service.updateUserKeep(UPDATED_USERNAME, UPDATED_PASSWORD, userName));
	}

	// PUT API Test case for updating user details by non-existing userName
	@Test
	public void updateUserKeepByNonExistingUserName() {
		String userName = USERNAME;
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> keep_Service.updateUserKeep(UPDATED_USERNAME, UPDATED_PASSWORD, userName));
	}

	// PUT API Test case for updating a particular notes of the existing user by existing notes name
	@Test
	public void updateNotesKeepByName() throws ResourceNotFoundException, IOException {
		MockMultipartFile file1 = new MockMultipartFile("file", "Java%session.txt", "text/plain",
				"Session on Java".getBytes());
		MockMultipartFile file2 = new MockMultipartFile("file", "Spring%session.txt", "text/plain",
				"Session on Spring".getBytes());
		MockMultipartFile updateFile = new MockMultipartFile("file", "Spring%Boot%session.txt", "text/plain",
				"Session on Spring Boot".getBytes());
		String userName = USERNAME, name = NOTES_NAME1;
		NOTES_IDs = new ArrayList<String>();
		NOTES_IDs.add(OBJECT_ID1);
		NOTES_IDs.add(OBJECT_ID2);
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		byte[] FILE1 = file1.getBytes();
		byte[] FILE2 = file2.getBytes();
		byte[] UPDATED_FILE = updateFile.getBytes();
		Notes_Keep notes1 = new Notes_Keep(NOTES_NAME1, NOTES1, FILE1, URL1);
		Notes_Keep notes2 = new Notes_Keep(NOTES_NAME2, NOTES2, FILE2, URL2);
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.of(user_Keep));
		assertEquals(user_Keep, keep_Service.getUserKeepByName(userName));
		ArrayList<String> l = user_Keep.getNotesIds();
		ArrayList<Notes_Keep> allNotesOfUser = new ArrayList<Notes_Keep>();
		allNotesOfUser.add(notes1);
		allNotesOfUser.add(notes2);
		for (int i = 0; i < l.size(); i++) {
			when(notes_Keep_Repo.findById(l.get(i))).thenReturn(Optional.of(allNotesOfUser.get(i)));
		}
		assertEquals(allNotesOfUser, keep_Service.getAllNotesKeepByName(userName));
		for (int i = 0; i < allNotesOfUser.size(); i++) {
			String s1 = allNotesOfUser.get(i).getName();
			if (s1.equals(name)) {
				when(notes_Keep_Repo.findByName(name)).thenReturn(Optional.of(allNotesOfUser.get(i)));
			}
		}
		assertEquals(1, keep_Service.getNotesKeepByName(userName, name).size());
		notes1.setName(NOTES_NAME1);
		notes1.setNotes(UPDATED_NOTES);
		notes1.setFile(UPDATED_FILE);
		notes1.setFileURL(UPDATED_URL);
		when(notes_Keep_Repo.save(notes1)).thenReturn(notes1);
		assertEquals(notes1, keep_Service.updateNotesKeep(updateFile, NOTES_NAME1, UPDATED_NOTES, userName, name));
	}

	// PUT API Test case for updating a particular existing notes in the DB with non-existing user name
	@Test
	public void updateNotesKeepByNonExistingUserName() throws IOException, ResourceNotFoundException {
		MockMultipartFile file1 = new MockMultipartFile("file", "Java%session.txt", "text/plain",
				"Session on Java".getBytes());
		MockMultipartFile updateFile = new MockMultipartFile("file", "Spring%Boot%session.txt", "text/plain",
				"Session on Spring Boot".getBytes());
		String userName = USERNAME, name = NOTES_NAME1;
		byte[] FILE1 = file1.getBytes();
		byte[] UPDATED_FILE = updateFile.getBytes();
		Notes_Keep notes1 = new Notes_Keep(NOTES_NAME1, NOTES1, FILE1, URL1);
		notes1.setName(NOTES_NAME1);
		notes1.setNotes(UPDATED_NOTES);
		notes1.setFile(UPDATED_FILE);
		notes1.setFileURL(UPDATED_URL);
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> keep_Service.updateNotesKeep(updateFile, NOTES_NAME1, UPDATED_NOTES, userName, name));
	}

	// PUT API Test case for updating a particular notes of the existing user by non-existing notes name
	@Test
	public void updateNotesKeepByExistingUserNameButNonExistingNotesName() throws ResourceNotFoundException, IOException {
		MockMultipartFile file1 = new MockMultipartFile("file", "Java%session.txt", "text/plain",
				"Session on Java".getBytes());
		MockMultipartFile updateFile = new MockMultipartFile("file", "Spring%Boot%session.txt", "text/plain",
				"Session on Spring Boot".getBytes());
		String userName = USERNAME, name = NOTES_NAME2;
		NOTES_IDs = new ArrayList<String>();
		NOTES_IDs.add(OBJECT_ID1);
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		byte[] FILE1 = file1.getBytes();
		Notes_Keep notes1 = new Notes_Keep(NOTES_NAME1, NOTES1, FILE1, URL1);
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.of(user_Keep));
		assertEquals(user_Keep, keep_Service.getUserKeepByName(userName));
		ArrayList<String> l = user_Keep.getNotesIds();
		ArrayList<Notes_Keep> allNotesOfUser = new ArrayList<Notes_Keep>();
		allNotesOfUser.add(notes1);
		for (int i = 0; i < l.size(); i++) {
			when(notes_Keep_Repo.findById(l.get(i))).thenReturn(Optional.of(allNotesOfUser.get(i)));
		}
		assertEquals(allNotesOfUser, keep_Service.getAllNotesKeepByName(userName));
		when(notes_Keep_Repo.findByName(name)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> keep_Service.updateNotesKeep(updateFile, NOTES_NAME2, UPDATED_NOTES, userName, name));
	}

	// DELETE API Test case for deleting a particular notes of the existing user by existing notes name
	@Test
	public void deleteNotesKeepByName() throws ResourceNotFoundException, IOException {
		MockMultipartFile file1 = new MockMultipartFile("file", "Java%session.txt", "text/plain",
				"Session on Java".getBytes());
		MockMultipartFile file2 = new MockMultipartFile("file", "Spring%session.txt", "text/plain",
				"Session on Spring".getBytes());
		String userName = USERNAME, name = NOTES_NAME1;
		NOTES_IDs = new ArrayList<String>();
		NOTES_IDs.add(OBJECT_ID1);
		NOTES_IDs.add(OBJECT_ID2);
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		byte[] FILE1 = file1.getBytes();
		byte[] FILE2 = file2.getBytes();
		Notes_Keep notes1 = new Notes_Keep(NOTES_NAME1, NOTES1, FILE1, URL1);
		Notes_Keep notes2 = new Notes_Keep(NOTES_NAME2, NOTES2, FILE2, URL2);
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.of(user_Keep));
		assertEquals(user_Keep, keep_Service.getUserKeepByName(userName));
		ArrayList<String> l = user_Keep.getNotesIds();
		ArrayList<Notes_Keep> allNotesOfUser = new ArrayList<Notes_Keep>();
		allNotesOfUser.add(notes1);
		allNotesOfUser.add(notes2);
		for (int i = 0; i < l.size(); i++) {
			when(notes_Keep_Repo.findById(l.get(i))).thenReturn(Optional.of(allNotesOfUser.get(i)));
		}
		assertEquals(allNotesOfUser, keep_Service.getAllNotesKeepByName(userName));
		ArrayList<Notes_Keep> oneNoteOfUser = new ArrayList<Notes_Keep>();
		oneNoteOfUser.add(notes1);
		for (int i = 0; i < allNotesOfUser.size(); i++) {
			String s1 = allNotesOfUser.get(i).getName();
			if (s1.equals(name)) {
				when(notes_Keep_Repo.findByName(name)).thenReturn(Optional.of(allNotesOfUser.get(i)));
			}
		}
		assertEquals(1, keep_Service.getNotesKeepByName(userName, name).size());
		for (int i = 0; i < l.size(); i++) {
			String nid = oneNoteOfUser.get(0).getId();
			if (l.get(i).equals(nid)) {
				l.remove(i);
				NOTES_IDs.remove(i);
			}
		}
		user_Keep.setNotesIds(l);
		when(user_Keep_Repo.save(user_Keep)).thenReturn(user_Keep);
		assertEquals(user_Keep, keep_Service.updateUserKeep(USERNAME, PASSWORD, userName));
		Notes_Keep deleteNK = oneNoteOfUser.get(0);
		notes_Keep_Repo.delete(deleteNK);
		verify(notes_Keep_Repo, times(1)).delete(deleteNK);
	}

	// DELETE API Test case for deleting a particular notes of the non-existing user
	@Test
	public void deleteNotesKeepByNonExistingUserName() {
		String userName = USERNAME, name = NOTES_NAME1;
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> keep_Service.deleteNotesKeep(userName, name));
	}

	// DELETE API Test case for deleting a particular notes of the existing user by non-existing notes name
	@Test
	public void deleteNotesKeepByExistingUserNameButNonExistingNotesName() throws ResourceNotFoundException, IOException {
		MockMultipartFile file1 = new MockMultipartFile("file", "Java%session.txt", "text/plain",
				"Session on Java".getBytes());
		String userName = USERNAME, name = NOTES_NAME2;
		NOTES_IDs = new ArrayList<String>();
		NOTES_IDs.add(OBJECT_ID1);
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		byte[] FILE1 = file1.getBytes();
		Notes_Keep notes1 = new Notes_Keep(NOTES_NAME1, NOTES1, FILE1, URL1);
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.of(user_Keep));
		assertEquals(user_Keep, keep_Service.getUserKeepByName(userName));
		ArrayList<String> l = user_Keep.getNotesIds();
		ArrayList<Notes_Keep> allNotesOfUser = new ArrayList<Notes_Keep>();
		allNotesOfUser.add(notes1);
		for (int i = 0; i < l.size(); i++) {
			when(notes_Keep_Repo.findById(l.get(i))).thenReturn(Optional.of(allNotesOfUser.get(i)));
		}
		assertEquals(allNotesOfUser, keep_Service.getAllNotesKeepByName(userName));
		when(notes_Keep_Repo.findByName(name)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> keep_Service.deleteNotesKeep(userName, name));
	}

	// DELETE API Test case for deleting a user by existing userName
	@Test
	public void deleteUserKeep() throws ResourceNotFoundException {
		String userName = USERNAME;
		NOTES_IDs = new ArrayList<String>();
		NOTES_IDs.add(OBJECT_ID1);
		NOTES_IDs.add(OBJECT_ID2);
		User_Keep user_Keep = new User_Keep(USERNAME, EMAIL, PASSWORD, NOTES_IDs);
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.of(user_Keep));
		assertEquals(user_Keep, keep_Service.getUserKeepByName(userName));
		ArrayList<String> l = user_Keep.getNotesIds();
		for (int i = 0; i < l.size(); i++) {
			notes_Keep_Repo.deleteById(l.get(i));
			verify(notes_Keep_Repo, times(1)).deleteById(l.get(i));
		}
		user_Keep_Repo.delete(user_Keep);
		verify(user_Keep_Repo, times(1)).delete(user_Keep);
	}

	// DELETE API Test case for deleting a user by non-existing userName
	@Test
	public void deleteUserKeepByNonExistingUserName() {
		String userName = USERNAME;
		when(user_Keep_Repo.findByUserName(userName)).thenReturn(Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> keep_Service.deleteUserKeep(userName));
	}
}
