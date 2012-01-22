package blue.mote;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

class DeviceManager extends Thread {

	public BluetoothDevice device;
	private final UUID uuid;
	private BluetoothSocket socket;
	private OutputStream outs;
	public boolean connected = false;
	
	OnConnectedCallback onConnectedCallback;
	OnNotConnectedCallback onNotConnectedCallback;
	OnErrorCallback onErrorCallback;
	OnReadCallback onReadCallback;
	OnDisconnectedCallback onDisconnectedCallback;
	
	interface OnConnectedCallback 	 { void call(); }
	interface OnNotConnectedCallback { void call(); }
	interface OnErrorCallback 		 { void call(String msg); }
	interface OnReadCallback 		 { void call(String data); }
	interface OnDisconnectedCallback { void call(); }
	
	DeviceManager(BluetoothDevice device, UUID uuid) {
		this.device = device;
		this.uuid = uuid;
	}
	
	private BluetoothSocket createNewSocket(){
		Method m = null;
		try {
			m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] {UUID.class});
		} catch (SecurityException e1) {
			e1.printStackTrace();
			return null;
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
			return null;
		}
		try {
			return (BluetoothSocket) m.invoke(device, uuid);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private BluetoothSocket createOldSocket(){
		Method m = null;
		try {
			m = device.getClass().getMethod("createInsecureRfcommSocket", new Class[] {int.class});
		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		}
		try {
			return (BluetoothSocket) m.invoke(device, 6);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}

	}
	@Override
	public void run() {
		super.run();
		try {
			// for devices < version 2.3 workaround 
			socket = createNewSocket();
			if(socket == null)
				socket = createOldSocket();
			
			socket.connect();
			outs = socket.getOutputStream();
			connected = true;
			if (onConnectedCallback != null)
				onConnectedCallback.call();
		} catch (final IOException e) {
			if (onErrorCallback != null)
				onErrorCallback.call(e.getMessage());
				System.out.println(e.getMessage());
			if (onNotConnectedCallback != null)
				onNotConnectedCallback.call();
			disconnect();
		}
	}

	void disconnect() {
		connected = false;
		device = null;
		
		if(socket == null) return;
		try {
			socket.close();
			socket = null;
			if (onDisconnectedCallback != null)
				onDisconnectedCallback.call();
		} catch (final IOException e) {
			if (onErrorCallback != null)
				onErrorCallback.call(e.getMessage());
		}
	}

	void write(String s) {
		if (outs == null) return;
		try {
			// TODO blocks, the thread has to manage a buffer
			outs.write(s.getBytes());
		} catch (final IOException e) {
			if (onErrorCallback != null)
				onErrorCallback.call(e.getMessage());
		}
	}
}