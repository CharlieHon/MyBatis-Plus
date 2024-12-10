package com.charlie.mp.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("user")  // 表名注解，标识实体类对应的表
public class User {

    // 主键注解，标识实体类中的主键字段。IdType支持的类型有：
    //  1. AUTO数据库ID自增
    //  2. INPUT：insert 前自行 set 主键值
    //  3. ASSIGN_ID：雪花算法生成Long类型的全局唯一id，这是默认的ID策略
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 普通字段注解，一般不需要添加，一些特殊情况除外：
    // 1. 成员变量名与数据库字段名不一致
    // 2. 成员变量是以isXXX命名，按照JavaBean的规范，MybatisPlus识别字段时会把is去除，这就导致与数据库不符
    // 3. 成员变量名与数据库一致，但是与数据库的关键字冲突。使用@TableField注解给字段名添加转义字符：``
    // 4. 成员变量不是数据库字段，@TableField(exist = false)
    @TableField("`username`")
    private String username;

    private String password;

    private String phone;

    private String info;

    private Integer status;

    private Integer balance;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
