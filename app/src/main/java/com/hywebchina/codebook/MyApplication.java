package com.hywebchina.codebook;

import android.app.Application;

public class MyApplication extends Application {
    public String mainPassword;

	public String getMainPassword() {
		return mainPassword;
	}

	public void setMainPassword(String mainPassword) {
		this.mainPassword = mainPassword;
	}
}