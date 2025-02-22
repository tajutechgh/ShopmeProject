package com.shopme.admin.question;

import java.io.IOException;
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
import com.shopme.common.entity.Question;
import com.shopme.common.exception.QuestionNotFoundException;

@Controller
public class QuestionController {
	private String defaultRedirectURL = "redirect:/questions/page/1?sortField=askTime&sortDir=desc";
	
	@Autowired private QuestionService questionService;
	
	@GetMapping("/questions")
	public String listFirstPage(Model model) {
		return defaultRedirectURL;
	}
	
	@GetMapping("/questions/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {

		Page<Question> page = questionService.listByPage(pageNum, sortField, sortDir, keyword);

		List<Question> listQuestions = page.getContent();

		long startCount = (pageNum - 1) * QuestionService.QUESTIONS_PER_PAGE + 1;
		long endCount = startCount + QuestionService.QUESTIONS_PER_PAGE - 1;

		if (endCount > page.getTotalElements()) {

			endCount = page.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listQuestions", listQuestions);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);

		return "question/questions";
	}
	
	@GetMapping("/questions/detail/{id}")
	public String viewQuestion(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		try {
			Question question = questionService.get(id);
			model.addAttribute("question", question);
			
			return "question/question_detail_modal";
		} catch (QuestionNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return defaultRedirectURL;	
		}
	}
	
	@GetMapping("/questions/edit/{id}")
	public String editQuestion(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		try {
			Question question = questionService.get(id);
			model.addAttribute("question", question);
			model.addAttribute("pageTitle", "Edit Question (ID: " + id + ")");
			
			return "question/question_form";
		} catch (QuestionNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return defaultRedirectURL;
		}
	}
	
	@PostMapping("/questions/save")
	public String saveQuestion(Question question, RedirectAttributes ra, @AuthenticationPrincipal ShopemeUserDetails userDetails) {
		try {
			questionService.save(question, userDetails.getUser());
			ra.addFlashAttribute("message", "The Question ID " + question.getId() + " has been updated successfully.");
		} catch (QuestionNotFoundException ex) {
			ra.addFlashAttribute("message", "Could not find any question with ID " + question.getId());
		}
		return defaultRedirectURL;
	}
	
	@GetMapping("/questions/{id}/approve")
	public String approveQuestion(@PathVariable("id") Integer id, RedirectAttributes ra) {
		questionService.approve(id);
		ra.addFlashAttribute("message", "The Question ID " + id + " has been approved.");
		return defaultRedirectURL;
	}
	
	@GetMapping("/questions/{id}/disapprove")
	public String disapproveQuestion(@PathVariable("id") Integer id, RedirectAttributes ra) {
		questionService.disapprove(id);
		ra.addFlashAttribute("message", "The Question ID " + id + " has been disapproved.");
		return defaultRedirectURL;
	}
	
	@GetMapping("/questions/delete/{id}")
	public String deleteQuestion(@PathVariable(name = "id") Integer id, RedirectAttributes ra) throws IOException {
		
		try {
			questionService.delete(id);
			
			ra.addFlashAttribute("message", String.format("The question ID %d has been deleted successfully.", id));
		} catch (QuestionNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
		}
					
		return defaultRedirectURL;
	}
}