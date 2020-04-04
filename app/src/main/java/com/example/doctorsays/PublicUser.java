package com.example.doctorsays;

public class PublicUser {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String age;
    private String sex;
    private String bloodGroup;
    private String photoUrl;

    public PublicUser() {

    }

    public PublicUser(String id, String name, String email, String photoUrl, String phoneNumber, String address, String age, String sex, String bloodGroup) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
        if (phoneNumber == null) {
            this.phoneNumber = "Not Visible";
        } else {
            this.phoneNumber = phoneNumber;
        }

        if (address.equals("null")) {
            this.address = "Not Visible";
        } else {
            this.address = address;
        }

        if (age.equals("null")) {
            this.age = "Not Visible";
        } else {
            this.age = age;
        }

        if (sex.equals("null")) {
            this.sex = "Not Visible";
        } else {
            this.sex = sex;
        }

        if (bloodGroup.equals("null")) {
            this.bloodGroup = "Not Visible";
        } else {
            this.bloodGroup = phoneNumber;
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getSex() {
        return sex;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void changeText (String string) {
        name = string;
    }
}
