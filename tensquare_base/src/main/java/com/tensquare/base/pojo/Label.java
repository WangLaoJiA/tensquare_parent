package com.tensquare.base.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "tb_label")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Label implements Serializable {//分布式要实现序列化

    @Id
    private String id;
    private String labelname;
    private String state;
    private Long count;
    private Long fans;
    private String recommend;


}
