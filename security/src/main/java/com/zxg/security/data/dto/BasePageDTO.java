package com.zxg.security.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasePageDTO {
    private Integer pageNo = 1;
    private Integer pageSize = 20;
}
