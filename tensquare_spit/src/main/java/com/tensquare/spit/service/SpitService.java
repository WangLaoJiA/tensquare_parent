package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SpitService {

    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Spit> findAll(){
        return this.spitDao.findAll();
    }

    public Spit findById(String id){
        return this.spitDao.findById(id).get();
    }

    public void save(Spit spit){
        spit.set_id(idWorker.nextId()+"");
        spit.setPublishtime(new Date());//发布日期
        spit.setVisits(0);//浏览量
        spit.setShare(0);//分享数
        spit.setThumbup(0);//点赞数
        spit.setComment(0);//回复数
        spit.setState("1");//状态

        //判断是否有人吐槽了 给发布文章的人回复数加1
        if (spit.getParentid()!=null && !"".equals(spit.getParentid())){

            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));
            Update update = new Update();
            update.inc("comment",1);

            mongoTemplate.updateFirst(query,update,"spit");


        }

        this.spitDao.save(spit);
    }

    public void update(Spit spit){
        this.spitDao.save(spit);
    }

    public void deleteById(String id){
        this.spitDao.deleteById(id);
    }

    public Page findByParentid(String parentid,int page,int size){
        Pageable pageable = PageRequest.of(page,size);
        return this.spitDao.findByParentid(parentid,pageable);
    }

    public void thumbup(String spitId) {
        //方法1    效率有问题 先查询后添加
        /*Spit spit = this.spitDao.findById(spitId).get();
        spit.setThumbup(spit.getThumbup()==null ? 1 : spit.getThumbup()+1);
        this.spitDao.save(spit);*/

        //方法2  22
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is("1"));
        Update update = new Update();
        update.inc("thumbup",1);
        mongoTemplate.updateFirst(query,update,"spit");

    }
}
