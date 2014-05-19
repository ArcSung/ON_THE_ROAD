package com.arc.android_timer;

import static android.provider.BaseColumns._ID;
import static com.arc.android_timer.DbConstants.Date;
import static com.arc.android_timer.DbConstants.Time;
import static com.arc.android_timer.DbConstants.Remark;
import static com.arc.android_timer.DbConstants.TABLE_NAME;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import at.abraxas.amarino.Amarino;

public class SQLiteDemoActivity extends Activity {
	
	private DBHelper dbhelper = null;
	private ListView listData = null;	

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_main);
        
        initView();
        
        openDatabase();
		showInList();
    }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		closeDatabase();
	}
	
	private void openDatabase(){
		dbhelper = new DBHelper(this); 
	}
	
	public void openDatabase_outside(Context context){
		dbhelper = new DBHelper(context); 
	}
	
	private void closeDatabase(){
		dbhelper.close();
	}

	private void initView(){
		listData = (ListView) findViewById(R.id.listData);
		listData.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int pos, long id) {
				// TODO Auto-generated method stub
				Log.i("android_timer", "pos: "+pos);
				return false;
			}
			
		});
	}
	    
    public void add(String addTime){
    	SQLiteDatabase db = dbhelper.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    	Date curDate = new Date(System.currentTimeMillis());
    	String str = formatter.format(curDate);
    	values.put(Date, str);
    	values.put(Time, addTime);
    	values.put(Remark, "NULL");
    	db.insert(TABLE_NAME, null, values);
    }
    
    private Cursor getCursor(){
    	SQLiteDatabase db = dbhelper.getReadableDatabase();
    	String[] columns = {_ID, Date, Time, Remark};
    	
    	Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);
    	startManagingCursor(cursor);
    	
    	return cursor;
    }
        
    
    private void showInList(){
    	
    	Cursor cursor = getCursor();
    	
    	String[] from = {Date, Time, Remark};
    	int[] to = {R.id.txtDate, R.id.txtTime, R.id.txtRemark};
    	
    	SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.data_item, cursor, from, to);
    	listData.setAdapter(adapter);
    }

    private void del(){
    	//String id = editId.getText().toString();
    	
    	//SQLiteDatabase db = dbhelper.getWritableDatabase();
    	//db.delete(TABLE_NAME, _ID + "=" + id, null);
    }
	
    private void update(){
    	//String id = editId.getText().toString();
    	
    	//ContentValues values = new ContentValues();
    	//values.put(Date, editName.getText().toString());
    	//values.put(Time, editTel.getText().toString());
    	//values.put(Remark, editEmail.getText().toString());
    	
    	//SQLiteDatabase db = dbhelper.getWritableDatabase();
    	//db.update(TABLE_NAME, values, _ID + "=" + id, null);
    }
    
}