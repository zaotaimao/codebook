package com.hywebchina.codebook.activity;

import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hywebchina.codebook.MyApplication;
import com.hywebchina.codebook.R;
import com.hywebchina.codebook.db.DatabaseHelper;
import com.hywebchina.codebook.db.model.MainPassword;
import com.hywebchina.codebook.util.StringUtil;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class AddPasswordActivity extends OrmLiteBaseActivity<DatabaseHelper> {
    private final String TAG = getClass().getSimpleName();

    private EditText password0;
    private EditText password1;

    private RuntimeExceptionDao<MainPassword, Integer> dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);

        dao = getHelper().getMainPasswordDao();

        password0 = (EditText) this.findViewById(R.id.password0);
        password1 = (EditText) this.findViewById(R.id.password1);
    }

    public void save(View view) {
        String passwordStr0 = password0.getText().toString();
        String passwordStr1 = password1.getText().toString();

        if(StringUtil.isBlank(passwordStr0)||StringUtil.isBlank(passwordStr1)){
            Toast.makeText(this, this.getString(R.string.error_null_password), Toast.LENGTH_SHORT).show();
        }
        else if(!passwordStr0.equals(passwordStr1)){
            Toast.makeText(this, this.getString(R.string.error_not_same), Toast.LENGTH_SHORT).show();
        }
        else{
            MainPassword pwd = new MainPassword(StringUtil.md5Digest(passwordStr0), new Date(), 0, false);
            dao.create(pwd);//将主密码存入DB
            MyApplication app = (MyApplication) getApplication();
            app.setMainPassword(passwordStr0);//将主密码放到Application里，解密其他密码时会用到
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

}
