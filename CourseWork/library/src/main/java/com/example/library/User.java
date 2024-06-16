package com.example.library;

public class User {
    private String username;
    private String password;
    private String phone;
    private String gender;
    private String birthday;
    private byte[] userimage;

    public User(String username, String password, String phone, String gender, String birthday, byte[] userimage) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.gender = gender;
        this.birthday = birthday;
        this.userimage = userimage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public byte[] getUserimage() { return userimage; }

    public void setUserimage(byte[] userimage) { this.userimage = userimage; }
}
