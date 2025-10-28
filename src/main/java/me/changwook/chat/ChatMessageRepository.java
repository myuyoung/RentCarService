package me.changwook.chat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 채팅 메시지 데이터 접근을 담당하는 Repository 인터페이스
 * 
 * 이 Repository는 채팅 메시지의 영구 저장을 가능하게 하며,
 * 사용자가 채팅방을 나갔다가 다시 들어와도 이전 메시지들을 볼 수 있도록 합니다.
 * 메시지 조회, 검색, 통계 분석 등의 다양한 기능을 제공합니다.
 */
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    /**
     * 특정 채팅방의 모든 메시지를 시간순으로 조회합니다.
     * 이 메서드는 사용자가 채팅방에 입장할 때 기존 메시지 히스토리를 불러오는 데 사용됩니다.
     * 
     * @param roomId 조회할 채팅방 ID
     * @return 해당 채팅방의 모든 메시지를 생성시간 오름차순으로 정렬한 리스트
     */
    List<ChatMessage> findByRoomIdOrderByCreatedAtAsc(String roomId);

    /**
     * 특정 채팅방의 메시지를 페이지네이션으로 조회합니다.
     * 메시지가 많은 채팅방에서 성능 최적화를 위해 사용됩니다.
     * 
     * @param roomId 조회할 채팅방 ID
     * @param pageable 페이지 정보 (페이지 번호, 크기, 정렬 조건)
     * @return 페이지네이션된 메시지 목록
     */
    Page<ChatMessage> findByRoomIdOrderByCreatedAtAsc(String roomId, Pageable pageable);

    /**
     * 특정 채팅방의 최근 메시지들을 제한된 개수만큼 조회합니다.
     * 채팅방 입장 시 최근 대화 내용만 빠르게 로드할 때 유용합니다.
     * Spring Data JPA의 Top 키워드를 사용한 단순화된 버전
     * 
     * @param roomId 조회할 채팅방 ID
     * @return 최근 100개 메시지 목록을 시간 역순으로 정렬
     */
    List<ChatMessage> findTop100ByRoomIdOrderByCreatedAtDesc(String roomId);

    /**
     * 특정 사용자가 보낸 메시지를 조회합니다.
     * 사용자별 메시지 히스토리 조회나 관리 기능에 사용됩니다.
     * 
     * @param sender 발신자 이름
     * @return 해당 사용자가 보낸 모든 메시지를 시간순으로 정렬
     */
    List<ChatMessage> findBySenderOrderByCreatedAtDesc(String sender);

    /**
     * 특정 채팅방에서 특정 사용자가 보낸 메시지를 조회합니다.
     * 채팅방 내에서 특정 사용자의 메시지만 필터링할 때 사용됩니다.
     * 
     * @param roomId 채팅방 ID
     * @param sender 발신자 이름
     * @return 조건을 만족하는 메시지 목록
     */
    List<ChatMessage> findByRoomIdAndSenderOrderByCreatedAtAsc(String roomId, String sender);

    /**
     * 특정 메시지 타입의 메시지들을 조회합니다.
     * 예를 들어, 이미지 메시지만 조회하거나 시스템 메시지만 조회할 때 사용됩니다.
     * 
     * @param messageType 조회할 메시지 타입
     * @return 해당 타입의 모든 메시지
     */
    List<ChatMessage> findByMessageTypeOrderByCreatedAtDesc(ChatMessage.MessageType messageType);

    /**
     * 특정 채팅방의 메시지 개수를 카운트합니다.
     * 채팅방 통계 정보나 관리 목적으로 사용됩니다.
     * 
     * @param roomId 채팅방 ID
     * @return 해당 채팅방의 총 메시지 개수
     */
    long countByRoomId(String roomId);

    /**
     * 메시지 내용에서 키워드를 검색합니다.
     * 채팅 내용 검색 기능을 제공할 때 사용됩니다.
     * 
     * @param keyword 검색할 키워드
     * @return 키워드가 포함된 메시지 목록을 시간 역순으로 정렬
     */
    List<ChatMessage> findByMessageContentContainingIgnoreCaseOrderByCreatedAtDesc(String keyword);

    /**
     * 특정 채팅방에서 키워드를 검색합니다.
     * 특정 채팅방 내에서의 메시지 검색 기능에 사용됩니다.
     * 
     * @param roomId 채팅방 ID
     * @param keyword 검색할 키워드
     * @return 조건을 만족하는 메시지 목록
     */
    List<ChatMessage> findByRoomIdAndMessageContentContainingIgnoreCaseOrderByCreatedAtDesc(String roomId, String keyword);

    /**
     * 특정 기간 내의 메시지들을 조회합니다.
     * 통계 분석이나 데이터 정리 작업에 활용됩니다.
     * 
     * @param startDate 시작 일시
     * @param endDate 종료 일시
     * @return 지정된 기간 내의 메시지 목록
     */
    List<ChatMessage> findByCreatedAtBetweenOrderByCreatedAtAsc(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 특정 채팅방의 특정 기간 내 메시지들을 조회합니다.
     * 채팅방별 일일/주간/월간 메시지 분석에 사용됩니다.
     * 
     * @param roomId 채팅방 ID
     * @param startDate 시작 일시
     * @param endDate 종료 일시
     * @return 조건을 만족하는 메시지 목록
     */
    List<ChatMessage> findByRoomIdAndCreatedAtBetweenOrderByCreatedAtAsc(String roomId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 파일이 첨부된 메시지들을 조회합니다.
     * 이미지나 동영상이 포함된 메시지들만 별도로 관리할 때 사용됩니다.
     * 
     * @return 파일 URL이 존재하는 모든 메시지
     */
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.fileUrl IS NOT NULL AND cm.fileUrl != ''")
    List<ChatMessage> findMessagesWithFiles();

    /**
     * 특정 채팅방의 파일이 첨부된 메시지들을 조회합니다.
     * 채팅방별 첨부 파일 관리나 갤러리 기능에 활용됩니다.
     * 
     * @param roomId 채팅방 ID
     * @return 해당 채팅방의 파일 첨부 메시지 목록
     */
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.roomId = :roomId AND cm.fileUrl IS NOT NULL AND cm.fileUrl != '' ORDER BY cm.createdAt DESC")
    List<ChatMessage> findFileMessagesByRoomId(@Param("roomId") String roomId);

    /**
     * 채팅방별 메시지 통계를 조회하는 커스텀 쿼리입니다.
     * 관리자 대시보드나 분석 리포트에 활용됩니다.
     * 
     * @return 채팅방별 메시지 개수 통계
     */
    @Query("SELECT cm.roomId, COUNT(cm) FROM ChatMessage cm GROUP BY cm.roomId ORDER BY COUNT(cm) DESC")
    List<Object[]> getMessageCountByRoom();

    @Query("select cm from ChatMessage cm where cm.roomId = :roomId order by cm.createdAt desc limit :limit")
    List<ChatMessage> findRecentMessagesByRoomId(String roomId, int limit);
}
