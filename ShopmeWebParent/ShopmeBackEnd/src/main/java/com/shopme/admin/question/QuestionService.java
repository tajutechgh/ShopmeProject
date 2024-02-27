package com.shopme.admin.question;

import java.util.Date;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Question;
import com.shopme.common.entity.User;
import com.shopme.common.exception.QuestionNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class QuestionService {
	
	public static final int QUESTIONS_PER_PAGE = 10;
	
	@Autowired
	private QuestionRepository questionRepo;
	
	public Page<Question> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
			
	    	Sort sort = Sort.by(sortField);
	
			sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
	
			Pageable pageable = PageRequest.of(pageNum - 1, QUESTIONS_PER_PAGE, sort);
	
			if (keyword != null) {
	
				return questionRepo.findAll(keyword, pageable);
			}
	
			return questionRepo.findAll(pageable);
	}	
	
	public Question get(Integer id) throws QuestionNotFoundException {
		try {
			return questionRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new QuestionNotFoundException("Could not find question with ID " + id);
		}
	}
	
	public void save(Question questionInForm, User user) throws QuestionNotFoundException {
		try {
			Question questionInDB = questionRepo.findById(questionInForm.getId()).get();
			questionInDB.setQuestionContent(questionInForm.getQuestionContent());
			questionInDB.setAnswer(questionInForm.getAnswer());
			questionInDB.setApproved(questionInForm.isApproved());
			
			if (questionInDB.isAnswered()) {
				questionInDB.setAnswerTime(new Date());
				questionInDB.setAnswerer(user);
			}
			
			questionRepo.save(questionInDB);
			
		} catch (NoSuchElementException ex) {
			throw new QuestionNotFoundException("Could not find any question with ID " + questionInForm.getId());
		}		
	}
	
	public void approve(Integer id) {
		
		questionRepo.updateApprovalStatus(id, true);
	}
	
	public void disapprove(Integer id) {
		
		questionRepo.updateApprovalStatus(id, false);
	}
	
	public void delete(Integer id) throws QuestionNotFoundException {
		
		if (!questionRepo.existsById(id)) {
			throw new QuestionNotFoundException("Could not find any question with ID " + id);
		}
		
		questionRepo.deleteById(id);
	}	
}