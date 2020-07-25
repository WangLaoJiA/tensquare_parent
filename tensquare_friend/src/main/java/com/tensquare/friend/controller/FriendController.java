package com.tensquare.friend.controller;

import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/friend")
@CrossOrigin
public class FriendController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private FriendService friendService;

    @Autowired
    private UserClient userClient;

    @RequestMapping(value = "/like/{friendid}/{type}",method = RequestMethod.PUT)
    public Result addFriend(@PathVariable("friendid") String friendid,
                            @PathVariable("type") String type){
        //x先验证是否登录了登录后才可以好友

        Claims claims = (Claims) request.getAttribute("claims_user");
        System.out.println(claims);
        if (claims==null){
            return new Result(false,StatusCode.ERROR,"权限不足");
        }
        //登录后拿取登录人的id
        String userId = claims.getId();
        System.out.println("登录时拿到的id是"+userId);

        //判断是添加好友还是添加非好友
        if (type!=null){
            if ("1".equals(type)){
                //添加好友
               int flag =friendService.addFriend(userId,friendid);
               if (flag==0){
                   return new Result(false, StatusCode.ERROR,"不能重复添加好友");
               }
               if (flag==1){
                    //feign调用  增加粉丝和关注
                   userClient.updFansAndFollow(userId,friendid,1);

                   return new Result(true, StatusCode.OK,"添加好友成功");
               }
            }else if ("2".equals(type)){
                //添加非好友
                int flag = this.friendService.addNoFriend(userId,friendid);
                if (flag==1){
                    //没有添加过
                    return new Result(true, StatusCode.OK,"添加非好友成功");

                }
                if (flag==2){
                    //添加过了
                    return new Result(false, StatusCode.ERROR,"不可以重复添加");
                }
            }
            return new Result(false, StatusCode.ERROR,"传递参数异常");

        }else {
            return new Result(false, StatusCode.ERROR,"传递参数异常");
        }
    }

    @RequestMapping(value = "/{friendid}",method = RequestMethod.DELETE)
    public Result delFriend(@PathVariable String friendid){

        Claims claims = (Claims) request.getAttribute("claims_user");
        System.out.println(claims);
        if (claims==null){
            return new Result(false,StatusCode.ERROR,"权限不足");
        }
        //登录后拿取登录人的id
        String userId = claims.getId();
        this.friendService.delFriend(userId,friendid);
        this.userClient.updFansAndFollow(userId,friendid,-1);
        return new Result(true,StatusCode.OK,"删除成功");

    }

}


