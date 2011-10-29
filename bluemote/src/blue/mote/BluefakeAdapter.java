package blue.mote;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;

public class BluefakeAdapter {

	static BluefakeAdapter getDefaultAdapter() {
		return new BluefakeAdapter();
	}
	
	public boolean cancelDiscovery() {
		return true;
	}

	public boolean disable() {
		return true;
	}

	public boolean enable() {
		return true;
	}

	public boolean equals(Object o) {
		return super.equals(o);
	}

	public String getAddress() {
		return "ca:fe:ba:be";
	}

	public Set<BluefakeDevice> getBondedDevices() {
		Set<BluefakeDevice> set = new HashSet<BluefakeDevice>();
		set.add(new BluefakeDevice());
		return set;
	}

	public String getName() {
		return "/dev/fake";
	}

	public BluetoothDevice getRemoteDevice(String address) {
		return null;
	}

	public int getScanMode() {
		return 0;
	}

	public int getState() {
		return 0;
	}

	public int hashCode() {
		return 0;
	}

	public boolean isDiscovering() {
		return false;
	}

	public boolean isEnabled() {
		return true;
	}

	public BluetoothServerSocket listenUsingRfcommWithServiceRecord(
			String name, UUID uuid) throws IOException {
		return null;
	}

	public boolean setName(String name) {
		return false;
	}

	public boolean startDiscovery() {
		return false;
	}

	public String toString() {
		return "What u r looking @?";
	}
	
}
