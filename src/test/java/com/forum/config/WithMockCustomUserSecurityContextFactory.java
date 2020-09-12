package com.forum.config;

import java.util.ArrayList;
import java.util.List;

import com.forum.dto.Authority;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
	@Override
	public SecurityContext createSecurityContext(WithMockCustomUser user) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();

		String userId = Long.toString(user.userId());
		String isAdmin = user.isAdmin();
		List<Authority> authorities = new ArrayList<Authority>();
		if ("Y".equalsIgnoreCase(isAdmin)) authorities.add(new Authority(Authority.ADMIN));
		Authentication auth = new UsernamePasswordAuthenticationToken(userId, null, authorities);
		context.setAuthentication(auth);
		return context;
	}
}