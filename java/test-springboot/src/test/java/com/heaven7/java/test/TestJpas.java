package com.heaven7.java.test;

import com.heaven7.java.springboot.entity.User;
import com.heaven7.java.springboot.entity.Weibo;
import com.heaven7.java.springboot.jpa.UserRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heaven7
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestJpas {

    @Autowired
    private UserRepository mUserRes;

    public void testAdd(){
        User user = new User();
        user.setUsername("test_1");
        user.setUserpwd("123456");

        List<Weibo> weobos = new ArrayList<>();

    }
}
