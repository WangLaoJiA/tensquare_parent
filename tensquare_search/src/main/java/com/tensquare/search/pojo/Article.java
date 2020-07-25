package com.tensquare.search.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;


@Data
//es中的数据存储的是文档
@Document(indexName = "tensquare_article",type = "article")
public class Article implements Serializable {

    @Id
    private String id;
    // 是否能被搜索
    //是否分词  就表示搜索的时候是整体匹配
    //是否存储 是否在页面上显示
    @Field(index = true,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
    private String title;

    @Field(index = true,analyzer="ik_max_word",searchAnalyzer="ik_max_word")
    private String content;

    private String state;

}