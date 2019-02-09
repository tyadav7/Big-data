package com.java.test;

public class Output {

	private String questionSet;
	private String section;
	private Double avgMarks;

	public String getQuestionSet() {
		return questionSet;
	}

	public void setQuestionSet(String questionSet) {
		this.questionSet = questionSet;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public Double getAvgMarks() {
		return avgMarks;
	}

	@Override
	public String toString() {
		return "Output [questionSet=" + questionSet + ", section=" + section + ", avgMarks=" + avgMarks + "]";
	}

	public void setAvgMarks(Double avgMarks) {
		this.avgMarks = avgMarks;
	}

}
