package com.hywebchina.codebook.db.model;

import com.j256.ormlite.field.DatabaseField;

public class CodeLine {
	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField
	private String name;
	
	@DatabaseField
	private String password;
	
	@DatabaseField
	private int orderNo;
	
	@DatabaseField(canBeNull = false, foreign = true)
    private CodePage page;
	
	public CodeLine(){
		
	}
	
	public CodeLine(String name, String password, int orderNo){
		this.name = name;
		this.password = password;
		this.orderNo = orderNo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public CodePage getPage() {
		return page;
	}

	public void setPage(CodePage page) {
		this.page = page;
	}
}
