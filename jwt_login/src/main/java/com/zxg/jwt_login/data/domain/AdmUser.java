package com.zxg.jwt_login.data.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdmUser extends Model<AdmUser> {

    @TableId
    private Integer id;

    private String username;

    @JsonIgnore
    private String password;

    private LocalDateTime updateTime;


}
