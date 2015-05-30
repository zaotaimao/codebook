package com.hywebchina.codebook.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hywebchina.codebook.MyApplication;
import com.hywebchina.codebook.R;
import com.hywebchina.codebook.db.DatabaseHelper;
import com.hywebchina.codebook.db.model.MainPassword;
import com.hywebchina.codebook.service.PasswordService;
import com.hywebchina.codebook.util.StringUtil;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class EditPasswordActivity extends OrmLiteBaseActivity<DatabaseHelper> {
    private final String TAG = getClass().getSimpleName();

    private EditText oldPassword;
    private EditText password0;
    private EditText password1;

    private RuntimeExceptionDao<MainPassword, Integer> dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        dao = getHelper().getMainPasswordDao();

        oldPassword = (EditText) this.findViewById(R.id.oldPassword);
        password0 = (EditText) this.findViewById(R.id.password0);
        password1 = (EditText) this.findViewById(R.id.password1);
    }

    public void save(View view) {
        String oldPasswordStr = oldPassword.getText().toString();
        String passwordStr0 = password0.getText().toString();
        String passwordStr1 = password1.getText().toString();

        //检查旧密码是否正确
        MainPassword entity = dao.queryForId(1);
        if(StringUtil.isBlank(oldPasswordStr)||StringUtil.isBlank(passwordStr0)||StringUtil.isBlank(passwordStr1)){
            Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
        }
        else if(!StringUtil.md5Digest(oldPasswordStr).equals(entity.getPassword())){
            Toast.makeText(this, "旧密码错误！", Toast.LENGTH_SHORT).show();
        }
        else if(!passwordStr0.equals(passwordStr1)){
            Toast.makeText(this, "两次输入的新密码不一致!", Toast.LENGTH_SHORT).show();
        }
        else{
            PasswordService service = new PasswordService(getHelper());
            try {
                service.changeMainPassword(oldPasswordStr, passwordStr0);
                MyApplication app = (MyApplication) getApplication();
                app.setMainPassword(passwordStr0);//将新的主密码放到Application里，解密其他密码时会用到
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            this.finish();
        }
    }

}
