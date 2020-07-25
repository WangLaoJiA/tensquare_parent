package com.tensquare.user.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        System.out.println("经过了拦截器");
        //无论如何都放行
        //拦截器知识把有请求头中包含token的令牌进行解析
        String header = request.getHeader("Authorization");

        if (header!=null  && !"".equals(header)){
            if(header.startsWith("Bearer ")){
                //对令牌进行 验证
                String token = header.substring(7);
                try {
                    Claims claims = jwtUtil.parseJWT(token);
                    System.out.println("工具类中id----"+claims.getId());
                    String roles = (String)claims.get("roles");
                    if (!roles.isEmpty() || roles.equals("admin")){
                        request.setAttribute("claims_admin",claims);
                    }
                    if (!roles.isEmpty() || roles.equals("user")){
                        request.setAttribute("claims_user",claims);
                    }
                }catch (Exception e){
                    throw  new RuntimeException("令牌不正确");
                }
            }
        }

        return true;
    }
}
