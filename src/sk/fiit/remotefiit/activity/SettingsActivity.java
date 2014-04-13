package sk.fiit.remotefiit.activity;
import sk.fiit.remotefiit.interfaces.DataStorage;
import sk.fiit.remotefiit.obj.CalibrationData;
import sk.fiit.remotefiit.obj.FileStorage;
import com.example.remotefiit.R;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
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

public class SettingsActivity extends Activity implements SensorEventListener{
	
	private SensorManager mSensorManager;
	private Sensor accelerometer;
	private ImageView ImageViewUp, ImageViewLeft, ImageViewLeft2, ImageViewRight, ImageViewRight2, ImageViewDown;
	private SeekBar seekBarLeft, seekBarRight, seekBarUp, seekBarDown;
	
	private TextView textViewValues;
	private DataStorage fs = new FileStorage();
	private double valueLeft, valueRight, valueForwards, valueBackwards;
	
	private Button defaultButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//odstrani uzky sedi pas s nazvom aplikacie pod notifikacnou listou
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_settings);
		//odstrani notifikacnu listu
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        valueBackwards = CalibrationData.getTiltBackwards();
        valueForwards = CalibrationData.getTiltForwards();
        valueLeft =CalibrationData.getTiltLeft();
        valueRight = CalibrationData.getTiltRight();  
        		ImageViewUp = (ImageView)findViewById(R.id.imageViewUp);
		ImageViewLeft = (ImageView)findViewById(R.id.imageViewLeft);
		ImageViewLeft2 = (ImageView)findViewById(R.id.imageViewLeft2);
		ImageViewRight = (ImageView)findViewById(R.id.imageViewRight);
		ImageViewRight2 = (ImageView)findViewById(R.id.imageViewRight2);
		ImageViewDown = (ImageView)findViewById(R.id.imageViewDown);
		
		seekBarLeft = (SeekBar)findViewById(R.id.seekBarLeft);
		seekBarRight = (SeekBar)findViewById(R.id.seekBarRight);
		seekBarUp = (SeekBar)findViewById(R.id.seekBarUp);
		seekBarDown = (SeekBar)findViewById(R.id.seekBarDown);
		textViewValues = (TextView)findViewById(R.id.textViewValues);
		
		textViewValues.setText("Left: "+roundTwoDecimals(valueLeft)+"\nRight: "+roundTwoDecimals(Math.abs(valueRight))+"\nUp: "+roundTwoDecimals(Math.abs((valueForwards-5.0)))+"\nDown: "+Math.abs(roundTwoDecimals(valueBackwards-5.0)));

		defaultButton = (Button)findViewById(R.id.buttonDefault);
		defaultButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				valueBackwards = 4;
				valueForwards = -1;
				valueLeft = 3;
				valueRight = -3;
				
				seekBarLeft.setProgress(100-((int)(valueLeft*10)));
				seekBarRight.setProgress(-((int)(valueRight*10)));
				seekBarUp.setProgress(40);
				seekBarDown.setProgress(100-(((int)valueBackwards-5)*10));
				
				Toast.makeText(getApplicationContext(), "Default settings has been applied",Toast.LENGTH_SHORT).show();
			}
		});
		
		seekBarLeft.setProgress(100-((int)(CalibrationData.getTiltLeft()*10)));
		seekBarRight.setProgress(-((int)(CalibrationData.getTiltRight()*10)));
		
		seekBarUp.setProgress(-((int)((CalibrationData.getTiltForwards()-5)*10)));
		seekBarDown.setProgress(100-(((int)CalibrationData.getTiltBackwards()-5)*10));
		
		seekBarLeft.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				valueLeft = ((double)(100-seekBar.getProgress())/10.0);
				textViewValues.setText("Left: "+roundTwoDecimals(valueLeft)+"\nRight: "+roundTwoDecimals(Math.abs(valueRight))+"\nUp: "+roundTwoDecimals(Math.abs((valueForwards-5.0)))+"\nDown: "+roundTwoDecimals(valueBackwards-5.0));
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if(progress>90)seekBar.setProgress(90);
				valueLeft = ((double)(100-seekBar.getProgress())/10.0);
				textViewValues.setText("Left: "+roundTwoDecimals(valueLeft)+"\nRight: "+roundTwoDecimals(Math.abs(valueRight))+"\nUp: "+roundTwoDecimals(Math.abs((valueForwards-5.0)))+"\nDown: "+roundTwoDecimals(valueBackwards-5.0));
			}
		});		
		
		seekBarRight.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				valueRight = -(seekBar.getProgress()/10.0);
				textViewValues.setText("Left: "+roundTwoDecimals(valueLeft)+"\nRight: "+roundTwoDecimals(Math.abs(valueRight))+"\nUp: "+roundTwoDecimals(Math.abs((valueForwards-5.0)))+"\nDown: "+roundTwoDecimals(valueBackwards-5.0));
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if(progress<10)seekBar.setProgress(10);
				valueRight = -(seekBar.getProgress()/10.0);
				textViewValues.setText("Left: "+roundTwoDecimals(valueLeft)+"\nRight: "+roundTwoDecimals(Math.abs(valueRight))+"\nUp: "+roundTwoDecimals(Math.abs((valueForwards-5.0)))+"\nDown: "+roundTwoDecimals(valueBackwards-5.0));
			}
		});
		
		seekBarUp.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				valueForwards = ((-seekBar.getProgress()/10.0)+5.0);
				textViewValues.setText("Left: "+roundTwoDecimals(valueLeft)+"\nRight: "+roundTwoDecimals(Math.abs(valueRight))+"\nUp: "+roundTwoDecimals(Math.abs((valueForwards-5.0)))+"\nDown: "+roundTwoDecimals(valueBackwards-5.0));
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if(progress<5)seekBar.setProgress(5);
				valueForwards = ((-seekBar.getProgress()/10.0)+5.0);
				textViewValues.setText("Left: "+roundTwoDecimals(valueLeft)+"\nRight: "+roundTwoDecimals(Math.abs(valueRight))+"\nUp: "+roundTwoDecimals(Math.abs((valueForwards-5.0)))+"\nDown: "+roundTwoDecimals(valueBackwards-5.0));
			}
		});
		
		seekBarDown.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				valueBackwards = (((100.0-seekBar.getProgress())/10)+5.0);
				textViewValues.setText("Left: "+roundTwoDecimals(valueLeft)+"\nRight: "+roundTwoDecimals(Math.abs(valueRight))+"\nUp: "+roundTwoDecimals(Math.abs((valueForwards-5.0)))+"\nDown: "+roundTwoDecimals(valueBackwards-5.0));
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if(progress>95)seekBar.setProgress(95);
				valueBackwards = (((100.0-seekBar.getProgress())/10)+5.0);
				textViewValues.setText("Left: "+roundTwoDecimals(valueLeft)+"\nRight: "+roundTwoDecimals(Math.abs(valueRight))+"\nUp: "+roundTwoDecimals(Math.abs((valueForwards-5.0)))+"\nDown: "+roundTwoDecimals(valueBackwards-5.0));
			}
		});
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		if(mSensorManager != null){
			if(mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER)!=null)accelerometer = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
		}

		if(mSensorManager == null){
			Toast.makeText(getApplicationContext(), "Error: Sensor Manager is missing", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}else if(accelerometer==null){
			Toast.makeText(getApplicationContext(), "Error: Accelerometer is missing", Toast.LENGTH_SHORT).show();
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
			if(event.values[0]>valueLeft)ImageViewLeft.setImageResource(R.drawable.sipka_l_up);
			else ImageViewLeft.setImageResource(0);
			if(event.values[0]<valueRight)ImageViewRight.setImageResource(R.drawable.sipka_r_up);
			else ImageViewRight.setImageResource(0);
		
			if(event.values[1]<valueForwards)ImageViewUp.setImageResource(R.drawable.sipka_u_up);
			else ImageViewUp.setImageResource(0);
			if(event.values[1]>valueBackwards)ImageViewDown.setImageResource(R.drawable.sipka_d_up);
			else ImageViewDown.setImageResource(0);

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
		}
		
		if(accelerometer!=null && mSensorManager!=null)mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        fs.storeData(valueForwards, valueBackwards, valueLeft, valueRight,1,1,1,1);//stara kalibracia, podla jednej hodnoty
        CalibrationData.setTiltBackwards(valueBackwards);
        CalibrationData.setTiltForwards(valueForwards);
        CalibrationData.setTiltLeft(valueLeft);
        CalibrationData.setTiltRight(valueRight);  
        Log.d("VR", "Left: "+valueLeft+"\nRight: "+valueRight+"\nUp: "+valueForwards+"\nDown: "+valueBackwards);

    }
	
	private double roundTwoDecimals(double d){
	    return Double.valueOf(String.format("%.2f", d).replace(",", "."));
	}
	
}
