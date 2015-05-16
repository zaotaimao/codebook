package com.hywebchina.codebook.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.hywebchina.codebook.R;
import com.hywebchina.codebook.db.DatabaseHelper;
import com.hywebchina.codebook.db.model.CodePage;
import com.hywebchina.codebook.util.CodePageComparator;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class MainActivity extends OrmLiteBaseActivity<DatabaseHelper> {
    private final String TAG = getClass().getSimpleName();

    private ListView listView;
    private ArrayAdapter<String> adapter;

    private List<CodePage> pageList;
    private List<String> strList = new ArrayList<String>();

    RuntimeExceptionDao<CodePage, Integer> dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dao = getHelper().getCodePageDao();

        listView = (ListView) findViewById(R.id.listView1);
        adapter = new ArrayAdapter<String>(this, R.layout.page_item, R.id.pageName, strList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(listener);
    }

    @Override
    public void onResume() {
        super.onResume();

        getData();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, AddActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.not_move);
                break;
            default:
                break;
        }
        return true;
    }

    public void getData(){
        strList.clear();
        pageList = dao.queryForAll();

        Collections.sort(pageList, new CodePageComparator());
        for(CodePage page : pageList){
            strList.add(page.getName());
        }
    }

    OnItemClickListener listener = new OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            int pageId = pageList.get(position).getId();
            Intent intent = new Intent(MainActivity.this, ViewActivity.class);
            intent.putExtra("id", pageId);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_right, R.anim.not_move);
        }
    };

    private long exitTime = 0;

    public void onBackPressed(){
        if ((System.currentTimeMillis() - exitTime) > 2000){
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }
        else{
            this.finish();
        }
    }
}
