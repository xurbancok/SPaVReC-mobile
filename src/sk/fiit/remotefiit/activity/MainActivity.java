package sk.fiit.remotefiit.activity;

import sk.fiit.remotefiit.obj.CalibrationData;
import sk.fiit.remotefiit.obj.SettingsDialog;

import com.example.remotefiit.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private String hostAdress;
	private SettingsDialog settingsDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//odstrani uzky sedi pas s nazvom aplikacie pod notifikacnou listou
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		//odstrani notifikacnu listu
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//findViewById(R.id.button1).setVisibility(View.GONE);
		
		//kalibracne udaje sa nahraju zo suboru do globalnej premennej 
		CalibrationData.initializeValue();
	}
	
	public void startMotion(View view){
		if(hostAdress==null || hostAdress.length()==0)hostAdress="123.123.123.123:1234";
		String[] host = hostAdress.split(":");
		Intent motion = new Intent(getApplicationContext(),MotionActivityOld.class);
		motion.putExtra("IP", host[0]);
		motion.putExtra("port", host[1]);
		startActivity(motion);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {           
	    super.onActivityResult(requestCode, resultCode, data);
	    if (requestCode == 0) {
	        if (resultCode == RESULT_OK) {
	            String contents = data.getStringExtra("SCAN_RESULT");
	            Log.d("VR", contents);
	            hostAdress = contents;
	            settingsDialog.setText(hostAdress);
	        }
	        if(resultCode == RESULT_CANCELED){
	        }
	    }
	}
	
	public void onClickSettings(View view){
		//settingsDialog = new SettingsDialog(MainActivity.this, hostAdress, this);
		//settingsDialog.show();
		Intent i = new Intent(getApplicationContext(),SettingsActivity.class);
		startActivity(i);
	}
	
	public void setHost(String input){
		this.hostAdress = input;
		((ImageView)findViewById(R.id.imageViewIcon)).setBackgroundResource(R.drawable.fajka);
	}
	
	public void startMotionPedestrian(View view){
		if(hostAdress==null || hostAdress.length()==0)hostAdress="123.123.123.123:1234";
		String[] host = hostAdress.split(":");
		//Toast.makeText(this, "Pedestrian", Toast.LENGTH_SHORT).show();
		Intent i = new Intent(getApplicationContext(),MotionActivityPedestrian.class);
		i.putExtra("IP", host[0]);
		i.putExtra("port", host[1]);
		startActivity(i);
	}
	public void startMotionHelicopter(View view){
		if(hostAdress==null || hostAdress.length()==0)hostAdress="123.123.123.123:1234";
		String[] host = hostAdress.split(":");
		//Toast.makeText(this, "Helicopter", Toast.LENGTH_SHORT).show();
		Intent i = new Intent(getApplicationContext(),MotionActivityHelicopter.class);
		i.putExtra("IP", host[0]);
		i.putExtra("port", host[1]);
		startActivity(i);
	}
	public void startMotionCar(View view){
		if(hostAdress==null || hostAdress.length()==0)hostAdress="123.123.123.123:1234";
		String[] host = hostAdress.split(":");
		//Toast.makeText(this, "Car", Toast.LENGTH_SHORT).show();
		Intent i = new Intent(getApplicationContext(),MotionActivityCar.class);
		i.putExtra("IP", host[0]);
		i.putExtra("port", host[1]);
		startActivity(i);
	}
//	
//	public void readQRCode(View view){
//		try {
//
//		    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//		    intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
//		    startActivityForResult(intent, 0);
//
//		} catch (Exception e) {
//		    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
//		    Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
//		    startActivity(marketIntent);
//
//		}
//	}
//	
}
