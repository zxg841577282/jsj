/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.zxg.job.config;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 定时任务配置
 *
 * @author Mark sunlightcs@gmail.com
 * @since 2.0.0 2017-04-20
 */
@Configuration
public class ScheduleConfig {

    @Bean
    public SchedulerFactory schedulerFactory() throws SchedulerException {

        //quartz参数
        Properties prop = new Properties();
        prop.put("org.quartz.scheduler.instanceName", "RenrenScheduler");
        prop.put("org.quartz.scheduler.instanceId", "AUTO");
        //线程池配置
        prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        prop.put("org.quartz.threadPool.threadCount", "1");
        prop.put("org.quartz.threadPool.threadPriority", "5");
        //JobStore配置

        prop.put("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");

        SchedulerFactory schedulerFactory = new StdSchedulerFactory(prop);

        return schedulerFactory;
    }

    /**
     * 通过SchedulerFactory获取Scheduler的实例
     */
//    @Bean(name = "RenrenScheduler")
    @Bean
    public Scheduler scheduler(SchedulerFactory schedulerFactory) throws  SchedulerException {
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.start();
        return scheduler;
    }

}
