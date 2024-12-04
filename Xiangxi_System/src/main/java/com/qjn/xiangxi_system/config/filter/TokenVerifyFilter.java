package com.qjn.xiangxi_system.config.filter;

import com.qjn.xiangxi_system.pojo.User;
import com.qjn.xiangxi_system.utils.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class TokenVerifyFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(TokenVerifyFilter.class);

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if(Constants.WHITE_URL_LIST.contains(requestURI) || 
                requestURI.contains("/img/upload/")){
            filterChain.doFilter(request,response);
            return;
        }

        String token = request.getHeader(Constants.TOKEN_NAME);
        if(!StringUtils.hasText(token)){
            Result result = Result.error(CodeEnum.NO_LOGIN);
            ResponseUtils.write(response,JSONUtils.toJSON(result));
            return;
        }

        if(!JWTUtils.verifyJWT(token)){
            Result result = Result.error(CodeEnum.TOKEN_ERROR);
            ResponseUtils.write(response,JSONUtils.toJSON(result));
            return;
        }

        User user = JWTUtils.parseUserFromJWT(token);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user,user.getPassword(),user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request,response);
    }
}
