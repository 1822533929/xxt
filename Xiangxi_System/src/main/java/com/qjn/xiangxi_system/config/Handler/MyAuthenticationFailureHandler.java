package com.qjn.xiangxi_system.config.Handler;




import com.qjn.xiangxi_system.utils.JSONUtils;
import com.qjn.xiangxi_system.utils.ResponseUtils;
import com.qjn.xiangxi_system.utils.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 登录失败的处理器
 *
 */
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        //登录失败，执行该方法，在该方法中返回json给前端
        //登录失败的统一结果
        Result result = Result.error(exception.getMessage());

        //把R对象转成json
        String resultJSON = JSONUtils.toJSON(result);

        //把R以json返回给前端
        ResponseUtils.write(response, resultJSON);
    }
}
