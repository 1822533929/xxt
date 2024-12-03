package com.qjn.xiangxi_system.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.code.kaptcha.Producer;
import com.qjn.xiangxi_system.utils.Result;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class SysController {
    private final static String LOGIN_CAPTCHA_SESSION_KEY = "loginCaptcha";

    private final static String LOGIN_CAPTCHA_SESSION_DATE = "loginCaptchaDate";

    /**
     * captcha expire time, millis
     */
    private final static long VALID_MILLIS_TIME = 30 * 1000 ;
    private static final Logger log = LoggerFactory.getLogger(SysController.class);

    @Resource
    private Producer captchaProducer;

    @PermitAll
    @RequestMapping("/captcha/captchaImage")
    public Result getKaptchaImage(HttpServletRequest req) throws Exception {
        String capText = captchaProducer.createText();
        
        // 获取或创建 session
        HttpSession session = req.getSession(true);
        System.out.println("生成验证码 Session ID: " + session.getId());
        System.out.println("生成的验证码: " + capText);
        
        // 存储验证码到 session
        session.setAttribute(LOGIN_CAPTCHA_SESSION_KEY, capText);
        session.setAttribute(LOGIN_CAPTCHA_SESSION_DATE, new Date());

        // 生成图片
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedImage bi = captchaProducer.createImage(capText);
        ImageIO.write(bi, "jpg", outputStream);

        byte[] imageBytes = outputStream.toByteArray();
        String base64Image = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);
        
        return Result.success(Map.of("img", base64Image));
    }

    @PermitAll
    @ResponseBody
    @PostMapping("/captcha/check")
    public Result checkHomeCaptcha(HttpServletRequest req) {
        HttpSession session = req.getSession(false); // 不创建新session
        System.out.println("验证码检查 Session ID: " + (session != null ? session.getId() : "null"));
        
        String captcha = req.getParameter("captcha");
        if (StringUtils.isEmpty(captcha)) {
            return Result.error("验证码不能为空");
        }
        System.out.println("输入的验证码: " + captcha);
        
        if (session == null) {
            return Result.error("验证码已过期");
        }
        
        String savedCaptcha = (String) session.getAttribute(LOGIN_CAPTCHA_SESSION_KEY);
        System.out.println("存储的验证码: " + savedCaptcha);
        
        Date sessionDate = (Date) session.getAttribute(LOGIN_CAPTCHA_SESSION_DATE);
        if (savedCaptcha == null || sessionDate == null) {
            return Result.error("验证码已过期");
        }
        
        if (System.currentTimeMillis() - sessionDate.getTime() > VALID_MILLIS_TIME) {
            return Result.error("验证码已过期");
        }
        
        if (!captcha.equalsIgnoreCase(savedCaptcha)) {
            return Result.error("验证码错误");
        }
        
        return Result.success();
    }

//    @GetMapping("/allAuth")
//    public Result<List<AuthTree>> authList() {
//        return Result.success(authInfoService.getAuthTreeByUserId(null));
//    }
//
//    @GetMapping("/permission/check")
//    public Result Permission(@RequestParam("permission") String permission) {
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        List<String> roleList = user.getPermissionList();
//        log.info("当前用户权限列表：{}",roleList);
//        if(roleList.contains(permission)){
//            return Result.success(true);
//        }
//        return Result.success(false);
//    }

}
