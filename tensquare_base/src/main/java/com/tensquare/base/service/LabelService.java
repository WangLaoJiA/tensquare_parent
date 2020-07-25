package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LabelService {

    @Autowired
    private LabelDao labelDao;

    @Autowired
    private IdWorker idWorker;



    public List<Label> findAll(){
        return this.labelDao.findAll();
    }


    public Label findLabelById(String labelId){
        return this.labelDao.findById(labelId).get();
    }

    public void save(Label label){
        label.setId(this.idWorker.nextId()+"");
        this.labelDao.save(label);
    }
    public void update(Label label){
        //有id是修改
        this.labelDao.save(label);
    }

    public void deleteById(String labelId){
        this.labelDao.deleteById(labelId);
    }


    public List<Label> findSearch(Label label) {

        return this.labelDao.findAll(new Specification<Label>() {

            @Override
            public Predicate toPredicate(Root<Label> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {

                List<Predicate> list = new ArrayList<>();


                if (null!=label.getLabelname() && !"".equals(label.getLabelname())){
                    Predicate predicate = cb.like(root.get("labelname").as(String.class), "%" + label.getLabelname() + "%");
                    list.add(predicate);
                }

                if (null!=label.getState() && !"".equals(label.getState())){
                    Predicate predicate = cb.like(root.get("state").as(String.class),  label.getState() );
                    list.add(predicate);
                }


                //new 一个数组作为最终返回值的条件
                Predicate[] parr = new Predicate[list.size()];
                //把list直接转换为数组
                parr = list.toArray(parr);
                return cb.and(parr);
            }
        });

    }

    public Page<Label> pageQuery(Label label, int page, int size) {

        //封装分页对象
         Pageable pageable =  PageRequest.of(page-1,size);

        return this.labelDao.findAll(new Specification<Label>() {

            @Override
            public Predicate toPredicate(Root<Label> root,
                                         CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {

                List<Predicate> list = new ArrayList<>();


                if (null!=label.getLabelname() && !"".equals(label.getLabelname())){
                    Predicate predicate = cb.like(root.get("labelname").as(String.class), "%" + label.getLabelname() + "%");
                    list.add(predicate);
                }

                if (null!=label.getState() && !"".equals(label.getState())){
                    Predicate predicate = cb.like(root.get("state").as(String.class),  label.getState() );
                    list.add(predicate);
                }


                //new 一个数组作为最终返回值的条件
                Predicate[] parr = new Predicate[list.size()];
                //把list直接转换为数组
                parr = list.toArray(parr);
                return cb.and(parr);
            }
        }, pageable);
    }
}
