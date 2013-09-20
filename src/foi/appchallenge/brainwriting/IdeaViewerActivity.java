package foi.appchallenge.brainwriting;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;
import foi.appchallenge.brainwriting.adapters.GalleryImageAdapter;

public class IdeaViewerActivity extends ActionBarActivity {
	private ImageView selectedImage;
	private Context context;
	int caseShow = 0;

	private Integer[] mImageIds = { R.drawable.image1, R.drawable.image2,
			R.drawable.image3

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_idea_viewer);

		context = this;
		Gallery gallery = (Gallery) findViewById(R.id.gallery);
		selectedImage = (ImageView) findViewById(R.id.iv_idea);
		gallery.setSpacing(15);
		
		Bundle bundle = getIntent().getExtras();
		caseShow = bundle.getInt("caseShow");
		if(caseShow == 0){
			gallery.setAdapter(new GalleryImageAdapter(this));
		}
		else if(caseShow == 1){
			Toast.makeText(context, "Show previous rounds ideas",
					Toast.LENGTH_SHORT).show();
		}
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayOptions(actionBar.getDisplayOptions()
				^ ActionBar.DISPLAY_SHOW_TITLE);
		
		
		
		

		//selectedImage.setImageResource(R.drawable.image1);

		// clicklistener for Gallery
		gallery.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Toast.makeText(context, "Your selected position = " + position,
						Toast.LENGTH_SHORT).show();
				// show the selected Image

				// get root folder
							String root = Environment.getExternalStorageDirectory().toString();
							File myDir = new File(root + "/Brainwriter/download");
							myDir.mkdirs();
							BitmapFactory.Options options = new BitmapFactory.Options();
							options.inPreferredConfig = Bitmap.Config.ARGB_8888;
							// load file if exists
							File imgFile = new File(myDir + "/image"
									+ String.valueOf(position + 1) + ".png");
							if (imgFile.exists()) {

								// if there is a file prepare canvas with loaded image
								Bitmap mutableBitmap = BitmapFactory.decodeFile(imgFile
										.getAbsolutePath());
								Bitmap bmp = mutableBitmap.copy(Bitmap.Config.ARGB_8888, true);
								selectedImage.setImageBitmap(bmp);
							}else{
								selectedImage.setImageResource(mImageIds[position]);
							}
			}

		});

	}
	
	 @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			getMenuInflater().inflate(R.menu.idea_viewer, menu);
			MenuItem search = menu.findItem(R.id.action_search);
			SearchView searchView = (SearchView) MenuItemCompat
					.getActionView(search);
			searchView.setQueryHint(context.getResources().getString(
					R.string.action_bar_search_hint));
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
		
			case R.id.action_edit:
				
				Toast.makeText(context, "EDIT", Toast.LENGTH_SHORT).show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
		}
}
