package com.zxg.back.service.adm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import entity.AdmUserRole;
import lombok.RequiredArgsConstructor;
import mapper.AdmUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/15
 * @Purpose:
 */

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdmUserRoleServiceImpl implements AdmUserRoleService {
    private final AdmUserRoleMapper admUserRoleMapper;

    @Override
    public List<AdmUserRole> selectByUserId(Integer userId) {

        QueryWrapper<AdmUserRole> qw = new QueryWrapper<>();
        qw.eq("user_id",userId);

        return admUserRoleMapper.selectList(qw);
    }
}
