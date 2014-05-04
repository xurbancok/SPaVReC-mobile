package sk.fiit.remotefiit.obj;

public class CalibrationData {
	private static final CalibrationData instance = new CalibrationData();
	private static double tiltForwards, tiltBackwards, tiltLeft, tiltRight;
	private static int tiltForwardsCount, tiltBackwardsCount, tiltLeftCount, tiltRightCount;
	private static FileStorage fs = new FileStorage();

	private static double  veticalMovementUp;
	private static double  veticalMovementDown;
	
	private static double noiseX, noiseY;
	
	public enum Tilting{
		TILT_LEFT, TILT_RIGHT, TILT_FORWARDS, TILT_BACKWARDS, UP, DOWN;
	}

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

	public static int getTiltRightCount() {
		return tiltRightCount;
	}

	public static void setTiltRightCount(int tiltRightCount) {
		CalibrationData.tiltRightCount = tiltRightCount;
	}

	public static int getTiltForwardsCount() {
		return tiltForwardsCount;
	}

	public static void setTiltForwardsCount(int tiltForwardsCount) {
		CalibrationData.tiltForwardsCount = tiltForwardsCount;
	}

	public static int getTiltBackwardsCount() {
		return tiltBackwardsCount;
	}

	public static void setTiltBackwardsCount(int tiltBackwardsCount) {
		CalibrationData.tiltBackwardsCount = tiltBackwardsCount;
	}

	public static int getTiltLeftCount() {
		return tiltLeftCount;
	}

	public static void setTiltLeftCount(int tiltLeftCount) {
		CalibrationData.tiltLeftCount = tiltLeftCount;
	}

	public static double getNoiseX() {
		return noiseX;
	}

	public static void setNoiseX(double noiseX) {
		CalibrationData.noiseX = noiseX;
	}

	public static double getNoiseY() {
		return noiseY;
	}

	public static void setNoiseY(double noiseY) {
		CalibrationData.noiseY = noiseY;
	}

	public static double getVeticalMovementUp() {
		return veticalMovementUp;
	}

	public static void setVeticalMovementUp(double veticalMovementUp) {
		CalibrationData.veticalMovementUp = veticalMovementUp;
	}

	public static double getVeticalMovementDown() {
		return veticalMovementDown;
	}

	public static void setVeticalMovementDown(double veticalMovementDown) {
		CalibrationData.veticalMovementDown = veticalMovementDown;
	}
}
