package sk.fiit.remotefiit.obj;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import sk.fiit.remotefiit.emun.Movement;
import sk.fiit.remotefiit.interfaces.DataFormatConverter;

public class JSONCreator implements DataFormatConverter {

	@Override
	public JSONObject dataToJSON(String inputData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject dataToJSON(ArrayList<Movement> inputData) {
		JSONObject result = new JSONObject();
		try {
			result.put("MOVE", inputData.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public JSONObject dataToJSON(String[]... inputData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject dataToJSON(Map<String, String[]> inputData) {
		// TODO Auto-generated method stub
		return null;
	}

}
