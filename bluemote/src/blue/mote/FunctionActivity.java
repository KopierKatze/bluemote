package blue.mote;

import blue.mote.BluemoteService.MyBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.WindowManager;

public class FunctionActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = new Intent(this, BluemoteService.class);
		bindService(intent, service_connection, BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onDestroy() {
		unbindService(service_connection);
		super.onDestroy();
	}
	
	public void disableScreenLock(){
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
	}
	
	public void reenableScreenLock(){
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
	}
	
	private BluemoteService bluemote_service;
	
	void sendKeyViaBT(String command){
		if (bluemote_service != null)
			bluemote_service.sendCommandToDevice(command);
	}
	
	private ServiceConnection service_connection = new ServiceConnection() {		
		public void onServiceDisconnected(ComponentName name) {}		
		public void onServiceConnected(ComponentName name, IBinder service) {
			MyBinder binder = (MyBinder)service;
			bluemote_service = binder.getService();
		}
	};
}
