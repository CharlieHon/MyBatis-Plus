package com.charlie.mp.controller;

import cn.hutool.core.bean.BeanUtil;
import com.charlie.mp.domain.dto.UserFormDTO;
import com.charlie.mp.domain.po.User;
import com.charlie.mp.domain.query.UserQuery;
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

    /**
     * 实现一个根据复杂条件查询用户的接口，查询条件如下：
     * - name：用户名关键字，可以为空
     * - status：用户状态，可以为空
     * - minBalance：最小余额，可以为空
     * - maxBalance：最大余额，可以为空
     */
    @GetMapping("/list")
    public List<UserVO> queryUsers(UserQuery query) {
        // 1.组织条件
        String username = query.getName();
        Integer status = query.getStatus();
        Integer minBalance = query.getMinBalance();
        Integer maxBalance = query.getMaxBalance();
        // 在组织查询条件的时候，我们加入了 username != null 这样的参数，意思就是当条件成立时才会添加这个查询条件，
        // 类似Mybatis的mapper.xml文件中的<if>标签。这样就实现了动态查询条件效果
        /*
        LambdaQueryWrapper<User> wrapper = new QueryWrapper<User>().lambda()
            .like(username != null, User::getUsername, username)
            .eq(status != null, User::getStatus, status)
            .ge(minBalance != null, User::getBalance, minBalance)
            .le(maxBalance != null, User::getBalance, maxBalance);
        List<User> users = userService.list(wrapper);
        */
        // 2. 查询用户
        // MybatisPlus会根据链式编程的最后一个方法来判断最终的返回结果
        //  one()：最多1个结果
        //  list()：返回集合结果
        //  count()：返回计数结果
        List<User> users = userService.lambdaQuery()
                .like(username != null, User::getUsername, username)
                .eq(status != null, User::getStatus, status)
                .ge(minBalance != null, User::getBalance, minBalance)
                .le(maxBalance != null, User::getBalance, maxBalance)
                .list();
        // 3. 处理vo
        return BeanUtil.copyToList(users, UserVO.class);
    }

    // 需求：改造根据id修改用户余额的接口，要求如下
    //  如果扣减后余额为0，则将用户status修改为冻结状态（2）
    // UPDATE user SET balance=?,status=? WHERE (id = ? AND balance = ?)
    @PutMapping("/{id}/deduct2/{money}")
    public void deductBalanceById2(@PathVariable("id") Long id, @PathVariable("money") Integer money) {
        userService.deductBalanceById2(id, money);
    }
}
