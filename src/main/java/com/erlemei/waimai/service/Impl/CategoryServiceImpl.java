package com.erlemei.waimai.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erlemei.waimai.common.CustomException;
import com.erlemei.waimai.entity.Category;
import com.erlemei.waimai.entity.Dish;
import com.erlemei.waimai.entity.Setmeal;
import com.erlemei.waimai.mapper.CategoryMapper;
import com.erlemei.waimai.service.CategoryService;
import com.erlemei.waimai.service.DishService;
import com.erlemei.waimai.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {


//   根据id删除分类，删除之前需要进行判断


  @Autowired
  private DishService dishService;
  @Autowired
  private SetmealService setmealService;



    @Override
    public void remove(Long id) {
        //构造查询条件
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
            //添加查询条件，根据分类id 进行查询
            dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
            int count1=dishService.count(dishLambdaQueryWrapper);
            //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
            if(count1>0){
                //已经关联菜品，抛出一个业务异常
           throw new CustomException("当前分类下关联了菜品，不能删除");

            }

            //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
            LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper=new LambdaQueryWrapper<>();
            //添加查询条件，根据分类id进行查询
            setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
            int count2=setmealService.count(setmealLambdaQueryWrapper);
            if(count2>0){
                //已经关联套餐，抛出一个业务异常
             throw new CustomException("当前分类下关联了套餐，不能删除");

            }


            //正常删除分类
            super.removeById(id);


    }
}
