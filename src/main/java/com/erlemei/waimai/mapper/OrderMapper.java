package com.erlemei.waimai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erlemei.waimai.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
