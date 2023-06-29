package com.erlemei.waimai.dto;

import com.erlemei.waimai.entity.OrderDetail;
import com.erlemei.waimai.entity.Orders;
import lombok.Data;

import java.util.List;

/**
 * @Author run
 * @Description TODO
 * @Date 2022/12/20/1:50
 * @Version 1.0
 */
@Data
public class OrdersDto extends Orders {


    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;

}




