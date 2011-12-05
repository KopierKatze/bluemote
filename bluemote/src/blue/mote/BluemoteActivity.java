package blue.mote;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;

public class BluemoteActivity extends Activity {

	static DeviceManager device_manager;
	static BluetoothAdapter bt_adapter;
	static HttpClient httpclient;
	static BluemoteActivity bluemote;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		bluemote = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		httpclient = new DefaultHttpClient(params);
		
		Intent intent = new Intent(this, ChooseDeviceActivity.class);
		startActivity(intent);
		
	}
	
	void disableScreenLock(){
		KeyguardManager keyguardManager = (KeyguardManager)getSystemService(Activity.KEYGUARD_SERVICE);
		KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
		lock.disableKeyguard();
	}
}