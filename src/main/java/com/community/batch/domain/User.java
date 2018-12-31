package com.community.batch.domain;

import com.community.batch.domain.enums.Grade;
import com.community.batch.domain.enums.SocialType;
import com.community.batch.domain.enums.UserStatus;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@EqualsAndHashCode(of = {"idx", "email"}) // 객체의 동등성을 비교하는 Equals와 HashCode 메서드를 구현하는 어노테이션
@Entity                                     // 비교 필드 값으로 유니크한 값인 idx와 email을 설정
@Table
@NoArgsConstructor
public class User extends BaseTimeEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column
    private String name;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String principal;

    @Column
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Builder
    public User(String name, String password, String email, String principal, SocialType socialType, UserStatus status, Grade grade) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.principal = principal;
        this.socialType = socialType;
        this.status = status;
        this.grade = grade;
    }

    public User setInactive() {
        status = UserStatus.INACTIVE;

        return this;
    }
}
