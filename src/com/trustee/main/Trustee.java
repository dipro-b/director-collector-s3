package com.trustee.main;

public class Trustee {
	private String myName;
	private String myJob;
	
	public Trustee() {
		
	}
	
	public Trustee(String name, String job) {
		this.myName = name;
		this.myJob = job;
	}
	
	public String getName() {
		return this.myName;
	}
	
	public String getJob() {
		return this.myJob; 
	}
	
	public void setName(String name) {
		this.myName = name;
	}
	
	public void setJob(String job) {
		this.myJob = job;
	}
}
