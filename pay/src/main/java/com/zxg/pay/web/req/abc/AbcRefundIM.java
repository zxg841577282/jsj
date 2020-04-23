package com.zxg.pay.web.req.abc;

import lombok.Data;

/**
 * @Author: zhou_xg
 * @Date: 2020/3/12 15:47
 * @Purpose:
 */
@Data
public class AbcRefundIM {
    private String orderNo;

    private String refundAmount;
}
