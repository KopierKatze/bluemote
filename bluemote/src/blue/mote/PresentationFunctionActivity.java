package blue.mote;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

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
		bindKey(R.id.black_btn, "B");
		
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
}
