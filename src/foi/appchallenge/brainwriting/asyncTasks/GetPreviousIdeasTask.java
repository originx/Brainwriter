package foi.appchallenge.brainwriting.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import foi.appchallenge.brainwriting.interfaces.IResponseListener;
/**
 * 
 * @author Mario Orsolic
 * Used for async ideas obtaining
 *
 */
public class GetPreviousIdeasTask extends AsyncTask<String, Void, String> {
	
	IResponseListener clientListener;
	private AsyncHttpClient client=new AsyncHttpClient();
	private Context c;
	private String r;
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
	public GetPreviousIdeasTask(Context c) {
		super();
		this.c = c;
	}

	@Override
	protected String doInBackground(String... params) {
		
		String groupName=params[0];
		String username=params[1];
		//while we wait to login and the server responds with 0

			try {
				final RequestParams p = new RequestParams();
				 p.put("group",  groupName);
				 p.put("user", username);
				client.get(c,"http://evodeployment.evolaris.net/brainwriting/previous", p,new AsyncHttpResponseHandler(){
					@Override
				    public void onSuccess(String response) {
						r=response;
						}
				});
				Thread.sleep(3000); //dont overflood the server, behave nicely
			} catch (InterruptedException e) {
				//Log.d("DEBUG","Interrupted :V");
			}
	
		return r;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
			clientListener.responseSuccess(r);
	}

	/**
	 * cleanup
	 */
	@Override
	protected void onCancelled() {

		client.cancelRequests(c, true);
		super.onCancelled();
	}
	
	
}
