package blue.mote;

import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class VlcFunctionActivity extends FunctionActivity {
	
	static boolean playing = false;
	static PhoneStateListener phonelistener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vlc);
		
		phonelistener = new PhoneStateListener(){
			@Override
		    public void onCallStateChanged(int state, String incomingNumber) {
		        switch (state) {
		        
		        case TelephonyManager.CALL_STATE_RINGING:
		            if(playing)
		            	switchPlayBtn();
		        	break;
		            
		        default:
		            break;
		        }
		    }
		};
		
		disableScreenLock();
		
		playKey();
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
	
	private void playKey(){
		final ImageButton btn = (ImageButton)findViewById(R.id.play);
		btn.setImageResource(playing ? R.drawable.pause : R.drawable.play);
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				switchPlayBtn();
			}
		});
	}
	
	private void switchPlayBtn(){
		final ImageButton btn = (ImageButton)findViewById(R.id.play);
		playing = !playing;
		btn.setImageResource(playing ? R.drawable.pause : R.drawable.play);
		sendKeyViaBT("key space\n");
	}
}
