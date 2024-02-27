package com.shopme.admin.article;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.security.ShopemeUserDetails;
import com.shopme.common.entity.Article;
import com.shopme.common.exception.ArticleNotFoundException;

@Controller
public class ArticleController {
	private String defaultRedirectURL = "redirect:/articles/page/1?sortField=title&sortDir=asc";
	
	@Autowired private ArticleService articleService;
	
	@GetMapping("/articles")
	public String listFirstPage(Model model) {
		return defaultRedirectURL;
	}
	
	@GetMapping("/articles/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {

		Page<Article> page = articleService.listByPage(pageNum, sortField, sortDir, keyword);

		List<Article> listArticles = page.getContent();

		long startCount = (pageNum - 1) * ArticleService.ARTICLES_PER_PAGE + 1;
		long endCount = startCount + ArticleService.ARTICLES_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {

			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listArticles", listArticles);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);

		return "articles/articles";
	}
	
//	@GetMapping("/articles/page/{pageNum}")
//	public String listByPage(@PagingAndSortingParam(moduleURL = "/articles", listName = "listArticles") PagingAndSortingHelper helper, 
//						@PathVariable(name = "pageNum") int pageNum) {
//		service.listByPage(pageNum, helper);
//		return "articles/articles";
//	}	
	
	@GetMapping("/articles/new")
	public String newArticle(Model model) {
		model.addAttribute("article", new Article());
		model.addAttribute("pageTitle", "Create New Article");
		
		return "articles/article_form";
	}	
	
	@PostMapping("/articles/save")
	public String saveArticle(Article article, RedirectAttributes ra, 
			@AuthenticationPrincipal ShopemeUserDetails userDetails) {
		
		articleService.save(article, userDetails.getUser());
		
		ra.addFlashAttribute("message", "The article has been saved successfully.");
		
		return defaultRedirectURL;
	}
	
	@GetMapping("/articles/edit/{id}")
	public String editArticle(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes ra) {
		try {
			Article article = articleService.get(id);
			model.addAttribute("article", article);
			model.addAttribute("pageTitle", "Edit Article (ID: " + id + ")");
			
			return "articles/article_form"; 
			
		} catch (ArticleNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			
			return defaultRedirectURL;
		}		
	}	
	
	@GetMapping("/articles/detail/{id}")
	public String viewArticle(@PathVariable(name = "id") Integer id, RedirectAttributes ra,  Model model) {
		try {
			Article article = articleService.get(id);
			model.addAttribute("article", article);
			
			return "articles/article_detail_modal";
			
		} catch (ArticleNotFoundException ex) {
			ra.addFlashAttribute("message", "Could not find any article with ID " + id);
			return defaultRedirectURL;
		}		
	}
	
	@GetMapping("/articles/{id}/enabled/{publishStatus}")
	public String publishArticle(@PathVariable("id") Integer id, 
			@PathVariable("publishStatus") String publishStatus, RedirectAttributes ra) {
		try {
			boolean published = Boolean.parseBoolean(publishStatus);					
			articleService.updatePublishStatus(id, published);		
			
			String publishResult = published ? "published." : "unpublished.";
			ra.addFlashAttribute("message", "The article ID " + id + " has been " + publishResult);
		} catch (ArticleNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
		}
		
		return defaultRedirectURL;
	}
	
	@GetMapping("/articles/delete/{id}")
	public String deleteArticle(@PathVariable(name = "id") Integer id, RedirectAttributes ra) {
		try {
			articleService.delete(id);
			ra.addFlashAttribute("message", "The article ID " + id + " has been deleted.");
		} catch (ArticleNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
		}
		
		return defaultRedirectURL;
	}	
}