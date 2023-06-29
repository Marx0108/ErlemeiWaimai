package com.erlemei.waimai.dto;

import com.erlemei.waimai.entity.Setmeal;
import com.erlemei.waimai.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
