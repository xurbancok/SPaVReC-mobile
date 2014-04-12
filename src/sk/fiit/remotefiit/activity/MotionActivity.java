package sk.fiit.remotefiit.activity;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
	protected PositionData positionData = new PositionData();
	protected DataToMovementTransformation dataToMovementTransformation = new DataToMovementTransformation();
	protected JSONCreator jsonCreator = new JSONCreator();
	protected boolean reset = false;
	protected InetAddress IPAddress = null;
	protected int port;
	//==============================================================
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_motion);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
		
		
		xCoord = (TextView) findViewById(R.id.textView1);
		yCoord = (TextView) findViewById(R.id.textView2);
		zCoord = (TextView) findViewById(R.id.textView3);	
		xCoord.setText("");
		yCoord.setText("");
		zCoord.setText("");
		
		joystickStred = (ImageView) findViewById(R.id.imageViewStred);
		joystickZaklad = (ImageView) findViewById(R.id.imageViewZaklad);
		buttonLeft = (ImageView) findViewById(R.id.imageViewButtonLeft);
		buttonRight = (ImageView) findViewById(R.id.imageViewButtonRight);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		if(mSensorManager != null){
			if(mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER)!=null)accelerometer = mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
			if((mSensorManager.getSensorList(Sensor.TYPE_GYROSCOPE)!=null) && (mSensorManager.getSensorList(Sensor.TYPE_GYROSCOPE).size()>0)) gyroscope = mSensorManager.getSensorList(Sensor.TYPE_GYROSCOPE).get(0);
			if((mSensorManager.getSensorList(Sensor.TYPE_PROXIMITY)!=null) && (mSensorManager.getSensorList(Sensor.TYPE_PROXIMITY).size()>0))proximity = mSensorManager.getSensorList(Sensor.TYPE_PROXIMITY).get(0);
		    if((mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION)!=null) && (mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION).size()>0))orientation = mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION).get(0);
		    if((mSensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD)!=null) && (mSensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD).size()>0))magneticField = mSensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD).get(0);
		    if((mSensorManager.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION)!=null) && (mSensorManager.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION).size()>0))linearAcceleration = mSensorManager.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION).get(0);
		}

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		MotionActivity.this.centerJoystick();
		MotionActivity.this.setButtons();
		
		joystickStred.setOnTouchListener(new OnTouchListener() {
			double a=0,b=0,c=0,x1,y1;
			int touchX,touchY, eventId;
			int joystickCenterX = joystickZaklad.getLeft()+((int)joystickZaklad.getWidth()/2);
			int joystickCenterY = joystickZaklad.getTop()+((int)joystickZaklad.getHeight()/2);
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
						a = (touchX-centerX);
						b = (touchY-centerY);
						c = Math.sqrt(a*a+b*b);

						x1 = ((a/c)*radius)+centerX;
						y1 = ((b/c)*radius)+centerY;
//						x1 = (Math.cos(Math.asin(b/c))*radius);
//						y1 = (radius*b/c);
//						
//						x1 = (touchX>centerX ? (centerX+x1) : (centerX-x1));
//						y1 = (touchY>centerY ? (centerY+y1) : (centerY-y1));

						
						
						joystickStredLayoutParams.leftMargin = (int) Math.round(x1)- (joystickStred.getWidth()/2);
						joystickStredLayoutParams.topMargin = (int) Math.round(y1)- (joystickStred.getHeight()/2);
						joystickStred.setLayoutParams(joystickStredLayoutParams);
						//=====================
					}else{
						joystickStredLayoutParams.leftMargin = touchX - (joystickStred.getWidth()/2);
						joystickStredLayoutParams.topMargin = touchY - (joystickStred.getHeight()/2);
						joystickStred.setLayoutParams(joystickStredLayoutParams);
					}
					
					if(joystickStredLayoutParams.leftMargin+(joystickStred.getWidth()/2)>=joystickCenterX+60){
						positionData.setJoystickRight(true);
						//ak drzim prst na joysticku a beham po celej ploche, tak zostanu nastavene vsetky premenne
						//a potom pri spracovani dat urci zle pohyb, lebo je tam if...else if
						positionData.setJoystickLeft(false);
					}
					else if(joystickStredLayoutParams.leftMargin+(joystickStred.getWidth()/2)<=joystickCenterX-60){
						positionData.setJoystickLeft(true);
						positionData.setJoystickRight(false);
					}
					if(joystickStredLayoutParams.topMargin+(joystickStred.getHeight()/2)>=joystickCenterY+60){
						positionData.setJoystickDown(true);
						positionData.setJoystickUp(false);
					}
					else if(joystickStredLayoutParams.topMargin+(joystickStred.getHeight()/2)<=joystickCenterY-60){
						positionData.setJoystickUp(true);
						positionData.setJoystickDown(false);
					}
					if(joystickStredLayoutParams.topMargin+(joystickStred.getHeight()/2)<joystickCenterY+60 
							&& joystickStredLayoutParams.topMargin+(joystickStred.getHeight()/2)>joystickCenterY-60){
						positionData.setJoystickUp(false);
						positionData.setJoystickDown(false);
					}
					if(joystickStredLayoutParams.leftMargin+(joystickStred.getWidth()/2)<joystickCenterX+60
							&& joystickStredLayoutParams.leftMargin+(joystickStred.getWidth()/2)>joystickCenterX-60){
						positionData.setJoystickLeft(false);
						positionData.setJoystickRight(false);
					}
					
					break;
				case MotionEvent.ACTION_UP:
					MotionActivity.this.centerJoystick();
					positionData.resetJoystick();
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
					//Toast.makeText(getApplicationContext(), "lavy gombik", Toast.LENGTH_SHORT).show();
					positionData.setVerticalMovement(-1);
				}
				else if (event.getAction() == MotionEvent.ACTION_UP){
					buttonLeft.setImageResource(R.drawable.button_left);
					positionData.setVerticalMovement(0);
				}
				return true;
			}
		});
		
		buttonRight.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN){
					buttonRight.setImageResource(R.drawable.button_right_down);
					//Toast.makeText(getApplicationContext(), "pravy gombik", Toast.LENGTH_SHORT).show();
					positionData.setVerticalMovement(1);
				}
				else if (event.getAction() == MotionEvent.ACTION_UP){
					buttonRight.setImageResource(R.drawable.button_right);
					positionData.setVerticalMovement(0);
				}
				return true;
			}
		});
		
	}
	
	//nastavi sa "páèka" joysticku na stred
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


	abstract public void onSensorChanged(SensorEvent event);
	
	protected abstract void dataProcessing(PositionData positionData) throws Exception;
	protected abstract void senzorRegistering();	
}
