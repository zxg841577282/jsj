package com.zxg.pay.web.req.abc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import other.ResultException;
import util.AssertUtils;

import javax.validation.constraints.NotNull;

/**
 * @Author: zhou_xg
 * @Date: 2020/3/10 17:29
 * @Purpose:
 */
@Data
@NoArgsConstructor
public class Orderitem {
    private String SubMerName;//二级商户名称
    private String SubMerId;//二级商户代码
    private String SubMerMCC;//二级商户 MCC 码
    private String SubMerchantRemarks;//二级商户备注项
    private String ProductID;//商品代码
    @NotNull(message = "商品名称不能为空")
    private String ProductName;//必须 商品名称
    private String UnitPrice;//商品总价
    private String Qty;//商品数量
    private String ProductRemarks;//商品备注项
    private String ProductType;//商品类型
    private String ProductDiscount;//商品折扣
    private String ProductExpiredDate;//商品有效期


    public Orderitem( String productName) {
        ProductName = productName;
        SubMerName = "";
        SubMerId = "";
        SubMerMCC = "";
        SubMerchantRemarks = "";
        ProductID = "";
        UnitPrice = "";
        Qty = "";
        ProductRemarks = "";
        ProductType = "";
        ProductDiscount = "";
        ProductExpiredDate = "";
    }

    public Orderitem(String subMerName, String subMerId, String subMerMCC, String subMerchantRemarks, String productID, String productName, String unitPrice, String qty, String productRemarks, String productType, String productDiscount, String productExpiredDate) {
        SubMerName = (AssertUtils.checkEmptyBack(subMerName));
        SubMerId = (AssertUtils.checkEmptyBack(subMerId));
        SubMerMCC = (AssertUtils.checkEmptyBack(subMerMCC));
        SubMerchantRemarks = (AssertUtils.checkEmptyBack(subMerchantRemarks));
        ProductID = (AssertUtils.checkEmptyBack(productID));
        ProductName = productName;
        UnitPrice = (AssertUtils.checkEmptyBack(unitPrice));
        Qty = (AssertUtils.checkEmptyBack(qty));
        ProductRemarks = (AssertUtils.checkEmptyBack(productRemarks));
        ProductType = (AssertUtils.checkEmptyBack(productType));
        ProductDiscount = (AssertUtils.checkEmptyBack(productDiscount));
        ProductExpiredDate = (AssertUtils.checkEmptyBack(productExpiredDate));
    }
}
