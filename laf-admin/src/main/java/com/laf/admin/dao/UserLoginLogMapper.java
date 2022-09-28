package com.laf.admin.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.laf.common.entity.sys.UserLoginLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户登录日志mapper
 */
@Mapper
public interface UserLoginLogMapper extends BaseMapper<UserLoginLog> {

}
