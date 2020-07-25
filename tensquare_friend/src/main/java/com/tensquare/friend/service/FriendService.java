package com.tensquare.friend.service;

import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FriendService {

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private NoFriendDao noFriendDao;

    public int addFriend(String userId, String friendid) {
        //判断userid到friendid是否有数据有 就是重复的 重复添加就返回0
        Friend f = this.friendDao.findByUseridAndFriendid(userId, friendid);
        if (null==f){
            //没有添加
            //判断对方是否喜欢了自己
            Friend df = this.friendDao.findByUseridAndFriendid(friendid, userId);
            if (null==df){
                //对方没有喜欢自己,单项喜欢
                f = new Friend();
                System.out.println("userid是=="+userId);
                f.setUserid(userId);
                f.setFriendid(friendid);
                System.out.println("friendid是=="+friendid);
                f.setIslike("0");
                this.friendDao.save(f);
            }else {
                //对方喜欢了自己
                //如果存在数据则将 两条数据的type都修改为1
                this.friendDao.updIslike("1",userId,friendid);
                this.friendDao.updIslike("1",friendid,userId);
            }
            return 1;
        }
        ///重复添加直接返回0


        return 0;
    }

    public int addNoFriend(String userId, String friendid) {
        //判断是否 已经添加
        NoFriend nof = this.noFriendDao.findByUseridAndFriendid(userId, friendid);
        if (null!=nof){
            //查询到存在数据已经添加过
            return 2;
        }
        nof = new NoFriend();
        nof.setUserid(userId);
        nof.setFriendid(friendid);
        this.noFriendDao.save(nof);
        return 1;
    }
    //删除好友
    public void delFriend(String userId, String friendid) {
        //删除好友表中的userid到friend 数据
        this.friendDao.delFriend(userId,friendid);
        //修改 你好友表中的状态为0
        this.friendDao.updIslike("0",friendid,userId);
        //非好友表中增加数据
        NoFriend noFriend = new NoFriend();
        noFriend.setUserid(userId);
        noFriend.setFriendid(friendid);
        this.noFriendDao.save(noFriend);

    }

}
