package com.hywebchina.codebook.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.hywebchina.codebook.Constant;
import com.hywebchina.codebook.MyApplication;
import com.hywebchina.codebook.R;
import com.hywebchina.codebook.db.DatabaseHelper;
import com.hywebchina.codebook.db.model.CodeLine;
import com.hywebchina.codebook.db.model.CodePage;
import com.hywebchina.codebook.util.DesUtil;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class ViewActivity extends OrmLiteBaseActivity<DatabaseHelper> {

    private TextView pageName;
    private TableLayout table;

    private LayoutInflater mInflater;

    private int pageId;
    private CodePage page;

    RuntimeExceptionDao<CodePage, Integer> pageDao;
    RuntimeExceptionDao<CodeLine, Integer> lineDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        mInflater=LayoutInflater.from(this);

        pageDao = getHelper().getCodePageDao();
        lineDao = getHelper().getCodeLineDao();

        pageName = (TextView) findViewById(R.id.pageName);
        table = (TableLayout) findViewById(R.id.table);

        Intent intent = getIntent();
        pageId = intent.getIntExtra("id", 0);
    }

    @Override
    public void onResume() {
        super.onResume();

        page = pageDao.queryForId(pageId);
        pageName.setText(page.getName());

        table.removeAllViews();
        for(CodeLine line : page.getLines()){
            TableRow row = (TableRow) mInflater.inflate(R.layout.table_row_view, null);
            table.addView(row);
            TextView lineName = (TextView) row.getChildAt(0);
            TextView linePassword = (TextView) row.getChildAt(1);
            ImageView see = (ImageView) row.getChildAt(2);
            lineName.setText(line.getName());
            linePassword.setText(Constant.FAKE_PASSWORD);
            see.setTag(line.getPassword());
            see.setOnTouchListener(listener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                delete();
                break;
            case R.id.action_edit:
                Intent intent = new Intent(this, EditActivity.class);
                intent.putExtra("id", page.getId());
                startActivity(intent);
                overridePendingTransition(R.anim.not_move, R.anim.not_move);
                break;
            case android.R.id.home:
                this.finish();
                overridePendingTransition(R.anim.not_move, R.anim.out_from_right);
            default:
                break;
        }
        return true;
    }

    private void delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(this.getString(R.string.delete_confirm));
        builder.setPositiveButton(this.getString(R.string.confirm),new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                lineDao.delete(page.getLines());
                pageDao.delete(page);
                ViewActivity.this.finish();
                overridePendingTransition(R.anim.not_move, R.anim.out_from_right);
            }
        });
        builder.setNegativeButton(this.getString(R.string.cancel), null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    OnTouchListener listener = new OnTouchListener(){
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            TableRow row = (TableRow)v.getParent();
            TextView linePassword = (TextView) row.getChildAt(1);
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                String password = (String)v.getTag();
                try {
                    MyApplication application = (MyApplication)getApplication();
                    DesUtil des = new DesUtil(application.getMainPassword());
                    password = des.decrypt(password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                linePassword.setText(password);
                return true;
            }
            else if (event.getAction() == MotionEvent.ACTION_UP){
                linePassword.setText(Constant.FAKE_PASSWORD);
                return true;
            }
            else
                return false;
        }
    };

    public void onBackPressed() {
        this.finish();
        overridePendingTransition(R.anim.not_move, R.anim.out_from_right);
    }
}
