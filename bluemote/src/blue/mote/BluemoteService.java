package blue.mote;

import java.util.Set;
import java.util.UUID;

import blue.mote.DeviceManager.OnConnectedCallback;
import blue.mote.DeviceManager.OnNotConnectedCallback;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

public class BluemoteService extends Service {

	static final int REQUEST_ENABLE_BT = 1;
	private final IBinder binder = new MyBinder();
	private MyListener listener = new MyListener() {
		public void newDevice(BluetoothDevice device) {}	
		public void clearDevices() {}
		public void connectedToDevice() {}
		public void notConnectedToDevice() {}
	};
	private UUID uuid;
	private DeviceManager device_manager;
	
	@Override
	public void onCreate() {
		uuid = UUID.fromString(getString(R.string.bluemote_uuid));
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter);
	}
	
	public IBinder onBind(Intent arg0) { return binder; }

	class MyBinder extends Binder {
		public BluemoteService getService() { return BluemoteService.this; }
	}
	
	public interface MyListener {
		public void newDevice(BluetoothDevice device);
		public void clearDevices();
		public void connectedToDevice();
		public void notConnectedToDevice();
	}
	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	            listener.newDevice(device);
	        }
	    }
	};
	
	
	public void connectToDevice(BluetoothDevice wanted_device) {
		BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
		if (device_manager == null || wanted_device != device_manager.device) {
			if (device_manager != null) device_manager.disconnect();
			device_manager = new DeviceManager(wanted_device, uuid);
			device_manager.onConnectedCallback = new OnConnectedCallback() {
				public void call() {
					listener.connectedToDevice();
				}
			};
			device_manager.onNotConnectedCallback = new OnNotConnectedCallback() {
				public void call() {
					listener.notConnectedToDevice();
					
				}
			};
			device_manager.start();
		}
	}
	
	public void registerListener(MyListener listener) {
		this.listener = listener;
	}
	
	void discoverDevices() {
		if (!isEnabled()) enable();
		BluetoothAdapter.getDefaultAdapter().startDiscovery();
		Set<BluetoothDevice> list =
			BluetoothAdapter.getDefaultAdapter().getBondedDevices();
		for (BluetoothDevice device : list) {
			listener.newDevice(device);
		}
	}
	
	public void disconnectFromDevice() {
		if(device_manager != null)
			device_manager.disconnect();
	}
	
	
	public void sendCommandToDevice(String command) {
		device_manager.write(command);
	}
	
	private boolean isEnabled() {
		return BluetoothAdapter.getDefaultAdapter().isEnabled();
	}
	
	private void enable() {
		BluetoothAdapter.getDefaultAdapter().enable();
	}
	
	public void onDestroy(){
		disconnectFromDevice();
		BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
		unregisterReceiver(mReceiver);
	}
}
