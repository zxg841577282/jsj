package entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/15
 * @Purpose:
 */
@Data
public class AdmMenu {
    @TableId
    private Integer id;

    private String name;

    private String url;

    private String code;
}
