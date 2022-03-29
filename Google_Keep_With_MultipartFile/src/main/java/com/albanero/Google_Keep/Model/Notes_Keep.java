package com.albanero.Google_Keep.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Notes_Keep {

	@Id
	private String id;
	private String name;
	private String notes;
	private byte[] file;
	private String fileURL;

	public Notes_Keep() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Notes_Keep(String name, String notes, byte[] file, String fileURL) {
		super();
		this.name = name;
		this.notes = notes;
		this.file = file;
		this.fileURL = fileURL;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public String getFileURL() {
		return fileURL;
	}

	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}

	
	
}
