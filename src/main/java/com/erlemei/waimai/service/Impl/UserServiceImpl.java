package com.erlemei.waimai.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erlemei.waimai.entity.User;
import com.erlemei.waimai.mapper.UserMapper;
import com.erlemei.waimai.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
