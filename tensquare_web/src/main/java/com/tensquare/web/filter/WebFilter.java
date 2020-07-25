package com.tensquare.web.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class WebFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //得到request上下文
        //requestContent
        RequestContext currentContext = RequestContext.getCurrentContext();
        //得到上下域名
        HttpServletRequest request = currentContext.getRequest();
        String header = request.getHeader("Authorization");
        System.out.println("得到的header=="+header);
        //判断是否发送头信息
        if (header!=null && !"".equals(header)){
           //把头信息继续向下传递
            currentContext.addZuulRequestHeader("Authorization",header);
        }
        //继续执行
        return null;
    }
}
