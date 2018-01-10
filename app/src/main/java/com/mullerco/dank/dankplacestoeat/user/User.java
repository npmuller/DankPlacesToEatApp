package com.mullerco.dank.dankplacestoeat.user;

import java.util.Date;

/**
 * Created by nmuller on 1/2/18.
 */

public class User {

    private int userId;
    private String username;
    private String firstName;
    private String lastName;
    private Date createdTime;

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getCreatedTime() {
        return createdTime;
    }
}
