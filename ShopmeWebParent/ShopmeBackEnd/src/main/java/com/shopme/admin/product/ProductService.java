package com.shopme.admin.product;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ProductNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {
	
	public static final int PRODUCTS_PER_PAGE = 7;
	
	@Autowired 
	private ProductRepository productRepo;
	
	public List<Product> listAll() {
		
		return (List<Product>) productRepo.findAll(Sort.by("id").ascending());
	}
	
	public Page<Product> listByPage(int pageNum, String sortField, String sortDir, String keyword, Integer categoryId) {

		Sort sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE, sort);

		if (keyword != null && !keyword.isEmpty()) {
			
			if(categoryId != null && categoryId > 0) {
				
				String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
				
				return productRepo.searchInCategory(categoryId, categoryIdMatch, keyword, pageable);
			}

			return productRepo.findAll(keyword, pageable);
		}
		
		if(categoryId != null && categoryId > 0) {
			
			String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
			
			return productRepo.findAllInCategory(categoryId, categoryIdMatch, pageable);
		}

		return productRepo.findAll(pageable);
	}
	
	public Page<Product> searchProducts(int pageNum, String sortField, String sortDir, String keyword) {
		
		Sort sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE, sort);
		
		return productRepo.searchProductsByName(keyword, pageable);
	}
	
	public Product save(Product product) {
		
		if (product.getId() == null) {
			
			product.setCreatedTime(new Date());
		}
		
		if (product.getAlias() == null || product.getAlias().isEmpty()) {
			
			String defaultAlias = product.getName().replaceAll(" ", "-");
			
			product.setAlias(defaultAlias);
			
		} else {
			
			product.setAlias(product.getAlias().replaceAll(" ", "-"));
		}
		
		product.setUpdatedTime(new Date());
		
		Product updatedProduct = productRepo.save(product);
		productRepo.updateReviewCountAndAverageRating(updatedProduct.getId());
		
		return updatedProduct;
	}
	
	public void saveProductPrice(Product productInForm) {
		
		Product productInDB = productRepo.findById(productInForm.getId()).get();
		
		productInDB.setCost(productInForm.getCost());
		productInDB.setPrice(productInForm.getPrice());
		productInDB.setDiscountPercent(productInForm.getDiscountPercent());
		
		productRepo.save(productInDB);
	}
	
	public String checkUnique(Integer id, String name) {
		
		boolean isCreatingNew = (id == null || id == 0);
		
		Product productByName = productRepo.findByName(name);
		
		if (isCreatingNew) {
			
			if (productByName != null) return "Duplicate";
			
		} else {
			
			if (productByName != null && productByName.getId() != id) {
				
				return "Duplicate";
			}
		}
		
		return "OK";
	}
	
	public void updateProductEnabledStatus(Integer id, boolean enabled) {
		
		productRepo.updateEnabledStatus(id, enabled);
	}
	
	public void delete(Integer id) throws ProductNotFoundException {
		
		Long countById = productRepo.countById(id);
		
		if (countById == null || countById == 0) {
			
			throw new ProductNotFoundException("Could not find any product with ID " + id);			
		}
		
		productRepo.deleteById(id);
	}	
	
	public Product get(Integer id) throws ProductNotFoundException {
		
		try {
			
			return productRepo.findById(id).get();
			
		} catch (NoSuchElementException ex) {
			
			throw new ProductNotFoundException("Could not find any product with ID " + id);
		}
	}

}
