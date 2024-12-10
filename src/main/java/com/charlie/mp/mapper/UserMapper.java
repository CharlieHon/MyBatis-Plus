package com.charlie.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.charlie.mp.domain.po.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author: charlie
 * @CreateTime: Created in 2024/12/10 11:28
 * @Description: UserMapper
 */
public interface UserMapper extends BaseMapper<User> {
    User queryById(Long id);

    // 注解方式
    // 自定义SQL：sql + wrapper方式，把where查询封装到Wrapper，其余的自己写
    @Update("UPDATE user SET balance = balance - #{money} ${ew.customSqlSegment}")
    void deductBalanceByIds(@Param("money") int money, @Param("ew") LambdaQueryWrapper<User> wrapper);

    // xml配置方式
    List<User> queryUsersByWrapper(@Param("ew") QueryWrapper<User> wrapper);


    @Update("UPDATE user SET balance = balance - #{money} WHERE id = #{id}")
    void deductMoneyById(@Param("id") Long id, @Param("money") Integer money);
}
