package sk.fiit.remotefiit.activity;

import com.example.remotefiit.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


public class MotionActivityPedestrian extends Activity{
	
		private TextView xCoord;
	    private TextView yCoord;	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_motion_pedestrian);
		xCoord = (TextView) findViewById(R.id.textView1);
	    yCoord = (TextView) findViewById(R.id.textView2);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
        // MotionEvent object holds X-Y values
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
        	 xCoord.setText(String.valueOf((int) event.getX()));
             yCoord.setText(String.valueOf((int) event.getY()));
     	    yCoord.setPadding((int) event.getX(), (int) event.getY(),0 ,0 );
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
        	 xCoord.setText(String.valueOf((int) event.getX()));
             yCoord.setText(String.valueOf((int) event.getY()));
      	    yCoord.setPadding((int) event.getX(), (int) event.getY(),0 ,0 );
        }else if(event.getAction() == MotionEvent.ACTION_UP){
        	 xCoord.setText("koniec");
             yCoord.setText("koniec");
        }
 
        return super.onTouchEvent(event);
    }
	
	
}
