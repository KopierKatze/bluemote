package com.test;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Bluetest extends Activity {
	static int REQUEST_ENABLE_BT = 5;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		TextView tView = (TextView) findViewById(R.id.textView1);
		ListView lView = (ListView) findViewById(R.id.listView1);
		BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
		BluetoothSocket socket = null;
		ArrayAdapter<String> btDevAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1);
		try {
			if (bAdapter == null) {
				tView.setText("no bt device");
			} else {
				if (!bAdapter.isEnabled()) {
					Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
				}

				Set<BluetoothDevice> devices = bAdapter.getBondedDevices();
				tView.setText(Integer.toString(devices.size()));
				String names = "";
				if (devices.size() > 0) {
					for (BluetoothDevice device : devices) {
						String deviceName = device.getName();
						btDevAdapter.add(deviceName);
						names = names + "" + deviceName;
						if (device.getName().equals("bay-0")) {
							tView.setText("found bay");
							socket = device
									.createRfcommSocketToServiceRecord(UUID
											.fromString("c6a54615-3868-4b38-bfa3-8aa0d6d2b9f5"));
						}
					}
				}
				lView.setAdapter(btDevAdapter);
				tView.setText(names);
				if (socket != null) {
					tView.setText("socket open");
					socket.connect();
					OutputStream ostream = socket.getOutputStream();
					ostream.write("key F11\n".getBytes());
					ostream.flush();
				}
			}
		} catch (IOException e) {
			tView.setText(e.getMessage());
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setText(Integer.toString(requestCode) + ", "
				+ Integer.toString(resultCode));
	}
}