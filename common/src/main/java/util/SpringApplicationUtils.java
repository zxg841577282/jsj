package util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/20
 * @Purpose:
 */
@Component
public class SpringApplicationUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext app) throws BeansException {
        if(applicationContext == null) {
            applicationContext = app;
        }
    }

    //通过name获取 Bean.
    public static Object getBean(String name){
        return applicationContext.getBean(name);
    }



    //获取applicationContext
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //通过class获取Bean.
    public static <T> T getBean(Class<T> clazz){
        if (getApplicationContext() == null){
            return null;
        }
        return getApplicationContext().getBean(clazz);
    }

    //通过name,以及Clazz返回指定的Bean
    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }

}
