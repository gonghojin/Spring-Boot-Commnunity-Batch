package com.community.batch;

import com.community.batch.domain.enums.UserStatus;
import com.community.batch.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
public class InactiveUserJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private UserRepository userRepository;

    /**
     *
     JobExecution 기본 1
    @Test
    public void 휴면_회원_전환_테스트() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(); // JobExecution은 실행 결과에 대한 정보를 담고 있다.

        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        assertEquals(0
                , userRepository
                        .findByUpdatedDateAndStatusEquals(LocalDateTime.now().minusYears(1), UserStatus.ACTIVE).size());
                        // 업데이트된 날짜가 1년 전이면 User 상탯값이 ACTIVE인 사용자들이 없어야, 휴면회원 배치 테스트가 성공
    }
     */


    /**
     * JobExecution 보완 1
     *
     * 휴면회원으로 전환하는 배치 로직에서 현재 시간 기준으로 1년 전의 날짜를 값으로 사용해 휴면전환 User를 조회했다
     * 위의 방법은 청크 단위로 Reader가 실행될 때마다 미세하게 현재 날짜값이 차이날 수 있다.
     * 따라서 JobParameter에 현재 시간을 주입해 Reader가 실행될 떄마다 모두 동일한 시간을 참조하게 설정한다.
     */
    @Test
    public void 휴면_회원_전환_테스트() throws Exception {
        Date nowDate = new Date();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(
                // JobParameterBuilder를 사용하면 간편하게 JobParameters를 생성할 수 있다.
                new JobParametersBuilder().addDate("nowDate", nowDate).
                toJobParameters());



    }
}
