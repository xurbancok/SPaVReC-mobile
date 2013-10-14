package sk.fiit.remotefiit.activity;

import com.example.remotefiit.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void startMotion(View view){
		String[] host = ((EditText) findViewById(R.id.editTextIP)).getText().toString().split(":");
		Intent motion = new Intent(getApplicationContext(),MotionActivity.class);
		motion.putExtra("IP", host[0]);
		motion.putExtra("port", host[1]);
		startActivity(motion);
	}
	
	public void readQRCode(View view){
		try {

		    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
		    intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
		    startActivityForResult(intent, 0);

		} catch (Exception e) {
		    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
		    Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
		    startActivity(marketIntent);

		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {           
	    super.onActivityResult(requestCode, resultCode, data);
	    if (requestCode == 0) {
	        if (resultCode == RESULT_OK) {
	            String contents = data.getStringExtra("SCAN_RESULT");
	            ((EditText) findViewById(R.id.editTextIP)).setText(contents);
	            Log.d("VR", contents);
	        }
	        if(resultCode == RESULT_CANCELED){
	            //handle cancel
	        }
	    }
	}
}
