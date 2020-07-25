package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/spit")
public class SpitController {

    @Autowired
    //Autowired 按照类型注入   如果类型找不到按照名称注入
    //resource  默认按照名称注入
    private SpitService spitService;
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        return new Result(true,
                StatusCode.OK,
                "吐槽查询成功",
                this.spitService.findAll());
    }

    @RequestMapping(value = "/{spitId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String spitId){
        return new Result(true,
                StatusCode.OK,
                "吐槽按照ID查询成功",
                this.spitService.findById(spitId));
    }

    @RequestMapping(value = "",method = RequestMethod.POST)
    public Result save(@RequestBody Spit spit){
        this.spitService.save(spit);
        return new Result(true,
                StatusCode.OK,
                "吐槽添加成功");
    }

    @RequestMapping(value = "/{spitId}",method = RequestMethod.PUT)
    public Result update(@PathVariable String spitId,@RequestBody Spit spit){
        spit.set_id(spitId);
        this.spitService.update(spit);
        return new Result(true,
                StatusCode.OK,
                "吐槽修改成功");
    }

    @RequestMapping(value = "/{spitId}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String spitId){
        this.spitService.deleteById(spitId);
        return new Result(true,
                StatusCode.OK,
                "吐槽删除成功,删除id是+"+spitId);
    }

    @RequestMapping(value = "/comment/{parentid}/{page}/{size}",method = RequestMethod.GET)
    public Result findByParentid(@PathVariable String parentid,
                                 @PathVariable int page,
                                 @PathVariable int size){
        Page<Spit> pageData = this.spitService.findByParentid(parentid, page, size);
        return new Result(true,StatusCode.OK,"分页成功"
                ,new PageResult<Spit>(pageData.getTotalElements(),pageData.getContent()));
    }

    @RequestMapping(value = "/thumbup/{spitId}",method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String spitId){

        //点赞成功向 redis存储一个标识
        String userid = "111";
        //判断当前用户是否已经点赞
        if (redisTemplate.opsForValue().get("thumbup_"+userid)!=null){
            return new Result(false,StatusCode.REPERROR,"不可以重复点赞");
        }

        this.spitService.thumbup(spitId);
        this.redisTemplate.opsForValue().set("thumbup_"+userid,1);

        return new Result(true,StatusCode.OK,"点赞成功");

    }


}
