package com.arc.ontheroad;

import static android.provider.BaseColumns._ID;
import static com.arc.ontheroad.DbConstants.Date;
import static com.arc.ontheroad.DbConstants.Phone;
import static com.arc.ontheroad.DbConstants.Remark;
import static com.arc.ontheroad.DbConstants.TABLE_NAME;
import static com.arc.ontheroad.DbConstants.Time;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import at.abraxas.amarino.Amarino;

public class SQLiteDemoActivity extends Activity {
	
	private static DBHelper dbhelper = null;
	private ListView listData = null;	
	JSONArray jsonArrayMain = null;
	JSONArray jsonArrayMain2 = null;
	ImageButton    pepole_info_button;
	ImageButton    search_button;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(1);
	
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
        setContentView(R.layout.record_main);
        
        initView();
        openDatabase();
        for(int i= 0; i<=200; i++)
            del(i);
        
        Intent intent = getIntent(); 
        final Double longitude = intent.getDoubleExtra("longitude", 0);
        final Double latitude = intent.getDoubleExtra("latitude", 0);
        Search_info(longitude, latitude);
        
	    pepole_info_button  = (ImageButton) findViewById(R.id.imageView2);
	    pepole_info_button.setOnClickListener(new ImageButton.OnClickListener() {
			   public void onClick(View v)
				{
				    Intent intent = new Intent();
					intent.setClass(SQLiteDemoActivity.this,MyCameraPreview.class);
					intent.putExtra("longitude",longitude); 
					intent.putExtra("latitude",latitude);
					startActivityForResult(intent, 0);


				}
		});  
	    
	    search_button  = (ImageButton) findViewById(R.id.imageView1);
	    search_button.setOnClickListener(new ImageButton.OnClickListener() {
			   public void onClick(View v)
				{
				   showInList();

				}
		});

		
    }
	
	public boolean onTouchEvent(MotionEvent event)
	{
        //if(event.getAction() == MotionEvent.ACTION_UP){
        int X= (int)event.getX();
        int Y= (int)event.getY();
        Log.i("Arc","X"+X+"Y"+Y);

        	showInList();        
        //}

		return false;  	
   }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
        for(int i= 0; i<=10; i++)
        del(i);
		closeDatabase();
	}
	
	private void Search_info(Double longitude, Double latitude){
		
		String url = "http://linarnan.co:3000/roadhelper/aloha?lat="+latitude+"&lng="+longitude;
    	new HttpAsyncTask().execute(url);

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
	

	    
    public static void add(String Path, String Compny, String YesOrNo ){
    	SQLiteDatabase db = dbhelper.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	values.put(Date, Path);
    	values.put(Time, Compny);
    	values.put(Remark, YesOrNo);
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
    	
    	String[] from = {Date, Time};
    	int[] to = { R.id.txtDate, R.id.txtTime};
    	
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
		//init SQL
	    Cursor cursor = fetehData(RowID);
    	    	
    	final String Path_db   = cursor.getString(1);
    	final String Company_db   = cursor.getString(2);
    	String Date_db = cursor.getString(3);
		
		// Setting_Dialog UI set up
		LinearLayout linear=new LinearLayout(this);
		linear.setOrientation(1);
		
		linear.setBackgroundColor(Color.rgb(60,60, 60));
		ImageView PhotoImage = new ImageView(this);	
		PhotoImage.setImageResource(R.drawable.pushcar);
		linear.addView(PhotoImage);
		if(Date_db == "Yes")
		{	
			TextView road = new TextView(this);
			road.setTextSize(20);
			road.setTextColor(Color.rgb(255,255,0));;
			road.setText("路平專案中");
			linear.addView(road);
		}
				
		TextView DateText = new TextView(this);
		DateText.setTextSize(20);
		DateText.setTextColor(Color.rgb(255,255, 255));;
		DateText.setText("路段: "+Path_db);
				
		TextView TimeText = new TextView(this);
		TimeText.setTextSize(20);
		TimeText.setTextColor(Color.rgb(255,255, 255));;
		TimeText.setText("公司: "+Company_db);
			
					
		
	    linear.addView(DateText);
	    linear.addView(TimeText);


	    builder.setView(linear); 
				
	    
	    AlertDialog setting_dialog = builder.create();
	    setting_dialog.show();
	}
	
	public static JSONArray  GET(String url){
        InputStream inputStream = null;
        String result = "";
        JSONArray jsonArray = null;
        try {
 
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
 
            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
 
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
 
            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        
     // 讀取回應
     		try {
     			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"utf8"),9999999);
     			//99999為傳流大小，若資料很大，可自行調整
     			StringBuilder sb = new StringBuilder();
     			String line = null;
     			while ((line = reader.readLine()) != null) {
     				//逐行取得資料
     				sb.append(line + "\n");
     			}
     			inputStream.close();
     			result = sb.toString();
     		} catch(Exception e) {
     			e.printStackTrace();
     		}
     		//String strJson="{\n\"000000000000000\": [\n    {\n        \"employee_boycode\": \"00\",\n        \"id\": \"000\",\n        \"address\": \"abcdef\",\n        \"name\": \"name\",\n        \"bankcode\": \"abc\",\n        \"branch_name\": \"abcd\",\n        \"account_no\": \"789\"\n    }\n]\n}\n";

     	    try {
     	    JSONObject jsnJsonObject = new JSONObject(result);


     	   JSONArray contacts = jsnJsonObject.getJSONArray("hasDigs");

       
     	           for(int i =0; i< contacts.length(); i ++)
     	           { 
     	        	   
     					String id = String.valueOf(i);
     					JSONObject data = contacts.getJSONObject(i);
     					String location = data.getString("location");
     					String comp = data.getString("placeholder");
     					Boolean flag = data.getBoolean("isOnRoadFlatenProject");
     	          //cityid = contacts.getString("city");
     	          //villageid = contacts.getString("village");
     	          //streetid = contacts.getString("street");
     	            Log.i("Parsed data is",":"+location+", "+comp+ ", "+flag);
     	            String YesOrNol;
     	            if(flag = true)
     	            	YesOrNol = "Yes";
     	            else
     	            	YesOrNol = "No";
     	           add(location, comp, YesOrNol);
     	           }   
     	        

     	    } catch (JSONException e) {
            	Log.i("ARC","3");
     	        e.printStackTrace();
     	    }
     		//轉換文字為JSONArray
     		/*try {
     			JSONObject jsnJsonObject = new JSONObject(result);
     		} catch(JSONException e) {
     			Log.i("ARC","2C" + e.getMessage());
     			e.printStackTrace();
     		}*/
 
        return jsonArray;
    }
	
	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
 
    }
	
	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
 
        	jsonArrayMain = GET(urls[0]); 
            return "OK";
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

       }
    }
    
}