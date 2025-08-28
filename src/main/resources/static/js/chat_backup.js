// Dream Drive - 실시간 채팅 클라이언트

class ChatClient {
    constructor() {
        this.stompClient = null;
        this.currentRoom = null;
        this.currentUser = null;
        this.connected = false;
        this.apiClient = new ApiClient();
        this.chatRooms = []; // 채팅방 목록을 저장할 배열
        
        // 초기화
        this.init();
    }

    init() {
        // 로그인 상태 확인
        this.checkAuthStatus();
        
        // WebSocket 연결
        this.connect();
        
        // 채팅방 목록 로드 (사용자가 경험한 에러를 해결하는 핵심 기능)
        this.loadChatRooms();
    }

    checkAuthStatus() {
        const token = this.apiClient.getAuthToken();
        if (token) {
            // 토큰에서 사용자 정보 추출 (간단한 디코딩)
            try {
                const payload = JSON.parse(atob(token.split('.')[1]));
                this.currentUser = payload.sub || 'User';
                document.getElementById('userInfo').textContent = `안녕하세요, ${this.currentUser}님`;
                document.getElementById('username').value = this.currentUser;
            } catch (e) {
                console.log('Token decoding failed, using default user');
                this.currentUser = 'Guest' + Math.floor(Math.random() * 1000);
                document.getElementById('username').value = this.currentUser;
            }
        } else {
            this.currentUser = 'Guest' + Math.floor(Math.random() * 1000);
            document.getElementById('username').value = this.currentUser;
        }
    }

    /**
     * 서버에서 채팅방 목록을 불러오는 핵심 메서드
     * 이 메서드가 "모집방 리스트를 찾을 수 없습니다" 에러를 해결합니다
     */
    async loadChatRooms() {
        try {
            console.log('채팅방 목록을 로드하는 중...');
            const response = await this.apiClient.get('/api/chat/rooms');
            
            if (response.success) {
                this.chatRooms = response.data;
                this.renderChatRoomsList();
                console.log('채팅방 목록 로드 성공:', this.chatRooms.length + '개의 방');
            } else {
                console.error('채팅방 목록 로드 실패:', response.message);
                this.showChatRoomsError(response.message);
            }
        } catch (error) {
            console.error('채팅방 목록 로드 중 오류:', error);
            this.showChatRoomsError('채팅방 목록을 불러오는 중 오류가 발생했습니다.');
        }
    }
    
    /**
     * 채팅방 목록을 화면에 렌더링합니다
     */
    renderChatRoomsList() {
        const container = document.getElementById('chatRoomsList');
        if (!container) return;
        
        if (this.chatRooms.length === 0) {
            container.innerHTML = `
                <div class="text-center py-8 text-gray-500">
                    <p>사용 가능한 채팅방이 없습니다.</p>
                </div>
            `;
            return;
        }
        
        const roomsHTML = this.chatRooms.map(room => `
            <div class="border rounded-lg p-4 hover:bg-gray-50 cursor-pointer transition-colors"
                 onclick="chatClient.enterSpecificRoom('${room.roomId}')">
                <div class="flex justify-between items-start mb-2">
                    <h4 class="font-semibold text-gray-900">${room.roomName}</h4>
                    <span class="text-xs text-gray-500 bg-gray-100 px-2 py-1 rounded">
                        ${room.participantCount || 0}명 참여중
                    </span>
                </div>
                <p class="text-sm text-gray-600 mb-3">${room.description}</p>
                <div class="flex justify-between items-center text-xs text-gray-400">
                    <span>방 ID: ${room.roomId}</span>
                    <span>생성일: ${new Date(room.createdAt).toLocaleDateString()}</span>
                </div>
            </div>
        `).join('');
        
        container.innerHTML = roomsHTML;
    }
    
    /**
     * 채팅방 목록 로드 실패 시 에러 메시지를 표시합니다
     */
    showChatRoomsError(message) {
        const container = document.getElementById('chatRoomsList');
        if (!container) return;
        
        container.innerHTML = `
            <div class="text-center py-8 text-red-500">
                <div class="mb-2">⚠️</div>
                <p class="mb-3">${message}</p>
                <button onclick="chatClient.loadChatRooms()" 
                        class="text-sm bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600">
                    다시 시도
                </button>
            </div>
        `;
    }
    
    /**
     * 특정 채팅방에 입장하는 메서드
     */
    enterSpecificRoom(roomId) {
        const username = document.getElementById('username').value.trim();
        
        if (!username) {
            alert('사용자명을 먼저 입력해주세요.');
            document.getElementById('username').focus();
            return;
        }
        
        // roomId를 설정하고 기존 입장 로직 사용
        document.getElementById('customRoomId').value = roomId;
        this.enterChatRoom(roomId, username);
    }

    connect() {
        // nginx 프록시 환경을 고려한 WebSocket 연결 설정
        const protocol = window.location.protocol === 'https:' ? 'https:' : 'http:';
        const host = window.location.host;
        const wsUrl = `${protocol}//${host}/ws-stomp`;
        
        console.log('WebSocket 연결 시도:', wsUrl);
        
        const socket = new SockJS(wsUrl);
        this.stompClient = Stomp.over(socket);
        
        // 디버그 모드 활성화
        this.stompClient.debug = (str) => {
            console.log('STOMP Debug:', str);
        };
        
        // 연결 설정
        this.stompClient.connect({}, 
            (frame) => {
                console.log('WebSocket Connected: ' + frame);
                this.connected = true;
                this.updateConnectionStatus('서버에 연결되었습니다', 'success');
            },
            (error) => {
                console.error('WebSocket Connection error:', error);
                this.connected = false;
                this.updateConnectionStatus('서버 연결 실패 - 재연결 시도 중...', 'error');
                
                // 5초 후 재연결 시도
                setTimeout(() => {
                    console.log('WebSocket 재연결 시도...');
                    this.connect();
                }, 5000);
            }
        );
    }

    updateConnectionStatus(message, status) {
        const statusDiv = document.getElementById('connectionStatus');
        statusDiv.innerHTML = `
            <div class="flex items-center">
                ${status === 'success' ? 
                    '<div class="w-3 h-3 bg-green-500 rounded-full mr-2"></div>' : 
                    status === 'error' ?
                    '<div class="w-3 h-3 bg-red-500 rounded-full mr-2"></div>' :
                    '<div class="loading-spinner mr-2"></div>'
                }
                <span>${message}</span>
            </div>
        `;
        
        // 상태에 따른 스타일 변경
        statusDiv.className = status === 'success' ? 
            'mb-4 p-3 rounded-md bg-green-100 text-green-800' :
            status === 'error' ?
            'mb-4 p-3 rounded-md bg-red-100 text-red-800' :
            'mb-4 p-3 rounded-md bg-yellow-100 text-yellow-800';
    }

    /**
     * 채팅방에 입장하는 개선된 메서드
     * 🎯 핵심 개선사항: 입장 시 기존 메시지 히스토리를 불러옵니다!
     * 이제 roomId와 username을 파라미터로 받아 더 유연하게 사용할 수 있습니다
     */
    async enterChatRoom(roomId = null, username = null) {
        // 파라미터로 받지 않은 경우 입력 필드에서 가져오기
        const finalUsername = username || document.getElementById('username').value.trim();
        const finalRoomId = roomId || document.getElementById('customRoomId').value.trim();
        
        if (!finalUsername) {
            alert('사용자명을 입력해주세요.');
            return;
        }
        
        if (!finalRoomId) {
            alert('채팅룸 ID를 입력해주세요.');
            return;
        }
        
        if (!this.connected) {
            alert('서버에 연결되지 않았습니다. 잠시 후 다시 시도해주세요.');
            return;
        }

        // 이전 구독이 있다면 해제 (중복 구독 방지)
        if (this.currentRoom && this.stompClient) {
            try {
                this.stompClient.unsubscribe(`/sub/chat/room/${this.currentRoom}`);
            } catch (e) {
                console.log('기존 구독 해제 중 오류:', e);
            }
        }

        this.currentRoom = finalRoomId;
        this.currentUser = finalUsername;
        
        // 🚨 중요: 같은 방에 재입장하는 경우가 아닐 때만 메시지 영역 초기화
        const messageArea = document.getElementById('messageArea');
        const currentRoomDisplay = document.getElementById('currentRoom');
        const isReenteringSameRoom = currentRoomDisplay.textContent === finalRoomId && messageArea.children.length > 0;
        
        if (!isReenteringSameRoom) {
            // 다른 방에 입장하거나 첫 입장인 경우에만 초기화
            messageArea.innerHTML = '';
            // 🎯 핵심 기능 추가: 기존 메시지 히스토리 불러오기
            await this.loadMessageHistory(finalRoomId);
        }
        
        // 채팅룸 구독 (실시간 메시지 수신)
        this.stompClient.subscribe(`/sub/chat/room/${finalRoomId}`, (message) => {
            const chatMessage = JSON.parse(message.body);
            this.displayMessage(chatMessage);
        });

        // 입장 메시지 전송
        const enterMessage = {
            type: 'ENTER',
            roomId: finalRoomId,
            sender: finalUsername,
            message: ''
        };
        
        this.stompClient.send('/pub/chat/message', {}, JSON.stringify(enterMessage));
        
        // UI 업데이트
        document.getElementById('joinSection').classList.add('hidden');
        document.getElementById('chatSection').classList.remove('hidden');
        document.getElementById('currentRoom').textContent = finalRoomId;
        
        // 메시지 입력 필드에 포커스
        document.getElementById('messageInput').focus();
        
        console.log(`채팅방 입장 완료: ${finalRoomId} (사용자: ${finalUsername})`);
    }
    
    /**
     * 🎯 새로 추가된 핵심 메서드: 채팅방의 기존 메시지 히스토리를 불러옵니다
     * 이 메서드가 메시지 지속성 문제를 해결하는 핵심 기능입니다!
     */
    async loadMessageHistory(roomId) {
        try {
            console.log(`채팅방 ${roomId}의 메시지 히스토리를 불러오는 중...`);
            
            // 새로운 API 엔드포인트 호출
            const response = await this.apiClient.get(`/api/chat/rooms/${roomId}/messages?limit=100`);
            
            if (response.success && response.data) {
                const messages = response.data;
                console.log(`${messages.length}개의 기존 메시지를 불러왔습니다`);
                
                // 기존 메시지들을 시간순으로 표시
                messages.forEach(message => {
                    // 데이터베이스 메시지를 DTO 형태로 변환
                    const messageDTO = this.convertEntityToDTO(message);
                    this.displayMessage(messageDTO, true); // 기존 메시지임을 표시
                });
                
                // 메시지 영역을 맨 아래로 스크롤
                const messageArea = document.getElementById('messageArea');
                messageArea.scrollTop = messageArea.scrollHeight;
                
                // 기존 메시지가 있는 경우 구분선 추가
                if (messages.length > 0) {
                    this.addMessageDivider();
                }
                
            } else {
                console.log('불러올 메시지가 없습니다');
            }
        } catch (error) {
            console.error('메시지 히스토리 로드 실패:', error);
            // 에러가 발생해도 채팅방 입장은 계속 진행
            this.showNotification('이전 메시지를 불러오는데 실패했습니다', 'warning');
        }
    }
    
    /**
     * 데이터베이스 엔티티를 DTO 형태로 변환하는 헬퍼 메서드
     */
    convertEntityToDTO(entity) {
        return {
            type: entity.messageType,
            roomId: entity.roomId,
            sender: entity.sender,
            message: entity.messageContent,
            fileUrl: entity.fileUrl,
            thumbnailUrl: entity.thumbnailUrl,
            timestamp: entity.createdAt // 생성 시간 추가
        };
    }
    
    /**
     * 기존 메시지와 새 메시지를 구분하는 구분선을 추가합니다
     */
    addMessageDivider() {
        const messageArea = document.getElementById('messageArea');
        const dividerElement = document.createElement('div');
        dividerElement.className = 'text-center py-4';
        dividerElement.innerHTML = `
            <div class="inline-block bg-blue-100 text-blue-800 text-xs px-3 py-1 rounded-full">
                ── 실시간 메시지 ──
            </div>
        `;
        messageArea.appendChild(dividerElement);
    }
    
    /**
     * 사용자에게 알림을 표시하는 헬퍼 메서드
     */
    showNotification(message, type = 'info') {
        const notification = document.createElement('div');
        const bgColor = type === 'success' ? 'bg-green-100 text-green-800' :
                        type === 'warning' ? 'bg-yellow-100 text-yellow-800' :
                        type === 'error' ? 'bg-red-100 text-red-800' :
                        'bg-blue-100 text-blue-800';
        
        notification.className = `fixed top-4 right-4 ${bgColor} px-4 py-2 rounded-lg shadow-lg z-50`;
        notification.textContent = message;
        
        document.body.appendChild(notification);
        
        // 3초 후 자동 제거
        setTimeout(() => {
            if (notification.parentNode) {
                notification.parentNode.removeChild(notification);
            }
        }, 3000);
    }

    leaveChatRoom() {
        if (this.currentRoom && this.currentUser) {
            // 퇴장 메시지 전송
            const leaveMessage = {
                type: 'LEAVE',
                roomId: this.currentRoom,
                sender: this.currentUser,
                message: ''
            };
            
            this.stompClient.send('/pub/chat/message', {}, JSON.stringify(leaveMessage));
        }
        
        // UI 복원 (메시지 영역은 초기화하지 않음 - 메시지 지속성 유지)
        document.getElementById('joinSection').classList.remove('hidden');
        document.getElementById('chatSection').classList.add('hidden');
        // document.getElementById('messageArea').innerHTML = ''; // 이 줄을 제거하여 메시지 지속성 유지
        
        this.currentRoom = null;
    }

    sendMessage() {
        const messageInput = document.getElementById('messageInput');
        const messageText = messageInput.value.trim();
        
        if (!messageText) {
            return;
        }
        
        if (!this.connected || !this.currentRoom) {
            alert('채팅룸에 입장해주세요.');
            return;
        }

        const message = {
            type: 'TALK',
            roomId: this.currentRoom,
            sender: this.currentUser,
            message: messageText
        };
        
        this.stompClient.send('/pub/chat/message', {}, JSON.stringify(message));
        messageInput.value = '';
    }

    async uploadFile() {
        const fileInput = document.getElementById('fileInput');
        const file = fileInput.files[0];
        
        if (!file) {
            return;
        }

        if (!this.currentRoom) {
            alert('채팅룸에 입장해주세요.');
            return;
        }

        // 파일 크기 체크 (10MB)
        if (file.size > 10 * 1024 * 1024) {
            alert('파일 크기는 10MB 이하여야 합니다.');
            return;
        }

        // 파일 타입 체크
        const allowedTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp', 'video/mp4', 'video/webm'];
        if (!allowedTypes.includes(file.type)) {
            alert('이미지 또는 비디오 파일만 업로드할 수 있습니다.');
            return;
        }

        const progressDiv = document.getElementById('uploadProgress');
        progressDiv.classList.remove('hidden');

        try {
            const formData = new FormData();
            formData.append('file', file);
            formData.append('roomId', this.currentRoom);
            formData.append('sender', this.currentUser);
            
            // 파일 타입에 따른 메시지 타입 결정
            const messageType = file.type.startsWith('image/') ? 'IMAGE' : 'VIDEO';
            formData.append('messageType', messageType);

            // 파일 업로드는 인증 없이 진행 (FormData는 자동으로 Content-Type 설정됨)
            const response = await fetch('/api/chat/upload-file', {
                method: 'POST',
                body: formData
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`HTTP ${response.status}: ${errorText}`);
            }

            const result = await response.json();
            if (result.success) {
                console.log('파일 업로드 성공:', result.data);
                alert('파일이 성공적으로 업로드되었습니다!');
            } else {
                throw new Error(result.message || '파일 업로드에 실패했습니다.');
            }
        } catch (error) {
            console.error('파일 업로드 오류:', error);
            alert('파일 업로드에 실패했습니다: ' + error.message);
        } finally {
            progressDiv.classList.add('hidden');
            fileInput.value = '';
        }
    }

    displayMessage(message, isHistoricalMessage = false) {
        const messageArea = document.getElementById('messageArea');
        const messageElement = document.createElement('div');
        
        // 메시지 타입에 따른 스타일링
        const isMyMessage = message.sender === this.currentUser;
        const baseClass = `max-w-xs lg:max-w-md px-4 py-2 rounded-lg ${
            isMyMessage ? 
            'bg-indigo-600 text-white ml-auto' : 
            'bg-white text-gray-900 border'
        }`;

        let messageContent = '';
        
        // 타임스탬프 처리 개선 (기존 메시지는 DB 타임스탬프, 새 메시지는 현재 시간)
        const messageTime = isHistoricalMessage && message.timestamp ? 
            new Date(message.timestamp).toLocaleTimeString() : 
            new Date().toLocaleTimeString();
        
        switch (message.type) {
            case 'ENTER':
                messageElement.className = 'text-center text-gray-500 text-sm mb-2';
                messageElement.innerHTML = `
                    <div class="bg-gray-100 rounded-full px-3 py-1 inline-block">
                        ${message.message}
                        ${isHistoricalMessage ? '<span class="text-xs text-gray-400 ml-2">(과거)</span>' : ''}
                    </div>
                `;
                break;
                
            case 'LEAVE':
                messageElement.className = 'text-center text-gray-500 text-sm mb-2';
                messageElement.innerHTML = `
                    <div class="bg-gray-100 rounded-full px-3 py-1 inline-block">
                        ${message.sender}님이 퇴장하셨습니다.
                        ${isHistoricalMessage ? '<span class="text-xs text-gray-400 ml-2">(과거)</span>' : ''}
                    </div>
                `;
                break;
                
            case 'TALK':
                messageElement.className = `flex mb-3 ${isMyMessage ? 'justify-end' : 'justify-start'}`;
                messageContent = `
                    <div class="${baseClass} ${isHistoricalMessage ? 'opacity-80' : ''}">
                        ${!isMyMessage ? `<div class="font-semibold text-xs mb-1 text-gray-600">${message.sender}</div>` : ''}
                        <div>${this.escapeHtml(message.message)}</div>
                        <div class="text-xs mt-1 ${isMyMessage ? 'text-indigo-200' : 'text-gray-500'}">
                            ${messageTime}
                            ${isHistoricalMessage ? ' (과거)' : ''}
                        </div>
                    </div>
                `;
                messageElement.innerHTML = messageContent;
                break;
                
            case 'IMAGE':
                messageElement.className = `flex mb-3 ${isMyMessage ? 'justify-end' : 'justify-start'}`;
                messageContent = `
                    <div class="${baseClass} ${isHistoricalMessage ? 'opacity-80' : ''}">
                        ${!isMyMessage ? `<div class="font-semibold text-xs mb-1 text-gray-600">${message.sender}</div>` : ''}
                        <div class="mb-2">
                            <img src="${message.fileUrl}" alt="Image" class="max-w-full h-auto rounded-lg cursor-pointer"
                                 onclick="window.open('${message.fileUrl}', '_blank')">
                        </div>
                        <div class="text-xs ${isMyMessage ? 'text-indigo-200' : 'text-gray-500'}">
                            ${messageTime}
                            ${isHistoricalMessage ? ' (과거)' : ''}
                        </div>
                    </div>
                `;
                messageElement.innerHTML = messageContent;
                break;
                
            case 'VIDEO':
                messageElement.className = `flex mb-3 ${isMyMessage ? 'justify-end' : 'justify-start'}`;
                messageContent = `
                    <div class="${baseClass} ${isHistoricalMessage ? 'opacity-80' : ''}">
                        ${!isMyMessage ? `<div class="font-semibold text-xs mb-1 text-gray-600">${message.sender}</div>` : ''}
                        <div class="mb-2">
                            <video controls class="max-w-full h-auto rounded-lg" style="max-height: 300px;">
                                <source src="${message.fileUrl}" type="video/mp4">
                                동영상을 재생할 수 없습니다.
                            </video>
                        </div>
                        <div class="text-xs ${isMyMessage ? 'text-indigo-200' : 'text-gray-500'}">
                            ${messageTime}
                            ${isHistoricalMessage ? ' (과거)' : ''}
                        </div>
                    </div>
                `;
                messageElement.innerHTML = messageContent;
                break;
        }
        
        messageArea.appendChild(messageElement);
        
        // 새 메시지인 경우에만 자동 스크롤 (기존 메시지 로드 시에는 스크롤하지 않음)
        if (!isHistoricalMessage) {
            messageArea.scrollTop = messageArea.scrollHeight;
        }
    }

    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    disconnect() {
        if (this.stompClient) {
            this.stompClient.disconnect();
        }
        this.connected = false;
        console.log('Disconnected');
    }
}

// 전역 함수들
let chatClient;

// 페이지 로드 시 채팅 클라이언트 초기화
document.addEventListener('DOMContentLoaded', function() {
    chatClient = new ChatClient();
});

// HTML에서 호출되는 함수들
// 기존 방식: 사용자가 직접 입력한 값으로 입장
function enterChatRoom() {
    chatClient.enterChatRoom();
}

// 새로운 방식: 사용자 정의 방 ID로 입장 (수동 입장)
function enterCustomRoom() {
    const username = document.getElementById('username').value.trim();
    const roomId = document.getElementById('customRoomId').value.trim();
    
    if (!username) {
        alert('사용자명을 먼저 입력해주세요.');
        document.getElementById('username').focus();
        return;
    }
    
    if (!roomId) {
        alert('채팅방 ID를 입력해주세요.');
        document.getElementById('customRoomId').focus();
        return;
    }
    
    chatClient.enterChatRoom(roomId, username);
}

function leaveChatRoom() {
    chatClient.leaveChatRoom();
}

function sendMessage() {
    chatClient.sendMessage();
}

function uploadFile() {
    chatClient.uploadFile();
}

function logout() {
    if (confirm('로그아웃하시겠습니까?')) {
        localStorage.removeItem('accessToken');
        window.location.href = '/login';
    }
}

// 페이지 언로드 시 연결 해제
window.addEventListener('beforeunload', function() {
    if (chatClient) {
        chatClient.leaveChatRoom();
        chatClient.disconnect();
    }
});