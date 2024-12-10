package com.charlie.mp.mapper;

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
}
