package com.taskmanager.web;

import com.taskmanager.model.Role;
import com.taskmanager.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class TestHeadersGenerator {

    @Autowired
    private JwtProvider jwtProvider;

    public HttpHeaders withRole(String roleName){
        HttpHeaders headers = new HttpHeaders();
        Role role = new Role();
        role.setRoleName(roleName);
        String token =  jwtProvider.createToken("anonymous", Arrays.asList(role));
        headers.setContentType(APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        return headers;
    }

}
