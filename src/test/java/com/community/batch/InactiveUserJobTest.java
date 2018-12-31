package com.community.batch;

import com.community.batch.domain.enums.UserStatus;
import com.community.batch.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
public class InactiveUserJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void 휴면_회원_전환_테스트() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(); // JobExecution은 실행 결과에 대한 정보를 담고 있다.

        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        assertEquals(0
                , userRepository
                        .findByUpdatedDateAndStatusEquals(LocalDateTime.now().minusYears(1), UserStatus.ACTIVE).size());
                        // 업데이트된 날짜가 1년 전이면 User 상탯값이 ACTIVE인 사용자들이 없어야, 휴면회원 배치 테스트가 성공
    }
}
