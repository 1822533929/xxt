package com.qjn.xiangxi_system.config.Handler;



import com.qjn.xiangxi_system.utils.CodeEnum;
import com.qjn.xiangxi_system.utils.JSONUtils;
import com.qjn.xiangxi_system.utils.ResponseUtils;
import com.qjn.xiangxi_system.utils.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 没有权限时的处理器
 *
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        //没有权限访问，执行该方法，在该方法中返回json给前端

        //登录失败的统一结果
        Result result = Result.error(CodeEnum.NO_AUTH);

        //把R对象转成json
        String resultJSON = JSONUtils.toJSON(result);

        //把R以json返回给前端
        ResponseUtils.write(response, resultJSON);
    }
}
