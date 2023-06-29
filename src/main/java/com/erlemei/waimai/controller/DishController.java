package com.erlemei.waimai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erlemei.waimai.common.R;
import com.erlemei.waimai.dto.DishDto;
import com.erlemei.waimai.entity.Category;
import com.erlemei.waimai.entity.Dish;
import com.erlemei.waimai.entity.DishFlavor;
import com.erlemei.waimai.service.CategoryService;
import com.erlemei.waimai.service.DishFlavorService;
import com.erlemei.waimai.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


//菜品管理
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    //    新增菜品
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

//      条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//      添加过滤条件
        queryWrapper.like(name != null, Dish::getName, name);
//        添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);
//          执行分页查询
        dishService.page(pageInfo, queryWrapper);
//          对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");


        List<Dish> records = pageInfo.getRecords();
        records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        List<DishDto> list = null;
        dishDtoPage.setRecords(list);
        return R.success(pageInfo);
    }


    //根据id查询菜品信息和对应的口味信息
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {

        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);

    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功");
    }

    //    根据条件来查询对应的菜品数据
//@GetMapping("/list")
//public R<List<Dish>> list(Dish dish){
////      构造查询条件
//    LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
//    queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//    queryWrapper.eq(Dish::getStatus,1);
//    //    添加排序条件
//    queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//
//List<Dish> list=dishService.list(queryWrapper);
//
//
//
//        return R.success(list);
//}
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
//      构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);
        //    添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
//        当前菜品的id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);
//        select * from dish_flavor where dish_id=?

            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);

            return dishDto;
        }).collect(Collectors.toList());


        return R.success(dishDtoList);
    }

    //    @DeleteMapping
//    public R<String> delete(Long ids) {
//        log.info("删除分类，id为：{}", ids);
////        categoryService.removeById(id);
//        dishService.removeById(ids);
//        return R.success("菜品信息删除成功");
//    }
    @DeleteMapping
    public R<String> delete(Long ids) {
        log.info("删除分类，id为：{}", ids);
//        categoryService.removeById(id);
        dishService.removeById(ids);
        return R.success("分类信息删除成功");
    }

//    @PostMapping("/status/{status}")
//    public R<String> ban_status(@PathVariable("status") Integer status, List<Long> ids) {
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.in(ids != null, Dish::getId, ids);
//        List<Dish> list = dishService.list(queryWrapper);
//        for (Dish dish : list) {
//            if (dish != null) {
//                dish.setStatus(status);
//                dishService.updateById(dish);
//            }
//
//        }
//        return R.success("售卖状态修改成功");
//
//    }
@PostMapping("/status/{st}")
public R<String> setStatus(@PathVariable int st, String ids){
    //处理string 转成Long
    String[] split = ids.split(",");
    List<Long> idList = Arrays.stream(split).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());

    //将每个id new出来一个Dish对象，并设置状态
    List<Dish> dishes = idList.stream().map((item) -> {
        Dish dish = new Dish();
        dish.setId(item);
        dish.setStatus(st);
        return dish;
    }).collect(Collectors.toList()); //Dish集合

    log.info("status ids : {}",ids);
    dishService.updateBatchById(dishes);//批量操作
    return R.success("操作成功");
}

}