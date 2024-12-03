package com.qjn.xiangxi_system.config.filter;



import com.qjn.xiangxi_system.pojo.User;
import com.qjn.xiangxi_system.utils.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenVerifyFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {

        if(Constants.WHITE_URL_LIST.contains(request.getRequestURI())||
                request.getRequestURI().contains("/img/upload/")){
            System.out.println("currentUrl"+request.getRequestURI());
            filterChain.doFilter(request,response);
        }else {
            //从前端请求中获取token
            String token = request.getHeader(Constants.TOKEN_NAME);
            //如果token为空
            if(!StringUtils.hasText(token)){

                Result result = Result.error(CodeEnum.NO_LOGIN);

                String resultJSON = JSONUtils.toJSON(result);

                ResponseUtils.write(response,resultJSON);
                return;
            }

//            System.out.println(token);
            if(!JWTUtils.verifyJWT(token)){//如果token无效
                Result result = Result.error(CodeEnum.TOKEN_ERROR);
                ResponseUtils.write(response,JSONUtils.toJSON(result));
                return;
            }

            User user = JWTUtils.parseUserFromJWT(token);
            System.out.println("user："+user);
            //在SecurityContextHolder中设置认证信息，认证处理中
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user,user.getPassword(),user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);





            filterChain.doFilter(request,response);
        }
    }
}
