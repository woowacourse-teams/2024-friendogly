package com.happy.friendogly.config;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolConfig {

    private static final Logger log = LoggerFactory.getLogger(ThreadPoolConfig.class);

    @Bean()
    public Executor asyncThreadPoolExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setThreadNamePrefix("Async-Thread-");
        threadPoolTaskExecutor.setCorePoolSize(1);
        threadPoolTaskExecutor.setMaxPoolSize(5);
        threadPoolTaskExecutor.setQueueCapacity(40);
        threadPoolTaskExecutor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                log.warn("큐가 가득찼습니다. 가장 오래된 작업을 삭제합니다. ActiveCount: {}, PoolSize: {}, QueueSize: {}",
                        executor.getActiveCount(),
                        executor.getPoolSize(),
                        executor.getQueue().size()
                );

                if (!executor.isShutdown()) {
                    executor.getQueue().poll();
                    executor.execute(r);
                }
            }
        });
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}
