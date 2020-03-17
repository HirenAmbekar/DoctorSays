package com.example.doctorsays;

public class Cards {

    private String name;
    private String age;
    private String sex;

    public Cards(String name, String age, String sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
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

    public void changeText (String string) {
        name = string;
    }
}
