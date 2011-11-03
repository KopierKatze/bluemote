package blue.mote;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothSocket;
import blue.mote.ChooseDeviceActivity.BluetoothDeviceWrap;

class DeviceManager extends Thread {

	private final BluetoothDeviceWrap device;
	private final UUID uuid;
	
	private BluetoothSocket socket;
	private InputStream ins;
	private OutputStream outs;
	private boolean connected = false;
	
	OnConnectedCallback onConnectedCallback;
	OnErrorCallback onErrorCallback;
	OnReadCallback onReadCallback;
	OnDisconnectedCallback onDisconnectedCallback;
	
	interface OnConnectedCallback 	 { void call(); }
	interface OnErrorCallback 		 { void call(String msg); }
	interface OnReadCallback 		 { void call(String data); }
	interface OnDisconnectedCallback { void call(); }

	DeviceManager(BluetoothDeviceWrap device, UUID uuid) {
		this.device = device;
		this.uuid = uuid;
	}

	@Override
	public void run() {
		super.run();

		try {
			socket = device.bt.createRfcommSocketToServiceRecord(uuid);
			socket.connect();
			connected = true;
			ins = socket.getInputStream();
			outs = socket.getOutputStream();
			if (onConnectedCallback != null)
				onConnectedCallback.call();
			while (connected) {
				final byte[] buffer = new byte[1024];
				ins.read(buffer);
				if (onReadCallback != null)
					onReadCallback.call(new String(buffer));
			}
		} catch (final IOException e) {
			if (onErrorCallback != null)
				onErrorCallback.call(e.getMessage());
			disconnect();
		}

	}

	void disconnect() {
		try {
			connected = false;
			socket.close();
			if (onDisconnectedCallback != null)
				onDisconnectedCallback.call();
		} catch (final IOException e) {
			if (onErrorCallback != null)
				onErrorCallback.call(e.getMessage());
		}
	}

	void write(String s) {
		try {
			// TODO blocks, the thread has to manage a buffer
			outs.write(s.getBytes());
		} catch (final IOException e) {
			if (onErrorCallback != null)
				onErrorCallback.call(e.getMessage());
		}
	}
}