package com.charlie.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charlie.mp.domain.po.User;

/**
 * @Author: charlie
 * @CreateTime: Created in 2024/12/10 15:10
 * @Description: IUserService
 */
public interface IUserService extends IService<User> {
    void deductBalanceById(Long id, Integer money);

    void deductBalanceById2(Long id, Integer money);
}
