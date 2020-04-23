package entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/15
 * @Purpose:
 */
@Data
public class AdmRole  {
    @TableId
    private Integer id;

    private String roleName;

    private Boolean isAdmin;//是否管理员

    @TableField(exist = false)
    private List<AdmMenu> menuList;


}
