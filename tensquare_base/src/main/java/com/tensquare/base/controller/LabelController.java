package com.tensquare.base.controller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController//对象转换json不用responseBody
@CrossOrigin
@RequestMapping("/label")
//标签

public class LabelController {

    @Autowired
    private LabelService labelService;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        String header = request.getHeader("Authorization");
        System.out.println("头信息是===="+header);
        return new Result(true, StatusCode.OK,"查询成功",labelService.findAll());
    }

    @RequestMapping(value = "/{labelId}",method = RequestMethod.GET)
    public Result findById(@PathVariable("labelId") String labelId){
        //int i =  1/0;
        return new Result(true, StatusCode.OK,"查询成功",this.labelService.findLabelById(labelId));
    }

    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Label label){
        this.labelService.save(label);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    @RequestMapping(value = "/{labelId}",method = RequestMethod.PUT)
    public Result update(@PathVariable("labelId") String labelId,@RequestBody Label label){
        label.setId(labelId);
        this.labelService.update(label);
        return new Result(true, StatusCode.OK,"修改成功");
    }

    @RequestMapping(value = "/{labelId}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable("labelId") String labelId){
        this.labelService.deleteById(labelId);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    //条件查询

    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public Result findSearch(@RequestBody Label label){
        List<Label> list  =  this.labelService.findSearch(label);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }

    //条件查询

    @RequestMapping(value = "/search/{page}/{size}",method = RequestMethod.POST)
    public Result pageQuery(@PathVariable int page, @PathVariable int size, @RequestBody Label label){

        Page<Label> pageData =  this.labelService.pageQuery(label,page,size);

        return new Result(true,StatusCode.OK,"查询成功",
                new PageResult<Label>(pageData.getTotalElements(),pageData.getContent()));
    }




}
