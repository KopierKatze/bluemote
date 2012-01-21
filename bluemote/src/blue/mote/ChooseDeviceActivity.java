package blue.mote;

import android.app.ListActivity;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import blue.mote.BluemoteService.MyBinder;

public class ChooseDeviceActivity extends ListActivity {
	
	static final int REQUEST_ENABLE_BT = 1;
	static ArrayAdapter<BluetoothDeviceWrap> btlist;
	
	private BluemoteService bluemote_service = null;
	
	private ServiceConnection service_connection = new ServiceConnection() {		
		public void onServiceDisconnected(ComponentName name) {}		
		public void onServiceConnected(ComponentName name, IBinder service) {
			MyBinder binder = (MyBinder)service;
			bluemote_service = binder.getService();
			bluemote_service.registerListener(new MyListener());
			bluemote_service.discoverDevices();
		}
	};
	
	class MyListener implements BluemoteService.MyListener {
		public void clearDevices() {
			btlist.clear();
		}
		public void newDevice(BluetoothDevice device) {
			btlist.add(new BluetoothDeviceWrap(device));
     		}
		public void connectedToDevice() {
			Intent intent = new Intent(
					ChooseDeviceActivity.this,
					ChooseFunctionActivity.class);
			startActivity(intent);
		}
		public void notConnectedToDevice() {
			showMessage("No you can't. Fail!");
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		btlist = new ArrayAdapter<BluetoothDeviceWrap>(this,
				android.R.layout.simple_list_item_1);
		setListAdapter(btlist);

		Intent intent = new Intent(this, BluemoteService.class);
		bindService(intent, service_connection, BIND_AUTO_CREATE);
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(
			new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (bluemote_service == null) {
						// please wait...
					} else {
						showMessage(((TextView) view).getText());
						bluemote_service.connectToDevice(
								btlist.getItem(position).bt);
					}
				}
			}
		);
	}

	@Override
	protected void onStart() {
		super.onStart();
		btlist.clear();
		if (bluemote_service != null) bluemote_service.discoverDevices();
		//showMessage("You have no Bluetooth. We can't go on here.");
	}
	
	void showMessage(final CharSequence s) {
		runOnUiThread(new Runnable(){
			public void run() {
				Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	class BluetoothDeviceWrap {
		public BluetoothDevice bt;
		public BluetoothDeviceWrap(BluetoothDevice bt) {
			this.bt = bt;
		}
		public String toString() {
			return bt.getName() + " | " + bt.getAddress();
		}
	}
	
	@Override
	protected void onDestroy() {
		unbindService(service_connection);
		super.onDestroy();
	}
}
