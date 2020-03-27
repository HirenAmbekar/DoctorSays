package com.example.doctorsays;

import android.net.Uri;

public class Users {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String age;
    private String sex;
    private String bloodGroup;
    private String photoUrl;
    private boolean phoneVisible, addressVisible, ageVisible, sexVisible, bloodGroupVisible;

    public Users(){

    }

    public Users(String id, String name, String email, String photoUrl, String phoneNumber, String address, String age, String sex, String bloodGroup, boolean phoneVisible, boolean ageVisible,
                 boolean addressVisible, boolean sexVisible, boolean bloodGroupVisible) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
        if (phoneNumber == "null") {
            this.phoneNumber = "Not Set";
        } else {
            this.phoneNumber = phoneNumber;
        }

        if (address == "null") {
            this.address = "Not Set";
        } else {
            this.address = address;
        }

        if (age == "null") {
            this.age = "Not Set";
        } else {
            this.age = age;
        }

        if (sex == "null") {
            this.sex = "Not Set";
        } else {
            this.sex = sex;
        }

        if (bloodGroup == "null") {
            this.bloodGroup = "Not Set";
        } else {
            this.bloodGroup = phoneNumber;
        }

        this.phoneVisible = phoneVisible;
        this.addressVisible = addressVisible;
        this.ageVisible = ageVisible;
        this.sexVisible = sexVisible;
        this.bloodGroupVisible = bloodGroupVisible;
    }

    public Users(String id, String name, String email, String photoUrl, String phoneNumber, String address, String age, String sex, String bloodGroup) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
        if (phoneNumber == null) {
            this.phoneNumber = "Not Visible";
        } else {
            this.phoneNumber = phoneNumber;
        }

        if (address == "null") {
            this.address = "Not Visible";
        } else {
            this.address = address;
        }

        if (age == "null") {
            this.age = "Not Visible";
        } else {
            this.age = age;
        }

        if (sex == "null") {
            this.sex = "Not Visible";
        } else {
            this.sex = sex;
        }

        if (bloodGroup == "null") {
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

    public boolean isPhoneVisible() {
        return phoneVisible;
    }

    public boolean isAddressVisible() {
        return addressVisible;
    }

    public boolean isAgeVisible() {
        return ageVisible;
    }

    public boolean isSexVisible() {
        return sexVisible;
    }

    public boolean isBloodGroupVisible() {
        return bloodGroupVisible;
    }

    public void changeText (String string) {
        name = string;
    }
}
