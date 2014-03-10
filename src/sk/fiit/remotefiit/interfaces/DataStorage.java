package sk.fiit.remotefiit.interfaces;

public interface DataStorage {
	void readData();
	void storeData(double tiltForwards, double tiltBackwards, double tiltLeft, double tiltRight);
}
