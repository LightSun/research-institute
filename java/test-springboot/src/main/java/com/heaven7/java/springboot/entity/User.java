package com.heaven7.java.springboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {

    public User() {
    }

    public User(String username, String userpwd) {
        this.username = username;
        this.userpwd = userpwd;
    }

    private long userId;
    private String username;
    private String userpwd;
    private ArrayList<String> images;

    @JsonIgnore
    private Set<Weibo> weibos;

    public ArrayList<String> getImages() {
        return images;
    }
    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Column(name="username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "userpwd")
    public String getUserpwd() {
        return userpwd;
    }

    public void setUserpwd(String userpwd) {
        this.userpwd = userpwd;
    }

    @OneToMany(fetch = FetchType.LAZY,cascade = {CascadeType.REMOVE},mappedBy = "user")
    public Set<Weibo> getWeibos() {
        return weibos;
    }

    public void setWeibos(Set<Weibo> weibos) {
        this.weibos = weibos;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", userpwd='" + userpwd + '\'' +
                '}';
    }
}