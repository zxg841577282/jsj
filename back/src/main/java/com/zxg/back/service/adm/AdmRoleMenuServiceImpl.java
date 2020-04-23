package com.zxg.back.service.adm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import entity.AdmRoleMenu;
import lombok.RequiredArgsConstructor;
import mapper.AdmRoleMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.ListUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/16
 * @Purpose:
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdmRoleMenuServiceImpl implements AdmRoleMenuService {
    private final AdmRoleMenuMapper admRoleMenuMapper;

    @Override
    public List<AdmRoleMenu> selectByRoleIds(List<Integer> roleList) {

        List<AdmRoleMenu> backList = new ArrayList<>();

        if (ListUtil.ListIsNull(roleList)){
            QueryWrapper<AdmRoleMenu> qw = new QueryWrapper<>();
            qw.in("role_id",roleList);
            backList = admRoleMenuMapper.selectList(qw);
        }

        return backList;
    }
}
