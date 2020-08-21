package com.rcdhotels.gestiondesolicitudes.model;

public class Permission {

	private int idUser;
	private String keyModule;
	private int create;
	private int read;
	private int update;
	private int delete;

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public String getKeyModule() {
		return keyModule;
	}
	public void setKeyModule(String keyModule) {
		this.keyModule = keyModule;
	}
	public int getCreate() {
		return create;
	}
	public void setCreate(int create) {
		this.create = create;
	}
	public int getRead() {
		return read;
	}
	public void setRead(int read) {
		this.read = read;
	}
	public int getUpdate() {
		return update;
	}
	public void setUpdate(int update) {
		this.update = update;
	}
	public int getDelete() {
		return delete;
	}
	public void setDelete(int delete) {
		this.delete = delete;
	}
	public Permission() {
		super();
	}
}
