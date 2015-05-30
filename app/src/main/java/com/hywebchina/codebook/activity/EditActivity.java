package com.hywebchina.codebook.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.hywebchina.codebook.MyApplication;
import com.hywebchina.codebook.R;
import com.hywebchina.codebook.db.DatabaseHelper;
import com.hywebchina.codebook.db.model.CodeLine;
import com.hywebchina.codebook.db.model.CodePage;
import com.hywebchina.codebook.util.DesUtil;
import com.hywebchina.codebook.util.StringUtil;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class EditActivity extends OrmLiteBaseActivity<DatabaseHelper> {
    private final String TAG = getClass().getSimpleName();

    private EditText pageName;
    private TableLayout table;
    private ImageView img1;

    private LayoutInflater mInflater;

    private CodePage page;
    private List<CodeLine> delList = new ArrayList<CodeLine>();
    private List<CodeLine> updateList = new ArrayList<CodeLine>();
    private List<CodeLine> addList = new ArrayList<CodeLine>();

    RuntimeExceptionDao<CodePage, Integer> pageDao;
    RuntimeExceptionDao<CodeLine, Integer> lineDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        mInflater=LayoutInflater.from(this);

        pageDao = getHelper().getCodePageDao();
        lineDao = getHelper().getCodeLineDao();

        pageName = (EditText) findViewById(R.id.pageName);
        table = (TableLayout) findViewById(R.id.table);
        img1 = (ImageView) findViewById(R.id.img1);
        img1.setOnClickListener(addListener);

        Intent intent = getIntent();
        page = pageDao.queryForId(intent.getIntExtra("id", 0));

        pageName.setText(page.getName());
        for(CodeLine line : page.getLines()){
            TableRow row = (TableRow) mInflater.inflate(R.layout.table_row, null);
            table.addView(row);
            EditText name = (EditText) row.getChildAt(0);
            EditText password = (EditText) row.getChildAt(1);
            ImageView del = (ImageView) row.getChildAt(2);
            name.setText(line.getName());
            try {
                MyApplication application = (MyApplication)getApplication();
                DesUtil des = new DesUtil(application.getMainPassword());
                password.setText(des.decrypt(line.getPassword()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            del.setTag(line.getId());
            del.setOnClickListener(delListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                int id = save();
                if(id!=0){
                    this.finish();
                    overridePendingTransition(R.anim.not_move, R.anim.not_move);
                }
                break;
            case android.R.id.home:
                this.finish();
                overridePendingTransition(R.anim.not_move, R.anim.out_from_right);
            default:
                break;
        }
        return true;
    }

    private int save() {
        String pName = pageName.getText().toString();
        if(StringUtil.isBlank(pName)){
            Toast.makeText(this, "值不能为空！", Toast.LENGTH_SHORT).show();
            return 0;
        }
        page.setName(pName);
        page.setModifyDate(new Date());

        for(int i=0;i<table.getChildCount();i++){
            TableRow row = (TableRow) table.getChildAt(i);
            EditText lineName = (EditText) row.getChildAt(0);
            EditText linePassword = (EditText) row.getChildAt(1);
            ImageView del = (ImageView) row.getChildAt(2);
            String lName = lineName.getText().toString();
            String lPassword = linePassword.getText().toString();
            if(StringUtil.isBlank(lName) || StringUtil.isBlank(lPassword)){
                Toast.makeText(this, "值不能为空！", Toast.LENGTH_SHORT).show();
                return 0;
            }

            try {
                MyApplication application = (MyApplication)getApplication();
                DesUtil des = new DesUtil(application.getMainPassword());
                lPassword = des.encrypt(lPassword);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Object tag =  del.getTag();
            if(tag!=null){
                int id = (Integer) tag;
                CodeLine line = getLine(id);
                line.setName(lName);
                line.setPassword(lPassword);
                updateList.add(line);
            }
            else{
                CodeLine line = new CodeLine(lName, lPassword, 0);
                line.setPage(page);
                page.getLines().add(line);
                addList.add(line);
            }
        }

        pageDao.update(page);
        for(CodeLine line : addList){
            lineDao.createOrUpdate(line);
        }
        for(CodeLine line : updateList){
            lineDao.createOrUpdate(line);
        }
        lineDao.delete(delList);
        return page.getId();
    }

    OnClickListener delListener = new OnClickListener(){
        @Override
        public void onClick(View v) {
            TableRow row = (TableRow)v.getParent();
            table.removeView(row);
            Object tag =  v.getTag();
            if(tag!=null){
                int id = (Integer) tag;
                CodeLine line = getLine(id);
                delList.add(line);
            }
        }
    };

    OnClickListener addListener = new OnClickListener(){
        @Override
        public void onClick(View v) {
            addTableRow();
        }
    };

    public void addTableRow(){
        TableRow row = (TableRow) mInflater.inflate(R.layout.table_row, null);
        table.addView(row);
        ImageView del = (ImageView) row.getChildAt(2);
        del.setOnClickListener(delListener);
    }

    protected CodeLine getLine(int id) {
        for(CodeLine line : page.getLines()){
            if(line.getId()==id){
                return line;
            }
        }
        return null;
    }

    public void onBackPressed() {
        this.finish();
        overridePendingTransition(R.anim.not_move, R.anim.not_move);
    }
}
