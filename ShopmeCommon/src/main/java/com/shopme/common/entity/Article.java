package com.shopme.common.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "articles")
public class Article extends IdBasedEntity {
	
	@Column(nullable = false, length = 256)
	private String title;
	
	@Column(nullable = false, length = 4500)
	@Lob
	private String content;
	
	@Column(nullable = false, length = 500)
	private String alias;
	
	@Enumerated(EnumType.ORDINAL)
	private ArticleType type;
	
	@Column(name = "updated_time")
	private Date updatedTime;
	
	private boolean published;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Article() {
	}
	
	public Article(Integer id, String title, ArticleType type, Date updatedTime, boolean published, User user) {
		this.id = id;
		this.title = title;
		this.type = type;
		this.updatedTime = updatedTime;
		this.published = published;
		this.user = user;
	}
	
	public Article(Integer id, String title) {
		this.id = id;
		this.title = title;
	}	

	public Article(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public ArticleType getType() {
		return type;
	}

	public void setType(ArticleType type) {
		this.type = type;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Article [title=" + title + ", type=" + type + "]";
	}
	
	
}