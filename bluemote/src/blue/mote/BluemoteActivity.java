package blue.mote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class BluemoteActivity extends Activity {

	static DeviceManager device_manager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Intent choose_device = new Intent(this, ChooseDeviceActivity.class);
		startActivity(choose_device);
	}
}