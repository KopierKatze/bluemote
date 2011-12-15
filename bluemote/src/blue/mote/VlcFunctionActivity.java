package blue.mote;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class VlcFunctionActivity extends Activity {
	static boolean playing = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vlc);
		BluemoteActivity.bluemote.disableScreenLock();
		ImageButton btn = (ImageButton)findViewById(R.id.play);
		btn.setImageResource(playing ? R.drawable.pause : R.drawable.play);
		playKey(R.id.play, "key space\n");
		bindKey(R.id.louder, 
				"keydown Control_L\n" + 
				"key Up\n" +
				"keyup Control_L\n");
		bindKey(R.id.quieter, 
				"keydown Control_L\n" + 
				"key Down\n" + 
				"keyup Control_L\n");
		bindKey(R.id.slowback, 
				"keydown Shift_L\n" + 
				"key Left\n" + 
				"keyup Shift_L\n");
		bindKey(R.id.slowforward, 
				"keydown Shift_L\n" + 
				"key Right\n" + 
				"keyup Shift_L\n");
		bindKey(R.id.fastback, "key p\n");
		bindKey(R.id.fastforward, "key n\n");
	}

	void bindKey(final int btn_id, final String key) {
		View btn = (View)findViewById(btn_id);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				sendKeyViaBT(key);
				
			}
		});
	}
	
	private void playKey(final int btn_id, final String key){
		final ImageButton btn = (ImageButton)findViewById(btn_id);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				playing = !playing;
				btn.setImageResource(playing ? R.drawable.pause : R.drawable.play);
				sendKeyViaBT(key);
			}
		});
	}
	
	private void sendKeyViaBT(String command){
		BluemoteActivity.device_manager.write(command);
	}
	
	protected void onStop(Bundle savedInstanceState){
		BluemoteActivity.bluemote.reenableScreenLock();
	}
	protected void onPause(Bundle savedInstanceState){
		BluemoteActivity.bluemote.reenableScreenLock();
	}
	protected void onDestroy(Bundle savedInstanceSate){
		BluemoteActivity.bluemote.reenableScreenLock();
	}
	protected void onResume(Bundle savedInstanceState){
		BluemoteActivity.bluemote.disableScreenLock();
	}
}
