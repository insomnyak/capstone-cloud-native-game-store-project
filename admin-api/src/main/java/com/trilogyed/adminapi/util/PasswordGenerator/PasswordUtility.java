package com.trilogyed.adminapi.util.PasswordGenerator;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtility {
    public static void main(String[] args)
    {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "Password";
        String encodedPassword = passwordEncoder.encode(password);
        System.out.println(encodedPassword);
    }
}
