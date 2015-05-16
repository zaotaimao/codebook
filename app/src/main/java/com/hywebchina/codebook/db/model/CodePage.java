package com.hywebchina.codebook.db.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;

public class CodePage {
	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField
	private String name;
	
	@DatabaseField
	private Date modifyDate;
	
	@DatabaseField
	private int orderNo;
	
	@ForeignCollectionField(eager = true)
    Collection<CodeLine> lines = new ArrayList<CodeLine>();
	
	public CodePage(){
		
	}
	
	public CodePage(String name, Date modifyDate, int orderNo){
		this.name = name;
		this.modifyDate = modifyDate;
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

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public Collection<CodeLine> getLines() {
		return lines;
	}

	public void setLines(Collection<CodeLine> lines) {
		this.lines = lines;
	}
}
