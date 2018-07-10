package com.qchery.funda.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "role/{role}", method = RequestMethod.GET)
    public Boolean login(@PathVariable final String role, final HttpServletRequest request)
            throws ServletException {
        //JwtFilter 过滤
        final Claims claims = (Claims) request.getAttribute("claims");
        return ((List<String>) claims.get("roles")).contains(role);
    }
}
