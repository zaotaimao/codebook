package com.hywebchina.codebook.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hywebchina.codebook.db.model.CodeLine;
import com.hywebchina.codebook.db.model.CodePage;
import com.hywebchina.codebook.db.model.MainPassword;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	private final String TAG = getClass().getSimpleName();
	
	private static final String DATABASE_NAME = "codebook.db";
	
	private static final int DATABASE_VERSION = 1;
	
	private RuntimeExceptionDao<MainPassword, Integer> mainPasswordDao = null;
	private RuntimeExceptionDao<CodePage, Integer> codePageDao = null;
	private RuntimeExceptionDao<CodeLine, Integer> codeLineDao = null;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.d(TAG, "onCreate");
			TableUtils.createTable(connectionSource, MainPassword.class);
			TableUtils.createTable(connectionSource, CodePage.class);
			TableUtils.createTable(connectionSource, CodeLine.class);
		} catch (SQLException e) {
			Log.e(TAG, "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		Log.d(TAG, "onUpgrade");
	}
	
	public RuntimeExceptionDao<MainPassword, Integer> getMainPasswordDao(){
		if (mainPasswordDao == null) {
			mainPasswordDao = getRuntimeExceptionDao(MainPassword.class);
		}
		return mainPasswordDao;
	}
	
	public RuntimeExceptionDao<CodePage, Integer> getCodePageDao(){
		if (codePageDao == null) {
			codePageDao = getRuntimeExceptionDao(CodePage.class);
		}
		return codePageDao;
	}
	
	public RuntimeExceptionDao<CodeLine, Integer> getCodeLineDao(){
		if (codeLineDao == null) {
			codeLineDao = getRuntimeExceptionDao(CodeLine.class);
		}
		return codeLineDao;
	}

	@Override
	public void close() {
		super.close();
		mainPasswordDao = null;
		codePageDao = null;
		codeLineDao = null;
	}
}
