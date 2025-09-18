// Dream Drive - 실시간 채팅 클라이언트 (중복 메시지 문제 해결 버전)

class ChatClient {
    constructor() {
        this.stompClient = null;
        this.currentRoom = null;
        this.currentUser = null;
        this.connected = false;
        // 🔧 전역 apiClient 사용으로 토큰 동기화
        this.apiClient = window.apiClient || apiClient;
        this.chatRooms = [];
        
        // 🔧 중복 방지를 위한 새로운 속성들
        this.subscription = null; // 현재 구독 객체 추적
        this.userStatus = new Map(); // 사용자별 입장/퇴장 상태 추적
        this.lastSentMessage = null; // 마지막 전송 메시지 추적
        this.isProcessingMessage = false; // 메시지 처리 중인지 확인
        this.messageHistory = new Set(); // 메시지 중복 체크용
        
        this.init();
    }

    init() {
        this.checkAuthStatus();
        this.connect();
        this.loadChatRooms();
        this.setupTokenRefreshListener();
    }

    setupTokenRefreshListener() {
        // 전역 토큰 갱신 이벤트 리스너
        window.addEventListener('tokenRefreshed', (event) => {
            console.log('📋 채팅에서 토큰 갱신 감지:', event.detail.token);
            
            // 내부 apiClient도 새 토큰으로 업데이트
            if (this.apiClient !== window.apiClient) {
                this.apiClient.saveAuthToken(event.detail.token);
            }
        });
    }

    checkAuthStatus() {
        const token = this.apiClient.getAuthToken();
        if (token) {
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
    
    enterSpecificRoom(roomId) {
        const username = document.getElementById('username').value.trim();
        
        if (!username) {
            alert('사용자명을 먼저 입력해주세요.');
            document.getElementById('username').focus();
            return;
        }
        
        document.getElementById('customRoomId').value = roomId;
        this.enterChatRoom(roomId, username);
    }

    connect() {
        const protocol = window.location.protocol === 'https:' ? 'https:' : 'http:';
        const host = window.location.host;
        const wsUrl = `${protocol}//${host}/ws-stomp`;
        
        console.log('WebSocket 연결 시도:', wsUrl);
        
        const socket = new SockJS(wsUrl);
        this.stompClient = Stomp.over(socket);
        
        this.stompClient.debug = (str) => {
            console.log('STOMP Debug:', str);
        };
        
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
        
        statusDiv.className = status === 'success' ? 
            'mb-4 p-3 rounded-md bg-green-100 text-green-800' :
            status === 'error' ?
            'mb-4 p-3 rounded-md bg-red-100 text-red-800' :
            'mb-4 p-3 rounded-md bg-yellow-100 text-yellow-800';
    }

    /**
     * 🔧 완전히 개선된 채팅방 입장 메서드 - 중복 메시지 문제 해결!
     */
    async enterChatRoom(roomId = null, username = null) {
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

        // 🔧 1단계: 기존 구독 완전히 해제 (중복 방지의 핵심!)
        if (this.subscription) {
            try {
                console.log('🔧 기존 구독 해제 중...');
                this.subscription.unsubscribe();
                this.subscription = null;
            } catch (e) {
                console.log('기존 구독 해제 중 오류:', e);
            }
        }

        // 🔧 2단계: 사용자 상태 초기화
        this.userStatus.clear();
        this.messageHistory.clear();
        this.isProcessingMessage = false;

        this.currentRoom = finalRoomId;
        this.currentUser = finalUsername;
        
        // 🔧 3단계: 메시지 영역 관리 (같은 방 재입장 체크)
        const messageArea = document.getElementById('messageArea');
        const currentRoomDisplay = document.getElementById('currentRoom');
        const isReenteringSameRoom = currentRoomDisplay.textContent === finalRoomId && messageArea.children.length > 0;
        
        if (!isReenteringSameRoom) {
            messageArea.innerHTML = '';
            await this.loadMessageHistory(finalRoomId);
        }
        
        // 🔧 4단계: 새로운 구독 생성 (중복 방지)
        console.log('🔧 새로운 구독 생성 중...');
        this.subscription = this.stompClient.subscribe(`/sub/chat/room/${finalRoomId}`, (message) => {
            try {
                const chatMessage = JSON.parse(message.body);
                this.handleReceivedMessage(chatMessage);
            } catch (e) {
                console.error('메시지 파싱 오류:', e);
            }
        });

        // 🔧 5단계: 중복 체크 후 입장 메시지 전송
        const userKey = `${finalUsername}_${finalRoomId}`;
        if (!this.userStatus.get(userKey)) {
            this.userStatus.set(userKey, true); // 입장 상태 기록
            
            const enterMessage = {
                type: 'ENTER',
                roomId: finalRoomId,
                sender: finalUsername,
                message: '',
                timestamp: Date.now() // 중복 체크용 타임스탬프
            };
            
            this.stompClient.send('/pub/chat/message', {}, JSON.stringify(enterMessage));
            console.log('🔧 입장 메시지 전송 완료');
        }
        
        // UI 업데이트
        document.getElementById('joinSection').classList.add('hidden');
        document.getElementById('chatSection').classList.remove('hidden');
        document.getElementById('currentRoom').textContent = finalRoomId;
        
        document.getElementById('messageInput').focus();
        
        console.log(`🔧 채팅방 입장 완료: ${finalRoomId} (사용자: ${finalUsername})`);
    }

    /**
     * 🔧 새로운 메시지 수신 처리 메서드 (중복 방지 로직 포함)
     */
    handleReceivedMessage(chatMessage) {
        // 🔧 메시지 중복 체크
        const messageId = this.generateMessageId(chatMessage);
        if (this.messageHistory.has(messageId)) {
            console.log('🔧 중복 메시지 감지, 무시:', messageId);
            return;
        }
        
        // 🔧 처리 중 플래그로 동시 처리 방지
        if (this.isProcessingMessage) {
            setTimeout(() => this.handleReceivedMessage(chatMessage), 100);
            return;
        }
        
        this.isProcessingMessage = true;
        
        try {
            this.messageHistory.add(messageId);
            this.displayMessage(chatMessage);
            
            // 🔧 메시지 히스토리 크기 제한 (메모리 절약)
            if (this.messageHistory.size > 1000) {
                const oldestMessage = this.messageHistory.values().next().value;
                this.messageHistory.delete(oldestMessage);
            }
        } finally {
            this.isProcessingMessage = false;
        }
    }

    /**
     * 🔧 메시지 고유 ID 생성 (중복 체크용)
     */
    generateMessageId(message) {
        const content = message.message || message.fileUrl || '';
        return `${message.type}_${message.sender}_${message.roomId}_${content}_${message.timestamp || Date.now()}`;
    }
    
    async loadMessageHistory(roomId) {
        try {
            console.log(`채팅방 ${roomId}의 메시지 히스토리를 불러오는 중...`);
            
            const response = await this.apiClient.get(`/api/chat/rooms/${roomId}/messages?limit=100`);
            
            if (response.success && response.data) {
                const messages = response.data;
                console.log(`${messages.length}개의 기존 메시지를 불러왔습니다`);
                
                messages.forEach(message => {
                    const messageDTO = this.convertEntityToDTO(message);
                    this.displayMessage(messageDTO, true);
                    
                    // 🔧 기존 메시지들도 중복 체크용 히스토리에 추가
                    const messageId = this.generateMessageId(messageDTO);
                    this.messageHistory.add(messageId);
                });
                
                const messageArea = document.getElementById('messageArea');
                messageArea.scrollTop = messageArea.scrollHeight;
                
                if (messages.length > 0) {
                    this.addMessageDivider();
                }
                
            } else {
                console.log('불러올 메시지가 없습니다');
            }
        } catch (error) {
            console.error('메시지 히스토리 로드 실패:', error);
            this.showNotification('이전 메시지를 불러오는데 실패했습니다', 'warning');
        }
    }
    
    convertEntityToDTO(entity) {
        return {
            type: entity.messageType,
            roomId: entity.roomId,
            sender: entity.sender,
            message: entity.messageContent,
            fileUrl: entity.fileUrl,
            thumbnailUrl: entity.thumbnailUrl,
            timestamp: entity.createdAt
        };
    }
    
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
    
    showNotification(message, type = 'info') {
        const notification = document.createElement('div');
        const bgColor = type === 'success' ? 'bg-green-100 text-green-800' :
                        type === 'warning' ? 'bg-yellow-100 text-yellow-800' :
                        type === 'error' ? 'bg-red-100 text-red-800' :
                        'bg-blue-100 text-blue-800';
        
        notification.className = `fixed top-4 right-4 ${bgColor} px-4 py-2 rounded-lg shadow-lg z-50`;
        notification.textContent = message;
        
        document.body.appendChild(notification);
        
        setTimeout(() => {
            if (notification.parentNode) {
                notification.parentNode.removeChild(notification);
            }
        }, 3000);
    }

    /**
     * 🔧 개선된 채팅방 퇴장 메서드
     */
    leaveChatRoom() {
        if (this.currentRoom && this.currentUser) {
            // 🔧 중복 체크 후 퇴장 메시지 전송
            const userKey = `${this.currentUser}_${this.currentRoom}`;
            if (this.userStatus.get(userKey)) {
                this.userStatus.set(userKey, false); // 퇴장 상태 기록
                
                const leaveMessage = {
                    type: 'LEAVE',
                    roomId: this.currentRoom,
                    sender: this.currentUser,
                    message: '',
                    timestamp: Date.now()
                };
                
                this.stompClient.send('/pub/chat/message', {}, JSON.stringify(leaveMessage));
                console.log('🔧 퇴장 메시지 전송 완료');
            }
        }
        
        // 🔧 구독 해제
        if (this.subscription) {
            this.subscription.unsubscribe();
            this.subscription = null;
        }
        
        // UI 복원
        document.getElementById('joinSection').classList.remove('hidden');
        document.getElementById('chatSection').classList.add('hidden');
        
        this.currentRoom = null;
        this.userStatus.clear();
    }

    /**
     * 🔧 개선된 메시지 전송 메서드 (중복 방지)
     */
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

        // 🔧 중복 전송 방지
        const messageWithTime = `${messageText}_${Date.now()}`;
        if (this.lastSentMessage === messageWithTime) {
            console.log('🔧 중복 메시지 전송 방지');
            return;
        }
        this.lastSentMessage = messageWithTime;

        const message = {
            type: 'TALK',
            roomId: this.currentRoom,
            sender: this.currentUser,
            message: messageText,
            timestamp: Date.now()
        };
        
        this.stompClient.send('/pub/chat/message', {}, JSON.stringify(message));
        messageInput.value = '';
        
        console.log('🔧 메시지 전송 완료:', messageText);
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

        if (file.size > 10 * 1024 * 1024) {
            alert('파일 크기는 10MB 이하여야 합니다.');
            return;
        }

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
            
            const messageType = file.type.startsWith('image/') ? 'IMAGE' : 'VIDEO';
            formData.append('messageType', messageType);

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
                this.showNotification('파일이 성공적으로 업로드되었습니다!', 'success');
            } else {
                throw new Error(result.message || '파일 업로드에 실패했습니다.');
            }
        } catch (error) {
            console.error('파일 업로드 오류:', error);
            this.showNotification('파일 업로드에 실패했습니다: ' + error.message, 'error');
        } finally {
            progressDiv.classList.add('hidden');
            fileInput.value = '';
        }
    }

    displayMessage(message, isHistoricalMessage = false) {
        const messageArea = document.getElementById('messageArea');
        const messageElement = document.createElement('div');
        
        const isMyMessage = message.sender === this.currentUser;
        const baseClass = `max-w-xs lg:max-w-md px-4 py-2 rounded-lg ${
            isMyMessage ? 
            'bg-indigo-600 text-white ml-auto' : 
            'bg-white text-gray-900 border'
        }`;

        let messageContent = '';
        
        const messageTime = isHistoricalMessage && message.timestamp ? 
            new Date(message.timestamp).toLocaleTimeString() : 
            new Date().toLocaleTimeString();
        
        switch (message.type) {
            case 'ENTER':
                messageElement.className = 'text-center text-gray-500 text-sm mb-2';
                messageElement.innerHTML = `
                    <div class="bg-green-100 text-green-800 rounded-full px-3 py-1 inline-block">
                        ${message.sender}님이 입장하셨습니다.
                        ${isHistoricalMessage ? '<span class="text-xs text-gray-400 ml-2">(과거)</span>' : ''}
                    </div>
                `;
                break;
                
            case 'LEAVE':
                messageElement.className = 'text-center text-gray-500 text-sm mb-2';
                messageElement.innerHTML = `
                    <div class="bg-red-100 text-red-800 rounded-full px-3 py-1 inline-block">
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
        // 🔧 완전한 정리
        if (this.subscription) {
            this.subscription.unsubscribe();
            this.subscription = null;
        }
        
        if (this.stompClient) {
            this.stompClient.disconnect();
        }
        
        this.connected = false;
        this.userStatus.clear();
        this.messageHistory.clear();
        console.log('🔧 연결 해제 완료');
    }
}

// 전역 함수들
let chatClient;

document.addEventListener('DOMContentLoaded', function() {
    chatClient = new ChatClient();
});

function enterChatRoom() {
    chatClient.enterChatRoom();
}

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

window.addEventListener('beforeunload', function() {
    if (chatClient) {
        chatClient.leaveChatRoom();
        chatClient.disconnect();
    }
});