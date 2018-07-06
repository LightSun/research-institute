package com.qchery.funda.modules.sys.controller;

import com.qchery.funda.Result;
import com.qchery.funda.modules.sys.entity.User;
import com.qchery.funda.modules.sys.model.UserModel;
import com.qchery.funda.modules.sys.service.UserService;
import com.qchery.funda.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @Controller 与 @ResponseBody 又会一起使用，所以我们使用 @RestController 注解来替换掉它们
 */
@RestController //json交互
@RequestMapping("/sys/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("list")
    public Result list(@RequestBody UserModel userModel) { //@RequestBody:  body
        List<User> userList = userService.listByAgeLargeThan(userModel.getAge());
        return ResultUtils.success(userList);
    }

    //注解： @ResponseBody 纯数据，不带界面
    @RequestMapping("login")
    public Result login(@RequestBody @Valid UserModel userModel) {
        User user = userService.login(userModel.getUsername(), userModel.getPassword());
        return ResultUtils.success(user);
    }

}
