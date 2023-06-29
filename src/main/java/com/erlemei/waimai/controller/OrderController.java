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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

////用户下单
//    @PostMapping("/submit")
//    public R<String> submit(@RequestBody Orders orders){
//        log.info("订单数据：{}",orders);
//        orderService.submit(orders);
//        return null;
//    }

    /**
     * 用户下单
     *
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        orderService.submit(orders);
        return R.success("下单成功");
    }


    /**
     * 订单状态修改——管理端
     *
     * @param orders
     * @return
     */
    @PutMapping
    public R<String> order(@RequestBody Orders orders) {
        //log.info("orders:{}", orders);
        Orders order = orderService.getById(orders.getId());
        if (order.getStatus() == 2) {
            orders.setStatus(3);
            orderService.updateById(orders);
            return R.success("订单派送成功");
        } else {
            orders.setStatus(4);
            orderService.updateById(orders);
            return R.success("订单已完成");
        }
    }

    /**
     * 页面显示——输入框查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, Long number, String beginTime, String endTime) {
        //log.info("beginTime:{}",beginTime);
        //log.info("endTime:{}",endTime);
        //页面构造器
        Page<Orders> pageInfo = new Page<>(page, pageSize);

        //查询所有orders表信息
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        //查询name
        if (number != null) {
            queryWrapper.like(Orders::getNumber, number);
        }
        //查询beginTime 大于等于这个时间
        if (beginTime != null) {
            queryWrapper.ge(Orders::getOrderTime, beginTime);
        }
        //查询endTime 小于等于这个时间
        if (endTime != null) {
            queryWrapper.le(Orders::getOrderTime, endTime);
        }
        queryWrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    @Autowired
    private OrderDetailService orderDetailService;


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
