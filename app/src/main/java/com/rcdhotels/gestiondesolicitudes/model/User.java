package com.rcdhotels.gestiondesolicitudes.model;

import java.util.ArrayList;

public class User {

    private String idUser;
    private String idCollaborator;
    private String role;
    private String userName;
    private String password;
    private String email;
    private String firstname;
    private String secondName;
    private String surName;
    private String secondSurName;
    private String active;
    private String createdDate;
    private String pswencryp;
    private String warehouse;
    private String mrpController;
    private Hotel hotel = new Hotel();
    private ArrayList<Permission> permissions = new ArrayList<>();

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdCollaborator() {
        return idCollaborator;
    }

    public void setIdCollaborator(String idCollaborator) {
        this.idCollaborator = idCollaborator;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getSecondSurName() {
        return secondSurName;
    }

    public void setSecondSurName(String secondSurName) {
        this.secondSurName = secondSurName;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getPswencryp() {
        return pswencryp;
    }

    public void setPswencryp(String pswencryp) {
        this.pswencryp = pswencryp;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public ArrayList<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<Permission> permissions) {
        this.permissions = permissions;
    }

    public User() {
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getMrpController() {
        return mrpController;
    }

    public void setMrpController(String mrpController) {
        this.mrpController = mrpController;
    }
}
