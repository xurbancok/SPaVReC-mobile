package sk.fiit.remotefiit.obj;

public class CalibrationData {
	private static final CalibrationData instance = new CalibrationData();
	private static double tiltForwards, tiltBackwards, tiltLeft, tiltRight;
	private static FileStorage fs = new FileStorage();

	// Private constructor prevents instantiation from other classes
	private CalibrationData() {
	}

	public static CalibrationData getInstance() {
		return instance;
	}

	public static double getTiltBackwards() {
		return tiltBackwards;
	}

	public static void setTiltBackwards(double tiltbackwards) {
		CalibrationData.tiltBackwards = tiltbackwards;
	}

	public static double getTiltLeft() {
		return tiltLeft;
	}

	public static void setTiltLeft(double tiltLeft) {
		CalibrationData.tiltLeft = tiltLeft;
	}

	public static double getTiltRight() {
		return tiltRight;
	}

	public static void setTiltRight(double tiltRight) {
		CalibrationData.tiltRight = tiltRight;
	}

	public static double getTiltForwards() {
		return tiltForwards;
	}

	public static void setTiltForwards(double tiltForwards) {
		CalibrationData.tiltForwards = tiltForwards;
	}
	
	public static void initializeValue(){
		fs.readData();
	}
}
