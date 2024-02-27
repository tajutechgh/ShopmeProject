package com.shopme.admin.brand;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.AmazonS3Util;
import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class BrandController {
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/brands")
	public String listFirstPage(Model model) {
		
		return listByPage(1, model, "id", "asc", null);
	}
	
	@GetMapping("/brands/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {

		Page<Brand> page = brandService.listByPage(pageNum, sortField, sortDir, keyword);

		List<Brand> listBrands = page.getContent();

		long startCount = (pageNum - 1) * BrandService.BRANDS_PER_PAGE + 1;
		long endCount = startCount + BrandService.BRANDS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {

			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);

		return "brands/brands";
	}
	
	@GetMapping("/brands/new")
	public String newBrand(Model model) {

		List<Category> listCategories = categoryService.listCategoriesUsedInForm();

		Brand brand = new Brand();

		model.addAttribute("brand", brand);
		model.addAttribute("pageTitle", "Create New Brand");
		model.addAttribute("listCategories", listCategories);

		return "brands/brand_form";
	}
	
	@PostMapping("/brands/save")
	public String saveBrand(Brand brand, @RequestParam("fileImage") MultipartFile multipartFile, RedirectAttributes redirectAttributes) throws IOException {
		
		if (!multipartFile.isEmpty()) {
			
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			
			brand.setLogo(fileName);
			
			Brand savedBrand = brandService.save(brand);
			
//			String uploadDir = "../brand-logos/" + savedBrand.getId();
//
//			FileUploadUtil.cleanDir(uploadDir);
//			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			
			String uploadDir = "brand-logos/" + savedBrand.getId();
			
			AmazonS3Util.removeFolder(uploadDir);
			AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
			
		} else {
			
			brandService.save(brand);
		}
		
		redirectAttributes.addFlashAttribute("message", "The brand has been saved successfully.");
		
		return "redirect:/brands";		
	}
	
	@GetMapping("/brands/edit/{id}")
	public String editBrand(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		
		try {
			
			Brand brand = brandService.get(id);
			
			List<Category> listCategories = categoryService.listCategoriesUsedInForm();
			
			model.addAttribute("brand", brand);
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("pageTitle", "Edit Brand (ID: " + id + ")");
			
			return "brands/brand_form";	
			
		} catch (BrandNotFoundException ex) {
			
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			
			return "redirect:/brands";
		}
	}
	
	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		
		try {
			
			brandService.delete(id);
			
//            String brandDir = "../brand-logos/" + id;
//			
//			FileUploadUtil.removeDir(brandDir);
			
			String brandDir = "brand-logos/" + id;
			
			AmazonS3Util.removeFolder(brandDir);
			
			redirectAttributes.addFlashAttribute("message", "The brand ID " + id + " has been deleted successfully");
			
		} catch (BrandNotFoundException ex) {
			
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		
		return "redirect:/brands";
	}
	
	@GetMapping("/brands/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		
		List<Brand> listBrands = brandService.listAll();
		
		BrandCsvExporter exporter = new BrandCsvExporter();
		
		exporter.export(listBrands, response);
	}

}
