package me.changwook.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.changwook.DTO.ChatMessageDTO;
import me.changwook.service.impl.LocalFileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ChatControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private LocalFileStorageService localFileStorageService;

    private MockMvc mockMvc;
    private WebSocketStompClient stompClient;
    private StompSession stompSession;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        
        // WebSocket 클라이언트 설정
        stompClient = new WebSocketStompClient(new SockJsClient(
                Arrays.asList(new WebSocketTransport(new StandardWebSocketClient()))));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        
        // WebSocket 연결
        String url = "ws://localhost:" + port + "/ws-stomp";
        stompSession = stompClient.connect(url, new StompSessionHandlerAdapter() {}).get(1, TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("WebSocket 연결 테스트")
    void websocketConnection_success() {
        assertThat(stompSession).isNotNull();
        assertThat(stompSession.isConnected()).isTrue();
    }

    @Test
    @DisplayName("채팅 메시지 전송 및 수신 테스트")
    void chatMessage_sendAndReceive_success() throws Exception {
        // Given
        String roomId = "test-room-1";
        BlockingQueue<ChatMessageDTO> result = new LinkedBlockingDeque<>();
        
        // 메시지 구독
        stompSession.subscribe("/sub/chat/room/" + roomId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessageDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                result.offer((ChatMessageDTO) payload);
            }
        });

        // When - 입장 메시지 전송
        ChatMessageDTO enterMessage = new ChatMessageDTO();
        enterMessage.setType(ChatMessageDTO.MessageType.ENTER);
        enterMessage.setRoomId(roomId);
        enterMessage.setSender("testUser");
        enterMessage.setMessage("");

        stompSession.send("/pub/chat/message", enterMessage);

        // Then
        ChatMessageDTO receivedMessage = result.poll(3, TimeUnit.SECONDS);
        assertThat(receivedMessage).isNotNull();
        assertThat(receivedMessage.getType()).isEqualTo(ChatMessageDTO.MessageType.ENTER);
        assertThat(receivedMessage.getRoomId()).isEqualTo(roomId);
        assertThat(receivedMessage.getSender()).isEqualTo("testUser");
        assertThat(receivedMessage.getMessage()).isEqualTo("testUser님이 입장하셨습니다.");
    }

    @Test
    @DisplayName("일반 텍스트 메시지 전송 테스트")
    void textMessage_send_success() throws Exception {
        // Given
        String roomId = "test-room-2";
        String testMessage = "안녕하세요! 테스트 메시지입니다.";
        BlockingQueue<ChatMessageDTO> result = new LinkedBlockingDeque<>();
        
        // 메시지 구독
        stompSession.subscribe("/sub/chat/room/" + roomId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessageDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                result.offer((ChatMessageDTO) payload);
            }
        });

        // When - 일반 메시지 전송
        ChatMessageDTO talkMessage = new ChatMessageDTO();
        talkMessage.setType(ChatMessageDTO.MessageType.TALK);
        talkMessage.setRoomId(roomId);
        talkMessage.setSender("testUser");
        talkMessage.setMessage(testMessage);

        stompSession.send("/pub/chat/message", talkMessage);

        // Then
        ChatMessageDTO receivedMessage = result.poll(3, TimeUnit.SECONDS);
        assertThat(receivedMessage).isNotNull();
        assertThat(receivedMessage.getType()).isEqualTo(ChatMessageDTO.MessageType.TALK);
        assertThat(receivedMessage.getRoomId()).isEqualTo(roomId);
        assertThat(receivedMessage.getSender()).isEqualTo("testUser");
        assertThat(receivedMessage.getMessage()).isEqualTo(testMessage);
    }

    @Test
    @DisplayName("이미지 파일 업로드 테스트")
    void imageUpload_success() throws Exception {
        // Given
        MockMultipartFile imageFile = new MockMultipartFile(
                "file", 
                "test-image.jpg", 
                "image/jpeg", 
                "test image content".getBytes()
        );
        
        String mockFileUrl = "http://localhost:" + port + "/media/test-uuid.jpg";
        when(localFileStorageService.saveFile(any())).thenReturn(mockFileUrl);

        // When & Then
        mockMvc.perform(multipart("/chat/upload-file")
                .file(imageFile)
                .param("roomId", "test-room-3")
                .param("sender", "testUser")
                .param("messageType", "IMAGE"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("비디오 파일 업로드 테스트")
    void videoUpload_success() throws Exception {
        // Given
        MockMultipartFile videoFile = new MockMultipartFile(
                "file", 
                "test-video.mp4", 
                "video/mp4", 
                "test video content".getBytes()
        );
        
        String mockFileUrl = "http://localhost:" + port + "/media/test-uuid.mp4";
        when(localFileStorageService.saveFile(any())).thenReturn(mockFileUrl);

        // When & Then
        mockMvc.perform(multipart("/chat/upload-file")
                .file(videoFile)
                .param("roomId", "test-room-4")
                .param("sender", "testUser")
                .param("messageType", "VIDEO"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("파일 업로드와 동시에 WebSocket 메시지 전송 테스트")
    void fileUploadWithWebSocketMessage_success() throws Exception {
        // Given
        String roomId = "test-room-5";
        BlockingQueue<ChatMessageDTO> result = new LinkedBlockingDeque<>();
        
        // 메시지 구독
        stompSession.subscribe("/sub/chat/room/" + roomId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessageDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                result.offer((ChatMessageDTO) payload);
            }
        });

        MockMultipartFile imageFile = new MockMultipartFile(
                "file", 
                "test-image.png", 
                "image/png", 
                "test image content".getBytes()
        );
        
        String mockFileUrl = "http://localhost:" + port + "/media/test-uuid.png";
        when(localFileStorageService.saveFile(any())).thenReturn(mockFileUrl);

        // When - 파일 업로드
        mockMvc.perform(multipart("/chat/upload-file")
                .file(imageFile)
                .param("roomId", roomId)
                .param("sender", "testUser")
                .param("messageType", "IMAGE"))
                .andExpect(status().isOk());

        // Then - WebSocket으로 파일 메시지가 전송되었는지 확인
        ChatMessageDTO receivedMessage = result.poll(5, TimeUnit.SECONDS);
        assertThat(receivedMessage).isNotNull();
        assertThat(receivedMessage.getType()).isEqualTo(ChatMessageDTO.MessageType.IMAGE);
        assertThat(receivedMessage.getRoomId()).isEqualTo(roomId);
        assertThat(receivedMessage.getSender()).isEqualTo("testUser");
        assertThat(receivedMessage.getFileUrl()).isEqualTo(mockFileUrl);
    }

    @Test
    @DisplayName("잘못된 파일 타입 업로드 시 예외 처리 테스트")
    void invalidFileUpload_handleError() throws Exception {
        // Given
        when(localFileStorageService.saveFile(any()))
                .thenThrow(new RuntimeException("Unsupported file type"));

        MockMultipartFile invalidFile = new MockMultipartFile(
                "file", 
                "test.txt", 
                "text/plain", 
                "test content".getBytes()
        );

        // When & Then - 에러가 발생해도 200 상태코드 반환 (에러 처리 로직이 있음)
        mockMvc.perform(multipart("/chat/upload-file")
                .file(invalidFile)
                .param("roomId", "test-room-6")
                .param("sender", "testUser")
                .param("messageType", "IMAGE"))
                .andExpect(status().isOk());
    }
}