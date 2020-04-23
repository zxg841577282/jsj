package com.zxg.get_logistics.web.req.wx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: zhou_xg
 * @Date: 2019/8/21 0021
 * @Purpose: 包裹信息，将传递给快递公司
 */
@Data
public class CargoIM {
    @ApiModelProperty(value = "包裹数量", dataType = "String")
    @NotNull(message = "包裹数量不能为NULL")
    private Integer count;

    @ApiModelProperty(value = "包裹总重量，单位是千克(kg)", dataType = "String")
    @NotNull(message = "包裹总重量不能为NULL")
    private Integer weight;

    @ApiModelProperty(value = "包裹长度，单位厘米(cm)", dataType = "String")
    @NotNull(message = "包裹长度不能为NULL")
    private Integer space_x;

    @ApiModelProperty(value = "包裹宽度，单位厘米(cm)", dataType = "String")
    @NotNull(message = "包裹宽度不能为NULL")
    private Integer space_y;

    @ApiModelProperty(value = "包裹高度，单位厘米(cm)", dataType = "String")
    @NotNull(message = "包裹高度不能为NULL")
    private Integer space_z;

    private List<CargoDetailListIM> detailListIMList;
}
