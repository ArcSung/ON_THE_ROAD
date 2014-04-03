package com.arc.android_timer;


import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;



public class BluetoothDiscovery 
{
	private static final String TAG = "DiscoveryBluetooth"; 
	ArrayList<String> BluetoothArray = new ArrayList<String>();
	private BluetoothAdapter mBTA;
	private SingBroadcastReceiver mReceiver;
	Set<BluetoothDevice> pairedDevices;
	
	public BluetoothDiscovery(Activity activity, ArrayList<String> array)
	{
		mBTA = BluetoothAdapter.getDefaultAdapter();
		BluetoothArray = array;
		if(mBTA.isDiscovering())
			mBTA.cancelDiscovery();
		
		pairedDevices = mBTA.getBondedDevices();
		mBTA.startDiscovery();
		
		mReceiver = new SingBroadcastReceiver();
		IntentFilter ifilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		activity.registerReceiver(mReceiver, ifilter); 
		
	}
	
	public ArrayList<String> getBTAddress()
	{
	    if (pairedDevices.size() > 0) 
	    {
	            for (BluetoothDevice device : pairedDevices) 
	            {
	            	BluetoothArray.add(device.getAddress());
	            }
	    }        
		
		return BluetoothArray;
	}

	// The BroadcastReceiver that listens for discovered devices and
	// changes the title when discovery is finished
	private class SingBroadcastReceiver extends BroadcastReceiver 
	{
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();

	        // When discovery finds a device
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            // Get the BluetoothDevice object from the Intent
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
	            	BluetoothArray.add(device.getAddress());
	            	Log.i(TAG, "device.getAddress():"+ device.getAddress());
	            }
	        }
	    }
	};
}
