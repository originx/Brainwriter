package foi.appchallenge.brainwriting.modules;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
/**
 * @author Mario Oršoliæ
 *  * Http get client as sync task which receive parameters of GetParametrs class
 *
 */
public class HttpGetClientSync {

	//local variables
	String responseText="";
	//url where we send get request
	String url="";
	//GET parametrs with structure <name><value> in a list
	List<NameValuePair> nameValuePairs;
	 /**
	  * Sends HTTP GET request and returns string as response
	  * @param params
	  * @return  Server response in string type
	  */
	public String getRequest(GetParameters... params) {
		url= params[0].url;
		if(!url.endsWith("?"))
	        url += "?";
		nameValuePairs = params[0].nameValuePairs;
		
	    String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");

	    url += paramString;

		//instantiate new  post class
		HttpGet post = new HttpGet(url);
		//create http client
		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response = null;
		try {
			//execute query and get return data
			response = client.execute(post);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//get response in HttpEntity type
		HttpEntity entity = response.getEntity();
		try {
			responseText = EntityUtils.toString(entity);
		} catch (ParseException e) {
			e.printStackTrace();
			return "Parse exception";
		} catch (IOException e) {
			e.printStackTrace();
			return "IO exception";
		}
		return responseText;
	}
}
