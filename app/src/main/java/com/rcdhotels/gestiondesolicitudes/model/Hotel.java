package com.rcdhotels.gestiondesolicitudes.model;

public class Hotel {

	private String idHotel;
	private String nameHotel;
	private String addressHotel;
	private String phone;
	private String email;
	private String contact;
	private String logo;
	private String closeTime;
	private String emailPass;
	private String codigoLogin;
	private int idSociety;

	public String getIdHotel() {
		return idHotel;
	}

	public void setIdHotel(String idHotel) {
		this.idHotel = idHotel;
	}

	public String getNameHotel() {
		return nameHotel;
	}

	public void setNameHotel(String nameHotel) {
		this.nameHotel = nameHotel;
	}

	public String getAddressHotel() {
		return addressHotel;
	}

	public void setAddressHotel(String addressHotel) {
		this.addressHotel = addressHotel;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}

	public String getEmailPass() {
		return emailPass;
	}

	public void setEmailPass(String emailPass) {
		this.emailPass = emailPass;
	}

	public String getCodigoLogin() {
		return codigoLogin;
	}

	public void setCodigoLogin(String codigoLogin) {
		this.codigoLogin = codigoLogin;
	}

	public Hotel() {
	}

	@Override
	public String toString() {
		return nameHotel;
	}

	public int getIdSociety() {
		return idSociety;
	}

	public void setIdSociety(int idSociety) {
		this.idSociety = idSociety;
	}
}
