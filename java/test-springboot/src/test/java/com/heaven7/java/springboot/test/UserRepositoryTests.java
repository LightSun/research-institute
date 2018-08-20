package com.heaven7.java.springboot.test;

import com.heaven7.java.springboot.entity.User;
import com.heaven7.java.springboot.jpa.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author heaven7
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository mUserRes;

    @Test
    public void testAdd(){
        mUserRes.deleteAllInBatch();

        User user = new User();
        user.setUsername("test_1");
        user.setUserpwd("123456");
        user.setImages(new ArrayList<>(Arrays.asList("123", "456", "789")));
        User user1 = mUserRes.saveAndFlush(user);

        User one = mUserRes.findOne(user1.getUserId());
        System.out.println(one.getImages());
    }
}
