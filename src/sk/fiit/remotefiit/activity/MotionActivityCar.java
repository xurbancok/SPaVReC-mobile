package sk.fiit.remotefiit.activity;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

import sk.fiit.remotefiit.emun.Movement;
import sk.fiit.remotefiit.obj.PositionData;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.remotefiit.R;

public class MotionActivityCar extends MotionActivity{
	
	Handler timerHandler = new Handler();
	Runnable timerRunnable;
	final private int sendingTime = 100;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findViewById(R.id.imageViewButtonLeft).setVisibility(View.GONE);
		findViewById(R.id.imageViewButtonRight).setVisibility(View.GONE);
		
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
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		//ovladacia packa sa pri prechode do vodorovnej polohy nevycentrovala v dosledku spodneho odsadenia 
		//celeho joysticka, preto bolo v tomto pripade nutne zrusit odsadenie
		RelativeLayout.LayoutParams joystickStredLayoutParam = (RelativeLayout.LayoutParams) joystickZaklad.getLayoutParams();
		joystickStredLayoutParam.bottomMargin=0;
		joystickZaklad.setLayoutParams(joystickStredLayoutParam);
		
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
	protected void dataProcessing(PositionData positionData) throws IOException {
		ArrayList<Movement> output = dataToMovementTransformation.carData(positionData);

		if (output.size() == 0)	return; // nic nerobime, neovladame VR, tak sa ani nic neodosle

		DatagramSocket clientSocket = new DatagramSocket();
		byte[] sendData = new byte[320];

		Log.d("VR", "AAA" + jsonCreator.dataToJSON(output).toString());

		sendData = jsonCreator.dataToJSON(output).toString().getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		//Log.d("Aspekty", String.valueOf(readUsage())); //meranie CPU
		clientSocket.send(sendPacket);

	}

	@Override
	protected void senzorRegistering() {
		if(accelerometer!=null && mSensorManager!=null)mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
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
		}
		
	}
	
	@Override
	protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        timerHandler.removeCallbacks(timerRunnable);
    }
}
