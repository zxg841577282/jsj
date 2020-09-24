package com.zxg.security.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;;
import com.zxg.security.data.dto.SysUserAddReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author wangjian
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUser extends Model<SysUser> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private String account;
    private String pwd;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer status;
    private Long roleId;

    public SysUser(String s) {
        this.id = 0L;
        this.name = "zxg";
        this.account = s;
        this.pwd = "$2a$10$pOlUtAyX6B4.uljbwik8i.AAEDR0ltMjmp49EblUmVxDHP3t/jJFe";
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.status = 1;
        this.roleId = 0L;
    }

    public SysUser(SysUserAddReq req, String pwd) {
        this.name = req.getName();
        this.account = req.getAccount();
        this.pwd = pwd;
        this.remark = req.getRemark();
        this.createTime = LocalDateTime.now();
        this.status = req.getStatus();
        this.roleId = req.getRoleId();
    }
}
