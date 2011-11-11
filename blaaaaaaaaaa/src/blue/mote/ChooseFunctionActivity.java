package blue.mote;

import java.util.UUID;

import android.app.ListActivity;
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

	UUID uuid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		uuid = UUID.fromString(getString(R.string.bluemote_uuid));
		
		final String[] functions = new String[] {
				getString(R.string.presentation_function),
				getString(R.string.vlc_function) };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, functions);
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

				if (cls == null)
					showMessage("unknown function "
							+ ((TextView) view).getText());
				else {
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
		
		//DeviceManager dm = new DeviceManager(bt_device, uuid);
		BtServer btserver = new BtServer(bt_device, uuid);
		System.out.println("run next");
		btserver.run();
		System.out.println("fred");
		/*dm.onErrorCallback = new DeviceManager.OnErrorCallback() {
			public void call(final String msg) {
				runOnUiThread(new Runnable() {
					public void run() {
						showMessage(msg);
					}
				});
			}
		};*/
		
		//dm.start();
		//BluemoteActivity.device_manager = dm;
	}

	void showMessage(CharSequence s) {
		Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
	}
}
