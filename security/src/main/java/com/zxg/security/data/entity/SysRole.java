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
public class SysRole extends Model<SysRole> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;

    public SysRole(String name) {
        this.name = name;
    }
}
