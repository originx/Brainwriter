package foi.appchallenge.brainwriting.asyncTasks;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import foi.appchallenge.brainwriting.interfaces.IResponseListener;
import foi.appchallenge.brainwriting.modules.HttpPostClient;
import foi.appchallenge.brainwriting.modules.PostParameters;
/**
 * 
 * @author Mario Orsolic
 * Used for submiting ideas
 *
 */
public class SubmitIdeasTask extends AsyncTask<PostParameters, Void, String> {
	
	IResponseListener clientListener;
	
	/**
	 * hook up necessary listener
	 * @param clientListener
	 */
	public void setListener(IResponseListener clientListener) {
        this.clientListener = clientListener;
    }
	


	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
			clientListener.responseSuccess(result);

		
	}



	@Override
	protected String doInBackground(PostParameters... params) {
		String groupName=params[0].groupName;
		String username=params[0].userName;
		String [] text = params[0].text;
		Object [] b = params[0].b;
		String url = "http://evodeployment.evolaris.net/brainwriting/submit?group="+groupName+"&user="+username;

            HttpPostClient client = new HttpPostClient(url);
            String data="";
            try {
				client.connectForMultipart();
            for (int i = 0; i < text.length; i++) {
    			client.addFormPart("text"+(i+1), text[i]);
    		}
            for (int i = 0; i < b.length; i++) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
            	Bitmap bmp=null;
            	bmp=(Bitmap)b[i];
            	bmp.compress(CompressFormat.PNG, 0, baos);
            	client.addFilePart("image"+(i+1), "image"+(i+1)+".png", baos.toByteArray());

            	
            }
            client.finishMultipart();
           data = client.getResponse();
            } catch (Exception e) {
				return data;
			}
            return "";
	
	}



	
}
