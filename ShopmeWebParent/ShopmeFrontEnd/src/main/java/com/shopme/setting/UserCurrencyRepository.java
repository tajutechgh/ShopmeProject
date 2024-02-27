package com.shopme.setting;

import org.springframework.data.repository.CrudRepository;

import com.shopme.common.entity.Currency;

public interface UserCurrencyRepository extends CrudRepository<Currency, Integer> {

}