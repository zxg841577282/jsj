package entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/16
 * @Purpose:
 */
@Data
public class AdmRoleMenu {

    @TableId
    private Integer id;

    private Integer roleId;

    private Integer menuId;

}
