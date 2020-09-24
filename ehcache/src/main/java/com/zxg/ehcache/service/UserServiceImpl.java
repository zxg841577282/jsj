package com.zxg.ehcache.service;

import com.zxg.ehcache.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    private static final Map<Long, User> DATABASES = new HashMap<Long, User>();

    static {
        DATABASES.put(1L, new User(1L, "user1"));
        DATABASES.put(2L, new User(2L, "user2"));
        DATABASES.put(3L, new User(3L, "user3"));
    }


    @Override
    @Cacheable(key = "#id",value = "user")
    public User getInfo(Long id) {
        log.info("查询用户【id】= {}", id);
        return DATABASES.get(id);
    }

    @Override
    @CachePut(key = "#user.id",value = "user")
    public void save(User user) {
        log.info("保存用户【user】= {}", user);
        DATABASES.put(user.getId(),user);
    }

    @Override
    @CacheEvict(key = "#id",value = "user")
    public void delete(Long id) {
        log.info("删除用户【id】= {}", id);
        DATABASES.remove(id);
    }
}
