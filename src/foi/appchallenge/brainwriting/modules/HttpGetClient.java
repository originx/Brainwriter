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

import android.os.AsyncTask;
import android.util.Log;
import foi.appchallenge.brainwriting.interfaces.IResponseListener;

/**
 * Http get client as async task which receive parameters of GetParametrs class
 * @author Mario Oršoliæ
 * @return Server response in string type
 */
public class HttpGetClient extends AsyncTask<GetParameters, Void,String> {
	IResponseListener clientListener;
	HttpGet post ;
	public HttpGetClient(){
	}
	/**
	 * Set listener for the client so the user can catch the data without compromising workflow of main thread
	 * @param clientListener Interface of the client
	 */
	public void setHttpGetClientListener(IResponseListener clientListener) {
        this.clientListener = clientListener;
    }
	//local variables
	String responseText="";
	//url where we send get request
	String url="";
	//GET parametrs with structure <name><value> in a list
	List<NameValuePair> nameValuePairs;
	
	protected String doInBackground(GetParameters... params) {
		if(isCancelled()) return "";
		url= params[0].url;
		if(!url.endsWith("?"))
	        url += "?";
		nameValuePairs = params[0].nameValuePairs;
		
	    String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");

	    url += paramString;

		//instantiate new  post class
	    post= new HttpGet(url);
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
	

@Override
/**
 * after execution call listener and return results
 */
protected void onPostExecute(String result) {
	super.onPostExecute(result);
	if(result==""){
		clientListener.responseFail();
	}else{
		Log.d("DEBUG", result);
		clientListener.responseSuccess(result);
		post.abort();
		post=null;
		
	}
}
@Override
protected void onCancelled() {
	
	super.onCancelled();
	if(post!=null)
	post.abort();
}

}
