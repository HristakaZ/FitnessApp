package models;

import android.os.Parcelable;

import java.io.Serializable;

public class User implements Serializable {

    private String email, password;

    public User() {

    }

    public User(String email) {
        this.email = email;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

}
