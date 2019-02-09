package com.java.test;

import java.io.Serializable;

public class QuestionData implements Serializable {
	
	
	private String qID;
	private String qdesc;
	private String section;
	private String category;
	@Override
	public String toString() {
		return "QuestionData [qID=" + qID + ", qdesc=" + qdesc + ", section=" + section + ", category=" + category
				+ ", diffLevel=" + diffLevel + ", createdBy=" + createdBy + ", createdDate=" + createdDate + "]";
	}
	private String diffLevel;
	private String createdBy;
	private String createdDate;
	
	public String getqID() {
		return qID;
	}
	public void setqID(String qID) {
		this.qID = qID;
	}
	public String getQdesc() {
		return qdesc;
	}
	public void setQdesc(String qdesc) {
		this.qdesc = qdesc;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDiffLevel() {
		return diffLevel;
	}
	public void setDiffLevel(String diffLevel) {
		this.diffLevel = diffLevel;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
}
