package me.changwook.domain;

import jakarta.persistence.*;
import lombok.*;
import me.changwook.DTO.MemberDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    @Builder.Default
    private Boolean licence = false;

    @Column(unique = true, nullable = false)
    private String email;

    private String phone;

    private String address;

    private String password;



    @Column(columnDefinition = "integer default 0")
    @Builder.Default
    private int failedLoginAttempts = 0;

    //계정 잠금 해제 시간 필드 추가
    @Column
    private LocalDateTime accountLockedUntil;

    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Rent> rent = new ArrayList<>();

    //더티체킹을 위한 메서드
    public void updateMember(Member member) {

        if(member.getName() != null) {
            this.name = member.getName();
        }

        if(member.getLicence() != null) {
            this.licence = member.getLicence();
        }

        if(member.getEmail() != null) {
            this.email = member.getEmail();
        }

        if(member.getPhone() != null) {
            this.phone = member.getPhone();
        }

        if(member.getAddress() != null) {
            this.address = member.getAddress();
        }

        if(member.getPassword() != null) {
            this.password = member.getPassword();
        }

    }


    //연관관계 편의 메서드
    public void setMemberAndRent(Rent rent){
        this.rent.add(rent);
        //반대편 설정
        rent.setMemberReference(this);
    }

    //연관관계 제거 메서드
    public void removeRent(Rent rent){
        this.rent.remove(rent);
        //반대편 설정
        rent.setMemberReference(null);
    }

    //Getter(변경 불가능 List반환)
    public List<Rent> getRent(){
        return Collections.unmodifiableList(rent);
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
