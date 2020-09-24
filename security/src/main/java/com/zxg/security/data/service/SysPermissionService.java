package com.zxg.security.data.service;


import com.zxg.security.data.PermissionResp;

import java.util.List;

public interface SysPermissionService {
    List<PermissionResp> perList(Long roleId);

    /**
     * 递归删除
     * @param perIds
     */
    void recursionDelete(List<Long> perIds);

    /**
     * 递归新增
     * @param perIds
     */
    void recursionAdd(List<String> perIds,List<PermissionResp> faList,Long faId);

    /**
     * 递归编辑
     * @param copyGetNowValueList
     */
    void recursionUpdate(List<String> copyGetNowValueList,List<PermissionResp> oldList,List<PermissionResp> faList);

}
