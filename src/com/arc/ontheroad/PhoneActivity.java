package com.arc.ontheroad;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup;

public class PhoneActivity extends Activity {
	String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
	String basepath = extStorageDirectory + "/ESOC_face/";
	ImageButton    pepole_info_button;
	ImageButton    cell_phone_button;
	ImageButton    cell_phone_button2;
	

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.phone);
		setRequestedOrientation(1);
	
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
		
	    pepole_info_button  = (ImageButton) findViewById(R.id.imageButton3);
	    pepole_info_button.setOnClickListener(new ImageButton.OnClickListener() {
			   public void onClick(View v)
				{
				    //Intent intent = new Intent();
					//intent.setClass(PhoneActivity.this,WebActivity.class);
					//startActivityForResult(intent, 0);
				   SQLdb_Dialog();

				}
		});  
	    
	    cell_phone_button  = (ImageButton) findViewById(R.id.imageButton1);
	    cell_phone_button.setOnClickListener(new ImageButton.OnClickListener() {
			   public void onClick(View v)
				{
				   String phoneNumber = "062932689"; 
				   Intent intentDial = new Intent("android.intent.action.CALL",Uri.parse("tel:"+phoneNumber));
				   startActivity(intentDial);
				}
		}); 
	    
	    cell_phone_button2  = (ImageButton) findViewById(R.id.ImageButton2);
	    cell_phone_button2.setOnClickListener(new ImageButton.OnClickListener() {
			   public void onClick(View v)
				{
				   String phoneNumber = "1999"; 
				   Intent intentDial = new Intent("android.intent.action.CALL",Uri.parse("tel:"+phoneNumber));
				   startActivity(intentDial);

				}
		});
	}
	
	private void SQLdb_Dialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		// Setting_Dialog UI set up
		LinearLayout linear=new LinearLayout(this);
		linear.setOrientation(1);
				
			
		ImageView AboutImage = new ImageView(this);
		AboutImage.setImageResource(R.drawable.aboutroad);
							
	    linear.addView(AboutImage);


	    builder.setView(linear); 
				
	    
	    AlertDialog setting_dialog = builder.create();
	    setting_dialog.show();
	}
}
