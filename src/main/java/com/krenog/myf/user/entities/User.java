package com.krenog.myf.user.entities;

import com.krenog.myf.entity.BaseEntity;
import com.krenog.myf.event.entities.EventMember;
import com.krenog.myf.user.services.user.CreateUserData;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "myf_user")
public class User extends BaseEntity {
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "push_token")
    private String pushToken;

    @Column(name = "last_login")
    private LocalDateTime lastLogin = LocalDateTime.now();

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @PrePersist
    @PreUpdate
    public void preUpdate() {
        super.setUpdateDate(LocalDateTime.now());
    }

    public User() {
    }

    public User(CreateUserData userData) {
        this.phoneNumber = userData.getPhoneNumber();
        this.username = userData.getUsername();
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
