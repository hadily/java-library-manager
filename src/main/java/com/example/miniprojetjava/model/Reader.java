package com.example.miniprojetjava.model;

import java.time.LocalDate;
import java.time.Period;

public class Reader {
    private int readerId;
    private String username;
    private String password;
    private String fullName;
    private LocalDate birthDate;
    private String picture;
    private int CIN;

    public Reader(String username, String firstName, String lastName, LocalDate birthDate, String picture, int CIN) {
        this.readerId ++;
        this.username = username;
        this.fullName = firstName + " " + lastName;
        this.birthDate = birthDate;
        this.picture = picture != null ? picture : "";
        this.CIN = CIN;
    }

    public Reader(int readerId, String username, String firstName, String lastName, LocalDate birthDate, String picture, int CIN) {
        this.readerId = readerId;
        this.username = username;
        this.fullName = firstName + " " + lastName;
        this.birthDate = birthDate;
        this.picture = picture != null ? picture : "";
        this.CIN = CIN;
    }

    public Reader(int readerId, String username, String fullName, LocalDate birthDate, String picture, int CIN) {
        this.readerId = readerId;
        this.username = username;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.picture = picture != null ? picture : "";
        this.CIN = CIN;
    }

    public Reader() {
    }

    public int getReaderId() { return readerId; }

    public void setReaderId(int readerId) { this.readerId = readerId;}

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }

    public void setFullName(String fullName) { this.fullName = fullName; }

    public LocalDate getBirthDate() { return birthDate; }

    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getPicture() { return picture; }

    public void setPicture(String picture) { this.picture = picture; }

    public int getCIN() { return CIN; }

    public void setCIN(int CIN) { this.CIN = CIN; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "Reader{" +
                "username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", birthDate=" + birthDate +
                ", picture='" + picture + '\'' +
                ", CIN=" + CIN +
                ", Password='" + password +
                '}';
    }

    public int getAge() {
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }
}
