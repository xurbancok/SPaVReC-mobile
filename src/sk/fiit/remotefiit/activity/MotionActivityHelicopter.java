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
	private Float[] gravity = {0f,0f,0f};
	private Float[] linear_acceleration = {0f,0f,0f};
	private long time = 0;
	private int timeDelay = 500; //500milisekund
	private float windowFilter = 3.0f;
	
	
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
			
			//high-pass filter pre akcelerometer data. vysledok je cca rovnaky ako
			//pri senzore linearneho zrychlenia
//		    final float alpha = 0.8f;
//		    gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
//	        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
//	        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
//
//	        linear_acceleration[0] = event.values[0] - gravity[0];
//	        linear_acceleration[1] = event.values[1] - gravity[1];
//	        linear_acceleration[2] = event.values[2] - gravity[2];
//	        Log.d("SENZOR","acc0 " +event.values[0]+" "+event.values[1]+" "+event.values[2]);
//	        Log.d("SENZOR","acc1 " +linear_acceleration[0]+" "+linear_acceleration[1]+" "+linear_acceleration[2]);
			break;
/*
		case (Sensor.TYPE_LINEAR_ACCELERATION):
			positionData.setLinearAccelerationX(event.values[0]);
			positionData.setLinearAccelerationY(event.values[1]);
			positionData.setLinearAccelerationZ(event.values[2]);
			
			if(event.values[2]<-windowFilter){
				//ideme dole
				Log.d("SENZOR", "rozdiel: "+ (System.currentTimeMillis()-time));
				if((System.currentTimeMillis()-time)>timeDelay){
					positionData.setVerticalMovement((positionData.getVerticalMovement()==1)? 0 : -1);
					//Log.d("SENZOR", "ideme dole");
					time = System.currentTimeMillis();
				}
			}else if(event.values[2]>windowFilter){
				//ideme hore
				Log.d("SENZOR", "rozdiel: "+ (System.currentTimeMillis()-time));
				if(System.currentTimeMillis()-time>timeDelay){	
					positionData.setVerticalMovement((positionData.getVerticalMovement()==-1)? 0 : 1);
					//Log.d("SENZOR", "ideme hore");
					time = System.currentTimeMillis();
				}
			}
			
			break;
			*/
		}
	}

	@Override
	protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        timerHandler.removeCallbacks(timerRunnable);
    }
}
