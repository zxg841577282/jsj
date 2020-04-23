package entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mapper.AdmUserMapper;
import other.AbsWrapper;
import util.SpringApplicationUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/14
 * @Purpose:
 */
@Getter
@Setter
public class AdmUser{
    @TableId
    private Integer id;

    private String username;

    private String password;

    private String headImg;

    private Date createTime;

    @TableField(exist = false)
    private List<AdmRole> roles;
}
