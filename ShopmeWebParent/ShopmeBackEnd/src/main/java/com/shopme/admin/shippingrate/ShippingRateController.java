package com.shopme.admin.shippingrate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;

@Controller
public class ShippingRateController {
	
	@Autowired 
	private ShippingRateService shipRateService;
	
	@GetMapping("/shipping_rates")
	public String listFirstPage(Model model) {
		
		return listByPage(1, model, "id", "asc", null);
	}
		
	@GetMapping("/shipping_rates/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {

		Page<ShippingRate> page = shipRateService.listByPage(pageNum, sortField, sortDir, keyword);

		List<ShippingRate> shippingRates = page.getContent();

		long startCount = (pageNum - 1) * ShippingRateService.RATES_PER_PAGE + 1;
		long endCount = startCount + ShippingRateService.RATES_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {

			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("shippingRates", shippingRates);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);

		return "shipping_rates/shipping_rates";
	}
	
	@GetMapping("/shipping_rates/new")
	public String newRate(Model model) {
		
		List<Country> listCountries = shipRateService.listAllCountries();
		
		model.addAttribute("rate", new ShippingRate());
		model.addAttribute("listCountries", listCountries);
		model.addAttribute("pageTitle", "New Rate");
		
		return "shipping_rates/shipping_rate_form";		
	}

	@PostMapping("/shipping_rates/save")
	public String saveRate(ShippingRate rate, RedirectAttributes ra) {
		
		try {
			
			shipRateService.save(rate);
			
			ra.addFlashAttribute("message", "The shipping rate has been saved successfully.");
			
		} catch (ShippingRateAlreadyExistsException ex) {
			
			ra.addFlashAttribute("message", ex.getMessage());
		}
		
		return "redirect:/shipping_rates";
	}
		
	@GetMapping("/shipping_rates/edit/{id}")
	public String editRate(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes ra) {
		
		try {
			
			ShippingRate rate = shipRateService.get(id);
			
			List<Country> listCountries = shipRateService.listAllCountries();
			
			model.addAttribute("listCountries", listCountries);			
			model.addAttribute("rate", rate);
			model.addAttribute("pageTitle", "Edit Rate (ID: " + id + ")");
			
			return "shipping_rates/shipping_rate_form";
			
		} catch (ShippingRateNotFoundException ex) {
			
			ra.addFlashAttribute("message", ex.getMessage());
			
			return "redirect:/shipping_rates";
		}
	}

	@GetMapping("/shipping_rates/cod/{id}/enabled/{supported}")
	public String updateCODSupport(@PathVariable(name = "id") Integer id, @PathVariable(name = "supported") Boolean supported, Model model, RedirectAttributes ra) {
		
		try {
			
			shipRateService.updateCODSupport(id, supported);
			
			ra.addFlashAttribute("message", "COD support for shipping rate ID " + id + " has been updated.");
			
		} catch (ShippingRateNotFoundException ex) {
			
			ra.addFlashAttribute("message", ex.getMessage());
		}
		
		return "redirect:/shipping_rates";
	}
	
	@GetMapping("/shipping_rates/delete/{id}")
	public String deleteRate(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes ra) {
		
		try {
			
			shipRateService.delete(id);
			
			ra.addFlashAttribute("message", "The shipping rate ID " + id + " has been deleted.");
			
		} catch (ShippingRateNotFoundException ex) {
			
			ra.addFlashAttribute("message", ex.getMessage());
		}
		
		return "redirect:/shipping_rates";
	}	
}