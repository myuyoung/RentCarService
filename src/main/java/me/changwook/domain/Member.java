package me.changwook.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import me.changwook.DTO.MemberDTO;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id",updatable = false)
    private Long id;

    private String name;

    @Column(columnDefinition = "BOOLEAN")
    private Boolean licence = false;

    @Column(unique = true, nullable = false)
    private String email;

    private String phone;

    private String address;

    private String password;

    @Column(columnDefinition = "integer default 0")
    private int failedLoginAttempts = 0;

    //계정 잠금 해제 시간 필드 추가
    @Column
    private LocalDateTime accountLockedUntil;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "rent_id")
    private Rent rent;

    //더티 체킹을 위한 메서드
    public void updateMember(MemberDTO memberDTO){
        this.name = memberDTO.getName();
        this.licence = memberDTO.getLicence();
    }

    // 로그인 시도 관련 메서드 추가

    //로그인 실패 시 횟수증가 메서드
    public void incrementFailedLoginAttempts(){
        this.failedLoginAttempts++;
    }

    //로그인 성공시 실패 횟수와 잠금시간 초기화
    public void resetLoginAttempts(){
        this.failedLoginAttempts = 0;
        this.accountLockedUntil =null;
    }

    //로그인 실패 횟수 5회 시 로그인 잠금시간 설정
    public void lockAccount(LocalDateTime unlockTime){
        this.accountLockedUntil = unlockTime;
    }

    //로그인 잠금 시간이 존재하거나 시간이 잠금시간 이전이라면 true반환
    public boolean isAccountLocked(){
        return this.accountLockedUntil != null && LocalDateTime.now().isBefore(this.accountLockedUntil);
    }
}
