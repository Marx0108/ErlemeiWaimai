package com.erlemei.waimai.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erlemei.waimai.entity.OrderDetail;
import com.erlemei.waimai.mapper.OrderDetailMapper;
import com.erlemei.waimai.service.OrderDetailService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {


    @Override
    public List<OrderDetail> getByOrderId(Long orderId) {
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId, orderId);
        List<OrderDetail> orderDetails = this.list(queryWrapper);
        return orderDetails;
    }


}
