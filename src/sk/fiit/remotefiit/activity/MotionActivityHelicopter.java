package sk.fiit.remotefiit.activity;

import android.hardware.SensorManager;
import android.os.Bundle;
import sk.fiit.remotefiit.obj.PositionData;

public class MotionActivityHelicopter extends MotionActivity{

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		senzorRegistering();
	}
	
	@Override
	protected void dataProcessing(PositionData positionData) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void senzorRegistering() {
		mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, linearAcceleration, SensorManager.SENSOR_DELAY_NORMAL);
	}	
	
	@Override
	protected void onResume(){
		super.onResume();
		mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, linearAcceleration, SensorManager.SENSOR_DELAY_NORMAL);
	}


}
