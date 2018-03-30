package com.pgs.spark.bigdata.to;

import java.util.Date;
import java.util.Set;

public class ArticleTO {

	private String content;
	private String title;
	private Set<String> tags;
	private Date date;

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public Date getDate() {
		return date;
	}

}
