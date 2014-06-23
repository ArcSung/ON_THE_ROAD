package com.arc.ontheroad;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback, Camera.AutoFocusCallback { 

	public Camera mycamera; 
	public TextView fpsText;
	public ViewToDraw vtd;
	
	int rgb[];
	int locat[];
	int FaceRct[];
	int[] bitmapData;
    int numberOfFaceDetected;
    int x1,x2,y1,y2,correct;
    float myEyesDistance,PoseX,PoseY,PoseZ;
    float X,Y;
    private Bitmap image2;
    Paint whitePaint = new Paint();
    Paint whitePaint2 = new Paint();
    JSONArray jsonArrayMain = null;
    Double longitude;
    Double latitude;
    static String addressid = "";
    static String cityid = "";
    static String villageid = "";
    static String streetid = "";
    
    Bitmap newb;

	
	SurfaceHolder mHolder;	
	
	int pickedH, pickedW;

	List<Camera.Size> cameraSize;
	public Bitmap onTouchOK(){
		return newb;
	}
	
	public boolean onTouchEvent(MotionEvent event){
		 //this.surfaceDestroyed(mHolder);	
  	     mycamera.stopPreview();
  	     Toast toast = Toast.makeText(getContext(),"儲存照片",Toast.LENGTH_SHORT);
        toast.show();
        
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    	Date curDate = new Date(System.currentTimeMillis());
    	String date = formatter.format(curDate);

    	//JSONArray obj = getJson("http://linarnan.co");
    	/*try {
    		 
    		 
			for(int i = 0; i< obj.length();i++){
				String id = String.valueOf(i);
				JSONObject data = obj.getJSONObject(i);
				Log.d("show",data.getString("Name"));
			}
 
		} catch (JSONException e) {
			e.printStackTrace();
		}*/
    	
    	whitePaint.setColor(Color.WHITE);
		whitePaint.setStyle(Paint.Style.FILL);
		whitePaint.setStrokeWidth(3);
		whitePaint.setTextSize(80);
		whitePaint.setTypeface(Typeface.MONOSPACE);
		
    	whitePaint2.setColor(Color.WHITE);
		whitePaint2.setStyle(Paint.Style.FILL);
		whitePaint2.setStrokeWidth(3);
		whitePaint2.setTextSize(120);
		whitePaint2.setTypeface(Typeface.MONOSPACE);
    	
    	//create the new blank bitmap
    	newb = Bitmap.createBitmap( 1080, 1920, Bitmap.Config.RGB_565 );//創建一個新的和SRC長度寬度一樣的點陣圖
    	Canvas cv = new Canvas( newb );
    	//draw src into
    	cv.drawBitmap( image2, 0, 0, null );//在 0，0座標開始畫入src
    	cv.drawText(cityid, 70, 1600, whitePaint2);
    	cv.drawText(villageid, 400, 1600, whitePaint);
    	cv.drawText(streetid, 70, 1700, whitePaint);
    	//draw watermark into
    	cv.drawText(date, 50, 1800, whitePaint);
    	//save all clip
    	cv.save( Canvas.ALL_SAVE_FLAG );//保存
    	
    	cv.restore();//存儲


		try {
			FileOutputStream fos = new FileOutputStream( "/sdcard/DCIM/Camera/Tainna_"+date+".jpg" );
			    if ( fos != null )
			    {
			    	newb.compress(Bitmap.CompressFormat.JPEG, 100, fos );
			    fos.close();
			    }
			    // setWallpaper( bitmap );
			    } 
			    catch( IOException e )
			    {
			    Log.e("testSaveView", "Exception: " + e.toString() );
			    }
	    	 Log.i("tag","yes");
 
	    	 correct = 1;	 
	    // }	 
	    // else correct=0;
		return false;
   	
   }
  
	public CameraView(Context context, ViewToDraw _vtd, Context _context, Double longit, Double latitude) {
		super(context);
		mHolder = getHolder(); 
		mHolder.addCallback(this); 
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    longitude = longit;
	    latitude = latitude;
		this.vtd = _vtd;
    	//String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+latitude+","+longitude+"&sensor=true";
		String url = "http://linarnan.co:3000/roadhelper/aloha?lat="+latitude+"&lng="+longit;
		Log.i("ARC",url);
    	new HttpAsyncTask().execute(url);
    	vtd.setAddress(cityid, villageid, streetid);
	}


	public void surfaceCreated(SurfaceHolder holder) { 
		int i, temp;
		mycamera = Camera.open();
		//取得相機所支援的所有解析度
		cameraSize = mycamera.getParameters().getSupportedPreviewSizes();
		if(cameraSize != null){
			//選取最大的攝影機解析度(其實不是很建議用最大解析度, 因為相當吃資源速度也會被拖慢)
			temp = 0;
			for(i=0;i<cameraSize.size();i++){
			if(temp < ((cameraSize.get(i).height) * (cameraSize.get(i).width))){
					pickedH = (cameraSize.get(i).height);
					pickedW = (cameraSize.get(i).width);
					temp = ((cameraSize.get(i).height) * (cameraSize.get(i).width));
				}
			}

		}else{
			Log.e("tag","null");
		};
		try {
			mycamera.setPreviewDisplay(holder); 
			mycamera.setDisplayOrientation(90);
			vtd.setSize(pickedH, pickedW);
		} catch (IOException e) { 
			e.printStackTrace();
		}
 	}
	
	public void surfaceDestroyed(SurfaceHolder holder) { 
		mycamera.setPreviewCallback(null);
		mycamera.release();
		mycamera = null;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) { 	
		Camera.Parameters parameters = mycamera.getParameters();
		parameters.setPreviewSize(pickedW, pickedH);
		mycamera.setParameters(parameters);
		
		//產生 buffer
        PixelFormat p = new PixelFormat();
        PixelFormat.getPixelFormatInfo(parameters.getPreviewFormat(),p);
        int bufSize = (pickedW*pickedH*p.bitsPerPixel);
        
        //把buffer給preview callback備用
        byte[] buffer = new byte[bufSize];
        mycamera.addCallbackBuffer(buffer);                            
        buffer = new byte[bufSize];
        mycamera.addCallbackBuffer(buffer);
        buffer = new byte[bufSize];
        mycamera.addCallbackBuffer(buffer);
        //設定預覽畫面更新時的callback
        mycamera.setPreviewCallbackWithBuffer(new PreviewCallback() {
        	
			public void onPreviewFrame(byte[] data, Camera camera) { 
				vtd.putImage(data);
				vtd.CameraSet();
				vtd.setAddress(cityid, villageid, streetid);
				//更新畫布 (call onDraw())
				vtd.invalidate();
				bitmapData = new int[pickedW * pickedH];
			    byte[] rgbBuffer = new byte[pickedW * pickedH * 3];  
			    vtd.decodeYUV420SP(bitmapData, data, pickedW, pickedH);
			    Bitmap image = Bitmap.createBitmap(bitmapData, pickedW, pickedH, Bitmap.Config.RGB_565);
			    int width = image.getWidth();
				int height = image.getHeight();
				Matrix mMatrix = new Matrix();
				mMatrix.setRotate(90);
				image2 =Bitmap.createBitmap(image,0,0,width,height,mMatrix,true);
			    				
	        	mycamera.addCallbackBuffer(data);

			}
		});
        
		mycamera.startPreview();
	}

	public void onAutoFocus(boolean success, Camera camera) {
		// TODO Auto-generated method stub
	}
}