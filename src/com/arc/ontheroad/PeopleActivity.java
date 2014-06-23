package com.arc.ontheroad;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;


import android.R.string;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.preference.PreferenceManager;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup;

public class PeopleActivity extends Activity {
	String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
	private static final int ACTIVITY_SELECT_name = 0;
	private static final int ACTIVITY_SELECT_email = 1;
	private static final int ACTIVITY_SELECT_Phone = 2;
	private static final int ACTIVITY_SELECT_address = 3;
	int select_flag = 0;
	ImageView pepole_info_view; 
	EditText  pepole_info_edittext;
	Button    pepole_info_button;

	String NAME;
	String EMAIL;
	String PHONE;
	String ADDRESS;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.people);
		setRequestedOrientation(1);
	
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
	    
	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	    NAME = prefs.getString("NAME", "小宋");
	    EMAIL = prefs.getString("EMAIL", "ArcLaviz@gmail.com");
	    PHONE = prefs.getString("PHONE", "0987654321");
	    ADDRESS = prefs.getString("ADDRESS", "台灣台南");
	    
	    pepole_info_view = (ImageView) findViewById(R.id.imageView2);
	    pepole_info_edittext = (EditText) findViewById(R.id.edittext);
	    pepole_info_button  = (Button) findViewById(R.id.button1);
	    
	    if(NAME != "NULL")
	    	pepole_info_edittext.setText(NAME);
	    
	    
	    pepole_info_button.setOnClickListener(new Button.OnClickListener() {
			   public void onClick(View v)
				{
				  if(select_flag == ACTIVITY_SELECT_name)
				  {
					  NAME = pepole_info_edittext.toString();
					  pepole_info_edittext.setText(EMAIL);
					  select_flag = ACTIVITY_SELECT_email;
					  pepole_info_view.setImageResource(R.drawable.email);
					  
				  }
				  else if (select_flag == ACTIVITY_SELECT_email)
				  {
					  EMAIL = pepole_info_edittext.toString();
					  pepole_info_edittext.setText(PHONE);
					  select_flag = ACTIVITY_SELECT_Phone;
					  pepole_info_view.setImageResource(R.drawable.callphone);
				  }
				  else if (select_flag == ACTIVITY_SELECT_Phone)
				  {
					  PHONE = pepole_info_edittext.toString();
					  pepole_info_edittext.setText(ADDRESS);
					  select_flag = ACTIVITY_SELECT_address;
					  pepole_info_view.setImageResource(R.drawable.address);
					  pepole_info_button.setText("完成");
				  }
				  else if (select_flag == ACTIVITY_SELECT_address)
				  {
					  ADDRESS = pepole_info_edittext.toString();
					  finishactivity();
				  }
				}
		});   
	    
	}
	
	void finishactivity()
	{
		PreferenceManager.getDefaultSharedPreferences(this)
		.edit()
			.putString("NAME", NAME)
			.putString("EMAIL", EMAIL)
			.putString("PHONE", PHONE)
			.putString("ADDRESS", ADDRESS);

		this.finish();
	}
	


}
