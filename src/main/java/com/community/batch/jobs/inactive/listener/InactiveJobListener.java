package com.community.batch.jobs.inactive.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j // 필드에 로그 객체를 따로 생성할 필요없이 로그 객체를 사용할 수 있도록 설정하는 롬복 어노테이션
@Component // 외부에서 InactiveJobListener를 주입받아서 사용할 수 있게 스프링 빈으로 등록
public class InactiveJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Before Job");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("After Job");
    }
}
