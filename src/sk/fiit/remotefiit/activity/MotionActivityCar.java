package sk.fiit.remotefiit.activity;

import sk.fiit.remotefiit.obj.PositionData;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;

import com.example.remotefiit.R;

public class MotionActivityCar extends MotionActivity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findViewById(R.id.imageViewButtonLeft).setVisibility(View.GONE);
		findViewById(R.id.imageViewButtonRight).setVisibility(View.GONE);
		senzorRegistering();
	}

	@Override
	protected void dataProcessing(PositionData positionData) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void senzorRegistering() {
		mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}

}
