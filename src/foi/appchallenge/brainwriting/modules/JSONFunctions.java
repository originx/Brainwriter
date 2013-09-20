package foi.appchallenge.brainwriting.modules;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
/**
 * Used for operating with JSON (JavaScript Object Notation) data
 * @author Renato Turic
 * @author Mario Orsolic
 *
 */
public class JSONFunctions {

	/**
	 * Collects the JSON data from a URL
	 * @param url The URL value where the JSON data is located
	 * @return JSONObject
	 */
	public static JSONObject getJSONData(String JSONString) {
		// initialize
		JSONObject jArray = null;

		// try parse the string to a JSON object
		try {
			jArray = new JSONObject(JSONString);
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		return jArray;
	}
	
	
	/**
	 * Removes brackets {} that are part of some strings
	 * @param targetString String that u want to clear of unnecessary brackets
	 * @return string without brackets
	 */
	public static String removeBrackets(String targetString){
		if (targetString.contains("{}")) {
			targetString = targetString.replace("{}", " ");
		}
		return targetString;
	}
	
	
	public static String getRoundNumber(String targetString){
		
		try {
			JSONObject json = getJSONData(targetString.replace("\"", ""));
			int round = json.getInt("round");
			return String.valueOf(round);
		} catch (JSONException e) {
			e.printStackTrace();
			return "0";
		}
		
	}
	
	public static String[] getTextIdeas(String targetString){
		String text1="";
		String text2="";
		String text3="";
		try {
			   JSONObject json = getJSONData(targetString.replace("\"", ""));
			   text1= json.getString("text1");
			    text2 = json.getString("text2");
			    text3 = json.getString("text3");

			   
			  } catch (JSONException e) {
			   e.printStackTrace();

			  }
		String [] result = {text1,text2,text3};
		return result;
	}
	
	public static String[] getImageIdeas(String targetString){
		String image1="";
		String image2="";
		String image3="";
		try {
			   JSONObject json;
			   json=new JSONObject(targetString);
			   image1= json.getString("image1");
			   image2 = json.getString("image2");
			   image3 = json.getString("image3");

			   
			  } catch (JSONException e) {
			   

			  }
		String [] result = {image1,image2,image3};
		return result;
	}
}
