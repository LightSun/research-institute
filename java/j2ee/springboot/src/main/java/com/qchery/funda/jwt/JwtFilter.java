package com.qchery.funda.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    @Override
    public void doFilter(
            final ServletRequest req, final ServletResponse res, final FilterChain chain)
            throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;

        // 客户端将token封装在请求头中，格式为（Bearer后加空格）：Authorization：Bearer +token
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ServletException("Missing or invalid Authorization header.");
            //TODO 一般这里响应json
        }

        // 去除Bearer 后部分
        final String token = authHeader.substring(7);

        try {
            // 解密token，拿到里面的对象claims
            final Claims claims =
                    Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
            // 将对象传递给下一个请求
            request.setAttribute("claims", claims);
        } catch (final SignatureException e) {
           // throw new ServletException("Invalid token.");
        }
        chain.doFilter(req, res);
    }
}
