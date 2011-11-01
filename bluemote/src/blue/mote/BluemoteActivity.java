package blue.mote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class BluemoteActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Intent choose_device = new Intent(this, ChooseDeviceActivity.class);
		startActivity(choose_device);
	}
}