package com.java.test;

import java.io.Serializable;

public class StudentConsolidatedReport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer studentId;
	private String questionSet;
	private Integer QT;
	private Integer VB;
	private Integer RA;
	private Integer questionAttempted;
	private String attemptDate;

	public Integer getStudentId() {
		return studentId;
	}

	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}

	public String getQuestionSet() {
		return questionSet;
	}

	public void setQuestionSet(String questionSet) {
		this.questionSet = questionSet;
	}

	public Integer getQT() {
		return QT;
	}

	public void setQT(Integer qT) {
		QT = qT;
	}

	public Integer getVB() {
		return VB;
	}

	public void setVB(Integer vB) {
		VB = vB;
	}

	public Integer getRA() {
		return RA;
	}

	public void setRA(Integer rA) {
		RA = rA;
	}

	public Integer getQuestionAttempted() {
		return questionAttempted;
	}

	public void setQuestionAttempted(Integer questionAttempted) {
		this.questionAttempted = questionAttempted;
	}

	public String getAttemptDate() {
		return attemptDate;
	}

	public void setAttemptDate(String attemptDate) {
		this.attemptDate = attemptDate;
	}

}
