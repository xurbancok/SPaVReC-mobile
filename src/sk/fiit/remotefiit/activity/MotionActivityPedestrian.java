package sk.fiit.remotefiit.activity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

import sk.fiit.remotefiit.emun.Movement;
import sk.fiit.remotefiit.interfaces.DataStorage;
import sk.fiit.remotefiit.obj.CalibrationData;
import sk.fiit.remotefiit.obj.FileStorage;
import sk.fiit.remotefiit.obj.PositionData;
import sk.fiit.remotefiit.obj.SystemUsage;

import com.example.remotefiit.R;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MotionActivityPedestrian extends MotionActivity{
	
	Handler timerHandler = new Handler();
	Runnable timerRunnable;
	final private int sendingTime = 100;
	private boolean isStill = true;
	private final double stillWindow = (3*Math.PI)/180;	//15 stupnov je trashold, kedy predpokladame, ze zariadenie je v pokoji
														//senzory vracaju v radianoch data
	//List<Long> cas = new ArrayList<Long>();
	
	private double xRange;
	private double yRange;
	
	private RelativeLayout.LayoutParams pauseStredLayoutParam, pauseBtnLayoutParam;

	private int pauseX;
	private int pauseY;
	private int volnost;
	private double dielX, dielY;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		findViewById(R.id.imageViewButtonLeft).setVisibility(View.GONE);
		findViewById(R.id.imageViewButtonRight).setVisibility(View.GONE);

		pauseStredLayoutParam = (RelativeLayout.LayoutParams) pauseZaklad.getLayoutParams();
		pauseStredLayoutParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
		pauseZaklad.setLayoutParams(pauseStredLayoutParam);

		pauseBtnLayoutParam = (RelativeLayout.LayoutParams) pauseBtn.getLayoutParams();
		pauseBtnLayoutParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		pauseBtn.setLayoutParams(pauseBtnLayoutParam);
		
		
		if(mSensorManager == null){
			Toast.makeText(getApplicationContext(), "Error: Sensor Manager is missing", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}else if(accelerometer==null){
			Toast.makeText(getApplicationContext(), "Error: Accelerometer is missing", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		
		senzorRegistering();
		timerRunnable = new Runnable() {
			@Override
			public void run() {
				try {
//					if(!isStill || !positionData.isReset()){
//						isStill = true;
					dataProcessing(positionData);
//					}
//					else Log.d("SENZOR", "nic");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				timerHandler.postDelayed(this, sendingTime);
				}
			};

			pauseBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(Integer.parseInt(pauseBtn.getTag().toString())==R.drawable.pause_btn){
						pauseBtn.setImageResource(R.drawable.resume);
						pauseBtn.setTag(R.drawable.resume);
						centerPauseButton();
//				       
//						mSensorManager.unregisterListener(MotionActivityPedestrian.this);
//				        timerHandler.removeCallbacks(timerRunnable);
				        MotionActivityPedestrian.this.onPause();
						Toast.makeText(getApplicationContext(), "Data capture pause", Toast.LENGTH_SHORT).show();
					}else{
						pauseBtn.setImageResource(R.drawable.pause_btn);
						pauseBtn.setTag(R.drawable.pause_btn);
						MotionActivityPedestrian.this.onResume();
						Toast.makeText(getApplicationContext(),  "Data capture start", Toast.LENGTH_SHORT).show();
					}
				}
			});

		}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		pauseX = pauseZaklad.getLeft()+(pauseZaklad.getHeight()/2)-(pauseStred.getHeight()/2);
		pauseY = pauseStred.getTop();
		volnost = pauseZaklad.getHeight()-pauseStred.getHeight();
	}
	
	
	@Override
	protected void senzorRegistering() {
		if(accelerometer!=null && mSensorManager!=null)mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		//mSensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		
		if(mSensorManager == null){
			Toast.makeText(getApplicationContext(), "Error: Sensor Manager is missing", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}else if(accelerometer==null){
			Toast.makeText(getApplicationContext(), "Error: Accelerometer is missing", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		
		if(accelerometer!=null && mSensorManager!=null)mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		//mSensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
		timerHandler.postDelayed(timerRunnable, 0);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		//long startTime = System.currentTimeMillis();
		switch (event.sensor.getType()) {
		case (Sensor.TYPE_ACCELEROMETER):
			positionData.setAccelerometerX(event.values[0]);
			positionData.setAccelerometerY(event.values[1]);
			positionData.setAccelerometerZ(event.values[2]);

			xRange = Math.abs(CalibrationData.getTiltLeft())+Math.abs(CalibrationData.getTiltRight());
			yRange = CalibrationData.getTiltBackwards()-CalibrationData.getTiltForwards();
			dielX = volnost/xRange;
			dielY = volnost/yRange;
	
			pauseStredLayoutParam = (RelativeLayout.LayoutParams) pauseStred.getLayoutParams();
			pauseStredLayoutParam.leftMargin = (int) (pauseX + (dielX*event.values[0])+xRange);
			pauseStredLayoutParam.topMargin = (int) (pauseY - (dielY*((event.values[1])-(CalibrationData.getTiltBackwards()))));
			pauseStred.setLayoutParams(pauseStredLayoutParam);

			break;	
		case (Sensor.TYPE_GYROSCOPE):
			Log.d("SENZOR", Math.abs(event.values[0])+ Math.abs(event.values[1])+ Math.abs(event.values[2])+"");
			if(Math.abs(event.values[0])+ Math.abs(event.values[1])+ Math.abs(event.values[2])>stillWindow){
				isStill = false;
			}
			break;
		}	
		//cas.add(System.currentTimeMillis()-startTime);
		//if(cas.size()==1000){Log.d("SYSTEM", cas.toString());}
	}

	@Override
	protected void dataProcessing(PositionData positionData) throws IOException {
		ArrayList<Movement> output = dataToMovementTransformation.pedestrianData(positionData);
		
		if (output.size()==0)return;	//nic nerobime, neovladame VR, tak sa ani nic neodosle
		
		DatagramSocket clientSocket = new DatagramSocket();
		byte[] sendData = new byte[320];

		Log.d("VR", "AAA"+jsonCreator.dataToJSON(output).toString());
		sendData = jsonCreator.dataToJSON(output).toString().getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length, IPAddress, port);
		clientSocket.send(sendPacket);
	}
	
	@Override
	protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        timerHandler.removeCallbacks(timerRunnable);
        fs.storeData(CalibrationData.getTiltForwards(), CalibrationData.getTiltBackwards(), CalibrationData.getTiltLeft(), CalibrationData.getTiltRight(), 
				CalibrationData.getTiltForwardsCount(), CalibrationData.getTiltBackwardsCount(), CalibrationData.getTiltLeftCount(), CalibrationData.getTiltRightCount());
    }
}
