package com.arc.android_timer;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import at.abraxas.amarino.Amarino;
import at.abraxas.amarino.AmarinoIntent;
 
public class MainActivity extends Activity  {
 
private TextView timer;
private TextView receive;
private Button start;
private Button stop;
private Button zero;
private Button end;

private ArduinoReceiver arduinoReceiver = new ArduinoReceiver();
private int s_start = 1, s_stop = 2;
private boolean startflag=false;
private int tsec=0,csec=0,cmin=0, minsec=0, Threshold=150;
private String DEVICE_ADDRESS = "NULL";
private String DEVICE_ADDRESS2 = "NULL";
private static final String TAG = "BluetoothStopCount"; 
private UiDialog UiDialogSetting;

//98:D3:31:B1:77:84
//00:14:01:25:11:21

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    timer = (TextView)findViewById(R.id.timer);
    receive = (TextView)findViewById(R.id.receive);
    start = (Button)findViewById(R.id.start);
    stop = (Button)findViewById(R.id.stop);
    zero = (Button)findViewById(R.id.zero);
    end = (Button)findViewById(R.id.end);
    
    
    
    
    //宣告Timer
    Timer timer01 =new Timer();
 
    //設定Timer(task為執行內容，0代表立刻開始,間格1秒執行一次)
    timer01.schedule(task, 0,10);
 
    //Button監聽
    start.setOnClickListener(listener);
    stop.setOnClickListener(listener);
    zero.setOnClickListener(listener);
    end.setOnClickListener(listener);
    
    UiDialogSetting = new UiDialog();
}

@Override
protected void onStart(){
	super.onStart();
	
	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	Threshold = prefs.getInt("Threshold", 150);
	//DEVICE_ADDRESS = prefs.getString("DEVICE_ADDRESS", "NULL");
	if(DEVICE_ADDRESS != "NULL")
		ArdConnect(DEVICE_ADDRESS);
	
	if(DEVICE_ADDRESS2 != "NULL")
		ArdConnect(DEVICE_ADDRESS2);
	
	registerReceiver(arduinoReceiver, new IntentFilter(AmarinoIntent.ACTION_RECEIVED));
	ArdSetting(Threshold);
}

@Override
protected void onStop() {
	super.onStop();
	
	PreferenceManager.getDefaultSharedPreferences(this)
	.edit()
		.putInt("Threshold", Threshold)
		//.putString("DEVICE_ADDRESS", DEVICE_ADDRESS)
	.commit();
	
	// if you connect in onStart() you must not forget to disconnect when your app is closed
	Amarino.disconnect(this, DEVICE_ADDRESS);
	Amarino.disconnect(this, DEVICE_ADDRESS2);
	
	// do never forget to unregister a registered receiver
	unregisterReceiver(arduinoReceiver);
}


@Override
protected void onResume(){
	super.onResume();
	Amarino.connect(this, DEVICE_ADDRESS);
	
}

 
//TimerTask無法直接改變元件因此要透過Handler來當橋樑
private Handler handler = new Handler(){
    public  void  handleMessage(Message msg) {
        super.handleMessage(msg);
        switch(msg.what){
            case 1:
                csec=tsec%60;
                cmin=tsec/60;
                String s="";
                if(cmin <10){
                    s="0"+cmin;
                }else{
                    s=""+cmin;
                }
                if(csec < 10){
                    s=s+":0"+csec;
                }else{
                    s=s+":"+csec;
                }
                s=s+"."+minsec;
                //s字串為00:00格式
                timer.setText(s);
           break;
        }
    }
};
 
private TimerTask task = new TimerTask(){
 
@Override
    public void run() {
// TODO Auto-generated method stub
        if (startflag){
    //如果startflag是true則每秒tsec+1
           minsec++;
           if(minsec>=100)
           {   
             tsec++;
             minsec=0;
           }  
           Message message = new Message();
 
    //傳送訊息1
           message.what =1;
           handler.sendMessage(message);
        }
    }
 
};
 
private OnClickListener listener =new OnClickListener(){
 
@Override
    public void onClick(View v) {
// TODO Auto-generated method stub
        switch(v.getId()){
            case R.id.start:
                startflag=true;
                ArdUpdateState(s_start);
            break;
            case R.id.stop:
                startflag=false;
                ArdUpdateState(s_stop);
            break;
            case R.id.zero:
                tsec=0;
                minsec=0;
            //TextView 初始化
                timer.setText("00:00");
            break;
            case R.id.end:
                finish();
            break;
        }
    } 
};



// Arduino relation function
public class ArduinoReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String data = null;
		
		// the device address from which the data was sent, we don't need it here but to demonstrate how you retrieve it
		final String address = intent.getStringExtra(AmarinoIntent.EXTRA_DEVICE_ADDRESS);
		int Finsih_flag = address.compareTo(DEVICE_ADDRESS);
		int Start_flag = address.compareTo(DEVICE_ADDRESS2);
		// the type of data which is added to the intent
		final int dataType = intent.getIntExtra(AmarinoIntent.EXTRA_DATA_TYPE, -1);
		
		// we only expect String data though, but it is better to check if really string was sent
		// later Amarino will support differnt data types, so far data comes always as string and
		// you have to parse the data to the type you have sent from Arduino, like it is shown below
		if (dataType == AmarinoIntent.STRING_EXTRA){
			data = intent.getStringExtra(AmarinoIntent.EXTRA_DATA);
			
			if (data != null){
				receive.setText(data);
				//mValueTV.setText(data);
				try {
					// since we know that our string value is an int number we can parse it to an integer
					final int sensorReading = Integer.parseInt(data);
					if(startflag==true && sensorReading <= Threshold && Finsih_flag == 0)
					{
						Log.i(TAG, "ArduinoReceiver:"+data);
					    startflag=false;
		                ArdUpdateState(s_stop);
					}
					else if(startflag==false && sensorReading <= Threshold && Start_flag == 0)
					{
						Log.i(TAG, "ArduinoReceiver2:"+data);
					    startflag=true;
		                ArdUpdateState(s_start);
					}
					//mGraph.addDataPoint(sensorReading);
				} 
				catch (NumberFormatException e) { Log.i(TAG, "oh data was not an integer");/* oh data was not an integer */ }
			}
		}
	}
}

public void ArdConnect(String str)
{
	Log.i(TAG, "ArdConnect:"+str);
	if(Amarino.isCorrectAddressFormat(str))
		Amarino.connect(this, str);
	else
		Log.d(TAG, "AddressFormat error:"+str);
}

public void ArdConnect_setting(String str, String str2)
{
	DEVICE_ADDRESS = str;
	Log.i(TAG, "DEVICE_ADDRESS :"+str);
	DEVICE_ADDRESS2 = str2;
	Log.i(TAG, "DEVICE_ADDRESS2 :"+str2);
}

public void ArdSetting(int arg)
{
	Threshold = arg;
	Log.i(TAG, "ArdSetting:"+arg);
	Amarino.sendDataToArduino(this, DEVICE_ADDRESS, 's', arg);
}

public void ArdUpdateState(int arg)
{
	Log.i(TAG, "ArdUpdateState:"+arg);
	Amarino.sendDataToArduino(this, DEVICE_ADDRESS, 'o', arg);
	//Amarino.sendDataToArduino(this, DEVICE_ADDRESS2, 'o', arg);
}


@Override
public boolean onCreateOptionsMenu(Menu menu) 
{
	MenuInflater inflater=getMenuInflater();
	inflater.inflate(R.menu.more_tab_menu, menu);
	return super.onCreateOptionsMenu(menu);
}

@Override
public boolean onOptionsItemSelected(MenuItem item) 
{
	UiDialogSetting.UiDialog_main(this, item.getItemId(), Threshold, DEVICE_ADDRESS, DEVICE_ADDRESS2);
	return true;
}

}
