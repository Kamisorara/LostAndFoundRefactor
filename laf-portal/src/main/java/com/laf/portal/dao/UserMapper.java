package com.laf.portal.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.laf.common.entity.sys.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户User Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    //根据用户名搜索用户id
    User getUserByName(String userName);

    //根据用户名查找用户id （用户注册时查找新增用户id用）
    Long selectUserIdByUserName(String userName);


}
