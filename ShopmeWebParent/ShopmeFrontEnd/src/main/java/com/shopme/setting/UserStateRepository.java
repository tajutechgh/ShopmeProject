package com.shopme.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

public interface UserStateRepository extends CrudRepository<State, Integer>, PagingAndSortingRepository<State, Integer> {

	public List<State> findByCountryOrderByNameAsc(Country country);
}
