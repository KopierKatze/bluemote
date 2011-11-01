package blue.mote;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.app.ListActivity;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import blue.mote.ChooseDeviceActivity.BluetoothDeviceWrap;

public class ChooseFunctionActivity extends ListActivity {

	static final String[] FUNCTIONS = new String[] { "Presentation", "VLC",
			"schlumpfen" };
	static final UUID MY_UUID =
		UUID.fromString("c6a54615-3868-4b38-bfa3-8aa0d6d2b9f5");

	DeviceManager device_manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, FUNCTIONS);
		setListAdapter(adapter);

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Class<?> cls = null;
				if (position == 0)
					cls = PresentationFunctionActivity.class;
				else if (position == 1)
					cls = VlcFunctionActivity.class;

				CharSequence toast_msg = ((TextView) view).getText();
				if (cls == null)
					toast_msg = "unknown function " + toast_msg;

				Toast.makeText(getApplicationContext(), toast_msg,
						Toast.LENGTH_SHORT).show();

				if (cls != null) {
					Intent intent = new Intent(ChooseFunctionActivity.this, cls);
					startActivity(intent);
				}
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();

		BluetoothDeviceWrap bt_device = ChooseDeviceActivity.bt_device;
		device_manager = new DeviceManager(bt_device);
		device_manager.start();
	}

	void showMessage(CharSequence s) {
		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
	}

	void onDeviceRead(String message) {
		showMessage(message);
	}

	void onDeviceConnected() {
		showMessage("connected");
	}

	class DeviceManager extends Thread {

		private final BluetoothDeviceWrap device;
		private BluetoothSocket socket;
		private InputStream ins;
		private OutputStream outs;
		private boolean connected = false;

		public DeviceManager(BluetoothDeviceWrap device) {
			this.device = device;
		}

		@Override
		public void run() {
			super.run();

			/*
			runOnUiThread(new Runnable() {
				public void run() {
					showMessage("device manager run");
				}
			});
			*/

			final String[] enr = new String[1];
			enr[0] = "0";
			try {
				socket = device.bt.createRfcommSocketToServiceRecord(MY_UUID);
				enr[0] += "1";
				socket.connect();
				enr[0] += "2";
				connected = true;
				ins = socket.getInputStream();
				enr[0] += "3";
				outs = socket.getOutputStream();
				enr[0] += "4";
				
				runOnUiThread(new Runnable() {
					public void run() {
						onDeviceConnected();
					}
				});
				
				write("key F11\n");
				
				while (connected) {
					final byte[] buffer = new byte[1024];
					ins.read(buffer);
				
					runOnUiThread(new Runnable() {
						public void run() {
							onDeviceRead(buffer.toString());
						}
					});
				}
			} catch (final IOException e) {
				
				runOnUiThread(new Runnable() {
					public void run() {
						showMessage(enr[0] + " " + e.getMessage());
					}
				});
				
				disconnect();
			}

			/*
			runOnUiThread(new Runnable() {
				public void run() {
					showMessage("device manager stops");
				}
			});
			*/
		}

		public void disconnect() {
			try {
				connected = false;
				socket.close();
			} catch (final IOException e) {
				
				runOnUiThread(new Runnable() {
					public void run() {
						showMessage(e.getMessage());
					}
				});
			}
		}

		public void write(String s) {
			try {
				outs.write(s.getBytes());
			} catch (final IOException e) {
				
				runOnUiThread(new Runnable() {
					public void run() {
						showMessage(e.getMessage());
					}
				});
			}
		}
	}
}
