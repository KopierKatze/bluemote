package blue.mote;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class PresentationFunctionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.presentation);
		bindKey(R.id.next_btn, "Right");
		bindKey(R.id.prev_btn, "Left");
		bindKey(R.id.black_btn, "b");
	}
	
	void bindKey(final int btn_id, final String key) {
		View btn = (View)findViewById(btn_id);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				sendKey(key);
			}
		});
	}
	
	void sendKey(String key) {
		BluemoteActivity.device_manager.write("key "+key+"\n");
	}
}
