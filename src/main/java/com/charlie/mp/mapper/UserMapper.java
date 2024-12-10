package com.charlie.mp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.charlie.mp.domain.po.User;

/**
 * @Author: charlie
 * @CreateTime: Created in 2024/12/10 11:28
 * @Description: UserMapper
 */
public interface UserMapper extends BaseMapper<User> {
    User queryById(Long id);
}
