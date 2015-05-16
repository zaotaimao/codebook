package com.hywebchina.codebook.db.model;

import java.util.Date;

import com.hywebchina.codebook.Constant;
import com.j256.ormlite.field.DatabaseField;

public class MainPassword {
	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField
	private String password;
	
	@DatabaseField
	private Date modifyDate;
	
	@DatabaseField
	private int wrongTimes;
	
	@DatabaseField
	private boolean locked;
	
	public MainPassword(){
		
	}
	
	public MainPassword(String password, Date modifyDate, int wrongTimes, boolean locked){
		this.password = password;
		this.modifyDate = modifyDate;
		this.wrongTimes = wrongTimes;
		this.locked = locked;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public int getWrongTimes() {
		return wrongTimes;
	}

	public void setWrongTimes(int wrongTimes) {
		this.wrongTimes = wrongTimes;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public void addWrongTimes(Integer wrongTimes) {
		this.wrongTimes = this.wrongTimes + wrongTimes;
		if(this.wrongTimes>=Constant.MAX_WRONG_TIMES){
			this.locked=true;
		}
	}
}
