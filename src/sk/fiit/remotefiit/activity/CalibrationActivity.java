package sk.fiit.remotefiit.activity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import sk.fiit.remotefiit.interfaces.DataStorage;
import sk.fiit.remotefiit.obj.CalibrationData;
import sk.fiit.remotefiit.obj.CalibrationData.Tilting;
import sk.fiit.remotefiit.obj.FileStorage;
import com.example.remotefiit.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.Vibrator;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class CalibrationActivity extends Activity implements SensorEventListener{
	
	private SensorManager mSensorManager;
	private Sensor accelerometer;
	private Sensor linearAcceleration;
	private Tilting tilt;
	private ImageView startButton;
	private ImageView nextButton;
	private TextView textViewTilting;
	private ImageView instructions;
	private List<Double> values = new ArrayList<Double>();;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//odstrani uzky sedi pas s nazvom aplikacie pod notifikacnou listou
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_calibration);
		//odstrani notifikacnu listu
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		textViewTilting = (TextView)findViewById(R.id.textViewTilting);
		tilt = Tilting.valueOf(getIntent().getStringExtra("tilt"));
		startButton = (ImageView)findViewById(R.id.imageViewStart);
		nextButton = (ImageView)findViewById(R.id.imageViewNext);
		instructions = (ImageView)findViewById(R.id.imageViewInstruction);
		
		nextButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i;
				switch(tilt){
				case TILT_LEFT:
					i = new Intent(getApplicationContext(),CalibrationActivity.class);
					i.putExtra("tilt", Tilting.TILT_RIGHT.toString());
					//finish();
					startActivityForResult(i, 10);
					break;
				case TILT_RIGHT:
					i = new Intent(getApplicationContext(),CalibrationActivity.class);
					i.putExtra("tilt", Tilting.TILT_FORWARDS.toString());
					//finish();
					startActivityForResult(i, 10);
					break;
				case TILT_FORWARDS:
					i = new Intent(getApplicationContext(),CalibrationActivity.class);
					i.putExtra("tilt", Tilting.TILT_BACKWARDS.toString());
					//finish();
					startActivityForResult(i, 10);
					break;
				case TILT_BACKWARDS:
					i = new Intent(getApplicationContext(),CalibrationActivity.class);
					i.putExtra("tilt", Tilting.UP.toString());
					startActivityForResult(i, 10);
					break;
				case UP:
					i = new Intent(getApplicationContext(),CalibrationActivity.class);
					i.putExtra("tilt", Tilting.DOWN.toString());
					startActivityForResult(i, 10);
					break;
				case DOWN:
					Toast.makeText(getApplicationContext(), "Calibration done", Toast.LENGTH_SHORT).show();
					setResult(10, null);
					finish();
					break;
				}
			}
		});

		
		startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startButton.setImageResource(R.drawable.repeat);
				values = new ArrayList<Double>();
				Toast.makeText(CalibrationActivity.this, "Calibration will starts in 2 seconds...", Toast.LENGTH_SHORT).show();
				new Thread() {
					public void run() {
						try {
							Thread.sleep(2000);
							final Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
						vibrator.vibrate(100);
						if (accelerometer != null && mSensorManager != null){
							mSensorManager.registerListener(CalibrationActivity.this, accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
							mSensorManager.registerListener(CalibrationActivity.this, linearAcceleration,SensorManager.SENSOR_DELAY_NORMAL);
						}
						Timer timer = new Timer();
						timer.schedule(new TimerTask() {
							@Override
							public void run() {
								mSensorManager.unregisterListener(CalibrationActivity.this);
								vibrator.vibrate(100);
								CalibrationActivity.this.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(CalibrationActivity.this, "OK", Toast.LENGTH_SHORT).show();
									}
								});
							}
						}, 2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}.start();
			}
		});

		AnimationDrawable frameAnimation;
		switch(tilt){
		case TILT_LEFT:
			textViewTilting.setText(Html.fromHtml("Please tilt your device to the <b><u>LEFT<\\u><\\b>"));
			instructions.setImageResource(R.drawable.tilt_left);
			frameAnimation = (AnimationDrawable) instructions.getDrawable();
			frameAnimation.start();
			break;
		case TILT_RIGHT:
			textViewTilting.setText(Html.fromHtml("Please tilt your device to the <b><u>RIGHT<\\u><\\b>"));
			instructions.setImageResource(R.drawable.tilt_right);
			frameAnimation = (AnimationDrawable) instructions.getDrawable();
			frameAnimation.start();
			break;
		case TILT_FORWARDS:
			textViewTilting.setText(Html.fromHtml("Please tilt your device <b><u>FORWARDS<\\u><\\b>"));
			instructions.setImageResource(R.drawable.tilt_forwards);
			frameAnimation = (AnimationDrawable) instructions.getDrawable();
			frameAnimation.start();
			break;
		case TILT_BACKWARDS:
			textViewTilting.setText(Html.fromHtml("Please tilt your device <b><u>BACKWARDS<\\u><\\b>"));
			instructions.setImageResource(R.drawable.tilt_backwards);
			frameAnimation = (AnimationDrawable) instructions.getDrawable();
			frameAnimation.start();
			break;
		case UP:
			textViewTilting.setText(Html.fromHtml("Please <b><u>move<\\u><\\b>")+" your device "+Html.fromHtml("<b><u>UP<\\u><\\b>"));
			instructions.setImageResource(R.drawable.move_up);
			frameAnimation = (AnimationDrawable) instructions.getDrawable();
			frameAnimation.start();
			break;
		case DOWN:
			textViewTilting.setText(Html.fromHtml("Please <b><u>move<\\u><\\b>")+" your device "+Html.fromHtml("<b><u>DOWN<\\u><\\b>"));
			instructions.setImageResource(R.drawable.move_down);
			frameAnimation = (AnimationDrawable) instructions.getDrawable();
			frameAnimation.start();
			nextButton.setImageResource(R.drawable.finish);
			break;
		}
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		if(mSensorManager != null){
			if(mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER)!=null)accelerometer = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
			if(mSensorManager.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION)!=null)linearAcceleration = mSensorManager.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION).get(0);
		}
		if(mSensorManager == null){
			Toast.makeText(getApplicationContext(), "Error: Sensor Manager is missing", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}else if(accelerometer==null){
			Toast.makeText(getApplicationContext(), "Error: Accelerometer is missing", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}else if(linearAcceleration==null){
			Toast.makeText(getApplicationContext(), "Error: Linear acceleration sensor is missing", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		
		switch (event.sensor.getType()) {
		case (Sensor.TYPE_ACCELEROMETER):
			switch(tilt){
			case TILT_LEFT:
				values.add(roundTwoDecimals(event.values[0]));
				break;
			case TILT_RIGHT:
				values.add(roundTwoDecimals(event.values[0]));
				break;
			case TILT_FORWARDS:
				values.add(roundTwoDecimals(event.values[1]));
				break;
			case TILT_BACKWARDS:
				values.add(roundTwoDecimals(event.values[1]));
				break;
			}
			break;	
		case (Sensor.TYPE_LINEAR_ACCELERATION):
			switch(tilt){
			case UP:
				values.add(roundTwoDecimals(event.values[2]));
				break;
			case DOWN:
				values.add(roundTwoDecimals(event.values[2]));
				break;
			}
			break;
		}
		
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
		}else if(linearAcceleration==null){
			Toast.makeText(getApplicationContext(), "Error: Linear acceleration sensor is missing", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		
	}
	
	@Override
	protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        double sum;
        
        if(values.size()<10)return;	//nepodarilo sa zozbierat aspon 10 hodnot na kalibraciu
        
        switch(tilt){
		case TILT_LEFT:
			Collections.sort(values); 
	        sum = 0;
	        for (int i = 0; i < 10; i++) {
				sum = sum + values.get(values.size()-1-i);
			}
			CalibrationData.setTiltLeft(roundTwoDecimals(sum/10));
			CalibrationData.setTiltLeftCount(10);
	        Toast.makeText(getApplicationContext(), CalibrationData.getTiltLeft()+" "+CalibrationData.getTiltLeftCount(), Toast.LENGTH_SHORT).show();
			break;
		case TILT_RIGHT:
			Collections.sort(values); 
	        sum = 0;
	        for (int i = 0; i < 10; i++) {
				sum = sum + values.get(i);
			}			
			CalibrationData.setTiltRight(roundTwoDecimals(sum/10));
			CalibrationData.setTiltRightCount(10);
	        Toast.makeText(getApplicationContext(), CalibrationData.getTiltRight()+" "+CalibrationData.getTiltRightCount(), Toast.LENGTH_SHORT).show();
			break;
		case TILT_FORWARDS:
			Collections.sort(values); 
	        sum = 0;
	        for (int i = 0; i < 10; i++) {
				sum = sum + values.get(i);
			}
			CalibrationData.setTiltForwards(roundTwoDecimals(sum/10));
			CalibrationData.setTiltForwardsCount(10);
	        Toast.makeText(getApplicationContext(), CalibrationData.getTiltForwards()+" "+CalibrationData.getTiltForwardsCount(), Toast.LENGTH_SHORT).show();
			break;
		case TILT_BACKWARDS:
			Collections.sort(values); 
	        sum = 0;
	        for (int i = 0; i < 10; i++) {
				sum = sum + values.get(values.size()-1-i);
			}
			CalibrationData.setTiltBackwards(roundTwoDecimals(sum/10));
			CalibrationData.setTiltBackwardsCount(10);
	        Toast.makeText(getApplicationContext(), CalibrationData.getTiltBackwards()+" "+CalibrationData.getTiltBackwardsCount(), Toast.LENGTH_SHORT).show();
			break;
		case UP:
			CalibrationData.setVeticalMovementUp(roundTwoDecimals(Collections.max(values)));
	        Toast.makeText(getApplicationContext(), String.valueOf(CalibrationData.getVeticalMovementUp()), Toast.LENGTH_SHORT).show();
			break;
		case DOWN:
			CalibrationData.setVeticalMovementDown(roundTwoDecimals(Collections.min(values)));
	        Toast.makeText(getApplicationContext(), String.valueOf(CalibrationData.getVeticalMovementDown()), Toast.LENGTH_SHORT).show();
			break;
		}
		DataStorage fs = new FileStorage();
		fs.storeData(CalibrationData.getTiltForwards(), CalibrationData.getTiltBackwards(), CalibrationData.getTiltLeft(), CalibrationData.getTiltRight(), 
				CalibrationData.getTiltForwardsCount(), CalibrationData.getTiltBackwardsCount(), CalibrationData.getTiltLeftCount(), CalibrationData.getTiltRightCount(),CalibrationData.getVeticalMovementUp(),CalibrationData.getVeticalMovementDown());
		
     }
	
	private double roundTwoDecimals(double d){
	    return Double.valueOf(String.format("%.2f", d).replace(",", "."));
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {           
	        if (resultCode == 10) {
				setResult(10, null);
	        	finish();
	        }
	}
}
