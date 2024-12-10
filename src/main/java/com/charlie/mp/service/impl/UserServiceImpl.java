package com.charlie.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charlie.mp.domain.po.User;
import com.charlie.mp.mapper.UserMapper;
import com.charlie.mp.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // 需求：改造根据id修改用户余额的接口，要求如下
    //  如果扣减后余额为0，则将用户status修改为冻结状态（2）
    @Override
    @Transactional
    public void deductBalanceById2(Long id, Integer money) {
        // 1.查询用户
        User user = getById(id);
        // 2.校验用户状态
        if (user == null || user.getStatus() == 2) {
            throw new RuntimeException("用户状态异常！");
        }
        // 3.校验余额是否充足
        if (user.getBalance() < money) {
            throw new RuntimeException("用户余额不足！");
        }
        // 4. 扣减余额 update user set balance = balance - ?
        int remainBalance = user.getBalance() - money;
        lambdaUpdate()
                .set(User::getBalance, remainBalance)                       // 更新余额
                .set(remainBalance == 0, User::getStatus, 2)    // 动态判断，是否更新status
                .eq(User::getId, id)                                        // 条件
                .eq(User::getBalance, user.getBalance())                    // 乐观锁
                .update();
    }
}
