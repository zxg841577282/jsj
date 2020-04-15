package entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/15
 * @Purpose:
 */
@Data
public class AdmRole implements GrantedAuthority {
    @TableId
    private Integer id;

    private String roleName;

    private Boolean isAdmin;//是否管理员

    @JsonIgnore
    @Override
    public String getAuthority() {
        return roleName;
    }
}
