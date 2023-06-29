package com.erlemei.waimai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erlemei.waimai.common.BaseContext;
import com.erlemei.waimai.common.R;
import com.erlemei.waimai.dto.OrdersDto;
import com.erlemei.waimai.entity.OrderDetail;
import com.erlemei.waimai.entity.Orders;
import com.erlemei.waimai.service.OrderDetailService;
import com.erlemei.waimai.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orderDetail")
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private OrderService orderService;


    /**
     * 用户订单分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> page(int page, int pageSize) {
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>();
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, ordersDtoPage, "records");
        List<Orders> records = pageInfo.getRecords();
        List<OrdersDto> list = records.stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);

            Long id = item.getId();
            List<OrderDetail> orderDetail = orderDetailService.getByOrderId(id);
            if (orderDetail != null) {
                ordersDto.setOrderDetails(orderDetail);
            }
            return ordersDto;
        }).collect(Collectors.toList());
        ordersDtoPage.setRecords(list);
        return R.success(ordersDtoPage);
    }

}
