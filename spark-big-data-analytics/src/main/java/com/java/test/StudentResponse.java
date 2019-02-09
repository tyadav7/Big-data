package com.java.test;

public class StudentResponse {
	private Integer studentID;
	private String questionSet;
	private String section;
	private String qID;
	private char answered;
	private char result;
	private String timeTaken;
	private String attemptTS;

	public Integer getStudentID() {
		return studentID;
	}

	public void setStudentID(Integer studentID) {
		this.studentID = studentID;
	}

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

	public String getqID() {
		return qID;
	}

	public void setqID(String qID) {
		this.qID = qID;
	}

	public char getAnswered() {
		return answered;
	}

	public void setAnswered(char answered) {
		this.answered = answered;
	}

	public char getResult() {
		return result;
	}

	public void setResult(char result) {
		this.result = result;
	}

	public String getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(String timeTaken) {
		this.timeTaken = timeTaken;
	}

	public String getAttemptTS() {
		return attemptTS;
	}

	public void setAttemptTS(String attemptTS) {
		this.attemptTS = attemptTS;
	}
}
