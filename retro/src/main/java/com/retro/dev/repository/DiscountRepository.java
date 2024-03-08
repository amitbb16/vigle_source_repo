package com.retro.dev.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.retro.dev.models.Discount;


@Repository
public interface DiscountRepository extends CrudRepository<Discount, Integer> {
    
	List<Discount> findAllByUserid(Long uid);

    Discount findById(Long id);
    
	@Query( "select o,((discountprice-actualprice)/100) as disc_percent from Discount o where discountprice-actualprice>=discount order by disc_percent desc" )
	List<Discount> findAllByDiscount(@Param("discount") Double discount);
	
}