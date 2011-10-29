package blue.mote;

import java.util.Set;

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
	static BluetoothAdapter bt_adapter;
	static ArrayAdapter<String> btlist;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		btlist = new ArrayAdapter<String>(this,
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
				startActivity(choose_function);
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (!activateBT())
			showMessage("You have no Bluetooth. We can't go on here.");
	}

	boolean activateBT() {
		bt_adapter = BluetoothAdapter.getDefaultAdapter();
		if (bt_adapter == null)
			return false;
		if (!bt_adapter.isEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		} else
			discoverBT();
		return true;
	}

	void discoverBT() {
		showMessage("Discovering Bluetooth devices.");
		Set<BluetoothDevice> devices = bt_adapter.getBondedDevices();
		for (BluetoothDevice device : devices) {
			btlist.add(device.getName() + " " + device.getAddress());
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
}
