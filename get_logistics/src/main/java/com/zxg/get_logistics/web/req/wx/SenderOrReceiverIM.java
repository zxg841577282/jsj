package com.zxg.get_logistics.web.req.wx;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: zhou_xg
 * @Date: 2019/8/21 0021
 * @Purpose:
 */
@Data
public class SenderOrReceiverIM {
    @ApiModelProperty(value = "姓名", dataType = "String")
    @NotNull(message = "姓名不能为NULL")
    private String name;

    @ApiModelProperty(value = "发件人座机号码", dataType = "String")
    private String tel;

    @ApiModelProperty(value = "发件人手机号码", dataType = "String")
    private String mobile;

    @ApiModelProperty(value = "发件人公司名称", dataType = "String")
    private String company;

    @ApiModelProperty(value = "发件人邮编", dataType = "String")
    private String post_code;

    @ApiModelProperty(value = "发件人国家", dataType = "String")
    private String country;

    @ApiModelProperty(value = "发件人省份", dataType = "String")
    private String province;

    @ApiModelProperty(value = "发件人市/地区", dataType = "String")
    private String city;

    @ApiModelProperty(value = "发件人区/县", dataType = "String")
    private String area;

    @ApiModelProperty(value = "发件人详细地址", dataType = "String")
    private String address;
}
