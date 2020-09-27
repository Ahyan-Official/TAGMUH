package com.sean.TagMuh;

class Servicer {


    String description;
    String email;
    String firstName;
    String lastName;
    String location;
    String phoneNumber;
    String profileImg;

    public Servicer() {
    }

    public Servicer(String description, String email, String firstName, String lastName, String location, String phoneNumber, String profileImg) {
        this.description = description;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.profileImg = profileImg;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }
}
