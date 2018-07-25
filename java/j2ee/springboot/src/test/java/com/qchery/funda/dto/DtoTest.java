package com.qchery.funda.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DtoTest {

    @Autowired
    private IUserService userService;

    private void print(List<User> list) {
        for (User u : list) {
            System.out.println(u.getId() + "===" + u.getUserName());
        }
    }

    private void addTestUsers() {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            User user = new User();
            user.setUserName("username__" + i);
            user.setNickName("nickname__" + i);
            user.setEmail("978136772@qq.com");
            user.setPassword("heaven7");
            list.add(user);
        }
        userService.save(list);
        userService.flush();
    }

    @Test
    public void test1() {
        addTestUsers();
        List<User> list = userService.findAll(new BaseSearch<User>(new SearchDto("userName", "eq", "username__0")));
        print(list);
        userService.deleteAllInBatch();
    }

    @Test
    public void test2() {
        addTestUsers();
        List<User> list = userService.findAll(SearchTools.buildSpecification(
                SearchTools.buildSpeDto("and", new SearchDto("and", "id", "gt", 2)),
                SearchTools.buildSpeDto("and", new SearchDto("userName", "neq", "user5"),
                        new SearchDto("or", "userName", "neq", "user9"))
        ));
        print(list);
        userService.deleteAllInBatch();
    }
}