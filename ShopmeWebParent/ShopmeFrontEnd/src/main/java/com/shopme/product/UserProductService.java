package com.shopme.product;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ProductNotFoundException;

@Service
public class UserProductService {
	
	public static final int PRODUCTS_PER_PAGE = 18;
	public static final int SEARCH_RESULTS_PER_PAGE = 18;

	@Autowired 
	private UserProductRepository productRepo;
	
	public Page<Product> listByCategory(int pageNum, Integer categoryId) {
		
		String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
		
		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);
		
		return productRepo.listByCategory(categoryId, categoryIdMatch, pageable);
	}
	
	public Page<Product> listByBrand(int pageNum, Integer brandId) {
		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);
		
		return productRepo.listByBrand(brandId, pageable);
	}
	
	public Product getProduct(String alias) throws ProductNotFoundException {
		
		Product product = productRepo.findByAlias(alias);
		
		if (product == null) {
			
			throw new ProductNotFoundException("Could not find any product with alias " + alias);
		}
		
		return product;
	}
	
	public Product getProduct(Integer id) throws ProductNotFoundException {
		
		try {
			
			Product product = productRepo.findById(id).get();
			
			return product;
			
		} catch (NoSuchElementException ex) {
			
			throw new ProductNotFoundException("Could not find any product with ID " + id);
		}
	}
	
	public Page<Product> search(String keyword, int pageNum) {
		
		Pageable pageable = PageRequest.of(pageNum - 1, SEARCH_RESULTS_PER_PAGE);
		
		return productRepo.search(keyword, pageable);
		
	}
}
