package com.albanero.Google_Keep.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.albanero.Google_Keep.Exceptions.ResourceNotFoundException;
import com.albanero.Google_Keep.Model.Notes_Keep;
import com.albanero.Google_Keep.Model.User_Keep;
import com.albanero.Google_Keep.Repository.Notes_Keep_Repository;
import com.albanero.Google_Keep.Repository.User_Keep_Repository;

@Service
public class Keep_Service {

	@Autowired
	private User_Keep_Repository user_Keep_Repo;

	@Autowired
	private Notes_Keep_Repository notes_Keep_Repo;

	// Create user and note where user contains only the object id of the notes
	public User_Keep createUserKeep(MultipartFile file, String userName, String password, String email,
			String notesName, String notes) throws IOException, ResourceNotFoundException {
		ArrayList<String> ua = new ArrayList<String>();
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		if (fileName.isEmpty()) {
			throw new ResourceNotFoundException("No File was Selected");
		}
		String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/keep/").path(fileName).toUriString();
		Notes_Keep notes_Keep = new Notes_Keep(notesName, notes, file.getBytes(), url);
		notes_Keep_Repo.save(notes_Keep);
		String url1 = ServletUriComponentsBuilder.fromCurrentContextPath().path("/keep/").path(fileName + "/")
				.path(notes_Keep.getId()).toUriString();
		notes_Keep.setFileURL(url1);
		notes_Keep_Repo.save(notes_Keep);
		ua.add(notes_Keep.getId());
		User_Keep user_Keep = new User_Keep(userName, email, password, ua);
		return user_Keep_Repo.save(user_Keep);
	}

	// Create notes by userName
	public User_Keep createNotesKeep(MultipartFile file, String notesName, String notes, String userName)
			throws IOException, ResourceNotFoundException {
		Optional<User_Keep> optionalUserKeep = Optional.of(user_Keep_Repo.findByUserName(userName).orElseThrow(
				() -> new ResourceNotFoundException("No user with username :- '" + userName + "' was found.")));
		User_Keep user_Keep = optionalUserKeep.get();
		ArrayList<String> ua = user_Keep.getNotesIds();
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		if (fileName.isEmpty()) {
			throw new ResourceNotFoundException("No File was Selected");
		}
		String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/keep/").path(fileName).toUriString();
		Notes_Keep notes_Keep = new Notes_Keep(notesName, notes, file.getBytes(), url);
		notes_Keep_Repo.save(notes_Keep);
		String url1 = ServletUriComponentsBuilder.fromCurrentContextPath().path("/keep/").path(fileName + "/")
				.path(notes_Keep.getId()).toUriString();
		notes_Keep.setFileURL(url1);
		notes_Keep_Repo.save(notes_Keep);
		ua.add(notes_Keep.getId());
		user_Keep.setNotesIds(ua);
		return user_Keep_Repo.save(user_Keep);
	}

	// Get all users
	public List<User_Keep> getUserKeep() {
		return user_Keep_Repo.findAll();
	}

	// Get user details by userName
	public User_Keep getUserKeepByName(String userName) throws ResourceNotFoundException {
		Optional<User_Keep> optionalUserKeep = Optional.of(user_Keep_Repo.findByUserName(userName).orElseThrow(
				() -> new ResourceNotFoundException("No user with username :- '" + userName + "' was found.")));
		return optionalUserKeep.get();
	}

	// Get all notes of the user by userName
	public List<Notes_Keep> getAllNotesKeepByName(String userName) throws ResourceNotFoundException {
		Optional<User_Keep> optionalUserKeep = Optional.of(user_Keep_Repo.findByUserName(userName).orElseThrow(
				() -> new ResourceNotFoundException("No user with username :- '" + userName + "' was found.")));
		User_Keep user_Keep = optionalUserKeep.get();
		ArrayList<String> l = user_Keep.getNotesIds();
		ArrayList<Notes_Keep> allNotesOfUser = new ArrayList<Notes_Keep>();
		for (int i = 0; i < l.size(); i++) {
			Optional<Notes_Keep> listNotesKeep = notes_Keep_Repo.findById(l.get(i));
			if (listNotesKeep.isPresent()) {
				allNotesOfUser.add(listNotesKeep.get());
			}
		}
		return allNotesOfUser;

	}

	// Get a particular notes of the user by notes name
	public List<Notes_Keep> getNotesKeepByName(String userName, String name) throws ResourceNotFoundException {
		Optional<User_Keep> optionalUserKeep = Optional.of(user_Keep_Repo.findByUserName(userName).orElseThrow(
				() -> new ResourceNotFoundException("No user with username :- '" + userName + "' was found.")));
		User_Keep user_Keep = optionalUserKeep.get();
		ArrayList<String> l = user_Keep.getNotesIds();
		ArrayList<Notes_Keep> allNotesOfUser = new ArrayList<Notes_Keep>();
		for (int i = 0; i < l.size(); i++) {
			Optional<Notes_Keep> listNotesKeep = notes_Keep_Repo.findById(l.get(i));
			if (listNotesKeep.isPresent()) {
				allNotesOfUser.add(listNotesKeep.get());
			}
		}
		ArrayList<Notes_Keep> oneNoteOfUser = new ArrayList<Notes_Keep>();
		for (int i = 0; i < allNotesOfUser.size(); i++) {
			String s1 = allNotesOfUser.get(i).getName();
			String s2 = name;
			if (s1.equals(s2)) {
				oneNoteOfUser.add(allNotesOfUser.get(i));
			}
		}
		return oneNoteOfUser;

	}

	// Update user details by userName
	public User_Keep updateUserKeep(String UserName, String password, String userName)
			throws ResourceNotFoundException {
		Optional<User_Keep> optionalUserKeep = Optional.of(user_Keep_Repo.findByUserName(userName).orElseThrow(
				() -> new ResourceNotFoundException("No user with username :- '" + userName + "' was found.")));
		User_Keep updateUK = optionalUserKeep.get();
		updateUK.setUserName(UserName);
		updateUK.setPassword(password);
		return user_Keep_Repo.save(updateUK);

	}

	// Update a particular notes of the user by notes name
	public Notes_Keep updateNotesKeep(MultipartFile file, String notesName, String notes, String userName, String name)
			throws IOException, ResourceNotFoundException {
		Optional<User_Keep> optionalUserKeep = Optional.of(user_Keep_Repo.findByUserName(userName).orElseThrow(
				() -> new ResourceNotFoundException("No user with username :- '" + userName + "' was found.")));
		try {
			User_Keep user_Keep = optionalUserKeep.get();
			ArrayList<String> l = user_Keep.getNotesIds();
			ArrayList<Notes_Keep> allNotesOfUser = new ArrayList<Notes_Keep>();
			for (int i = 0; i < l.size(); i++) {
				Optional<Notes_Keep> listNotesKeep = notes_Keep_Repo.findById(l.get(i));
				if (listNotesKeep.isPresent()) {
					allNotesOfUser.add(listNotesKeep.get());
				}
			}
			ArrayList<Notes_Keep> oneNoteOfUser = new ArrayList<Notes_Keep>();
			for (int i = 0; i < allNotesOfUser.size(); i++) {
				String s1 = allNotesOfUser.get(i).getName();
				String s2 = name;
				if (s1.equals(s2)) {
					oneNoteOfUser.add(allNotesOfUser.get(i));
				}
			}
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			if (fileName.isEmpty()) {
				throw new ResourceNotFoundException("No File was Selected");
			}
			String url = ServletUriComponentsBuilder.fromCurrentContextPath().path("/keep/").path(fileName)
					.toUriString();
			Notes_Keep updateNK = oneNoteOfUser.get(0);
			updateNK.setName(notesName);
			updateNK.setNotes(notes);
			updateNK.setFile(file.getBytes());
			updateNK.setFileURL(url);
			notes_Keep_Repo.save(updateNK);
			String url1 = ServletUriComponentsBuilder.fromCurrentContextPath().path("/keep/").path(fileName + "/")
					.path(updateNK.getId()).toUriString();
			updateNK.setFileURL(url1);
			return notes_Keep_Repo.save(updateNK);
		} catch (Exception e) {
			throw new ResourceNotFoundException("No Notes was found with the name :- '" + name + "'.");
		}
	}

	// Delete a particular notes of the user by notes name
	public void deleteNotesKeep(String userName, String name) throws ResourceNotFoundException {
		Optional<User_Keep> optionalUserKeep = Optional.of(user_Keep_Repo.findByUserName(userName).orElseThrow(
				() -> new ResourceNotFoundException("No user with username :- '" + userName + "' was found.")));
		try {
			User_Keep user_Keep = optionalUserKeep.get();
			ArrayList<String> l = user_Keep.getNotesIds();
			ArrayList<Notes_Keep> allNotesOfUser = new ArrayList<Notes_Keep>();
			for (int i = 0; i < l.size(); i++) {
				Optional<Notes_Keep> listNotesKeep = notes_Keep_Repo.findById(l.get(i));
				if (listNotesKeep.isPresent()) {
					allNotesOfUser.add(listNotesKeep.get());
				}
			}
			ArrayList<Notes_Keep> oneNoteOfUser = new ArrayList<Notes_Keep>();
			for (int i = 0; i < allNotesOfUser.size(); i++) {
				String s1 = allNotesOfUser.get(i).getName();
				String s2 = name;
				if (s1.equals(s2)) {
					oneNoteOfUser.add(allNotesOfUser.get(i));
				}
			}
			for (int i = 0; i < l.size(); i++) {
				String nid = oneNoteOfUser.get(0).getId();
				if (l.get(i).equals(nid)) {
					l.remove(i);
				}
			}
			user_Keep.setNotesIds(l);
			user_Keep_Repo.save(user_Keep);
			Notes_Keep deleteNK = oneNoteOfUser.get(0);
			notes_Keep_Repo.delete(deleteNK);
		} catch (Exception e) {
			throw new ResourceNotFoundException("No Notes was found with the name :- '" + name + "'.");
		}
	}

	// Delete user by userName and all the notes of that particular user
	public void deleteUserKeep(String userName) throws ResourceNotFoundException {
		Optional<User_Keep> optionalUserKeep = Optional.of(user_Keep_Repo.findByUserName(userName).orElseThrow(
				() -> new ResourceNotFoundException("No user with username :- '" + userName + "' was found.")));
		User_Keep user_Keep = optionalUserKeep.get();
		ArrayList<String> l = user_Keep.getNotesIds();
		for (int i = 0; i < l.size(); i++) {
			notes_Keep_Repo.deleteById(l.get(i));
		}
		user_Keep_Repo.delete(user_Keep);
	}
}
