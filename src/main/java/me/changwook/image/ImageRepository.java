package me.changwook.image;

import me.changwook.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("SELECT i FROM Image i WHERE i.member = :member")
    List<Image> findByMember(@Param("member") Member member);

    // TODO: 날짜 범위 조회
    // List<Image> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // TODO: 파일 크기 기반 조회 (용량 관리)
    // List<Image> findByFileSizeGreaterThan(Long minSize);
}