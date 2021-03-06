package com.community.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @EnableBatchProcessing
 * 스프링 부트 배치 스타터에 미리 정의된 설정들을 실행시키는 마법의 어노테이션.
 * JobBuilder, StepBuilder, JobRepository, JobLauncher 등 다양한 설정이 자동으로 주입
 */
@EnableBatchProcessing
@Configuration
public class TestJobConfig {

    @Bean
    public JobLauncherTestUtils jobLauncherTestUtils() {
        return new JobLauncherTestUtils(); // Job 실행에 필요한 JobLauncher를 필드값으로 갖는 JobLaucherTestUtils를 빈으로 등록
    }
}
