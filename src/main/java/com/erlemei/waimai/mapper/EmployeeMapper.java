package com.erlemei.waimai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.erlemei.waimai.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
