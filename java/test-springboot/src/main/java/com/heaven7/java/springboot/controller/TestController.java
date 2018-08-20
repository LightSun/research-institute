package com.heaven7.java.springboot.controller;

import com.heaven7.java.springboot.entity.User;
import com.heaven7.java.springboot.entity.Weibo;
import com.heaven7.java.springboot.jpa.UserRepository;
import com.heaven7.java.springboot.jpa.WeiboRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class TestController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WeiboRepository weiboRepository;

    @RequestMapping("/searchUser/{username}")
    public @ResponseBody
    List<User> searchUser(@PathVariable("username") String username) {
        List<User> result = this.userRepository.findByUsernameContaining(username);
        return result;
    }

    @RequestMapping("/username/{username}")
    public List<Weibo> getUserWeibo(@PathVariable("username") String username) {
        return this.weiboRepository.searchUserWeibo(username,new Sort(new Sort.Order(Sort.Direction.DESC,"weiboId")));
    }

    @RequestMapping("/simpleSearch")
    public Page<Weibo> simpleSearch(String username, String weiboText, int pageNo, int pageSize){
        User user = this.userRepository.getByUsernameIs(username);
        return this.weiboRepository.findByUserIsAndWeiboTextContaining(user,weiboText,new PageRequest(pageNo,pageSize));
    }
}