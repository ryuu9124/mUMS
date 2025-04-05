package com.example.demo6;

import javafx.scene.image.Image;
import java.io.ByteArrayInputStream;

public class UserData {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String departureId;
    private String status;
    private String address;
    private String birthday;
    private byte[] profileImage;

    // Байгуулагч функц
    public UserData(String id, String firstName, String lastName, String email,
                    String departureId, String status, String address,
                    String birthday, byte[] profileImage) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.departureId = departureId;
        this.status = status;
        this.address = address;
        this.birthday = birthday;
        this.profileImage = profileImage;
    }

    // Getters
    public String getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFullName() { return firstName + " " + lastName; }
    public String getEmail() { return email; }
    public String getDepartureId() { return departureId; }
    public String getStatus() { return status; }
    public String getAddress() { return address; }
    public String getBirthday() { return birthday; }

    // Зургийг обьектоор авах
    public Image getProfileImage() {
        if (profileImage != null && profileImage.length > 0) {
            return new Image(new ByteArrayInputStream(profileImage));
        }
        return null;
    }

    public byte[] getProfileImageBytes() {
        return profileImage;
    }
}