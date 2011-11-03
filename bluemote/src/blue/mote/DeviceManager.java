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
	
	private OnConnectedCallback onConnectedCallback;
	private OnErrorCallback onErrorCallback;
	private OnReadCallback onReadCallback;
	private OnDisconnectedCallback onDisconnectedCallback;
	
	interface OnConnectedCallback 	 { void call(); }
	interface OnErrorCallback 		 { void call(String msg); }
	interface OnReadCallback 		 { void call(String data); }
	interface OnDisconnectedCallback { void call(); }

	public DeviceManager(BluetoothDeviceWrap device, UUID uuid) {
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
			if (getOnConnectedCallback() != null)
				getOnConnectedCallback().call();
			while (connected) {
				final byte[] buffer = new byte[1024];
				ins.read(buffer);
				if (getOnReadCallback() != null)
					getOnReadCallback().call(new String(buffer));
			}
		} catch (final IOException e) {
			if (getOnErrorCallback() != null)
				getOnErrorCallback().call(e.getMessage());
			disconnect();
		}

	}

	public void disconnect() {
		try {
			connected = false;
			socket.close();
			if (getOnDisconnectedCallback() != null)
				getOnDisconnectedCallback().call();
		} catch (final IOException e) {
			if (getOnErrorCallback() != null)
				getOnErrorCallback().call(e.getMessage());
		}
	}

	public void write(String s) {
		try {
			// TODO blocks, the thread has to manage a buffer
			outs.write(s.getBytes());
		} catch (final IOException e) {
			if (getOnErrorCallback() != null)
				getOnErrorCallback().call(e.getMessage());
		}
	}

	void setOnConnectedCallback(OnConnectedCallback onConnectedCallback) {
		this.onConnectedCallback = onConnectedCallback;
	}

	OnConnectedCallback getOnConnectedCallback() {
		return onConnectedCallback;
	}

	void setOnErrorCallback(OnErrorCallback onErrorCallback) {
		this.onErrorCallback = onErrorCallback;
	}

	OnErrorCallback getOnErrorCallback() {
		return onErrorCallback;
	}

	void setOnReadCallback(OnReadCallback onReadCallback) {
		this.onReadCallback = onReadCallback;
	}

	OnReadCallback getOnReadCallback() {
		return onReadCallback;
	}

	void setOnDisconnectedCallback(OnDisconnectedCallback onDisconnectedCallback) {
		this.onDisconnectedCallback = onDisconnectedCallback;
	}

	OnDisconnectedCallback getOnDisconnectedCallback() {
		return onDisconnectedCallback;
	}
}