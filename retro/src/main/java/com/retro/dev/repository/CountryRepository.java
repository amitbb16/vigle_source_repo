package com.retro.dev.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.retro.dev.models.Country;

@Repository
public interface CountryRepository extends CrudRepository<Country, Long> {
    
	List<Country> findAll();
	
	List<Country> findAllByctryContinent(String ctryContinent);

	Optional<Country> findById(Long id);
}