package com.zxg.security.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public SysPermission(String value,Integer type,String name) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public SysPermission(String value, String name, Long faId) {
        this.name = name;
        this.value = value;

        int aaa = 0;
        String a = value.split("_")[0];
        if (a.equals("but")){
            aaa = 2;
        }
        if (a.equals("menu")){
            aaa = 1;
        }

        this.type = aaa;
        this.faId = faId;
    }


}
