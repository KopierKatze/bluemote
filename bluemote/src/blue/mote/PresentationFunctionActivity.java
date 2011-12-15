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

public class PresentationFunctionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.presentation);
		BluemoteActivity.bluemote.disableScreenLock();
		
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
	
	private void sendKeyViaBT(String command){
		BluemoteActivity.device_manager.write(command);
	}
	
	void sendKey(String key) {
	
		URI ur = null;
		try {
			ur = new URI("http://192.168.2.106:4242/cmd");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		StringEntity ent = null;
		try {
			ent = new StringEntity("key " + key + "\n");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		HttpPost post = new HttpPost();
		post.setURI(ur);
		post.setEntity(ent);
		
		HttpResponse response = null;
		try {
			response = BluemoteActivity.httpclient.execute(post);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
