package com.example.app.medicalapplication.model;

import java.io.Serializable;

public class Order implements Serializable {
    private String id;
    private String userName;
    private String docUserName;
    private String image;
    private String name;
    private String address;
    private String phone;
    private String describe;

    public String getDocUserName() {
        return docUserName;
    }

    public void setDocUserName(String docUserName) {
        this.docUserName = docUserName;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPharmacyUserName() {
        return pharmacyUserName;
    }

    public void setPharmacyUserName(String deliveryUserName) {
        this.pharmacyUserName = deliveryUserName;
    }

    private String pharmacyUserName;
    private String pharmacyName;
    private String pharmacyPhone;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPharmacyName() {
        return pharmacyName;
    }

    public void setPharmacyName(String deliveryName) {
        this.pharmacyName = deliveryName;
    }

    public String getPharmacyPhone() {
        return pharmacyPhone;
    }

    public void setPharmacyPhone(String deliveryPhone) {
        this.pharmacyPhone = deliveryPhone;
    }

    public Order() {
    }

    public Order(String id, String userName, String image, String name, String address, String phone, String pharmacyName, String pharmacyPhone) {
        this.id = id;
        this.userName = userName;
        this.image = image;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.pharmacyName = pharmacyName;
        this.pharmacyPhone = pharmacyPhone;
    }
}
