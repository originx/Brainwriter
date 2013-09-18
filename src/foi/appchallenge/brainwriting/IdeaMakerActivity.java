package foi.appchallenge.brainwriting;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

public class IdeaMakerActivity extends ActionBarActivity {

	Context context;
	protected RadioGroup rgDrawOptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_idea_maker);
		context = this;

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayOptions(actionBar.getDisplayOptions() ^ ActionBar.DISPLAY_SHOW_TITLE);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
		final String[] ideas = { "Idea 1","Idea 2", "Idea 3"};
		SpinnerAdapter mSpinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, ideas);
		
		OnNavigationListener mOnNavigationListener = new OnNavigationListener() {
	
			  @Override
			  public boolean onNavigationItemSelected(int position, long itemId) {
				  Toast.makeText(context, ideas[position].toString(), Toast.LENGTH_SHORT)
					.show();
			    return true;
			  }
			};

		actionBar.setListNavigationCallbacks(mSpinnerAdapter, mOnNavigationListener);
		
		
		rgDrawOptions = (RadioGroup) findViewById(R.id.rg_draw_options);
		rgDrawOptions
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group,
							int checkedRadioButtonId) {
						switch (checkedRadioButtonId) {
						case R.id.rb_hand:
							Toast.makeText(context, "HAND", Toast.LENGTH_SHORT)
									.show();
							break;
						case R.id.rb_brush:
							Toast.makeText(context, "BRUSH", Toast.LENGTH_SHORT)
									.show();
							break;
						case R.id.rb_text:
							Toast.makeText(context, "TEXT", Toast.LENGTH_SHORT)
									.show();
							break;
						case R.id.rb_eraser:
							Toast.makeText(context, "ERASER", Toast.LENGTH_SHORT)
									.show();
							break;
						case R.id.rb_color:
							Toast.makeText(context, "COLOR", Toast.LENGTH_SHORT)
									.show();
							break;
						case R.id.rb_photo:
							Toast.makeText(context, "PHOTO", Toast.LENGTH_SHORT)
									.show();
							break;
						case R.id.rb_clear_all:
							Toast.makeText(context, "CLEAR ALL",
									Toast.LENGTH_SHORT).show();
							break;
						default:
							break;
						}
					}
				});
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.idea_maker, menu);
	    MenuItem search = menu.findItem(R.id.action_search);
	    SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
	    searchView.setQueryHint(context.getResources().getString(R.string.action_bar_search_hint));
	    searchView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
			}
		});
	    
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        /*case R.id.action_search:
	        	Toast.makeText(context, "SEARCH",
						Toast.LENGTH_SHORT).show();
	            return true;*/
	        case R.id.action_send:
	        	Toast.makeText(context, "SEND",
						Toast.LENGTH_SHORT).show();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
}
