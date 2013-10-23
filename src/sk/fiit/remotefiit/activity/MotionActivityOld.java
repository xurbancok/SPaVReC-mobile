package sk.fiit.remotefiit.activity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import org.json.JSONException;

import sk.fiit.remotefiit.emun.Movement;
import sk.fiit.remotefiit.obj.DataToMovementTransformation;
import sk.fiit.remotefiit.obj.JSONCreator;
import sk.fiit.remotefiit.obj.PositionData;

import com.example.remotefiit.R;
import android.app.Activity;
import android.content.Context;
import android.hardware.*;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MotionActivityOld extends Activity implements SensorEventListener{

	private SensorManager mSensorManager;
	private Sensor gyroscope, accelerometer, proximity, orientation, magneticField, linearAcceleration;
//	private String outProximity = "0.0";
//	private String[] outAccelerometer = new String[3];
//	private String[] outGyroscope = new String[3];
//	private String[] outOrientation = new String[3];
//	private String[] outMagneticField = new String[3];
	private TextView proxi, gyro, acce, orient;
	private PositionData positionData = new PositionData();
	private DataToMovementTransformation dataToMovementTransformation = new DataToMovementTransformation();
	private JSONCreator jsonCreator = new JSONCreator();
	
	private int lock = 0;
	private boolean reset = false;
	private boolean extendedFunction = false;
	private InetAddress IPAddress = null;
	private int port;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_motion_old);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		try {
			IPAddress = InetAddress.getByName(getIntent().getStringExtra("IP"));	//ziska IP
			port = Integer.valueOf(getIntent().getStringExtra("port"));
		} catch (UnknownHostException e) {
			Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
			finish();
			e.printStackTrace();	
			return;		//finish neukoncil aktivitu, resp. metoda pokracovala dalej a doslo k spusteniu senzorov
		}

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		accelerometer = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
		gyroscope = mSensorManager.getSensorList(Sensor.TYPE_GYROSCOPE).get(0);
		proximity = mSensorManager.getSensorList(Sensor.TYPE_PROXIMITY).get(0);
		orientation = mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION).get(0);
		magneticField = mSensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD).get(0);
		linearAcceleration = mSensorManager.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION).get(0);
		
		//mSensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		//mSensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL);
		//mSensorManager.registerListener(this, orientation, SensorManager.SENSOR_DELAY_NORMAL);
		//mSensorManager.registerListener(this, magneticField, SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, linearAcceleration, SensorManager.SENSOR_DELAY_NORMAL);	
		
		proxi = (TextView) findViewById(R.id.editText1);
		gyro = (TextView) findViewById(R.id.editText2);
		acce = (TextView) findViewById(R.id.editText3);
		orient = (TextView) findViewById(R.id.editText4);
		
		Button b = (Button) findViewById(R.id.buttonFunction);
		b.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
					extendedFunction = true;
				else if (event.getAction() == MotionEvent.ACTION_UP)
					extendedFunction = false;
				return false;
			}
		});
	}

	public void resetPosition(View view){
		reset = true;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case (Sensor.TYPE_GYROSCOPE):
			// Log.d("VR", "-------------GYRO-------------");
			positionData.setGyroscopeX(event.values[0]);
			positionData.setGyroscopeY(event.values[1]);
			positionData.setGyroscopeZ(event.values[2]);
			
/*	
			outGyroscope[0] = String.format("%.2f%n", event.values[0]).replace("\n", "").replace(",", ".");
			outGyroscope[1] = String.format("%.2f%n", event.values[1]).replace("\n", "").replace(",", ".");
			outGyroscope[2] = String.format("%.2f%n", event.values[2]).replace("\n", "").replace(",", ".");
			Log.d("VR", outGyroscope[0] = String.format("%.2f%n",event.values[0]));
			Log.d("VR", outGyroscope[0] = String.format("%.2f%n",event.values[1]));
			Log.d("VR", outGyroscope[0] = String.format("%.2f%n",event.values[2]));
*/
			break;
		case (Sensor.TYPE_ACCELEROMETER):
			// Log.d("VR", "-----------ACCELEROMETER-----------");
			positionData.setAccelerometerX(event.values[0]);
			positionData.setAccelerometerY(event.values[1]);
			positionData.setAccelerometerZ(event.values[2]);
/*			
			outAccelerometer[0] = String.format("%.2f%n", event.values[0]).replace("\n", "").replace(",", ".");
			outAccelerometer[1] = String.format("%.2f%n", event.values[1]).replace("\n", "").replace(",", ".");
			outAccelerometer[2] = String.format("%.2f%n", event.values[2]).replace("\n", "").replace(",", ".");
			Log.d("VR", outAccelerometer[0]);
			Log.d("VR", outAccelerometer[1]);
			Log.d("VR", outAccelerometer[2]);
*/
			break;
		case (Sensor.TYPE_PROXIMITY):
			// Log.d("VR", "-----------PROXIMITY-----------");
			positionData.setProximity(event.values[0]);
			//outProximity = String.valueOf(event.values[0]).replace("\n", "");
			// Log.d("VR", outProximity);
			break;
		case (Sensor.TYPE_ORIENTATION):
			// Log.d("VR", "-----------MAGNET-----------");
			positionData.setOrientationX(event.values[0]);
			positionData.setOrientationY(event.values[1]);
			positionData.setOrientationZ(event.values[2]);
/*			
			outOrientation[0] = String.format("%.2f%n", event.values[0]).replace("\n", "").replace(",", ".");
			outOrientation[1] = String.format("%.2f%n", event.values[1]).replace("\n", "").replace(",", ".");
			outOrientation[2] = String.format("%.2f%n", event.values[2]).replace("\n", "").replace(",", ".");
			Log.d("VR", outAccelerometer[0]);
			Log.d("VR", outAccelerometer[1]);
			Log.d("VR", outAccelerometer[2]);
*/
			break;
		case (Sensor.TYPE_MAGNETIC_FIELD):
			// Log.d("VR", "-------------Magnet-------------");
			positionData.setMagneticFieldX(event.values[0]);
			positionData.setMagneticFieldY(event.values[1]);
			positionData.setMagneticFieldZ(event.values[2]);
/*			
			outMagneticField[0] = String.format("%.2f%n", event.values[0]).replace("\n", "").replace(",", ".");
			outMagneticField[1] = String.format("%.2f%n", event.values[1]).replace("\n", "").replace(",", ".");
			outMagneticField[2] = String.format("%.2f%n", event.values[2]).replace("\n", "").replace(",", ".");
*/
			break;
		case (Sensor.TYPE_LINEAR_ACCELERATION):
			positionData.setLinearAccelerationX(event.values[0]);
			positionData.setLinearAccelerationY(event.values[1]);
			positionData.setLinearAccelerationZ(event.values[2]);
			break;
		}
		try {
			vypis();
		} catch (JSONException e) {
			Toast.makeText(getApplicationContext(), "JSONException",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (UnknownHostException e) {
			Toast.makeText(getApplicationContext(), "Nepodarilo sa odoslat",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void vypis() throws JSONException, IOException, SocketException {
		if (lock < 5) {
			lock++;
			return;
		}
		lock = 0;
		
		ArrayList<Movement> output = dataToMovementTransformation.rawData(positionData, extendedFunction, reset);
		if(reset==true)reset=false;
/*		
		// robime zo ziskanych dat JSON, tym ich pripravime na odoslanie
		JSONObject jsonAccelerometer = getJSON(outAccelerometer);
		JSONObject jsonGyroscope = getJSON(outGyroscope);
		JSONObject jsonOrientation = getJSON(outOrientation);
		JSONObject jsonMagneticField = getJSON(outMagneticField);
		
		JSONObject jsonMain = new JSONObject();
		jsonMain.put("Accelerometer", jsonAccelerometer);
		jsonMain.put("Gyroscope", jsonGyroscope);
		jsonMain.put("Orientation", jsonOrientation);
		jsonMain.put("Magnetic_field", jsonMagneticField);
		jsonMain.put("Proximity", outProximity);
		jsonMain.put("reset", reset);
		jsonMain.put("EF", extendedFunction);
		
		Log.d("VR", jsonMain.toString());
*/
		acce.setText("Accelerometer:\nX: " + positionData.getAccelerometerX() + "\nY: "
				+ positionData.getAccelerometerY() + "\nZ: " + positionData.getAccelerometerZ());
		gyro.setText("Linear acc:\nX: " + positionData.getLinearAccelerationX() + "\nY: "
				+ positionData.getLinearAccelerationY() + "\nZ: " +positionData.getLinearAccelerationZ());
		proxi.setText("Proximity: " + positionData.getProximity());
		orient.setText("Orientation:\nX: " + positionData.getOrientationX() + "\nY: "
				+ positionData.getOrientationY() + "\nZ: " + positionData.getOrientationZ());

		DatagramSocket clientSocket = new DatagramSocket();
		//InetAddress IPAddress = InetAddress.getByName("192.168.173.1");
		//InetAddress IPAddress = InetAddress.getByName("147.175.180.193");
		byte[] sendData = new byte[320];
		//String sentence = jsonMain.toString();

		Log.d("VR", "AAA"+jsonCreator.dataToJSON(output).toString());
		
		sendData = jsonCreator.dataToJSON(output).toString().getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length, IPAddress, port);
		clientSocket.send(sendPacket);

	}
	
	@Override
	protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
