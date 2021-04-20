package com.forum.gateway.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.forum.gateway.config.RouterValidator;
import com.forum.gateway.constant.SecurityConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class JWTAuthorizationFilter implements GatewayFilter  {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired private RouterValidator routerValidator;
    
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (routerValidator.isSecured.test(request)) {
            final String token = this.getJwtToken(request);
            if (token == null) return this.onError(exchange, "Token is missing in cookie", HttpStatus.UNAUTHORIZED);
            DecodedJWT decodedJWT = this.getDecodedJWT(token);
            if (decodedJWT == null) return this.onError(exchange, "Token can't be validated", HttpStatus.UNAUTHORIZED);
            this.populateRequestWithHeaders(exchange, decodedJWT);
        }
        return chain.filter(exchange);
    }
    
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }
    
    private String getJwtToken(ServerHttpRequest request) {
        if (request == null || request.getCookies() == null || CollectionUtils.isEmpty(request.getCookies().get(SecurityConstants.COOKIE_NAME))) return null;
        return request.getCookies().get(SecurityConstants.COOKIE_NAME).get(0).getValue();
    }
    
    private DecodedJWT getDecodedJWT(String token){
        try {
            return JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
            .build()
            .verify(token);
            //TODO: need to check the member info expiry date
        } catch (Exception e){
            logger.debug(e.getMessage());
            return null;
        }
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, DecodedJWT decodedJWT) {
        exchange.getRequest().mutate()
                            .header("userId", decodedJWT.getSubject())
                            .header("userName", decodedJWT.getClaim("userName").asString())
                            .header("isAdmin", decodedJWT.getClaim("isAdmin").asString())
                            .build();
    }
}



// @Override
//     protected void doFilterInternal(HttpServletRequest req,
//                                     HttpServletResponse res,
//                                     FilterChain chain) throws IOException, ServletException {
//         if (req.getCookies() == null 
//         || !Arrays.asList(req.getCookies()).stream().anyMatch(c -> c.getName().equals(SecurityConstants.COOKIE_NAME))) {
//             chain.doFilter(req, res);
//             return;
//         }

//         UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

//         SecurityContextHolder.getContext().setAuthentication(authentication);
//         chain.doFilter(req, res);
//     }

//     private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) /*throws ApplicationException*/ {
//         String token = Arrays.asList(request.getCookies()).stream().filter(c -> c.getName().equals(SecurityConstants.COOKIE_NAME)).findFirst().map(Cookie::getValue).orElse(null);
//         if (token != null) {
//             // parse the token.
//             DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
//                     .build()
//                     .verify(token);
//             Long userId = NumberUtils.isParsable(decodedJWT.getSubject()) ? Long.parseLong(decodedJWT.getSubject()) : -1L;
//             String userName = decodedJWT.getClaim("userName").asString();
//             String isAdmin = decodedJWT.getClaim("isAdmin").asString();

//             Date expiryDate = JWT.decode(token).getExpiresAt();
//             Date issueAt = JWT.decode(token).getIssuedAt();
//             Date today = new Date();
//             if (userId > 0 && StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(isAdmin)) {
//                 try {
//                     Boolean isValidDate = memberService.validedJwtDate(userId, issueAt);
//                     if (BooleanUtils.isTrue(isValidDate) && today.before(expiryDate)) {
//                         List<Authority>authories = new ArrayList<Authority>();
//                         if ("Y".equals(isAdmin)) authories.add(new Authority(Authority.ADMIN));
//                         return new UsernamePasswordAuthenticationToken(userId.toString(), null, authories);
//                     } else throw new ApplicationException("JWT expired", HttpStatus.UNAUTHORIZED);
//                 } catch (ApplicationException e){
//                     logger.error("" + e);
//                     return null;
//                 }
//             }
//             return null;
//         }
//         return null;
//     }