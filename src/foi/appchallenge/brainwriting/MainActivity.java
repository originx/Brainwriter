package foi.appchallenge.brainwriting;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	Context context;
	EditText groupName;
    EditText username;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        groupName = (EditText)findViewById(R.id.et_group_name);
        username = (EditText)findViewById(R.id.et_username);
        Button start = (Button)findViewById(R.id.b_start);
        
        
        start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Toast.makeText(
						context,
						"TEST: " + groupName.getText().toString() + " "
								+ username.getText().toString(),
						Toast.LENGTH_SHORT).show();
				
				Intent i = new Intent(context, IdeaMakerActivity.class);
				startActivity(i);
			}
		});
        
    }
    
    
    
}
