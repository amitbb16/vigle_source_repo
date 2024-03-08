package com.retro.dev.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.retro.dev.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	Optional<User> findByEmail(String email);

	@Query( "select o from User o where id in :ids" )
	List<User> findByIds(@Param("ids") List<Long> idsList);
	
	/*@Query( "select o from User o where company_start_date < date_sub(CURDATE(), INTERVAL 0 DAY) and company_start_date > date_sub(now(), INTERVAL numberOfDays DAY)" )
	List<User> findAllUsersRegisteredOnSpecificDay(@Param("numberOfDays") int numberOfDays);*/
	
}
