package sk.fiit.remotefiit.activity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import sk.fiit.remotefiit.emun.Movement;
import sk.fiit.remotefiit.obj.PositionData;

public class MotionActivityHelicopter extends MotionActivity{

	Handler timerHandler = new Handler();
	Runnable timerRunnable;
	final private int sendingTime = 100;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		senzorRegistering();
		timerRunnable = new Runnable() {
			@Override
			public void run() {
				try {
					dataProcessing(positionData);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				timerHandler.postDelayed(this, sendingTime);
				}
			};
	}
	
	@Override
	protected void dataProcessing(PositionData positionData) throws IOException{
		ArrayList<Movement> output = dataToMovementTransformation.helicopterData(positionData);
		
		if (output.size()==0)return;	//nic nerobime, neovladame VR, tak sa ani nic neodosle
		
		DatagramSocket clientSocket = new DatagramSocket();
		byte[] sendData = new byte[320];

		Log.d("VR", "AAA"+jsonCreator.dataToJSON(output).toString());
		
		sendData = jsonCreator.dataToJSON(output).toString().getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length, IPAddress, port);
		clientSocket.send(sendPacket);
		
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
		timerHandler.postDelayed(timerRunnable, 0);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case (Sensor.TYPE_ACCELEROMETER):
			positionData.setAccelerometerX(event.values[0]);
			positionData.setAccelerometerY(event.values[1]);
			positionData.setAccelerometerZ(event.values[2]);
			break;
		case (Sensor.TYPE_LINEAR_ACCELERATION):
			positionData.setLinearAccelerationX(event.values[0]);
			positionData.setLinearAccelerationY(event.values[1]);
			positionData.setLinearAccelerationZ(event.values[2]);
			break;
		}
		
	}

	@Override
	protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        timerHandler.removeCallbacks(timerRunnable);
    }
}
