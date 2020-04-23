package com.zxg.back.web.req.adm;

import entity.AdmUser;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mapper.AdmUserMapper;
import other.AbsWrapper;
import util.SpringApplicationUtils;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/20
 * @Purpose:
 */
@Getter
@Setter
public class AdmPageListIM extends AbsWrapper<AdmUser> {

    private String username;


    @Override
    public void setWrap() {
        like("username",username);
    }

    @Override
    protected void setMapper() {
        this.baseMapper = SpringApplicationUtils.getBean(AdmUserMapper.class);
    }
}
