package com.community.batch.jobs.inactive;

import com.community.batch.domain.User;
import com.community.batch.domain.enums.UserStatus;
import com.community.batch.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * < InacitveUserJobConfig는 청크지향 프로세싱, 현재 클래스는 Tasklet을 사용한 방식 >
 *  청크 지향 프로세싱의 이점은 무엇일까?
 *  1000여개의 데이터에 비해 배치 로직을 실행한다고 가정합시다. 청크로 나누지 않았을 떄는 하나만 실패해도 다른 성공한 999 개의 데이터가 롤백된다.
 *  그런데 청크 단위를 10으로 해서 배치 처리를 하면 도중에 배치 처리에 실패하더라도 다른 청크는 영향을 받지 않는다.
 *  이러한 이류로 스프링 배치에서는 청크 단위의 프로그래밍을 지양한다.
 *  그렇다면 반대로 청크 지향 프로세싱이 아닌 방식은 무엇일까?
 *  여기에는 Tasklet을 사용한 방식이 있습니다. Tasklet은 임의의 Step을 실행할 떄 하나의 작업으로 처리하는 방식입니다.
 *  읽기, 처리 쓰기로 나뉜 방식이 청크 지향 프로세싱이라면, 이를 단일 작업으로 만드는 개님이다.
 */
@Component
@AllArgsConstructor
public class InactiveItemTasklet implements Tasklet {

    private UserRepository userRepository;

    // 기존은 청크 지향 프로세싱 방식의 구조를 하나로 합쳐 놓은 것(읽기 -> 처리 -> 쓰기)
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        // reader
        Date nowDate = (Date) chunkContext.getStepContext().getJobParameters().get("nowDate");
        LocalDateTime now = LocalDateTime.ofInstant(nowDate.toInstant(), ZoneId.systemDefault());

        List<User> inactiveUsers =
                userRepository.findByUpdatedDateBeforeAndStatusEquals(now.minusYears(1), UserStatus.ACTIVE);

        // processor
        inactiveUsers = inactiveUsers.stream()
                .map(User :: setInactive )
                .collect(Collectors.toList());

        // writer
        userRepository.saveAll(inactiveUsers);

        return RepeatStatus.FINISHED;
    }
}
