package com.albanero.Google_Keep.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.albanero.Google_Keep.Exceptions.ResourceNotFoundException;
import com.albanero.Google_Keep.Model.Notes_Keep;
import com.albanero.Google_Keep.Model.User_Keep;
import com.albanero.Google_Keep.Repository.Notes_Keep_Repository;
import com.albanero.Google_Keep.Service.Keep_Service;

@RestController
@RequestMapping(value = "/keep")
public class Keep_Controller {

	@Autowired
	private Keep_Service keep_Service;
	@Autowired
	private Notes_Keep_Repository notes_Keep_Repo;

	// Create user and note where user contains only the object id of the notes
	@PostMapping("/create/user-notes")
	public ResponseEntity<User_Keep> createUserKeep(@RequestParam("file") MultipartFile File,
			@RequestParam("username") String UserName, @RequestParam("password") String Password,
			@RequestParam("email") String Email, @RequestParam("notesname") String NotesName,
			@RequestParam("notes") String Notes) throws IOException, ResourceNotFoundException {
		return ResponseEntity.ok(keep_Service.createUserKeep(File, UserName, Password, Email, NotesName, Notes));
	}

	// Create notes by userName
	@PostMapping("/create/notes/{userName}")
	public ResponseEntity<User_Keep> createNotesKeep(@RequestParam("file") MultipartFile File,
			@RequestParam("notesname") String NotesName, @RequestParam("notes") String Notes,
			@PathVariable String userName) throws ResourceNotFoundException, IOException {
		return ResponseEntity.ok(keep_Service.createNotesKeep(File, NotesName, Notes, userName));
	}

	// Get all users
	@GetMapping("/get/user-notes")
	public List<User_Keep> getUserKeep() {
		return keep_Service.getUserKeep();
	}

	// Get user details by userName
	@GetMapping("/get/user-notes/{userName}")
	public ResponseEntity<User_Keep> getUserKeepByName(@PathVariable String userName) throws ResourceNotFoundException {
		return ResponseEntity.ok(keep_Service.getUserKeepByName(userName));
	}

	// Get all notes of the user by userName
	@GetMapping("/get/notes/{userName}")
	public List<Notes_Keep> getAllNotesKeepByName(@PathVariable String userName) throws ResourceNotFoundException {
		return keep_Service.getAllNotesKeepByName(userName);
	}

	// Get a particular notes of the user by notes name
	@GetMapping("/get/notes/{userName}/{name}")
	public List<Notes_Keep> getNotesKeepByName(@PathVariable String userName, @PathVariable String name)
			throws ResourceNotFoundException {
		return keep_Service.getNotesKeepByName(userName, name);
	}

	// Update user details by userName
	@PutMapping("/put/userdetails/{userName}")
	public ResponseEntity<User_Keep> updateUserKeep(@RequestParam("username") String UserName,
			@RequestParam("password") String Password, @PathVariable String userName) throws ResourceNotFoundException {
		return ResponseEntity.ok(keep_Service.updateUserKeep(UserName, Password, userName));
	}

	// Update a particular notes of the user by notes name
	@PutMapping("/put/notesdetails/{userName}/{name}")
	public ResponseEntity<Notes_Keep> updateNotesKeep(@RequestParam("file") MultipartFile File,
			@RequestParam("notesname") String NotesName, @RequestParam("notes") String Notes,
			@PathVariable String userName, @PathVariable String name) throws IOException, ResourceNotFoundException {
		return ResponseEntity.ok(keep_Service.updateNotesKeep(File, NotesName, Notes, userName, name));
	}

	// Delete a particular notes of the user by notes name
	@DeleteMapping("/delete/notesdetails/{userName}/{name}")
	public String deleteNotesKeep(@PathVariable String userName, @PathVariable String name)
			throws ResourceNotFoundException {
		keep_Service.deleteNotesKeep(userName, name);
		return userName + ", Your " + name + " notes was successfully removed";
	}

	// Delete user by userName and all the notes of that particular user
	@DeleteMapping("/delete/userdetails/{userName}")
	public String deleteUserKeep(@PathVariable String userName) throws ResourceNotFoundException {
		keep_Service.deleteUserKeep(userName);
		return userName + ", your account and all your notes were successfully removed";
	}

	// Download the file of the notes
	@GetMapping("/{fileName}/{id}")
	ResponseEntity<byte[]> downloadFile(@PathVariable String fileName, @PathVariable String id,
			HttpServletRequest request) {
		Optional<Notes_Keep> notes_Keep = notes_Keep_Repo.findById(id);
		String mimeType = request.getServletContext().getMimeType(fileName);
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + fileName)
				.body(notes_Keep.get().getFile());
	}
}
