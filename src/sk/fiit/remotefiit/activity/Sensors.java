package sk.fiit.remotefiit.activity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import com.example.remotefiit.R;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.*;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Sensors extends Activity implements SensorEventListener {

	private SensorManager mSensorManager;
	private Sensor gyroscope;
	private Sensor accelerometer;
	private Sensor proximity;
	private String outProximity = "0.0";
	private String[] outAccelerometer = new String[3];
	private String[] outGyroscope = new String[3];
	private TextView proxi;
	private TextView gyro;
	private TextView acce;
	private int pocet = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		accelerometer = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
		gyroscope = mSensorManager.getSensorList(Sensor.TYPE_GYROSCOPE).get(0);
		proximity = mSensorManager.getSensorList(Sensor.TYPE_PROXIMITY).get(0);
		mSensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, proximity,SensorManager.SENSOR_DELAY_NORMAL);

		proxi = (TextView)findViewById(R.id.editText1);
		gyro = (TextView)findViewById(R.id.editText2);
		acce = (TextView)findViewById(R.id.editText3);
		
		Button b = (Button) findViewById(R.id.buttonExit);
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				System.exit(RESULT_OK);
			}
		});
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
			switch (event.sensor.getType()) {
			case (Sensor.TYPE_GYROSCOPE):
				Log.d("VR", "-------------GYRO-------------");
				outGyroscope[0] = String.format("%.2f%n",event.values[0]);
				outGyroscope[1] = String.format("%.2f%n",event.values[1]);
				outGyroscope[2] = String.format("%.2f%n",event.values[2]);
//				Log.d("VR", outGyroscope[0] = String.format("%.2f%n",event.values[0]));
//				Log.d("VR", outGyroscope[0] = String.format("%.2f%n",event.values[1]));
//				Log.d("VR", outGyroscope[0] = String.format("%.2f%n",event.values[2]));
				break;
			case (Sensor.TYPE_ACCELEROMETER):
				Log.d("VR", "-----------ACCELEROMETER-----------");
				outAccelerometer[0] = String.format("%.2f%n",event.values[0]);
				outAccelerometer[1] = String.format("%.2f%n",event.values[1]);
				outAccelerometer[2] = String.format("%.2f%n",event.values[2]);
//				Log.d("VR", outAccelerometer[0]);
//				Log.d("VR", outAccelerometer[1]);
//				Log.d("VR", outAccelerometer[2]);
				break;
			case (Sensor.TYPE_PROXIMITY):
				Log.d("VR", "-----------PROXIMITY-----------");
				outProximity = String.valueOf(event.values[0]);
//				Log.d("VR", outProximity);
				break;
			}
			vypis();
	}

	void vypis(){
		Log.d("VT", String.valueOf(pocet));
		pocet++;
		acce.setText("Accelerometer:\n"+outAccelerometer[0]+outAccelerometer[1]+outAccelerometer[2]);
		gyro.setText("Gyroscope:\n"+outGyroscope[0]+outGyroscope[1]+outGyroscope[2]);
		proxi.setText("Proximity: "+outProximity);
	}
}

