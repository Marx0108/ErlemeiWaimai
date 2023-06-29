package com.erlemei.waimai.dto;

import com.erlemei.waimai.entity.Dish;
import com.erlemei.waimai.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {
    //菜品对应的口味数据
    private List<DishFlavor> flavors = new ArrayList<>();
    private String categoryName;
    private Integer copies;
}
