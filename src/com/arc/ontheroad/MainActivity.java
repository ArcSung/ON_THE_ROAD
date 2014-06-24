package com.arc.ontheroad;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;


import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup;

public class MainActivity extends Activity {
	String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
	String basepath = extStorageDirectory + "/ESOC_face/";
	
	ImageView mainview_view;
	ImageView preview_view;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(1);
	
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
		
	    setContentView(R.layout.preview);
	    ImageView preview_view = (ImageView) findViewById(R.id.ImageView_preview);
		preview_view.setImageResource(R.drawable.priview_an);  
		preview_view.setOnClickListener( new View.OnClickListener(){
	        public void onClick(View view)
	        {
	        	Intent intent = new Intent();
	        	intent.setClass(MainActivity.this,MainActivity2.class);
	        	startActivity(intent);
	        	finish();
	        }
	    });
	}	

}
