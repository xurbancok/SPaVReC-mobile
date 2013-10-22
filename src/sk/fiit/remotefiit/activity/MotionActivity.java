package sk.fiit.remotefiit.activity;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.json.JSONException;

import sk.fiit.remotefiit.obj.DataToMovementTransformation;
import sk.fiit.remotefiit.obj.JSONCreator;
import sk.fiit.remotefiit.obj.PositionData;

import com.example.remotefiit.R;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public abstract class MotionActivity extends Activity implements SensorEventListener{

	protected final int spaceBetweenButtons = 20;	//medzera medzi tlacidlami
	protected final int radius = 120;	//polomer pohybu gulisky joysticka, 1/4 priemeru gulicky 
	protected TextView xCoord;
	protected TextView yCoord;
	protected TextView zCoord;
	protected int centerX;
	protected int centerY;
	protected ImageView joystickStred;
	protected ImageView joystickZaklad;
	protected ImageView buttonLeft;
	protected ImageView buttonRight;	

	//============ z povodneho prototypu zo zakladnej triedy =======
	protected SensorManager mSensorManager;
	protected Sensor gyroscope, accelerometer, proximity, orientation, magneticField, linearAcceleration;
	protected TextView proxi, gyro, acce, orient;
	protected PositionData positionData = new PositionData();
	protected DataToMovementTransformation dataToMovementTransformation = new DataToMovementTransformation();
	protected JSONCreator jsonCreator = new JSONCreator();
	protected int lock = 0;
	protected boolean reset = false;
	protected boolean extendedFunction = false;
	protected InetAddress IPAddress = null;
	protected int port;
	//==============================================================
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_motion);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
		
		
		xCoord = (TextView) findViewById(R.id.textView1);
		yCoord = (TextView) findViewById(R.id.textView2);
		zCoord = (TextView) findViewById(R.id.textView3);		
		joystickStred = (ImageView) findViewById(R.id.imageViewStred);
		joystickZaklad = (ImageView) findViewById(R.id.imageViewZaklad);
		buttonLeft = (ImageView) findViewById(R.id.imageViewButtonLeft);
		buttonRight = (ImageView) findViewById(R.id.imageViewButtonRight);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		accelerometer = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
		gyroscope = mSensorManager.getSensorList(Sensor.TYPE_GYROSCOPE).get(0);
		proximity = mSensorManager.getSensorList(Sensor.TYPE_PROXIMITY).get(0);
		orientation = mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION).get(0);
		magneticField = mSensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD).get(0);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		MotionActivity.this.centerJoystick();
		MotionActivity.this.setButtons();
		
		joystickStred.setOnTouchListener(new OnTouchListener() {
			double a=0,b=0,c=0,x1,y1;
			int touchX,touchY, eventId;
			RelativeLayout.LayoutParams joystickStredLayoutParams;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				eventId = event.getAction();
				switch (eventId) {
				case MotionEvent.ACTION_MOVE:
					touchX = (int) event.getRawX();
					touchY = (int) event.getRawY();
					joystickStredLayoutParams = (RelativeLayout.LayoutParams) joystickStred.getLayoutParams();

					if(isOut(touchX,touchY)){				//ak sme presli prstom mimo plochy joysticka
						a = Math.abs(touchX-centerX);
						b = Math.abs(touchY-centerY);
						c = Math.sqrt(a*a+b*b);

						x1 = (Math.cos(Math.asin(b/c))*radius);
						y1 = (radius*b/c);
						
						x1 = (touchX>centerX ? (centerX+x1) : (centerX-x1));
						y1 = (touchY>centerY ? (centerY+y1) : (centerY-y1));

						joystickStredLayoutParams.leftMargin = (int) Math.round(x1)- (joystickStred.getHeight()/2);
						joystickStredLayoutParams.topMargin = (int) Math.round(y1)- (joystickStred.getWidth()/2);
						joystickStred.setLayoutParams(joystickStredLayoutParams);
						//=====================
						return true;
					}
					joystickStredLayoutParams.leftMargin = touchX - (joystickStred.getHeight()/2);
					joystickStredLayoutParams.topMargin = touchY - (joystickStred.getWidth()/2);
					joystickStred.setLayoutParams(joystickStredLayoutParams);
					break;
				case MotionEvent.ACTION_UP:
					MotionActivity.this.centerJoystick();
					break;
				default:
					break;
				}
				return true;
			}
		});
		
		buttonLeft.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN){
					buttonLeft.setImageResource(R.drawable.button_left_down);
					Toast.makeText(getApplicationContext(), "lavy gombik", Toast.LENGTH_SHORT).show();
				}
				else if (event.getAction() == MotionEvent.ACTION_UP){
					buttonLeft.setImageResource(R.drawable.button_left);
				}
				return true;
			}
		});
		
		buttonRight.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN){
					buttonRight.setImageResource(R.drawable.button_right_down);
					Toast.makeText(getApplicationContext(), "pravy gombik", Toast.LENGTH_SHORT).show();
				}
				else if (event.getAction() == MotionEvent.ACTION_UP){
					buttonRight.setImageResource(R.drawable.button_right);		
				}
				return true;
			}
		});
		
	}
	
	//nastavi sa "p��ka" joysticku na stred
	public void centerJoystick(){
		RelativeLayout.LayoutParams joystickStredLayoutParam = (RelativeLayout.LayoutParams) joystickStred.getLayoutParams();
		joystickStredLayoutParam.leftMargin = joystickZaklad.getLeft()+(joystickZaklad.getHeight()/2)-(joystickStred.getHeight()/2);
		joystickStredLayoutParam.topMargin = joystickZaklad.getTop()+(joystickZaklad.getWidth()/2)-(joystickStred.getWidth()/2);
		joystickStred.setLayoutParams(joystickStredLayoutParam);
		this.centerX = joystickZaklad.getLeft()+(joystickZaklad.getHeight()/2);
		this.centerY = joystickZaklad.getTop()+(joystickZaklad.getWidth()/2);
	}
	
	private boolean isOut(int actX, int actY){
		double r;
		r = (actX-centerX)*(actX-centerX)+(actY-centerY)*(actY-centerY);
		if (Math.sqrt(r)>radius) return true;
		else return false;
	}
	
	private void setButtons(){
		RelativeLayout.LayoutParams buttonLeftParams = (RelativeLayout.LayoutParams) buttonLeft.getLayoutParams();
		buttonLeftParams.width = (joystickZaklad.getWidth()/2-spaceBetweenButtons);
		buttonLeft.setLayoutParams(buttonLeftParams);
		
		RelativeLayout.LayoutParams buttonRightParams = (RelativeLayout.LayoutParams) buttonRight.getLayoutParams();
		buttonRightParams.width = (joystickZaklad.getWidth()/2-spaceBetweenButtons);
		buttonRight.setLayoutParams(buttonRightParams);
	}
	
	/*
	 * @Override public boolean onTouchEvent(MotionEvent event) { // MotionEvent
	 * object holds X-Y values if(event.getAction() == MotionEvent.ACTION_DOWN)
	 * { xCoord.setText(String.valueOf((int) event.getX()));
	 * yCoord.setText(String.valueOf((int) event.getY()));
	 * //yCoord.setPadding((int) event.getX(), (int) event.getY(),0,0 );
	 * //j.setPadding((int) event.getX(), (int) event.getY(),0 ,0 ); }else
	 * if(event.getAction() == MotionEvent.ACTION_MOVE){
	 * xCoord.setText(String.valueOf((int) event.getX()));
	 * yCoord.setText(String.valueOf((int) event.getY())); }else
	 * if(event.getAction() == MotionEvent.ACTION_UP){ xCoord.setText("koniec");
	 * yCoord.setText("koniec"); }
	 * 
	 * return super.onTouchEvent(event); }
	 */
	
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		switch (event.sensor.getType()) {
		case (Sensor.TYPE_GYROSCOPE):
			positionData.setGyroscopeX(event.values[0]);
			positionData.setGyroscopeY(event.values[1]);
			positionData.setGyroscopeZ(event.values[2]);
			break;
		case (Sensor.TYPE_ACCELEROMETER):
			positionData.setAccelerometerX(event.values[0]);
			positionData.setAccelerometerY(event.values[1]);
			positionData.setAccelerometerZ(event.values[2]);
			break;
		case (Sensor.TYPE_PROXIMITY):
			positionData.setProximity(event.values[0]);
			break;
		case (Sensor.TYPE_ORIENTATION):
			positionData.setOrientationX(event.values[0]);
			positionData.setOrientationY(event.values[1]);
			positionData.setOrientationZ(event.values[2]);
			break;
		case (Sensor.TYPE_MAGNETIC_FIELD):
			positionData.setMagneticFieldX(event.values[0]);
			positionData.setMagneticFieldY(event.values[1]);
			positionData.setMagneticFieldZ(event.values[2]);
			break;
		case (Sensor.TYPE_LINEAR_ACCELERATION):
			positionData.setLinearAccelerationX(event.values[0]);
			positionData.setLinearAccelerationY(event.values[1]);
			positionData.setLinearAccelerationZ(event.values[2]);
			break;
		}
		dataProcessing(positionData);
	}
	
	protected abstract void dataProcessing(PositionData positionData);
	protected abstract void senzorRegistering();	
}
