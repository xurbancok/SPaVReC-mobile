package sk.fiit.remotefiit.activity;

import com.example.remotefiit.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MotionActivityPedestrian extends Activity {

	private final int radius = 120;
	private TextView xCoord;
	private TextView yCoord;
	private int centerX;
	private int centerY;
	private ImageView joystickStred;
	private ImageView joystickZaklad;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_motion_pedestrian);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		xCoord = (TextView) findViewById(R.id.textView1);
		yCoord = (TextView) findViewById(R.id.textView2);
		joystickStred = (ImageView) findViewById(R.id.imageViewStred);
		joystickZaklad = (ImageView) findViewById(R.id.imageViewZaklad);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		
		MotionActivityPedestrian.this.centerJoystick();
		
		joystickStred.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int eid = event.getAction();
				switch (eid) {
				case MotionEvent.ACTION_MOVE:
					int x = (int) event.getRawX();
					int y = (int) event.getRawY();
					RelativeLayout.LayoutParams mParams = (RelativeLayout.LayoutParams) joystickStred.getLayoutParams();

					if(isOut(x,y)){
						//=====================
						
						double a=0,b=0,c=0;
						a = Math.abs(x-centerX);
						b = Math.abs(y-centerY);
						c = Math.sqrt(a*a+b*b);
						
						double x1,y1;
						x1 = (Math.cos(Math.asin(b/c))*radius);
						y1 = (radius*b/c);
						
						if (x>centerX){
							x1=centerX+x1;
						}else{
							x1=centerX-x1;
						}
						if(y>centerY){
							y1=centerY+y1;
						}else{
							y1=centerY-y1;
						}
						
						xCoord.setText(String.valueOf(x1));
						yCoord.setText(String.valueOf(y1));
						xCoord.setText(String.valueOf((Math.abs(Math.cos(Math.asin(b/c)))*radius)));
						
						mParams.leftMargin = (int) Math.round(x1)- (joystickStred.getHeight()/2);
						mParams.topMargin = (int) Math.round(y1)- (joystickStred.getWidth()/2);
						joystickStred.setLayoutParams(mParams);
						//=====================
						
						return true;
					}

					mParams.leftMargin = x - (joystickStred.getHeight()/2);
					mParams.topMargin = y - (joystickStred.getWidth()/2);
					joystickStred.setLayoutParams(mParams);
					break;
				case MotionEvent.ACTION_UP:
					MotionActivityPedestrian.this.centerJoystick();
					break;
				default:
					break;
				}
				return true;
			}
		});

	}
	
	//nastavi sa "páèka" joysticku na stred
	public void centerJoystick(){
		RelativeLayout.LayoutParams mParams = (RelativeLayout.LayoutParams) joystickStred.getLayoutParams();
		mParams.leftMargin = joystickZaklad.getLeft()+(joystickZaklad.getHeight()/2)-(joystickStred.getHeight()/2);
		mParams.topMargin = joystickZaklad.getTop()+(joystickZaklad.getWidth()/2)-(joystickStred.getWidth()/2);
		joystickStred.setLayoutParams(mParams);
		this.centerX = joystickZaklad.getLeft()+(joystickZaklad.getHeight()/2);
		this.centerY = joystickZaklad.getTop()+(joystickZaklad.getWidth()/2);
	}
	
	private boolean isOut(int actX, int actY){
		double r;
		r = (actX-centerX)*(actX-centerX)+(actY-centerY)*(actY-centerY);
		if (Math.sqrt(r)>radius) return true;
		else return false;
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
}
