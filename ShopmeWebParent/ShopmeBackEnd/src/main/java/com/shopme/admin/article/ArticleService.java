package com.shopme.admin.article;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Article;
import com.shopme.common.entity.ArticleType;
import com.shopme.common.entity.User;
import com.shopme.common.exception.ArticleNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ArticleService {
	public static final int ARTICLES_PER_PAGE = 10;
	
	@Autowired 
	private ArticleRepository articleRepo;
	
	public Page<Article> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
		
    	Sort sort = Sort.by(sortField);

		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

		Pageable pageable = PageRequest.of(pageNum - 1, ARTICLES_PER_PAGE, sort);

		if (keyword != null) {

			return articleRepo.findAll(keyword, pageable);
		}

		return articleRepo.findAll(pageable);
	}
	
//	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
//		helper.listEntities(pageNum, ARTICLES_PER_PAGE, repo);
//	}	
	
	public List<Article> listAll() {
		return articleRepo.findPublishedArticlesWithIDAndTitleOnly();
	}
	
	public List<Article> listArticlesForMenu() {
		return articleRepo.findByTypeOrderByTitle(ArticleType.MENU_BOUND);
	}
	
	public void save(Article article, User user) {
		setDefaultAlias(article);
				
		article.setUpdatedTime(new Date());
		article.setUser(user);
		
		articleRepo.save(article);
	}
	
	private void setDefaultAlias(Article article) {
		if (article.getAlias() == null || article.getAlias().isEmpty()) {
			article.setAlias(article.getTitle().replaceAll(" ", "-"));
		}		
	}
	
	public Article get(Integer id) throws ArticleNotFoundException {
		try {
			return articleRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new ArticleNotFoundException("Could not find any article with ID " + id);
		}
	}	
	
	public void updatePublishStatus(Integer id, boolean published) throws ArticleNotFoundException {
		if (!articleRepo.existsById(id)) {
			throw new ArticleNotFoundException("Could not find any article with ID " + id);
		}
		articleRepo.updatePublishStatus(id, published);
	}
	
	public void delete(Integer id) throws ArticleNotFoundException {
		if (!articleRepo.existsById(id)) {
			throw new ArticleNotFoundException("Could not find any article with ID " + id);			
		}
		articleRepo.deleteById(id);
	}	
}