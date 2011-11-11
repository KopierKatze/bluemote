package blue.mote;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import blue.mote.ChooseDeviceActivity.BluetoothDeviceWrap;

public class BtServer extends Thread {
	UUID uuid;
	BluetoothDeviceWrap device;
	BluetoothServerSocket serversocket;
	BluetoothSocket bt_socket;
	String name = "Stationary device discovery";

	BtServer(BluetoothDeviceWrap device, UUID uuid) {
		this.device = device;
		this.uuid = uuid;
	}

	@Override
	public void run() {
		super.run();

		serversocket = openServerSocket();
		System.out.println("serversocket");
		bt_socket = waitForRequests(serversocket);
//		System.out.println("bt_socket");
//		sendTest(bt_socket);
//		System.out.println("test");
//		try {
//			serversocket.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	BluetoothServerSocket openServerSocket() {
		while (true) {
			try {
				serversocket = BluemoteActivity.bt_adapter
						.listenUsingRfcommWithServiceRecord(name, uuid);
				if(serversocket != null)
					break;
			} catch (IOException e) {
				System.out.println("ERROR---------------");
				e.printStackTrace();
			}
		}
		return serversocket;
	}

	BluetoothSocket waitForRequests(BluetoothServerSocket serversocket){
		try {
			System.out.println("start acc");
			bt_socket = serversocket.accept();
			System.out.println("acced");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bt_socket;
	}
	
	void sendTest(BluetoothSocket bt_socket){
		String test = "key F11 \n";
		byte[] buff = test.getBytes();
		OutputStream outstream = null;
		try {
			outstream = bt_socket.getOutputStream();
			outstream.write(buff);
			outstream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
