package com.arc.ontheroad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity2 extends Activity {
	String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
	String basepath = extStorageDirectory + "/ESOC_face/";
	private static final int ACTIVITY_SELECT_CAMERA = 0;
	private static final int ACTIVITY_SELECT_Search = 1;
	private static final int ACTIVITY_SELECT_Phone = 2;
	private static final int ACTIVITY_SELECT_Grolloc = 3;
	private static final int ACTIVITY_SELECT_People = 4;
	
	ImageView mainview_view;
	ImageView preview_view;
	boolean   touch = true;
	Double longitude;
	Double latitude;	
	HttpAsyncTask HttpGetData;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(1);
	
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
		
	    setContentView(R.layout.preview);
	    ImageView main_view = (ImageView) findViewById(R.id.ImageView_preview);
		main_view.setImageResource(R.drawable.main_index); 
		
		setlocations();  // get Coordinates
		
		String url = "http://linarnan.co:3000/roadhelper/aloha?lat="+latitude+"&lng="+longitude;
		Log.i("ARC",url);
		HttpGetData = new HttpAsyncTask();
		HttpGetData.execute(url);
	}
	
	public boolean onTouchEvent(MotionEvent event)
	{
        if(event.getAction() == MotionEvent.ACTION_UP){
        int X= (int)event.getX();
        int Y= (int)event.getY();
        Log.i("Arc","X"+X+"Y"+Y);
        if((X > 400) && (X < 700) && (Y > 630) && (Y < 830) && (touch = true))
        {	
        	touch = false;
        	Log.i("Arc","Canmara");
			Intent intent = new Intent();
			intent.setClass(MainActivity2.this,MyCameraPreview.class);
			intent.putExtra("longitude",longitude); 
			intent.putExtra("latitude",latitude);
			startActivityForResult(intent, ACTIVITY_SELECT_CAMERA);
        }
        else if ((X > 400) && (X < 700) && (Y > 1130) && (Y < 1530)&& (touch = true))
        {	
        	touch = false;
        	Log.i("Arc","Search");
			Intent intent = new Intent();
			intent.setClass(MainActivity2.this,SQLiteDemoActivity.class);
			intent.putExtra("longitude",longitude); 
			intent.putExtra("latitude",latitude);
			startActivityForResult(intent, ACTIVITY_SELECT_Search);
        }
        else if ((X > 110) && (X < 230) && (Y > 1690) && (Y < 1800) && (touch = true))
        {	
        	touch = false;
        	Log.i("Arc","gralloc");
			Intent galleryIntent = new Intent(Intent.ACTION_PICK,
			Images.Media.INTERNAL_CONTENT_URI);
			startActivityForResult(galleryIntent, ACTIVITY_SELECT_Grolloc);
        }
        else if ((X > 460) && (X < 610) && (Y > 1690) && (Y < 1800) && (touch = true))
        {	
        	touch = false;
        	Log.i("Arc","phone");
			Intent intent = new Intent();
			intent.setClass(MainActivity2.this,PhoneActivity.class);
			startActivityForResult(intent, ACTIVITY_SELECT_Phone);
        }
        else if ((X > 855) && (X < 995) && (Y > 1690) && (Y < 1800) && (touch = true))
        {	
        	touch = false;
        	Log.i("Arc","people");
			Intent intent = new Intent();
			intent.setClass(MainActivity2.this,PeopleActivity.class);
			startActivityForResult(intent, ACTIVITY_SELECT_People);
        }
        
        }

	
		return false;  	
   }
	
   public void setlocations()
   {       
        LocationManager status=(LocationManager)(this.getSystemService(Context.LOCATION_SERVICE));
        if(status.isProviderEnabled(LocationManager.GPS_PROVIDER)||status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            //if GPS or internet location open， than  call locationServiceInitial() 
            locationServiceInitial();
        } else {
            Toast.makeText(this,"請開啟GPS或網路謝",Toast.LENGTH_LONG).show();
    
            //  startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));//open settings
        }
 
    }
	
	private void locationServiceInitial()
	{
		LocationManager lms=(LocationManager)getSystemService(LOCATION_SERVICE);//取得系統location service
        Criteria criteria=new Criteria();//system provider standard
        criteria.setSpeedRequired(true);
        String bestProvider = lms.getBestProvider(criteria,true);//選擇最高精度        
        Location location=lms.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(location == null){
//如果抓不到就取得最後一筆有記錄的地點
            location=lms.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
           
            longitude=location.getLongitude();//取得經度
            latitude = location.getLatitude();//取得緯度
 
        }
        if (location != null){
        	longitude=location.getLongitude();//取得經度
        	latitude = location.getLatitude();//取得緯度
 
        }
    }
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACTIVITY_SELECT_CAMERA ) 
		{
			touch = true;
			try {

				
				
			} catch (Exception e) {}
		}
		else if (requestCode == ACTIVITY_SELECT_Search ) 
		{
			touch = true;
			try {
			} catch (Exception e) {}			
		}
		else if (requestCode == ACTIVITY_SELECT_Grolloc ) 
		{
			touch = true;
			try {
				Uri currImageURI = data.getData();
				String[] proj = { Images.Media.DATA, Images.Media.ORIENTATION };
				Cursor cursor = managedQuery(currImageURI, proj, null, null,null);
				int columnIndex = cursor.getColumnIndex(proj[0]);
				cursor.moveToFirst();
				String mCurrentImagePath = cursor.getString(columnIndex);
				Bitmap bitmap = BitmapFactory.decodeFile(mCurrentImagePath);
				Dialog(bitmap);
			} catch (Exception e) {}			
		}
	}
	

		
	
	
	private void Dialog(Bitmap photo)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//init SQL
    	    			
		// Setting_Dialog UI set up
		LinearLayout linear=new LinearLayout(this);
		linear.setOrientation(1);
				
	
		ImageView PhotoImage = new ImageView(this);			
		PhotoImage.setImageBitmap(photo);
		linear.addView(PhotoImage);


	    builder.setView(linear); 
		
	    builder.setPositiveButton("上傳", new DialogInterface.OnClickListener() 
		{
	        public void onClick(DialogInterface dialog, int id) 
	        {
	        	
	        }
	    });
		
	    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() 
	    {
	        public void onClick(DialogInterface dialog, int id) 
	        {
	        	
	        }
	    });
	    
	    AlertDialog setting_dialog = builder.create();
	    setting_dialog.show();
	}

}
