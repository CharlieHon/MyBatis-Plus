package com.charlie.mp.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: charlie
 * @CreateTime: Created in 2024/12/10 11:16
 * @Description: User
 */
@Data
public class User {

    private Long id;

    private String username;

    private String password;

    private String phone;

    private String info;

    private Integer status;

    private Integer balance;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
