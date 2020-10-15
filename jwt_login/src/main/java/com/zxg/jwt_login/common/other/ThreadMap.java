package com.zxg.jwt_login.common.other;


import com.zxg.jwt_login.data.domain.AdmUser;

public class ThreadMap {
    public static ThreadLocal<AdmUser> threadLocal = new ThreadLocal<>();

    public static AdmUser getUser() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }
}
