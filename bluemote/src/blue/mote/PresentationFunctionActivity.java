package blue.mote;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class PresentationFunctionActivity extends FunctionActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.presentation);
		
		disableScreenLock();
		
		bindKey(R.id.next_btn, "key Right");
		bindKey(R.id.prev_btn, "key Left");
		bindKey(R.id.black_btn, "key B");
		
	}
	
	void bindKey(final int btn_id, final String key) {
		View btn = (View)findViewById(btn_id);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				sendKeyViaBT(key);
			}
		});
	}
}
