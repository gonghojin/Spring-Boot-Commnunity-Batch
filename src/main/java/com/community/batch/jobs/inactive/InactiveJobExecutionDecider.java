package com.community.batch.jobs.inactive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

import java.util.Random;

/**
 * 특정 조건에 따라 Step의 실행 여부를 설정하기 위한 방법
 * 이러한 상황에 대비해 스프링 배치에서는 흐름을 제어하는 Flow를 제공한다.
 * 이 예시에서는 랜덤하게 정수를 생성해 양수면 Step을 실행하고 음수면 행동을 취하지 않도록 Flow를 사용해 본다.
 *
 */
@Slf4j
public class InactiveJobExecutionDecider implements JobExecutionDecider {

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        if (new Random().nextInt() > 0) {
            log.info("FlowExecutionStatus.COMPLETED");
            return FlowExecutionStatus.COMPLETED;
        }

        log.info("FlowExecutionStatus.FAILED");
        return FlowExecutionStatus.FAILED;
    }
}
