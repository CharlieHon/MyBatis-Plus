package com.charlie.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charlie.mp.domain.po.User;
import com.charlie.mp.mapper.UserMapper;
import com.charlie.mp.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * @Author: charlie
 * @CreateTime: Created in 2024/12/10 15:11
 * @Description: UserServiceImpl
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    // 根据id扣减用户余额
    @Override
    public void deductBalanceById(Long id, Integer money) {
        // 1.查询用户
        User user = getById(id);
        // 2.判断用户状态
        if (user == null || user.getStatus() == 2) {
            throw new RuntimeException("用户状态异常");
        }
        // 3.判断用户余额
        if (user.getBalance() < money) {
            throw new RuntimeException("用户余额不足");
        }
        // 4.扣减余额
        baseMapper.deductMoneyById(id, money);
    }
}
