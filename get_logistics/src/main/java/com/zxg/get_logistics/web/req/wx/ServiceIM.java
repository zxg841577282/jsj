package com.zxg.get_logistics.web.req.wx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: zhou_xg
 * @Date: 2019/8/21 0021
 * @Purpose: 服务类型
 */
@Data
public class ServiceIM {
    @ApiModelProperty(value = "服务类型ID", dataType = "String")
    @NotNull(message = "服务类型ID不能为NULL")
    private Integer service_type;

    @ApiModelProperty(value = "服务名称", dataType = "String")
    @NotNull(message = "服务名称不能为NULL")
    private String service_name;
}
