package com.arc.android_timer;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;


public class UiDialog implements OnSeekBarChangeListener
{
	
	private static final String TAG = "Dialog"; 
	private String[] AddressList = {"98:D3:31:B1:77:84","00:14:01:25:11:21"};
	private String DEVICE_ADDRESS;
	private String DEVICE_ADDRESS2;
	private int Threshold;
	
	private SeekBar ThresholdSeek;
	private TextView ThresholdText;
	private TextView FinishText;
	private TextView StartText;
	private Spinner  FinishSpinner;
	private Spinner  StartSpinner;
	private BluetoothDiscovery BTD;
	
	Activity MainActivity;
	
	public void UiDialog_main(Activity activity, int arg, int Threshold_formMain, String DEVICE_ADDRESS_formMain, String DEVICE_ADDRESS_formMain2)
	{
		Log.i(TAG, "UiDialog_main arg:"+arg);
		MainActivity = activity;
		switch(arg)
		{
		    case R.id.setting:
		    	Threshold = Threshold_formMain;
		    	DEVICE_ADDRESS = DEVICE_ADDRESS_formMain;
		    	DEVICE_ADDRESS2 = DEVICE_ADDRESS_formMain2;
		    	Setting_Dialog();
		    	break;
		    case R.id.about:
		    	Log.i(TAG, "UiDialog_main about");
		    	About_Dialog();
		    	break;
		}
	}
	
	private void About_Dialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity);
		builder.setTitle("About").setMessage("Writer: Arc Sung").show();		
	}
	
	private void Setting_Dialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity);
		builder.setTitle("Setting");
		
		// Setting_Dialog UI set up
		LinearLayout linear=new LinearLayout(MainActivity);
		linear.setOrientation(1);
		
		
		ThresholdText=new TextView(MainActivity);
		ThresholdText.setTypeface(null, Typeface.BOLD);
		ThresholdText.setText("Threshold:" + Threshold + "cm");
		
		ThresholdSeek=new SeekBar(MainActivity);
		ThresholdSeek.setProgress((int)((Threshold - 20)* 0.435));
		ThresholdSeek.setOnSeekBarChangeListener(this);
		
		FinishText=new TextView(MainActivity);
		FinishText.setTypeface(null, Typeface.BOLD);
		FinishText.setText("Finishing Address:");
			
		FinishSpinner = new Spinner(MainActivity);
		
	    ArrayList<String> spinnerArray = new ArrayList<String>();
	    spinnerArray.add(DEVICE_ADDRESS);
		BTD = new BluetoothDiscovery(MainActivity, spinnerArray);
	    //spinnerArray.add("98:D3:31:B1:77:84");
	    //spinnerArray.add("00:14:01:25:11:21");
	    spinnerArray = BTD.getBTAddress();
	    
	    FinishSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
	    {
	    	public void onItemSelected(AdapterView adapterView, View view, int position, long id)
	    	{
	    		DEVICE_ADDRESS = adapterView.getSelectedItem().toString();
	    	}
	    	
	    	public void onNothingSelected(AdapterView arg0){}; 
	    });
	    
	    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(MainActivity, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
	    FinishSpinner.setAdapter(spinnerArrayAdapter);
	    
		StartText=new TextView(MainActivity);
		StartText.setTypeface(null, Typeface.BOLD);
		StartText.setText("Starting Address:");
		
		StartSpinner = new Spinner(MainActivity);
		
	    ArrayList<String> spinnerArray2 = new ArrayList<String>();
	    spinnerArray2.add(DEVICE_ADDRESS2);
		BTD = new BluetoothDiscovery(MainActivity, spinnerArray2);
	    //spinnerArray.add("98:D3:31:B1:77:84");
	    //spinnerArray.add("00:14:01:25:11:21");
	    spinnerArray = BTD.getBTAddress();
	    
	    StartSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
	    {
	    	public void onItemSelected(AdapterView adapterView, View view, int position, long id)
	    	{
	    		DEVICE_ADDRESS2 = adapterView.getSelectedItem().toString();
	    	}
	    	
	    	public void onNothingSelected(AdapterView arg0){}; 
	    });
	    
	    ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(MainActivity, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
	    StartSpinner.setAdapter(spinnerArrayAdapter2);
	    		
		
	    linear.addView(ThresholdText);
	    linear.addView(ThresholdSeek);
	    linear.addView(FinishText);
	    linear.addView(FinishSpinner);
	    linear.addView(StartText);
	    linear.addView(StartSpinner);

	    builder.setView(linear); 
				
		
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() 
		{
	        public void onClick(DialogInterface dialog, int id) 
	        {
	        	int flag = DEVICE_ADDRESS.compareTo(DEVICE_ADDRESS2);
	        	if (flag == 0)
	        	{
	        		DEVICE_ADDRESS  = "NULL";
	        		DEVICE_ADDRESS2 = "NULL";
	        		Toast.makeText(MainActivity, "starting and Finishing address can't set same", Toast.LENGTH_LONG).show();
	        	}	
	    		((MainActivity) MainActivity).ArdConnect_setting(DEVICE_ADDRESS, DEVICE_ADDRESS2);
	    		if(DEVICE_ADDRESS != "NULL")
	    		{
	    			((MainActivity) MainActivity).ArdConnect(DEVICE_ADDRESS);
	    		}
	    		
	    		if(DEVICE_ADDRESS2 != "NULL")
	    		{
	    			((MainActivity) MainActivity).ArdConnect(DEVICE_ADDRESS2);
	    		}	
	        }
	    });
		
	    builder.setNegativeButton("Clean", new DialogInterface.OnClickListener() 
	    {
	        public void onClick(DialogInterface dialog, int id) 
	        {
	        	DEVICE_ADDRESS  = "NULL";
        		DEVICE_ADDRESS2 = "NULL";
        		((MainActivity) MainActivity).ArdConnect_setting(DEVICE_ADDRESS, DEVICE_ADDRESS2);
	        }
	    });
	    
	    AlertDialog setting_dialog = builder.create();
	    setting_dialog.show();
	}
	


	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		Threshold=(int) (progress * 2.3 + 20);
		ThresholdText.setText("Threshold:" + Threshold + "cm");
		((MainActivity) MainActivity).ArdSetting(Threshold);		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	
}