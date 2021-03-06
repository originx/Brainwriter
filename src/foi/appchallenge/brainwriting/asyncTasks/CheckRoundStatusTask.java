package foi.appchallenge.brainwriting.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import foi.appchallenge.brainwriting.interfaces.IResponseListener;
import foi.appchallenge.brainwriting.modules.JSONFunctions;
/**
 * 
 * @author Mario Orsolic
 * Used for async server round status checking
 *
 */
public class CheckRoundStatusTask extends AsyncTask<String, Void, String> {
	
	IResponseListener clientListener;
	private AsyncHttpClient client=new AsyncHttpClient();
	private String roundNumber="0";
	private Context c;
	private volatile boolean running = true;
	private String cRound;
	/**
	 * hook up necessary listener
	 * @param clientListener
	 */
	public void setListener(IResponseListener clientListener) {
        this.clientListener = clientListener;
    }
	
	/**
	 * COnstructor
	 * @param c Pass the necessary context for future use and http client
	 */
	public CheckRoundStatusTask(Context c,String currentRound) {
		super();
		this.c = c;
		cRound=currentRound;
		roundNumber=cRound;
		running=true;
	}

	@Override
	protected String doInBackground(String... params) {
		
		String groupName=params[0];
		
		//while we wait to login and the server responds with 0
		while(running && cRound.equals(roundNumber)){
			try {
				Thread.sleep(3000); //dont overflood the server, behave nicely
				final RequestParams p = new RequestParams();
				 p.put("group",  groupName);
				
				client.get(c,"http://evodeployment.evolaris.net/brainwriting/status", p,new AsyncHttpResponseHandler(){
					@Override
				    public void onSuccess(String response) {
						roundNumber=JSONFunctions.getRoundNumber2(response);
						}
				});
			
			} catch (InterruptedException e) {
				//Log.d("DEBUG","Interrupted :V");
			}
			Log.d("RE","!:"+roundNumber);
			
		}
		return roundNumber;
		
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
			Log.d("RE",roundNumber);
			clientListener.responseSuccess(roundNumber);
		
	}

	/**
	 * cleanup
	 */
	@Override
	protected void onCancelled() {
		running=false;
		client.cancelRequests(c, true);
		super.onCancelled();
	}
	
	
}
