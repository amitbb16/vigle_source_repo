package com.retro.dev.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.retro.dev.models.BusinessServices;

@Repository
public interface BusinessServicesRepository extends CrudRepository<BusinessServices, Long> {

    List<BusinessServices> findAll();

    Optional<BusinessServices> findById(Long id);

    @Query( "select o from BusinessServices o where isProvider='N'" )
    List<BusinessServices> findAllOrders();

    @Query( "select o from BusinessServices o where isProvider='Y'" )
    List<BusinessServices> findAllServices();

    @Query( "select o from BusinessServices o where userId=:userId order by createdDate desc" )
    List<BusinessServices> findAllByUserIdSorted(Long userId);
    
    List<BusinessServices> findAllByUserId(Long userId);

	@Query( "select o from BusinessServices o where category_id in :categoryIds" )
	List<BusinessServices> findAllBycategoryIds(@Param("categoryIds") List<Long> categoryIds);
    
	@Query( "select o from BusinessServices o inner join SubCategory s on s.subcategoryId = o.categoryId where o.userId=:userId and s.category.categoryId in :categoryIds" )
	List<BusinessServices> findAllBycategoryIdAndUserId(@Param("userId") Long userId, @Param("categoryIds") List<Long> categoryIds);

	@Query( "select o from BusinessServices o where service_price>servicePrice and service_price<priceTo" )
	List<BusinessServices> findAllByPrice(@Param("servicePrice") float servicePrice, @Param("priceTo") float priceTo);
    
	@Query( "select o from BusinessServices o where upper(service_loc) like upper('%serviceLoc%')" )
	List<BusinessServices> findAllByServiceLoc(@Param("serviceLoc") String serviceLoc);
    
	@Query( "select o from BusinessServices o where created_date >= 'createdDate' and created_date<=sysdate()" )
	List<BusinessServices> findAllByCreatedDate(@Param("createdDate") Date createdDate);

    @Query( "select o from BusinessServices o where isProvider='Y' and service_id in : ids" )
    List<BusinessServices> findAllServicesUsingServiceIds(@Param("ids") List<Long> ids);

}