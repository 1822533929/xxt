package com.qjn.xiangxi_system.config.Handler;



import com.qjn.xiangxi_system.mapper.UserMapper;
import com.qjn.xiangxi_system.pojo.User;
import com.qjn.xiangxi_system.utils.JSONUtils;
import com.qjn.xiangxi_system.utils.JWTUtils;
import com.qjn.xiangxi_system.utils.ResponseUtils;
import com.qjn.xiangxi_system.utils.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {



    @Resource
    private UserMapper userMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        String userJSON = JSONUtils.toJSON(user);
        String jwt = JWTUtils.createJWT(userJSON);
        
        // 登录成功的统一结果
        Result<String> result = Result.success(jwt,"登陆成功");
        String resultJSON = JSONUtils.toJSON(result);
        ResponseUtils.write(response, resultJSON);
    }
}
