import http from 'k6/http';
import { check, sleep } from 'k6';
import { SharedArray } from 'k6/data';
import papaparse from 'https://jslib.k6.io/papaparse/5.1.1/index.js';
import exec from 'k6/execution'

// --- 1. CSV 데이터 로드 ---
// Spring Boot가 생성한 'dummy-users.csv' 파일을 읽어옵니다.
const users = new SharedArray('users', function () {
    const csvData = open('../dummy-users.csv');
    const parsedData = papaparse.parse(csvData, { header: true }).data;
    return parsedData;
});

// --- 2. 테스트 옵션 ---
export const options = {
    scenarios: {
        loginAndUseApi: {
            executor: 'per-vu-iterations',
            vus: 100,       // 100명의 가상 유저
            iterations: 1,  // 각 VU당 1회 실행 (테스트 시나리오에 맞게 조정)
            maxDuration: '1m',
        },
    },
};

const BASE_URL = 'http://localhost:7950'; // 실제 서버 주소

// --- 3. k6 테스트 실행 ---
export default function (data) {
    // 3-1. 각 VU(가상 유저)에게 CSV의 고유한 유저 정보 할당
    const vuIndex = exec.vu.idInTest - 1;
    const user = users[vuIndex % users.length];

    if (!user) {
        console.error(`VU ${vuIndex}: 사용할 유저 데이터가 없습니다.`);
        return;
    }

    const email = user.email;
    const password = user.password; // CSV에서 읽어온 '원본' 비밀번호

    // 3-2. 로그인 API 호출 (실제 로그인 엔드포인트로 변경)
    const loginPayload = JSON.stringify({
        email: email,
        password: password,
    });

    const params = {
        headers: { 'Content-Type': 'application/json' },
    };

    const loginRes = http.post(`${BASE_URL}/auth/login`, loginPayload, params);

    check(loginRes, {
        '[Login] status is 200': (r) => r.status === 200,
        '[Login] AccessToken received': (r) => r.json('accessToken') !== null,
    });

    // 3-3. 로그인 실패 시 중단
    if (loginRes.status !== 200) {
        console.error(`VU ${vuIndex} (${email}) 로그인 실패: ${loginRes.status}`);
        return;
    }

    // 3-4. (로그인 성공 시) AccessToken을 사용한 다른 API 호출
    const accessToken = loginRes.json('accessToken');
    const authParams = {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${accessToken}`,
        },
    };

    const myPageRes = http.get(`${BASE_URL}/api/v1/members/me`, authParams);

    check(myPageRes, {
        '[MyPage] status is 200': (r) => r.status === 200,
    });

    sleep(1);
}