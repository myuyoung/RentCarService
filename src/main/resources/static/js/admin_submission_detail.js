(function(){
  async function init(){
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || 'null');
    const token = apiClient.getAuthToken();
    if (!token || !userInfo || userInfo.role !== 'ADMIN') {
      notification.error('관리자 전용 페이지입니다.');
      window.location.href = '/login';
      return;
    }

    const id = location.pathname.split('/').pop();
    try{
      const res = await apiClient.get(`/api/admin/car-submissions/${id}`);
      if(!res.success){
        document.getElementById('submission-detail').innerHTML = '<div class="text-gray-500">상세 정보를 불러오지 못했습니다.</div>';
        return;
      }
      const s = res.data;
      renderDetail(s);
      bindActions(s.id);
    }catch(e){
      document.getElementById('submission-detail').innerHTML = '<div class="text-gray-500">상세 정보를 불러오지 못했습니다.</div>';
    }
  }

  function renderDetail(s){
    console.log('이미지 디버깅 정보:', s.imageUrls);
    
    const imageUrls = s.imageUrls || [];
    console.log('이미지 URL 배열:', imageUrls);
    
    let images = '';
    if (imageUrls.length > 0) {
      images = imageUrls.map((url) => {
        console.log('이미지 URL:', url);
        return `<img src="${url}" alt="이미지" class="w-40 h-28 object-cover rounded border" onload="console.log('이미지 로드 성공: ${url}')" onerror="console.log('이미지 로드 실패: ${url}')"/>`;
      }).join('');
    } else {
      images = '<div class="text-gray-400">이미지가 없습니다.</div>';
    }
    
    console.log('생성된 이미지 HTML:', images);
    
    const html = `
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <div class="text-sm text-gray-500">신청 ID</div>
          <div class="font-mono">${s.id}</div>
        </div>
        <div>
          <div class="text-sm text-gray-500">회원</div>
          <div>${s.memberName || '-'} <span class="text-gray-400 text-xs">(${s.memberId || '-'})</span></div>
        </div>
        <div>
          <div class="text-sm text-gray-500">차량명</div>
          <div>${s.carName || '-'}</div>
        </div>
        <div>
          <div class="text-sm text-gray-500">차량번호</div>
          <div>${s.rentCarNumber || '-'}</div>
        </div>
        <div>
          <div class="text-sm text-gray-500">요금</div>
          <div>${s.rentPrice ?? '-'}</div>
        </div>
        <div>
          <div class="text-sm text-gray-500">상태</div>
          <div>${s.status}</div>
        </div>
      </div>
      <div class="mt-6">
        <div class="text-sm text-gray-500 mb-2">업로드 이미지</div>
        <div class="flex flex-wrap gap-3">${images}</div>
      </div>`;
    document.getElementById('submission-detail').innerHTML = html;
  }

  function bindActions(id){
    document.getElementById('approve-btn').addEventListener('click', async ()=>{
      if(!confirm('이 신청을 승인하시겠습니까?')) return;
      const r = await apiClient.post(`/api/admin/car-submissions/${id}/approve`, {});
      if(r.success){
        notification.success('승인되었습니다.');
        location.reload();
      }else{
        notification.error('승인에 실패했습니다.');
      }
    });
    document.getElementById('reject-btn').addEventListener('click', async ()=>{
      if(!confirm('이 신청을 반려하시겠습니까?')) return;
      const r = await apiClient.post(`/api/admin/car-submissions/${id}/reject`, {});
      if(r.success){
        notification.success('반려되었습니다.');
        location.reload();
      }else{
        notification.error('반려에 실패했습니다.');
      }
    });
  }

  document.addEventListener('DOMContentLoaded', init);
})();


