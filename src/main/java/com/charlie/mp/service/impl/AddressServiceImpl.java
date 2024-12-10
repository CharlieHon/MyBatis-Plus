package com.charlie.mp.service.impl;

import com.charlie.mp.domain.po.Address;
import com.charlie.mp.mapper.AddressMapper;
import com.charlie.mp.service.IAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author charlie
 * @since 2024-12-10
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

}
