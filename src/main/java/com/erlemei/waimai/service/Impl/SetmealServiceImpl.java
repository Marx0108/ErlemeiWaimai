package com.erlemei.waimai.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erlemei.waimai.common.CustomException;
import com.erlemei.waimai.dto.SetmealDto;
import com.erlemei.waimai.entity.Setmeal;
import com.erlemei.waimai.entity.SetmealDish;
import com.erlemei.waimai.mapper.SetmealMapper;
import com.erlemei.waimai.service.SetmealDishService;
import com.erlemei.waimai.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;


    ///新增套餐，同时需要保存套餐和菜品的关联关系
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
//保存套餐的基本信息，操作setmeal，执行insert操作
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

//保存套餐和菜品的关联信息，操作setmeal——dish，执行insert操作
        setmealDishService.saveBatch(setmealDishes);
    }

    //删除套餐，同时需要删除套餐和菜品的关联数据
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //查询套餐状态，确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<Setmeal>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);


        int count = this.count(queryWrapper);
        if (count > 0) {
            //        如果不能删除，抛出一个业务异常
            throw new CustomException("套餐正在售卖中，不能删除");

        }
//        如果可以删除，先删除套餐表中的数据--setmeal
        this.removeByIds(ids);


//        删除关系表中的数据
//     delete from setmeal_dish where setmeal_id in(1,2,3)
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getDishId, ids);

        setmealDishService.remove(lambdaQueryWrapper);


    }

    @Override
    public SetmealDto getData(Long id) {

        //因为前端给到我们的字段就是id，我们先根据这个id去查询具体的套餐
        Setmeal setmeal = this.getById(id);//根据id查询到套餐
        //构造扩展出来的setmealDto对象
        //最后我们会将所有的数据封装到setmealDto当中
        SetmealDto setmealDto = new SetmealDto();
        //构造菜品套餐关联的条件查询
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        //根据套餐来查询菜品
        queryWrapper.eq(id != null, SetmealDish::getSetmealId, id);//这里根据套餐id查询关联的菜品
        if (setmeal != null)//查询到的套餐不是空
        {
//            拷贝一下数据
            BeanUtils.copyProperties(setmeal, setmealDto);//先将套餐的的数据字段拷贝到扩展的实体类
            //这里具体对关联的菜品进行了查询
            List<SetmealDish> list = setmealDishService.list(queryWrapper);//这是查询到的菜品数据

            setmealDto.setSetmealDishes(list);//将菜品数据传过去
            return setmealDto;
        }

        return null;
    }

}

