package blue.mote;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;

public class BluemoteActivity extends Activity {

	static DeviceManager device_manager;
	static BluetoothAdapter bt_adapter;
	static HttpClient httpclient;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		httpclient = new DefaultHttpClient();
		Intent intent = new Intent(this, PresentationFunctionActivity.class);
		startActivity(intent);
		
	}
}