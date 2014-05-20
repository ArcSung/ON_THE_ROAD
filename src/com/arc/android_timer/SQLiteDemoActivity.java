package com.arc.android_timer;

import static android.provider.BaseColumns._ID;
import static com.arc.android_timer.DbConstants.Date;
import static com.arc.android_timer.DbConstants.Time;
import static com.arc.android_timer.DbConstants.Remark;
import static com.arc.android_timer.DbConstants.TABLE_NAME;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
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
				SQLdb_Dialog(id);
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
    
    private Cursor fetehData(long rowID) throws SQLException
    {   	
    	SQLiteDatabase db = dbhelper.getReadableDatabase();
    	String[] columns = {_ID, Date, Time, Remark};
    	
    	Cursor cursor = db.query(true, TABLE_NAME, columns, _ID + "=" + rowID, null, null, null, null, null);
        
    	if(cursor !=null)
    		cursor.moveToFirst();
    	  	
    	return cursor;
    }
        
    
    private void showInList(){
    	
    	Cursor cursor = getCursor();
    	
    	String[] from = {_ID, Date, Time, Remark};
    	int[] to = {R.id.txtID, R.id.txtDate, R.id.txtTime, R.id.txtRemark};
    	
    	SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.data_item, cursor, from, to);
    	listData.setAdapter(adapter);
    }

    private void del(long rowID){
    	
    	Log.i("android_timer", "id: "+rowID);
    	SQLiteDatabase db = dbhelper.getWritableDatabase();
    	db.delete(TABLE_NAME, _ID + "=" + rowID, null);
    }
	
    private void update(long rowID, String Date_db,  String Time_db, String Remark_db){
    	
    	ContentValues values = new ContentValues();
    	values.put(Date,  Date_db);
    	values.put(Time, Time_db);
    	values.put(Remark, Remark_db);
    	
    	Log.i("android_timer", "id: "+rowID);
    	Log.i("android_timer", "Date_db: "+Date_db);
    	Log.i("android_timer", "Time_db: "+Time_db);
    	Log.i("android_timer", "Remark_db: "+Remark_db);
    	SQLiteDatabase db = dbhelper.getWritableDatabase();
    	db.update(TABLE_NAME, values, _ID + "=" + rowID, null);
    }
    
	private void SQLdb_Dialog(final long RowID)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Detail");
		Log.i("android_timer", "id_dialog: "+RowID);
		//init SQL
	    Cursor cursor = fetehData(RowID);
    	    	
    	final String Date_db   = cursor.getString(1);
    	final String Time_db   = cursor.getString(2);
    	String Remark_db = cursor.getString(3);
		
		// Setting_Dialog UI set up
		LinearLayout linear=new LinearLayout(this);
		linear.setOrientation(1);
				
		TextView DateText = new TextView(this);
		DateText.setTypeface(null, Typeface.BOLD);
		DateText.setText("Date: "+Date_db);
				
		TextView TimeText = new TextView(this);
		TimeText.setTypeface(null, Typeface.BOLD);
		TimeText.setText("Time :"+Time_db);
			
		TextView RemarkText = new TextView(this);
		RemarkText.setTypeface(null, Typeface.BOLD);
		RemarkText.setText("Remark: ");	
		
		final EditText RemarkEdit = new EditText(this);
		RemarkEdit.setTypeface(null, Typeface.BOLD);
		RemarkEdit.setText(Remark_db);	
		
	    linear.addView(DateText);
	    linear.addView(TimeText);
	    linear.addView(RemarkText);
	    linear.addView(RemarkEdit);

	    builder.setView(linear); 
				
		
		builder.setPositiveButton("Update", new DialogInterface.OnClickListener() 
		{
	        public void onClick(DialogInterface dialog, int id) 
	        {
	        	update(RowID, Date_db, Time_db, RemarkEdit.getText().toString());
	        	showInList();
	        }
	    });
		
	    builder.setNegativeButton("Delet", new DialogInterface.OnClickListener() 
	    {
	        public void onClick(DialogInterface dialog, int id) 
	        {
	        	del(RowID);
	        	showInList();
	        }
	    });
	    
	    AlertDialog setting_dialog = builder.create();
	    setting_dialog.show();
	}
    
}