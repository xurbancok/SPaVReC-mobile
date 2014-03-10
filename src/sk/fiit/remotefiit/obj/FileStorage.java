package sk.fiit.remotefiit.obj;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Environment;
import android.util.Log;
import sk.fiit.remotefiit.interfaces.DataStorage;

public class FileStorage implements DataStorage {

	@Override
	public void readData() {
		File root = Environment.getExternalStorageDirectory();
		File file = new File(root, "/FiitRemote/calibrationData");
		if (file.exists() && file.canRead()) {
			FileInputStream fis;
				try {
					fis = new FileInputStream(file);
					String result = convertStreamToString(fis);
					fis.close();
					JSONObject jObject = new JSONObject(result);
					CalibrationData.setTiltBackwards(jObject.getDouble("tiltBackwards"));
					CalibrationData.setTiltForwards(jObject.getDouble("tiltForwards"));		
					CalibrationData.setTiltLeft(jObject.getDouble("tiltLeft"));
					CalibrationData.setTiltRight(jObject.getDouble("tiltRight"));
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		}
		else{
			CalibrationData.setTiltBackwards(4);
			CalibrationData.setTiltForwards(-1);		
			CalibrationData.setTiltLeft(3);
			CalibrationData.setTiltRight(-3);
		}

	}

	@Override
	public void storeData(double tiltForwards, double tiltBackwards, double tiltLeft, double tiltRight) {
		File root = Environment.getExternalStorageDirectory();
		File dir = new File(root, "/FiitRemote/");
		dir.mkdirs(); 
		File file = new File(dir, "calibrationData");
		try {
		        if (root.canWrite()) {
		        	JSONObject result = new JSONObject();
		        	result.put("tiltBackwards", tiltBackwards);
		        	result.put("tiltForwards", tiltForwards);
		        	result.put("tiltLeft", tiltLeft);
		        	result.put("tiltRight",tiltRight);
		        	
		        	FileWriter filewriter = new FileWriter(file);
		            BufferedWriter out = new BufferedWriter(filewriter);
		            out.write(result.toString());
		            out.close();
		        }
		    } catch (IOException e) {
		        Log.e("VR", "Application couldn't write the data" + e.getMessage());
		    } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
	//precitanie celeho suboru do jednej premennej ako String, aby sa dal z toho spravit jednoducho JSON
	private static String convertStreamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append("\n");
		}
		reader.close();
		return sb.toString();
	}

}
