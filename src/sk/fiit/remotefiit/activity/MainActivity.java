package sk.fiit.remotefiit.activity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.remotefiit.R;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.*;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {

	private SensorManager mSensorManager;
	private Sensor gyroscope, accelerometer, proximity, orientation, magneticField;
	private String outProximity = "0.0";
	private String[] outAccelerometer = new String[3];
	private String[] outGyroscope = new String[3];
	private String[] outOrientation = new String[3];
	private String[] outMagneticField = new String[3];
	private TextView proxi, gyro, acce, orient,magnet;
	private long pocet = 0;
	private long start;
	private int lock = 0;
	private boolean reset = false;
	private boolean extendedFunction = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		start = System.currentTimeMillis();

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		accelerometer = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER)
				.get(0);
		gyroscope = mSensorManager.getSensorList(Sensor.TYPE_GYROSCOPE).get(0);
		proximity = mSensorManager.getSensorList(Sensor.TYPE_PROXIMITY).get(0);
		orientation = mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION).get(0);
		magneticField = mSensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD).get(0);
		
		mSensorManager.registerListener(this, gyroscope,
				SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, accelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, proximity,
				SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, orientation,
				SensorManager.SENSOR_DELAY_NORMAL);
		mSensorManager.registerListener(this, magneticField,
				SensorManager.SENSOR_DELAY_NORMAL);
		
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
	
	public void exitProgram(View view){
			Log.d("VR",	"CAS: "	+ String.valueOf(System.currentTimeMillis()	- start));
			finish();
			System.exit(RESULT_OK);
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
			outGyroscope[0] = String.format("%.2f%n", event.values[0])
					.replace("\n", "").replace(",", ".");
			outGyroscope[1] = String.format("%.2f%n", event.values[1])
					.replace("\n", "").replace(",", ".");
			outGyroscope[2] = String.format("%.2f%n", event.values[2])
					.replace("\n", "").replace(",", ".");
			// Log.d("VR", outGyroscope[0] =
			// String.format("%.2f%n",event.values[0]));
			// Log.d("VR", outGyroscope[0] =
			// String.format("%.2f%n",event.values[1]));
			// Log.d("VR", outGyroscope[0] =
			// String.format("%.2f%n",event.values[2]));
			break;
		case (Sensor.TYPE_ACCELEROMETER):
			// Log.d("VR", "-----------ACCELEROMETER-----------");
			outAccelerometer[0] = String.format("%.2f%n", event.values[0])
					.replace("\n", "").replace(",", ".");
			outAccelerometer[1] = String.format("%.2f%n", event.values[1])
					.replace("\n", "").replace(",", ".");
			outAccelerometer[2] = String.format("%.2f%n", event.values[2])
					.replace("\n", "").replace(",", ".");
			// Log.d("VR", outAccelerometer[0]);
			// Log.d("VR", outAccelerometer[1]);
			// Log.d("VR", outAccelerometer[2]);
			break;
		case (Sensor.TYPE_PROXIMITY):
			// Log.d("VR", "-----------PROXIMITY-----------");
			outProximity = String.valueOf(event.values[0]).replace("\n", "");
			;
			// Log.d("VR", outProximity);
			break;
		case (Sensor.TYPE_ORIENTATION):
			// Log.d("VR", "-----------MAGNET-----------");
			outOrientation[0] = String.format("%.2f%n", event.values[0])
					.replace("\n", "").replace(",", ".");
			outOrientation[1] = String.format("%.2f%n", event.values[1])
					.replace("\n", "").replace(",", ".");
			outOrientation[2] = String.format("%.2f%n", event.values[2])
					.replace("\n", "").replace(",", ".");
			// Log.d("VR", outAccelerometer[0]);
			// Log.d("VR", outAccelerometer[1]);
			// Log.d("VR", outAccelerometer[2]);
			break;
		case (Sensor.TYPE_MAGNETIC_FIELD):
			// Log.d("VR", "-------------Magnet-------------");
			outMagneticField[0] = String.format("%.2f%n", event.values[0])
					.replace("\n", "").replace(",", ".");
			outMagneticField[1] = String.format("%.2f%n", event.values[1])
					.replace("\n", "").replace(",", ".");
			outMagneticField[2] = String.format("%.2f%n", event.values[2])
					.replace("\n", "").replace(",", ".");
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
		
		if(reset==true)reset=false;
		
		Log.d("VR", jsonMain.toString());
		// Log.d("VR", String.valueOf(pocet));
		pocet++;
		Log.d("VR",	"CAS: " + String.valueOf(System.currentTimeMillis() - start));
		acce.setText("Accelerometer:\nX: " + outAccelerometer[0] + "\nY: "
				+ outAccelerometer[1] + "\nZ: " + outAccelerometer[2]);
		gyro.setText("Gyroscope:\nX: " + outGyroscope[0] + "\nY: "
				+ outGyroscope[1] + "\nZ: " + outGyroscope[2]);
		proxi.setText("Proximity: " + outProximity);
		orient.setText("Orientation:\nX: " + outOrientation[0] + "\nY: "
				+ outOrientation[1] + "\nZ: " + outOrientation[2]);

		DatagramSocket clientSocket = new DatagramSocket();
		//InetAddress IPAddress = InetAddress.getByName("192.168.173.1");
		InetAddress IPAddress = InetAddress.getByName("147.175.180.193");
		byte[] sendData = new byte[320];
		String sentence = jsonMain.toString();
		sendData = sentence.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData,
				sendData.length, IPAddress, 9876);
		clientSocket.send(sendPacket);

	}

	private JSONObject getJSON(String[] input) throws JSONException {
		JSONObject result = new JSONObject();
		result.put("X", input[0]);
		result.put("Y", input[1]);
		result.put("Z", input[2]);
		return result;
	}
}
