package com.API.server.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Question {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String question;
	private String[] options;
	private String rightanswer;
	private LocalDate startdate;
	private LocalDate enddate;
	private String status;

	public Question() {
		
	}

	

	public Question(int id, String question, String[] options, String rightanswer, LocalDate startdate, LocalDate enddate,
			String status) {
		super();
		this.id = id;
		this.question = question;
		this.options = options;
		this.rightanswer = rightanswer;
		this.startdate = startdate;
		this.enddate = enddate;
		this.status = status;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getQuestion() {
		return question;
	}



	public void setQuestion(String question) {
		this.question = question;
	}



	public String[] getOptions() {
		return options;
	}



	public void setOptions(String[] options) {
		this.options = options;
	}



	public String getRightanswer() {
		return rightanswer;
	}



	public void setRightanswer(String rightanswer) {
		this.rightanswer = rightanswer;
	}



	public LocalDate getStartdate() {
		return startdate;
	}



	public void setStartdate(LocalDate startdate) {
		this.startdate = startdate;
	}



	public LocalDate getEnddate() {
		return enddate;
	}



	public void setEnddate(LocalDate enddate) {
		this.enddate = enddate;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}


	
	

}
