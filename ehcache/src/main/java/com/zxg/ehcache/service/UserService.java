package com.zxg.ehcache.service;

import com.zxg.ehcache.entity.User;

public interface UserService {

    User getInfo(Long id);

    void save(User user);

    void delete(Long id);
}
