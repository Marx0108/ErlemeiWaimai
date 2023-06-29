package com.erlemei.waimai.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erlemei.waimai.entity.Employee;
import com.erlemei.waimai.mapper.EmployeeMapper;
import com.erlemei.waimai.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService{
}
