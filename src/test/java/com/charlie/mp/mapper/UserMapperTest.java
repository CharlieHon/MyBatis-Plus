package com.charlie.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.charlie.mp.domain.po.Address;
import com.charlie.mp.domain.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: charlie
 * @CreateTime: Created in 2024/12/10 11:30
 * @Description: UserMapperTest
 */
@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    // insert()
    @Test
    void testInsert() {
        User user = new User();
        user.setId(5L);
        user.setUsername("Lucy");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
        user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    // selectById
    @Test
    void testSelectById() {
        User user = userMapper.selectById(5L);
        System.out.println("user=" + user);
    }

    // selectBatchIds
    @Test
    void testSelectByIds() {
        List<User> users = userMapper.selectBatchIds(List.of(1L, 2L, 3L));
        users.forEach(System.out::println);
    }

    // updateByIds
    @Test
    void testUpdateById() {
        User user = new User();
        user.setId(5L);
        user.setBalance(20000);
        userMapper.updateById(user);
    }

    // deleteById
    @Test
    void testDeleteById() {
        userMapper.deleteById(5L);
    }

    // 测试手写sql：queryById
    @Test
    void testQueryById() {
        User user = userMapper.queryById(1L);
        System.out.println("user=" + user);
    }

    // 2.1 条件构造器
    // QueryWrapper: 无论是修改、删除、查询，都可以使用QueryWrapper来构建查询条件
    @Test
    void testQueryWrapper() {
        // 1. 构建查询条件 where name like %o% and balance >= 1000
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .select("id", "username", "info", "balance")
                .like("username", "o")
                .ge("balance", 1000);
        // 2. 查询数据
        List<User> users = userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }

    // 更新用户名为jack的用户的余额为2000
    @Test
    void testUpdateByQueryWrapper() {
        // 1.构建查询条件 where name = "Jack"
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", "Jack");
        // 2.更新数据，user中非null字段都会作为set语句
        User user = new User();
        user.setBalance(2999);
        userMapper.update(user, wrapper);
    }

    // 更新id为1,2,4的用户的余额，扣200，SET的赋值结果是基于字段现有值的，这个时候就要利用UpdateWrapper中的setSql功能
    // UPDATE user SET balance = balance - 200 WHERE (id IN (?,?,?))
    @Test
    void testUpdateWrapper() {
        List<Long> ids = List.of(1L, 2L, 4L);
        // 1. 生成SQL
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .setSql("balance = balance - 200")  // set balance = balance - 200
                .in("id", ids);             // where id in (1, 2, 4)
        // 2.更新，注意第一个参数可以给null，也就是不使用实体对象的字段进行更新，而是基于UpdateWrapper中的setSQL来更新
        userMapper.update(null, wrapper);
    }

    // SELECT id,`username`,info,balance FROM user WHERE (`username` LIKE ? AND balance >= ?)
    @Test
    void testLambdaQueryWrapper() {
        // 1.构建条件 WHERE username LIKE "%o%" AND balance >= 1000
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .select(User::getId, User::getUsername, User::getInfo, User::getBalance)
                .like(User::getUsername, "o")
                .ge(User::getBalance, 1000);
        // 2.查询
        List<User> users = userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }


    // 2.2 自定义SQL
    // UPDATE user SET balance = balance - ? WHERE (id IN (?,?,?))
    @Test
    void testCustomWrapper() {
        // 1.准备自定义查询条件
        List<Long> ids = List.of(1L, 2L, 4L);
        //QueryWrapper<User> wrapper = new QueryWrapper<User>().in("id", ids);
        LambdaQueryWrapper<User> wrapper = new QueryWrapper<User>().lambda().in(User::getId, ids);

        // 2.调用mapper的自定义方法，直接传递Wrapper
        // 就是把wrapper传到自定义的方法中，wrapper中的条件使用特殊占位符标在SQL注解中，并且方法参数要有@Param（“ew”）
        userMapper.deductBalanceByIds(200, wrapper);
    }

    // 多表关联：查询出所有收货地址在北京的并且用户id在1、2、4之中的用户
    // SELECT * FROM user u INNER JOIN address a ON u.id = a.user_id WHERE (u.id IN (?,?,?) AND a.city = ?)
    @Test
    void testCustomJoinWrapper() {
        // 1.准备自定义查询条件
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .in("u.id", List.of(1L, 2L, 4L))
                .eq("a.city", "北京");
        // 2.调用mapper的自定义方法
        List<User> users = userMapper.queryUsersByWrapper(wrapper);
        users.forEach(System.out::println);
    }
}
