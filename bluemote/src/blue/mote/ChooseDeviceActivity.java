package blue.mote;

import java.util.Set;
import java.util.UUID;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ChooseDeviceActivity extends ListActivity {
	
	static final int REQUEST_ENABLE_BT = 1;
	static ArrayAdapter<BluetoothDeviceWrap> btlist;
	static BluetoothDeviceWrap bt_device;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		btlist = new ArrayAdapter<BluetoothDeviceWrap>(this,
				android.R.layout.simple_list_item_1);
		setListAdapter(btlist);

		final Intent choose_function = new Intent(this,
				ChooseFunctionActivity.class);

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				showMessage(((TextView) view).getText());
				bt_device = btlist.getItem(position);
				DeviceManager dm = new DeviceManager(bt_device, UUID.fromString(getString(R.string.bluemote_uuid)));
				dm.run();
				BluemoteActivity.device_manager = dm;
				startActivity(choose_function);
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		btlist.clear();
		if (!activateBT())
			showMessage("You have no Bluetooth. We can't go on here.");
	}

	boolean activateBT() {
		BluemoteActivity.bt_adapter = BluetoothAdapter.getDefaultAdapter();
		if (BluemoteActivity.bt_adapter == null)
			return false;
		if (!BluemoteActivity.bt_adapter.isEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		} else
			discoverBT();
		return true;
	}

	void discoverBT() {
		showMessage("Discovering Bluetooth devices.");
		Set<BluetoothDevice> bonded_devices = BluemoteActivity.bt_adapter.getBondedDevices();
		for (BluetoothDevice device : bonded_devices) {
			btlist.add(new BluetoothDeviceWrap(device));
		}
		// TODO discovering devices
		// https://developer.android.com/guide/topics/wireless/bluetooth.html
	}
	
	void showMessage(CharSequence s) {
		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == RESULT_OK)
				discoverBT();
			else
				showMessage("We really need Bluetooth to go on.");
		}
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
}
