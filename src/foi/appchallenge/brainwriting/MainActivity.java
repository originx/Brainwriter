package foi.appchallenge.brainwriting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import foi.appchallenge.brainwriting.asyncTasks.CheckServerStatusTask;
import foi.appchallenge.brainwriting.interfaces.IResponseListener;
import foi.appchallenge.brainwriting.services.CountDownTimerService;

public class MainActivity extends ActionBarActivity {

	Context context;
	EditText groupName;
    EditText username;
    TextView statusText;
    boolean connecting=false;
	private long lastPressedTime;
	private static final int PERIOD = 2000;
    //shared preferences
    private SharedPreferences prefs;
    //private String roundNumber ="0";  //for later use
    private CheckServerStatusTask checkStatus;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        groupName = (EditText)findViewById(R.id.et_group_name);
        username = (EditText)findViewById(R.id.et_username);
        ImageButton start = (ImageButton)findViewById(R.id.b_start);
        statusText= (TextView)findViewById(R.id.statusText);
        //create preferences manager for saving username
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
    
        start.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				// Uncomment to start this activitiy
				// TODO put button for it in action bar
				//Intent i = new Intent(context, IdeaViewerActivity.class);
				//startActivity(i);
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
					//put group name in preferences
					editor.putString("groupName", groupName.getText().toString());
					editor.commit();
					//create asynctask
					checkStatus = new CheckServerStatusTask(MainActivity.this);
					checkStatus.setListener(new IResponseListener() {
						
						@Override
						public void responseSuccess(String data) {//if successfull response login
							Intent i = new Intent(context, IdeaMakerActivity.class);
							startActivity(i);
						}
						@Override
						public void responseFail() {
							//TODO possible notification about already started group etc via callback
							Toast.makeText(context, R.string.noGroupNameError, Toast.LENGTH_SHORT).show();
						}
					});
					
					checkStatus.execute(groupName.getText().toString());
					statusText.setText(R.string.connectingText);
					connecting=true;
				}	
			}
		});         
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		

		return super.onCreateOptionsMenu(menu);
	}
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
	
		case R.id.action_idea_gallery:
			
			Intent i = new Intent(context, IdeaViewerActivity.class);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
	        switch (event.getAction()) {
	        case KeyEvent.ACTION_DOWN:
	            if (event.getDownTime() - lastPressedTime < PERIOD) {
	                finish();
	            } else {
	            	if(connecting==true){ //if connecting break it and refresh text
	        			checkStatus.cancel(true);
	        			statusText.setText(R.string.connectText);
	        		}
	                Toast.makeText(getApplicationContext(), R.string.exitBtnText,
	                        Toast.LENGTH_SHORT).show();
	                lastPressedTime = event.getEventTime();
	            }
	            return true;
	        }
	    }
	    return false;
	}
}
