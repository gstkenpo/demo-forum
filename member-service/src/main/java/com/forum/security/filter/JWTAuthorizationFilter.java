package com.forum.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.forum.constant.SecurityConstants;
import com.forum.dto.Authority;
import com.forum.exception.ApplicationException;
import com.forum.service.MemberService;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private MemberService memberService;
    public JWTAuthorizationFilter(AuthenticationManager authManager, MemberService memberService) {
        super(authManager);
        this.memberService = memberService;
    }

@Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        if (req.getCookies() == null 
        || !Arrays.asList(req.getCookies()).stream().anyMatch(c -> c.getName().equals(SecurityConstants.COOKIE_NAME))) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) /*throws ApplicationException*/ {
        String token = Arrays.asList(request.getCookies()).stream().filter(c -> c.getName().equals(SecurityConstants.COOKIE_NAME)).findFirst().map(Cookie::getValue).orElse(null);
        if (token != null) {
            // parse the token.
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
                    .build()
                    .verify(token);
            Long userId = NumberUtils.isParsable(decodedJWT.getSubject()) ? Long.parseLong(decodedJWT.getSubject()) : -1L;
            String userName = decodedJWT.getClaim("userName").asString();
            String isAdmin = decodedJWT.getClaim("isAdmin").asString();

            Date expiryDate = JWT.decode(token).getExpiresAt();
            Date issueAt = JWT.decode(token).getIssuedAt();
            Date today = new Date();
            if (userId > 0 && StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(isAdmin)) {
                try {
                    Boolean isValidDate = memberService.validedJwtDate(userId, issueAt);
                    if (BooleanUtils.isTrue(isValidDate) && today.before(expiryDate)) {
                        List<Authority>authories = new ArrayList<Authority>();
                        if ("Y".equals(isAdmin)) authories.add(new Authority(Authority.ADMIN));
                        return new UsernamePasswordAuthenticationToken(userId.toString(), null, authories);
                    } else throw new ApplicationException("JWT expired", HttpStatus.UNAUTHORIZED);
                } catch (ApplicationException e){
                    logger.error("" + e);
                    return null;
                }
            }
            return null;
        }
        return null;
    }
}