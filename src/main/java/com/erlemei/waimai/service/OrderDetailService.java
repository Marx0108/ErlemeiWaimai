package com.erlemei.waimai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.erlemei.waimai.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService extends IService<OrderDetail> {


    public List<OrderDetail> getByOrderId(Long orderId);

}
