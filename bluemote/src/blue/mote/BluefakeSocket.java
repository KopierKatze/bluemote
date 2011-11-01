package blue.mote;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothDevice;

public class BluefakeSocket {

	private byte[] buf = "foo bar\nbaz".getBytes();
	private OutputStream outs = new ByteArrayOutputStream();
	private InputStream ins = new ByteArrayInputStream(buf);
	
	public void close() throws IOException {
	}

	public void connect() throws IOException {
	}

	public InputStream getInputStream() throws IOException {
		return ins;
	}

	public OutputStream getOutputStream() throws IOException {
		return outs;
	}

	public BluetoothDevice getRemoteDevice() {
		return null;
	}
}
