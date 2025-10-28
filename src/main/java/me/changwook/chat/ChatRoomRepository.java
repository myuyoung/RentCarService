package me.changwook.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 채팅방 데이터 접근을 담당하는 Repository 인터페이스
 * Spring Data JPA를 활용하여 데이터베이스의 채팅방 정보를 관리합니다.
 * 
 * 이 Repository는 채팅방의 기본적인 CRUD 작업뿐만 아니라
 * 채팅방 검색, 활성 상태 관리 등의 비즈니스 로직을 지원합니다.
 */
@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {

    /**
     * 활성 상태인 모든 채팅방을 조회합니다.
     * 비활성화된 채팅방은 제외하고 현재 사용 가능한 채팅방만 반환합니다.
     * 
     * @return 활성 상태인 채팅방 목록을 생성시간 순으로 정렬하여 반환
     */
    List<ChatRoom> findByIsActiveTrueOrderByCreatedAtAsc();

    /**
     * 채팅방 이름으로 검색합니다.
     * 부분 일치 검색을 지원하여 사용자가 채팅방을 쉽게 찾을 수 있습니다.
     * 
     * @param roomName 검색할 채팅방 이름 (부분 문자열 가능)
     * @return 이름에 검색어가 포함된 활성 채팅방 목록
     */
    List<ChatRoom> findByRoomNameContainingIgnoreCaseAndIsActiveTrue(String roomName);

    /**
     * 참가자 수가 특정 값 이상인 채팅방들을 조회합니다.
     * 인기 채팅방이나 활발한 채팅방을 찾을 때 유용합니다.
     * 
     * @param minParticipants 최소 참가자 수
     * @return 조건을 만족하는 채팅방 목록을 참가자 수 내림차순으로 정렬
     */
    List<ChatRoom> findByParticipantCountGreaterThanEqualAndIsActiveTrueOrderByParticipantCountDesc(Integer minParticipants);

    /**
     * 채팅방 ID로 활성 채팅방을 조회합니다.
     * 채팅방 접근 시 해당 방이 존재하고 활성 상태인지 확인하는 데 사용됩니다.
     * 
     * @param roomId 조회할 채팅방 ID
     * @return 활성 상태인 채팅방 정보 (존재하지 않거나 비활성 상태면 empty)
     */
    Optional<ChatRoom> findByRoomIdAndIsActiveTrue(String roomId);

    /**
     * 채팅방 존재 여부를 확인합니다.
     * 새로운 채팅방 생성 시 중복 ID 체크에 사용됩니다.
     * 
     * @param roomId 확인할 채팅방 ID
     * @return 채팅방 존재 여부
     */
    boolean existsByRoomId(String roomId);

    /**
     * 모든 채팅방의 총 참가자 수를 조회하는 커스텀 쿼리입니다.
     * 시스템 전체의 채팅 활동 통계를 확인할 때 사용됩니다.
     * 
     * @return 전체 채팅방의 참가자 수 합계
     */
    @Query("SELECT COALESCE(SUM(c.participantCount), 0) FROM ChatRoom c WHERE c.isActive = true")
    Long getTotalParticipantCount();

    /**
     * 빈 채팅방(참가자가 없는 방)들을 조회합니다.
     * 정리 작업이나 비활성 채팅방 관리에 사용됩니다.
     * 
     * @return 참가자가 없는 활성 채팅방 목록
     */
    @Query("SELECT c FROM ChatRoom c WHERE c.participantCount = 0 AND c.isActive = true")
    List<ChatRoom> findEmptyRooms();

    /**
     * 최근 채팅방들을 조회합니다.
     * 참가자 수와 최근 활동을 기준으로 정렬하여 반환합니다.
     * 
     * @param limit 조회할 채팅방 수 제한
     * @return 최근 활성 채팅방 목록
     */
    @Query(value = "SELECT * FROM chat_rooms WHERE is_active = true ORDER BY created_at DESC LIMIT ?1", nativeQuery = true)
    List<ChatRoom> findRecentRooms(int limit);

    /**
     * 특정 기간 이후에 생성된 채팅방들을 조회합니다.
     * 
     * @param cutoffDate 기준 날짜
     * @return 지정된 기간 이후에 생성된 채팅방 목록
     */
    @Query("SELECT c FROM ChatRoom c WHERE c.isActive = true AND c.createdAt > :cutoffDate ORDER BY c.createdAt DESC")
    List<ChatRoom> findRecentChatRoomsAfterDate(@Param("cutoffDate") LocalDateTime cutoffDate);
}
