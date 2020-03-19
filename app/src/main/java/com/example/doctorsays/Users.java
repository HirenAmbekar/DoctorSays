package com.example.doctorsays;

class Users {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String age;
    private String sex;
    private String bloodGroup;

    public Users(String id, String name, String age, String sex, String bloodGroup) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.bloodGroup = bloodGroup;
    }

    public Users(String id, String name, String email, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        if (phoneNumber == null) {
            this.phoneNumber = "null";
        } else {
            this.phoneNumber = phoneNumber;
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

    public String getSex() {
        return sex;
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
}
