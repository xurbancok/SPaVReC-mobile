package sk.fiit.remotefiit.interfaces;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONObject;

import sk.fiit.remotefiit.emun.Movement;

public interface DataFormatConverter {

	JSONObject dataToJSON(String inputData);
	
	JSONObject dataToJSON(ArrayList<Movement> inputData);
	
	JSONObject dataToJSON(String[]...inputData);
	
	JSONObject dataToJSON(Map<String, String[]> inputData);
}
