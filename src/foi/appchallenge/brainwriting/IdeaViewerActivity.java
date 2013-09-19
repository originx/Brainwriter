package foi.appchallenge.brainwriting;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;
import foi.appchallenge.brainwriting.adapters.GalleryImageAdapter;

public class IdeaViewerActivity extends Activity {
	private ImageView selectedImage;
	private Context context;

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
		gallery.setAdapter(new GalleryImageAdapter(this));

		selectedImage.setImageResource(R.drawable.image1);

		// clicklistener for Gallery
		gallery.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Toast.makeText(context, "Your selected position = " + position,
						Toast.LENGTH_SHORT).show();
				// show the selected Image
				selectedImage.setImageResource(mImageIds[position]);
			}

		});

	}
}
