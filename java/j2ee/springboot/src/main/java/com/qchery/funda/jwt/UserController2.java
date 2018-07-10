package com.qchery.funda.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController2 {

    //这里模拟数据库
    private final Map<String, List<String>> userDb = new HashMap<>();

    @SuppressWarnings("unused")
    private static class UserLogin {
        public String name;
        public String password;
    }

    public UserController2() {
        userDb.put("tom", Arrays.asList("user"));
        userDb.put("wen", Arrays.asList("user", "admin"));
    }
    /*以上是模拟数据库，并往数据库插入tom和sally两条记录*/


    @RequestMapping(value = "login3", method = RequestMethod.POST)
    public LoginResponse login(@RequestBody final UserLogin login)
        throws ServletException {
        if (login.name == null || !userDb.containsKey(login.name)) {
            throw new ServletException("Invalid login");
        }
        String token = Jwts.builder().setSubject(login.name)
                .claim("roles", userDb.get(login.name)).setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "secretkey").compact();

        //加密生成token
        return new LoginResponse(token);
    }

    @SuppressWarnings("unused")
    private static class LoginResponse {
        public String token;

        public LoginResponse(final String token) {
            this.token = token;
        }
    }
}