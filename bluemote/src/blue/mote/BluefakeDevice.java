package blue.mote;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothSocket;
import android.os.Parcel;

public class BluefakeDevice {

	public BluetoothSocket createRfcommSocketToServiceRecord(UUID uuid)
			throws IOException {
		return null;
	}

	public int describeContents() {
		return 0;
	}

	public boolean equals(Object o) {
		return super.equals(o);
	}

	public String getAddress() {
		return "ca:fe:ba:be";
	}

	public BluetoothClass getBluetoothClass() {
		return null;
	}

	public int getBondState() {
		return 0;
	}

	public String getName() {
		return "fakebt";
	}

	public int hashCode() {
		return 0;
	}

	public String toString() {
		return "u r l @";
	}

	public void writeToParcel(Parcel out, int flags) {
	}
}
