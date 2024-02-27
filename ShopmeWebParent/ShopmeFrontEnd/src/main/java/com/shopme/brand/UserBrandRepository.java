package com.shopme.brand;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopme.common.entity.Brand;

public interface UserBrandRepository extends JpaRepository<Brand, Integer> {

}