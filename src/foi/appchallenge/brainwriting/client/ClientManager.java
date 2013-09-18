package foi.appchallenge.brainwriting.client;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;
import foi.appchallenge.brainwriting.modules.GetParameters;
import foi.appchallenge.brainwriting.modules.HttpGetClientSync;
import foi.appchallenge.brainwriting.modules.HttpPostClient;
import foi.appchallenge.brainwriting.modules.JSONFunctions;

public class ClientManager {
	// prepare params for sending
	final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);

	final HttpGetClientSync klijent = new HttpGetClientSync();

	// create parameters which are sent to httpGetClient
	final GetParameters params = new GetParameters();

	JSONObject array = new JSONObject();
	/**
	 * Starts session for given group name
	 * @param groupName Name of the group for session
	 * @return json message if session has started
	 */
	public String startSession(String groupName) {
		nameValuePairs.add(new BasicNameValuePair("group", groupName));
		params.url = "http://evodeployment.evolaris.net/brainwriting/start";
		params.nameValuePairs = nameValuePairs;
		return klijent.getRequest(params);
	}
	
	/**
	 * Checks session status for given group name
	 * @param groupName Name of group for which we check session
	 * @return json message as status of session in form of round:number
	 */
	public String checkSession(String groupName) {
		nameValuePairs.add(new BasicNameValuePair("group", groupName));
		params.url = "http://evodeployment.evolaris.net/brainwriting/status";
		params.nameValuePairs = nameValuePairs;

		return JSONFunctions.getRoundNumber(klijent.getRequest(params));

	}
	
	/**
	 * Recieve ideas from previous round
	 * @param groupName name of the group
	  * @param username Name of user who recieve ideas
	 * @return json message of previous ideas
	 */
	public String getIdeas(String groupName , String username) {
		nameValuePairs.add(new BasicNameValuePair("group", groupName));
		nameValuePairs.add(new BasicNameValuePair("user", username));

		params.url = "http://evodeployment.evolaris.net/brainwriting/previous";
		params.nameValuePairs = nameValuePairs;

		String a= klijent.getRequest(params);
		Log.d("DEBUG", a);
		return a;
	}
	
	/**
	 * Submit ideas from specific user via http post multipart/form-data
	 * @param groupName Name of the sending group
	 * @param username Username of the sender
	 * @param text Array of strings that represent text of each specific idea
	 * @param b Array of bitmaps that represent visualization of each specific idea
	 * @return
	 */
	public boolean submitIdeas(String groupName,String username,String text[],Bitmap b[]){
		String url = "http://evodeployment.evolaris.net/brainwriting/submit?group="+groupName+"&user="+username;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
            HttpPostClient client = new HttpPostClient(url);
            try {
				client.connectForMultipart();
			
            for (int i = 0; i < text.length; i++) {
    			client.addFormPart("text"+i+1, text[i]);
    		}
            for (int i = 0; i < b.length; i++) {
            	b[i].compress(CompressFormat.PNG, 0, baos);
            	 client.addFilePart("image"+i+1, "image"+i+1+".png", baos.toByteArray());
            }
            client.finishMultipart();
		return true;
            } catch (Exception e) {
				e.printStackTrace();
				return false;
			}
            
	}
}
