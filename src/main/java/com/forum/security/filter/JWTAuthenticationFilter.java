package com.forum.security.filter;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forum.bo.UserBo;
import com.forum.constant.SecurityConstants;
import com.forum.dto.Authority;
import com.forum.dto.MemberDto;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/rest/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            MemberDto creds = new ObjectMapper().readValue(req.getInputStream(), MemberDto.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        
        UserBo userBo = (UserBo) auth.getPrincipal();
        String userName = userBo.getUsername();
        Long userId = userBo.getUserId();
        Boolean isAdmin = userBo.getAuthorities().stream().filter(authority -> Authority.ADMIN.equals(authority.getAuthority())).count() > 0;

        String token = JWT.create()
                .withSubject(userId.toString())
                .withClaim("userName", userName)
                .withClaim("isAdmin", BooleanUtils.isTrue(isAdmin) ? "Y" : "N")
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));
        res.addCookie(new Cookie(SecurityConstants.COOKIE_NAME, token));
    }
}