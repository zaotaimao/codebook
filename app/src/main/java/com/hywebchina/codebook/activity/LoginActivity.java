package com.hywebchina.codebook.activity;

import com.hywebchina.codebook.MyApplication;
import com.hywebchina.codebook.R;
import com.hywebchina.codebook.db.DatabaseHelper;
import com.hywebchina.codebook.db.model.MainPassword;
import com.hywebchina.codebook.util.StringUtil;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends OrmLiteBaseActivity<DatabaseHelper> {
    private final String TAG = getClass().getSimpleName();

    private EditText password;

    private MainPassword entity;

    private RuntimeExceptionDao<MainPassword, Integer> dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dao = getHelper().getMainPasswordDao();

        password = (EditText) this.findViewById(R.id.password);

        entity = dao.queryForId(1);
        if(entity==null){
            Log.d(TAG, "go to AddPasswordActivity");
            Intent intent = new Intent(this, AddPasswordActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    public void login(View view) {
        String passwordStr = password.getText().toString();
        Log.d(TAG, "Password:"+passwordStr);

        if(entity.isLocked()){
            Log.d(TAG, "locked");
            Toast.makeText(this, this.getString(R.string.error_locked), Toast.LENGTH_SHORT).show();
        }
        else if(!checkPassword(passwordStr)){
            Log.d(TAG, "wrong password");
            Toast.makeText(this, this.getString(R.string.error_password_wrong), Toast.LENGTH_SHORT).show();
            entity.addWrongTimes(1);//将错误次数加1，如果错误次数达到10，锁定账号
            dao.update(entity);
        }
        else{
            Log.d(TAG, "go to MainActivity");
            entity.setWrongTimes(0);//将错误次数归0
            dao.update(entity);
            MyApplication app = (MyApplication) getApplication();
            app.setMainPassword(passwordStr);//将主密码放到Application里，解密其他密码时会用到
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    private boolean checkPassword(String passwordStr) {
        if(StringUtil.md5Digest(passwordStr).equals(entity.getPassword())){
            return true;
        }
        return false;
    }

}
