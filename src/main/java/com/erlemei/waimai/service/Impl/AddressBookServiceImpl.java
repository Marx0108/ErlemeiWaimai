package com.erlemei.waimai.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.erlemei.waimai.entity.AddressBook;
import com.erlemei.waimai.mapper.AddressBookMapper;
import com.erlemei.waimai.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
