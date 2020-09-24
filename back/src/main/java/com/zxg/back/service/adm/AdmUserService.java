package com.zxg.back.service.adm;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import entity.AdmUser;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface AdmUserService extends UserDetailsService {

    IPage<AdmUser> selectPageList(Page page);

    AdmUser getById(Integer id);
}
