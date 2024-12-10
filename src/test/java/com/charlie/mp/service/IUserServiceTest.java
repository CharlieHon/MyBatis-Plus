package com.charlie.mp.service;

import com.charlie.mp.domain.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @Author: charlie
 * @CreateTime: Created in 2024/12/10 11:30
 * @Description: 需求：批量插入10万条用户数据，并做出对比
 */
@SpringBootTest
public class IUserServiceTest {

    @Autowired
    private IUserService userService;

    // 1. 普通for循环插入
    @Test
    void testSaveOneByOne() {
        long b = System.currentTimeMillis();
        for (int i = 1; i <= 100000; i++) {
            userService.save(buildUser(i));
        }
        long e = System.currentTimeMillis();
        System.out.println("耗时：" + (e - b));    // 耗时：55737
    }

    // 2. IService的批量插入(耗时：6570)
    // 3. 开启 `rewriteBatchedStatements=true`参数(耗时：3310)
    @Test
    void testSaveBatch() {
        ArrayList<User> list = new ArrayList<>(1000);
        long b = System.currentTimeMillis();
        for (int i = 1; i <= 100000; i++) {
            list.add(buildUser(i));
            if (i % 1000 == 0) {
                userService.saveBatch(list);
                list.clear();
            }
        }
        long e = System.currentTimeMillis();
        System.out.println("耗时：" + (e - b));
    }

    // 3. 开启 `rewriteBatchedStatements=true`参数

    private User buildUser(int i) {
        User user = new User();
        user.setUsername("user_" + i);
        user.setPassword("123");
        user.setPhone("" + (18688190000L + i));
        user.setBalance(2000);
        user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(user.getCreateTime());
        return user;
    }
}
