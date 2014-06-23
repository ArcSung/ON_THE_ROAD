package com.arc.ontheroad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MyCameraPreview extends Activity {
    /** Called when the activity is first created. */
    int correct=0;
    CameraView cameraView;
	Bitmap newb;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 取得全螢幕
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
 
        Intent intent = getIntent(); 
        Double longitude = intent.getDoubleExtra("longitude", 0);
        Double latitude = intent.getDoubleExtra("latitude", 0);
        
        //Log.i("Arc","longitude"+longitude+"  latitude"+latitude);
        setContentView(R.layout.premain); //#1        
        //鎖住螢幕方向
        setRequestedOrientation(1);   //Upright    
        //取得畫圖的View
        ViewToDraw dtw = (ViewToDraw) findViewById(R.id.vtd);
        dtw.setSize(display.getHeight(), display.getWidth()); //#2
 
        //產生攝影機預覽surfaceView
        cameraView = new CameraView(this, dtw, this.getApplicationContext(), longitude, latitude);
        //把預覽的surfaceView加到名為preview的FrameLayout
        ((FrameLayout) findViewById(R.id.preview)).addView(cameraView);
               
    }//end onCreate(Bundle savedInstanceState)
    
    
	public boolean onTouchEvent(MotionEvent event)
    {
		newb=cameraView.onTouchOK();
		Dialog(newb);
		//MyCameraPreview.this.finish();  
		return false;   	
    }//end onTouchEvent(MotionEvent event)
	
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
	        	MyCameraPreview.this.finish(); 
	        }
	    });
		
	    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() 
	    {
	        public void onClick(DialogInterface dialog, int id) 
	        {
	        	MyCameraPreview.this.finish();  
	        }
	    });
	    
	    AlertDialog setting_dialog = builder.create();
	    setting_dialog.show();
	}
}