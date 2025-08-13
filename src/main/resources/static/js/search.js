// 검색 결과 페이지 스크립트
(function () {
  class SearchPage {
    constructor() {
      this.resultsContainer = document.getElementById('search-results');
      this.emptyView = document.getElementById('search-empty');
      this.summary = document.getElementById('search-summary');
      this.init();
    }

    init() {
      const params = utils.parseURLParams();
      this.renderSummary(params);
      this.loadResults(params);
    }

    renderSummary(params) {
      const chips = [];
      if (params.segment) chips.push(`차종: ${params.segment}`);
      if (params.fuelType) chips.push(`연료: ${params.fuelType}`);
      if (params.minPrice) chips.push(`최소가격: ${utils.formatCurrency(Number(params.minPrice))}`);
      if (params.maxPrice) chips.push(`최대가격: ${utils.formatCurrency(Number(params.maxPrice))}`);
      if (params.q) chips.push(`키워드: ${params.q}`);
      this.summary.textContent = chips.length ? chips.join(' · ') : '전체 결과';
    }

    async loadResults(params) {
      try {
        loading.show();
        const qs = new URLSearchParams(params).toString();
        const response = await apiClient.get(`/api/rentcars/search?${qs}`);
        const cars = response?.data?.content ?? [];
        this.renderList(cars);
      } catch (e) {
        console.error(e);
        notification.error('검색 결과를 불러오지 못했습니다.');
      } finally {
        loading.hide();
      }
    }

    renderList(cars) {
      if (!cars || cars.length === 0) {
        this.resultsContainer.innerHTML = '';
        this.emptyView.classList.remove('hidden');
        return;
      }

      this.emptyView.classList.add('hidden');
      this.resultsContainer.innerHTML = '';

      cars.forEach((car) => {
        this.resultsContainer.appendChild(this.createCarElement(car));
      });
    }

    createCarElement(car) {
      const el = document.createElement('div');
      el.className = 'group relative car-card';

      const imageUrl = car.imageUrl ||
        `https://placehold.co/600x400/e2e8f0/4a5568?text=${encodeURIComponent(car.name)}`;

      el.innerHTML = `
        <div class="w-full min-h-80 bg-gray-200 aspect-w-1 aspect-h-1 rounded-md overflow-hidden group-hover:opacity-75 lg:h-80 lg:aspect-none">
          <img src="${imageUrl}" alt="${car.name}" class="car-image" onerror="this.src='https://placehold.co/600x400/e2e8f0/4a5568?text=${encodeURIComponent(car.name)}'" />
        </div>
        <div class="mt-4 flex justify-between">
          <div class="flex-1">
            <h3 class="text-sm text-gray-700 font-medium">
              <a href="/car-detail?id=${car.id || ''}" class="hover:text-indigo-600">
                <span aria-hidden="true" class="absolute inset-0"></span>
                ${car.name}
              </a>
            </h3>
            <p class="mt-1 text-sm text-gray-500">${car.rentCarNumber || '정보 없음'}</p>
            <div class="mt-2 flex flex-wrap gap-1">
              ${car.category ? `<span class="badge badge-primary">${car.category}</span>` : ''}
              ${car.fuelType ? `<span class="badge badge-success">${car.fuelType}</span>` : ''}
            </div>
          </div>
          <div class="text-right">
            <p class="text-lg font-bold text-gray-900">${utils.formatCurrency(car.rentPrice)}</p>
            <p class="text-xs text-gray-500">/ 일</p>
          </div>
        </div>`;
      return el;
    }
  }

  window.pageInitializers = window.pageInitializers || {};
  window.pageInitializers.initSearchPage = () => {
    window.searchPage = new SearchPage();
  };
})();


