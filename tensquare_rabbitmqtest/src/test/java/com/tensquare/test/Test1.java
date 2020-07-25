package com.tensquare.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

public class Test1 {
    public static void main(String[] args) {
        System.out.println((long) (Math.random()*1000000)+"");
    }
    @Test
    public void addddd(){
        String random = RandomStringUtils.randomNumeric(6);
        System.out.println(random);
    }
}
