package foi.appchallenge.brainwriting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MainActivity extends ActionBarActivity {

	Context context;
	EditText groupName;
    EditText username;
    //shared preferences
    private SharedPreferences prefs;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        groupName = (EditText)findViewById(R.id.et_group_name);
        username = (EditText)findViewById(R.id.et_username);
        Button start = (Button)findViewById(R.id.b_start);
        //create preferences manager for saving username
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
    
        start.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Toast.makeText(
						context,
						"TEST: " + groupName.getText().toString() + " "
								+ username.getText().toString(),
						Toast.LENGTH_SHORT).show();
				
				if(username.getText().toString()==""){
					Toast.makeText(context, R.string.noUsernameError, Toast.LENGTH_SHORT).show();
				}else{
				editor.putString("username", username.getText().toString());
				editor.commit();
				}
				if(groupName.getText().toString()==""){
					Toast.makeText(context, R.string.noGroupNameError, Toast.LENGTH_SHORT).show();
				}else{
					editor.putString("groupName", groupName.getText().toString());
					editor.commit();
					final RequestParams params = new RequestParams();
					 params.put("group",  groupName.getText().toString());
					AsyncHttpClient client = new AsyncHttpClient();
					client.get("http://evodeployment.evolaris.net/brainwriting/status", params,new AsyncHttpResponseHandler() {
					
						@Override
					    public void onSuccess(String response) {
							//TODO parsat response string i vidjet dal je round == 0
							if(true){
								AsyncHttpClient c2 = new AsyncHttpClient();
								c2.get("http://evodeployment.evolaris.net/brainwriting/start", params, new AsyncHttpResponseHandler() {
									
									@Override public void onSuccess(String response) {
										//TODO remove this info
										Toast.makeText(context, "created group"+"faadfsfads", Toast.LENGTH_SHORT).show();
										Intent i = new Intent(context, IdeaMakerActivity.class);
										startActivity(i);
									}
								});
								


							}else{
								//TODO remove this info
								Toast.makeText(context, "created group", Toast.LENGTH_SHORT).show();
								Intent i = new Intent(context, IdeaMakerActivity.class);
								startActivity(i);	
							}
					    }
					});				
				}	
			}
		});
        
    }
    
    
    
}
