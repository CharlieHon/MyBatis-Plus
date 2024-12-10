package com.charlie.mp.controller;

import cn.hutool.core.bean.BeanUtil;
import com.charlie.mp.domain.dto.UserFormDTO;
import com.charlie.mp.domain.po.User;
import com.charlie.mp.domain.vo.UserVO;
import com.charlie.mp.service.IUserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: charlie
 * @CreateTime: Created in 2024/12/10 15:14
 * @Description: UserController
 */
@Api(tags = "用户管理接口")
@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {

    private final IUserService userService;

    /**** 使用Service方法直接实现 ***/

    // 新增用户
    @PostMapping
    public void saveUser(@RequestBody UserFormDTO userFormDTO) {
        // 1.转换DTO为PO
        User user = BeanUtil.copyProperties(userFormDTO, User.class);
        // 2.新增
        userService.save(user);
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public void removeUserById(@PathVariable("id") Long userId) {
        userService.removeById(userId);
    }

    // 根据id查询用户
    @GetMapping("/{id}")
    public UserVO queryUserById(@PathVariable("id") Long userId) {
        User user = userService.getById(userId);
        return BeanUtil.copyProperties(user, UserVO.class);
    }

    // 根据id批量查询
    @GetMapping
    public List<UserVO> queryUserByIds(@RequestParam("ids") List<Long> ids) {
        List<User> users = userService.listByIds(ids);
        return BeanUtil.copyToList(users, UserVO.class);
    }

    /**** 一些带有业务逻辑的接口则需要在service中自定义实现 ***/

    // 根据id扣减用户余额
    // UPDATE user SET balance = balance - ? WHERE id = ?
    @PutMapping("/{id}/deduction/{money}")
    public void deductBalanceById(@PathVariable("id") Long id, @PathVariable("money") Integer money) {
        userService.deductBalanceById(id, money);
    }
}
