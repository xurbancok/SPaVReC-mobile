package sk.fiit.remotefiit.obj;

public class PositionData {

	private double accelerometerX;
	private double accelerometerY;
	private double accelerometerZ;
	
	private double gyroscopeX;
	private double gyroscopeY;
	private double gyroscopeZ;

	private double orientationX;
	private double orientationY;
	private double orientationZ;

	private double magneticFieldX;
	private double magneticFieldY;
	private double magneticFieldZ;
	
	private double linearAccelerationX;
	private double linearAccelerationY;
	private double linearAccelerationZ;
	
	private double proximity;

	public void setProximity(double proximity) {
		this.proximity = roundTwoDecimals(proximity);
	}

	public double getProximity() {
		return proximity;
	}

	public void setGyroscopeZ(double gyroscopeZ) {
		this.gyroscopeZ = roundTwoDecimals(gyroscopeZ);
	}

	public double getGyroscopeZ() {
		return gyroscopeZ;
	}

	public void setGyroscopeY(double gyroscopeY) {
		this.gyroscopeY = roundTwoDecimals(gyroscopeY);
	}

	public double getGyroscopeY() {
		return gyroscopeY;
	}

	public void setGyroscopeX(double gyroscopeX) {
		this.gyroscopeX = roundTwoDecimals(gyroscopeX);
	}

	public double getGyroscopeX() {
		return gyroscopeX;
	}

	public void setAccelerometerZ(double accelerometerZ) {
		this.accelerometerZ = roundTwoDecimals(accelerometerZ);
	}

	public double getAccelerometerZ() {
		return accelerometerZ;
	}

	public void setAccelerometerY(double accelerometerY) {
		this.accelerometerY = roundTwoDecimals(accelerometerY);
	}

	public double getAccelerometerY() {
		return accelerometerY;
	}

	public void setAccelerometerX(double accelerometerX) {
		this.accelerometerX = roundTwoDecimals(accelerometerX);
	}

	public double getAccelerometerX() {
		return accelerometerX;
	}

	public void setOrientationX(double orientationX) {
		this.orientationX = roundTwoDecimals(orientationX);
	}

	public double getOrientationX() {
		return orientationX;
	}

	public void setOrientationY(double orientationY) {
		this.orientationY = roundTwoDecimals(orientationY);
	}

	public double getOrientationY() {
		return orientationY;
	}

	public void setOrientationZ(double orientationZ) {
		this.orientationZ = roundTwoDecimals(orientationZ);
	}

	public double getOrientationZ() {
		return orientationZ;
	}

	public void setMagneticFieldX(double magneticFieldX) {
		this.magneticFieldX = roundTwoDecimals(magneticFieldX);
	}

	public double getMagneticFieldX() {
		return magneticFieldX;
	}

	public void setMagneticFieldY(double magneticFieldY) {
		this.magneticFieldY = roundTwoDecimals(magneticFieldY);
	}

	public double getMagneticFieldY() {
		return magneticFieldY;
	}

	public void setMagneticFieldZ(double magneticFieldZ) {
		this.magneticFieldZ = roundTwoDecimals(magneticFieldZ);
	}

	public double getMagneticFieldZ() {
		return magneticFieldZ;
	}
	
	private double roundTwoDecimals(double d){
	    return Double.valueOf(String.format("%.2f", d).replace(",", "."));
	}

	public double getLinearAccelerationX() {
		return linearAccelerationX;
	}

	public void setLinearAccelerationX(double linearAccelerationX) {
		this.linearAccelerationX = roundTwoDecimals(linearAccelerationX);
	}

	public double getLinearAccelerationY() {
		return linearAccelerationY;
	}

	public void setLinearAccelerationY(double linearAccelerationY) {
		this.linearAccelerationY = roundTwoDecimals(linearAccelerationY);
	}

	public double getLinearAccelerationZ() {
		return linearAccelerationZ;
	}

	public void setLinearAccelerationZ(double linearAccelerationZ) {
		this.linearAccelerationZ = roundTwoDecimals(linearAccelerationZ);
	}
}
