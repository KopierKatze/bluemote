package blue.mote;

import android.app.Activity;
import android.os.Bundle;

public class PresentationFunctionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.presentation);
		sendKey("F11");
	}
	
	void sendKey(String key) {
		BluemoteActivity.device_manager.write("key "+key+"\n");
	}
}
