package com.erlemei.waimai.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erlemei.waimai.common.R;
import com.erlemei.waimai.dto.SetmealDto;
import com.erlemei.waimai.entity.Category;
import com.erlemei.waimai.entity.Setmeal;
import com.erlemei.waimai.service.CategoryService;
import com.erlemei.waimai.service.SetmealDishService;
import com.erlemei.waimai.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//套餐管理
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    //新增套餐
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("套餐信息：{}", setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    //套餐分页查询
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
//分页构造器对象
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
//    添加查询条件，根据name进行like模糊查询
        queryWrapper.like(name != null, Setmeal::getName, name);
//添加排序条件，根据更新时间降序排列
        queryWrapper.orderByDesc(Setmeal::getCreateTime);
        setmealService.page(pageInfo, queryWrapper);
//   对象拷贝
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
//        对象拷贝
            BeanUtils.copyProperties(item, setmealDto);

            //        分类id
            Long categoryId = item.getCategoryId();
//        根据分类id查询
            Category category = categoryService.getById(categoryId);
            if (category != null) {
//            分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);

            }
            return setmealDto;

        }).collect(Collectors.toList());
        dtoPage.setRecords(list);
        return R.success(dtoPage);

    }


    //删除套餐
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("ids:{}", ids);
        setmealService.removeWithDish(ids);

        return R.success("套餐数据删除成功");

    }

    //    根据条件查询套餐
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 批量修改套餐停售和启售
     *
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(
            @PathVariable("status") int status,
            @RequestParam List<Long> ids
    ) {
        List<Setmeal> setmeals = new ArrayList<>();
        for (Long id : ids) {
            Setmeal setmeal = new Setmeal();
            setmeal.setStatus(status);
            setmeal.setId(id);
            setmeals.add(setmeal);
        }
        setmealService.updateBatchById(setmeals);
        return R.success("套餐状态修改成功");
    }


    /*这里主要做一个数据回显*/
    @GetMapping("/{id}")
    public R<SetmealDto> getData(@PathVariable Long id) {
        SetmealDto setmealDto = setmealService.getData(id);
        return R.success(setmealDto);
    }

}
