package me.changwook.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "member_id",updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    private String name;

    @Column(columnDefinition = "BOOLEAN")
    @Builder.Default
    private Boolean licence = false;

    @Column(unique = true, nullable = false)
    private String email;

    private String phone;

    private String address;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.USER;

    @Column(columnDefinition = "integer default 0")
    @Builder.Default
    private int failedLoginAttempts = 0;

    //계정 잠금 해제 시간 필드 추가
    @Column
    private LocalDateTime accountLockedUntil;

    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY)
    @Builder.Default
    private List<Rent> rent = new ArrayList<>();

    //더티체킹을 위한 메서드
    public void updateMember(Member memberUpdates) {

        if(memberUpdates.getName() != null) {
            this.name = memberUpdates.getName();
        }

        if(memberUpdates.getLicence() != null) {
            this.licence = memberUpdates.getLicence();
        }

        if(memberUpdates.getEmail() != null) {
            this.email = memberUpdates.getEmail();
        }

        if(memberUpdates.getPhone() != null) {
            this.phone = memberUpdates.getPhone();
        }

        if(memberUpdates.getAddress() != null) {
            this.address = memberUpdates.getAddress();
        }

        if(memberUpdates.getPassword() != null) {
            this.password = memberUpdates.getPassword();
        }

    }

    //연관관계 편의 메서드
    public void addMemberAndRent(Rent rentItem){
        this.rent.add(rentItem);
        //반대편 설정
        rentItem.setMemberReference(this);
    }

    //연관관계 제거 메서드
    public void removeRent(Rent rentItem){
        this.rent.remove(rentItem);
        //반대편 설정
        rentItem.setMemberReference(null);
    }

    //Getter(변경 불가능 List반환)
    public List<Rent> getRent(){
        return Collections.unmodifiableList(this.rent);
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
