package com.tensquare.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

@Component
public class ManagerFilter extends ZuulFilter {

    @Autowired
    private JwtUtil jwtUtil;


    //过滤器类型  pre post
    @Override
    public String filterType() {
        return "pre";
    }

    //过滤器的执行顺序  数子越小表示越先执行
    @Override
    public int filterOrder() {
        return 0;
    }

    //当前过滤器是否开启 true开启
    @Override
    public boolean shouldFilter() {
        return true;
    }

    //过滤的内容  任何obj内容都表示继续执行
    //setSendzuulResponse 不再继续执行
    @Override
    public Object run() throws ZuulException {
        System.out.println("经过后台过滤器了");
        //先得到头部
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        if (request.getMethod().equals("OPTIONS")){
            return null;
        }
        if (request.getRequestURL().indexOf("login")>0){
            return null;
        }


        //得到头信息
        String header = request.getHeader("Authorization");
        if (header!=null && !"".equals(header)){
            if (header.startsWith("Bearer ")){
                String token = header.substring(7);
                //解析
                try {
                    Claims claims = jwtUtil.parseJWT(token);
                    String roles =(String) claims.get("roles");
                    if (roles.equals("admin")){
                        //转发头信息 并且放行
                        requestContext.addZuulRequestHeader("Authorization",header);
                        return null;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    requestContext.setSendZuulResponse(false);//终止运行
                }
            }
        }
        requestContext.setSendZuulResponse(false);//终止运行
        requestContext.setResponseStatusCode(403);
        requestContext.setResponseBody("权限不足");
        requestContext.getResponse().setContentType("text/html:charset=utf-8");
        return null;
    }
}
