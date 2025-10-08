package me.changwook.test_repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import me.changwook.TestConfig;
import me.changwook.configuration.config.QuerydslConfig;
import me.changwook.domain.*;
import me.changwook.repository.CategoryRepository;
import me.changwook.repository.MemberRepository;
import me.changwook.repository.RentCarsRepository;
import me.changwook.repository.RentRepository;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({QuerydslConfig.class, TestConfig.class})
@org.springframework.test.context.TestPropertySource(properties = {
        "spring.jpa.properties.hibernate.default_batch_fetch_size=0"
})
class NPlusOneDetectionTest {

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private RentCarsRepository rentCarsRepository;

	@Autowired
	private RentRepository rentRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private MemberRepository memberRepository;

	private Statistics stats;

	@BeforeEach
	void setUp() {
		stats = entityManagerFactory.unwrap(SessionFactoryImplementor.class).getStatistics();
		stats.setStatisticsEnabled(true);
		stats.clear();
	}

	private List<RentCars> prepareCarsWithCategories(int count) {
		List<RentCars> cars = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			Category category = Category.builder()
					.fuelType(FuelType.GASOLINE)
					.rentCarsSegment(RentCarsSegment.SMALL)
					.build();
			categoryRepository.save(category);

			RentCars car = RentCars.builder()
					.name("CAR-" + i)
					.rentCarNumber("NUM-" + i)
					.rentPrice(10000 + i)
					.totalDistance(1000 + i)
					.recommend(1L)
					.category(category)
					.build();
			cars.add(rentCarsRepository.save(car));
		}
		// 1차 캐시 비우기 (LAZY 로딩 시 실제 쿼리 발생 확인)
		entityManager.flush();
		entityManager.clear();
		return cars;
	}

	private Member prepareMemberWithRentsAndCars(int rentCount) {
		Member member = Member.builder()
				.name("user")
				.email("user@example.com")
				.password("pw")
				.build();
		member = memberRepository.save(member);

		for (int i = 0; i < rentCount; i++) {
			Category category = Category.builder()
					.fuelType(FuelType.DIESEL)
					.rentCarsSegment(RentCarsSegment.MEDIUM)
					.build();
			categoryRepository.save(category);

			RentCars car = RentCars.builder()
					.name("RENT-CAR-" + i)
					.rentCarNumber("RNUM-" + i)
					.rentPrice(20000 + i)
					.totalDistance(5000 + i)
					.recommend(10L)
					.category(category)
					.build();
			car = rentCarsRepository.save(car);

			Rent rent = Rent.builder()
					.member(member)
					.rentCars(car)
					.rentDate(LocalDate.now().plusDays(1).atStartOfDay())
					.duration(1)
					.endDate(LocalDate.now().plusDays(2).atStartOfDay())
					.build();
			rentRepository.save(rent);
		}

		entityManager.flush();
		entityManager.clear();
		return member;
	}

	@Test
	@Transactional
	@DisplayName("N+1: RentCarsRepository.findAllByCarId()에서 category 접근 시 N개의 추가 쿼리 발생")
	void nPlusOne_on_RentCars_findAllByCarId_whenAccessCategory() {
		int carCount = 5;
		prepareCarsWithCategories(carCount);

		stats.clear();
		List<RentCars> cars = rentCarsRepository.findAllByRentCars();
		// lazy 연관(category)에 접근하여 쿼리 유발
		for (RentCars car : cars) {
			if (car.getCategory() != null) {
				car.getCategory().getFuelType();
			}
		}

		long executed = stats.getPrepareStatementCount();
		// select rentcars + N번(category) 이상
		assertThat(executed).isGreaterThanOrEqualTo(1 + carCount);
	}

	@Test
	@Transactional
	@DisplayName("N+1: RentRepository.findOverLappingReservations()에서 member/rentCars 접근 시 추가 쿼리 다수")
	void nPlusOne_on_RentRepository_findOverLappingReservations_whenAccessAssociations() {
		int rentCount = 5;
		prepareMemberWithRentsAndCars(rentCount);
		// 임의로 첫 번째 차량의 기간과 겹치도록 조회
		LocalDateTime start = LocalDate.now().atStartOfDay();
		LocalDateTime end = LocalDate.now().plusDays(5).atStartOfDay();

		RentCars anyCar = rentCarsRepository.findAll().getFirst();

		stats.clear();
		List<Rent> rents = rentRepository.findOverLappingReservations(anyCar, start, end);
		for (Rent r : rents) {
			// LAZY 연관 접근으로 추가 쿼리 유발
			r.getMember().getEmail();
			r.getRentCars().getName();
		}

		long executed = stats.getPrepareStatementCount();
		// 1회(조회) + 각 row마다 member/rentCars 접근으로 최소 1+N 이상 발생
		assertThat(executed).isGreaterThan(1);
		assertThat(rents).isNotEmpty();
	}

	@Test
	@Transactional
	@DisplayName("N+1: MemberRepository.findByIdWithRents() 이후 rent->rentCars 접근 시 N개 추가 쿼리")
	void nPlusOne_on_MemberRepository_findByIdWithRents_whenAccessRentCarsFromRents() {
		int rentCount = 6;
		Member member = prepareMemberWithRentsAndCars(rentCount);

		stats.clear();
		Member loaded = memberRepository.findByIdWithRents(member.getId()).orElseThrow();
		loaded.getRent().forEach(r -> {
			// rent.rentCars는 LAZY → 개별 쿼리
			r.getRentCars().getName();
		});

		long executed = stats.getPrepareStatementCount();
		// member+rent fetch는 1회지만 rentCars 접근 시 N회 추가
		assertThat(executed).isGreaterThanOrEqualTo(1 + rentCount);
	}
}


