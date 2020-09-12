package com.forum.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
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
        String header = req.getHeader(SecurityConstants.HEADER_STRING);
        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.HEADER_STRING);
        if (token != null) {
            // parse the token.
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
                    .build()
                    .verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""));
            Long userId = NumberUtils.isParsable(decodedJWT.getSubject()) ? Long.parseLong(decodedJWT.getSubject()) : -1L;
            String userName = decodedJWT.getClaim("userName").asString();
            String isAdmin = decodedJWT.getClaim("isAdmin").asString();

            Date expiryDate = JWT.decode(token.replace(SecurityConstants.TOKEN_PREFIX, "")).getExpiresAt();
            Date issueAt = JWT.decode(token.replace(SecurityConstants.TOKEN_PREFIX, "")).getIssuedAt();
            Date today = new Date();
            if (userId > 0 && StringUtils.isNotEmpty(userName) 
            && StringUtils.isNotEmpty(isAdmin) && (today.before(expiryDate))) {
                try {
                    Boolean isValidDate = memberService.validedJwtDate(userId, issueAt);
                    if (BooleanUtils.isTrue(isValidDate)) {
                        List<Authority>authories = new ArrayList<Authority>();
                        if ("Y".equals(isAdmin)) authories.add(new Authority(Authority.ADMIN));
                        return new UsernamePasswordAuthenticationToken(userId.toString(), null, authories);
                    }
                    else return null;
                } catch (ApplicationException e){
                    logger.error("", e);
                    return null;
                }
            }
            return null;
        }
        return null;
    }
}