package sk.fiit.remotefiit.obj;

import sk.fiit.remotefiit.activity.MainActivity;

import com.example.remotefiit.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class SettingsDialog extends Dialog{

	public SettingsDialog(final Context context, String host, final MainActivity mainActivity) {
		super(context);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.settings_dialog);
		
		this.setCanceledOnTouchOutside(true);
		
		if (host!=null){
			((EditText) findViewById(R.id.editText1)).setText(host);
		}
		
		ImageButton qr = (ImageButton) findViewById(R.id.imageButtonQr);
		qr.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
				    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
				    intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
				    ((Activity) context).startActivityForResult(intent, 0);

				} catch (Exception e) {
				    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
				    Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
				    ((Activity) context).startActivity(marketIntent);
				}
			}
		});
		
		Button ok = (Button) findViewById(R.id.buttonOk);
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String address = ((EditText) findViewById(R.id.editText1)).getText().toString();
				if(address.matches("\\b([0-9]{1,3}\\.){3}[0-9]{1,3}:[0-9]{1,}\\b")==true){
				 mainActivity.setHost(address.trim());
				 SettingsDialog.this.dismiss();
				}else{
					Toast.makeText(getContext(), "Invalid address", Toast.LENGTH_LONG).show();
				}

				

			}
		});
		Button cancel = (Button) findViewById(R.id.buttonCancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SettingsDialog.this.dismiss();
			}
		});
	}
	
	public void setText(String input){
		((EditText) findViewById(R.id.editText1)).setText(input);
	}
	
}
