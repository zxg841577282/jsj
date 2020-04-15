package com.zxg.back.service.adm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import entity.AdmRole;
import entity.AdmUserRole;
import lombok.RequiredArgsConstructor;
import mapper.AdmRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/15
 * @Purpose:
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdmRoleServiceImpl implements AdmRoleService {
    private final AdmRoleMapper admRoleMapper;
    private final AdmUserRoleService admUserRoleService;

    @Override
    public List<AdmRole> selectByUserId(Integer userId) {

        List<AdmUserRole> list = admUserRoleService.selectByUserId(userId);

        List<AdmRole> roleList = new ArrayList<>();
        if (list.size()>0){
            QueryWrapper<AdmRole> qw = new QueryWrapper<>();
            qw.in("id",list.stream().map(AdmUserRole::getRoleId).collect(Collectors.toList()));

            roleList = admRoleMapper.selectList(qw);
        }

        return roleList;
    }
}
