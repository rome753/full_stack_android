package cc.rome753.fullstack.bean;

/**
 * Created by crc on 16/11/17.
 */

public class Register extends Login {

    String email;

    public Register(){}

    public Register(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
