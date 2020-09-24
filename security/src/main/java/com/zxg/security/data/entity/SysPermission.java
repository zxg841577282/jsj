package com.zxg.security.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysPermission extends Model<SysPermission> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;//权限内容

    private String value;//权限描述

    private String url;//页面路由

    private Integer type;//类型 0目录  1菜单  2按钮

    private Long faId;

    @TableField(exist = false)
    private List<SysPermission> sonList;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public enum PermissionType{
        catalog("目录",0),
        menu("菜单",1),
        button("按钮",2),
        ;

        private String desc;
        private Integer code;
    }

    public SysPermission(String value, String name, Long faId,Integer type) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.faId = faId;
    }


}
