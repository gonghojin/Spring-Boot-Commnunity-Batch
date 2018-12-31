package com.community.batch.jobs;

import com.community.batch.domain.User;
import com.community.batch.domain.enums.UserStatus;
import com.community.batch.jobs.readers.QueueItemReader;
import com.community.batch.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Configuration
public class InactiveUserJobConfig {

    private UserRepository userRepository;

    @Bean
    public Job inactiveUserJob(JobBuilderFactory jobBuilderFactory, Step inacitveJobStep) {
        return jobBuilderFactory.get("inactiveUserJob")
                .preventRestart()
                .start(inacitveJobStep)
                .build();
    }

    @Bean
    public Step inactiveJobStep(StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("inactiveUserStep")
                .<User, User> chunk(10) // chunk는 아이템에서 커밋되는 수를 말한다
                .reader(inactiveUserReader())
                .processor(inactiveUserProcessor())
                .writer(inactiveUserWriter())
                .build();
    }

    /**
     * @StepScope
     *  기본 빈 생성은 싱글톤이지만, @StepScope를 사용하면 해당 메서드는 Step의 주기에 따라 새로운 빈을 생성한다.
     *  즉, 각 Step의 실행마다 새로 빈을 만들기 때문에 지연 생성이 가능하다.
     *  <주의할 사항은> @StepScope는 기본 프록시 모드가 반환되는 클래스 타입을 참조하기 때문에 @StepScope를 사용하면,
     *  반드시!! 구현된 반환 타입을 명시해 반환해야 한다는 것이다. 예제에서는 반환 타입을 QueueItemReader<User>라고 명시
     *  https://jojoldu.tistory.com/132
     */
    @Bean
    @StepScope
    public QueueItemReader<User> inactiveUserReader() {
        List<User> oldUsers =
                userRepository.findByUpdatedDateAndStatusEquals(
                        LocalDateTime.now().minusYears(1), UserStatus.ACTIVE
                );

        return new QueueItemReader<>(oldUsers);
    }

    // reader에서 읽은 User를 휴면 상태로 전환하는 processor 메서드
    public ItemProcessor<User, User> inactiveUserProcessor() {
        //return User :: setInactive;// 자바 8의 메서드 레퍼런스를 사용하면 이 문장하나로 끝
        // 배워보자: http://multifrontgarden.tistory.com/126
        return new ItemProcessor<User, User>() {
            @Override
            public User process(User user) throws Exception {
                return user.setInactive();
            }
        };
    }

    /**
     * 리스트 타입을 앞서 설정한 청크 단위로 받는다.
     * 청크 단위를 10으로 설정했기 때문에 휴면회원 10가 주어지며,
     * saveAll() 메서드를 사용해서 한번에 DB에 저장한다.
     */
    /**
     *  로직 이해: 람다식
     *  람다식의 형태는 매개변수를 가진 코드 블록이지만, 런타임 시에는 익명 구현 객체를 생성한다.
     *      람다식 -> 매개변수를 가진 코드 블록 -> 익명 구현 객체
     *  예를들어, Runnable 인터페이스의 익명 구현 객체를 생성하는 전형적인 코드는 다음과 같다.
     *      Runnable runnable = new Runnable() {
     *       public void run() {....}
     *      };
     *  위 코드에서 익명 구현 객체를 람다식으로 표현하면 다음과 같다.
     *  Runnable runnable = () -> {...};
     *
     *  어떤 인터페이스를 구현할 것인가는 대입되는 인터페이스가 무엇이냐에 달려있다.
     *  위 코드는 Runnable 변수에 대입되므로, 람다식은 Runnable의 익명 구현 객체를 생성한다.
     *
     *  또한 하나의 매개변수만 있다면 괄호()를 생략할 수 있고,
     *  중괄호 {}에 하나의 실행문만 있거나 return만 있을 경우 중괄호 {}도 생략할 수 있다.
     *
     *  위의 개념을 이해하면, 밑의 구조의 원리를 알 수 있을 것이다.
     */
    public ItemWriter<User> inactiveUserWriter() {
        return ((List<? extends User> users) -> userRepository.saveAll(users));
    }
}
