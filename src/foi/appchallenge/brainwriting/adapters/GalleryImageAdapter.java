package foi.appchallenge.brainwriting.adapters;

import java.io.File;

import foi.appchallenge.brainwriting.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

@SuppressWarnings("deprecation")
public class GalleryImageAdapter extends BaseAdapter {
	private Context mContext;

	private Integer[] mImageIds = { R.drawable.image1, R.drawable.image2,R.drawable.image3 };

	public GalleryImageAdapter(Context context) {
		mContext = context;
	}

	public int getCount() {
		return mImageIds.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	// Override this method according to your need
	@SuppressWarnings("deprecation")
	public View getView(int index, View view, ViewGroup viewGroup) {
		// TODO Auto-generated method stub
		ImageView i = new ImageView(mContext);
		// get root folder
					String root = Environment.getExternalStorageDirectory().toString();
					File myDir = new File(root + "/Brainwriter/download");
					myDir.mkdirs();
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inPreferredConfig = Bitmap.Config.ARGB_8888;
					// load file if exists
					File imgFile = new File(myDir + "/image"
							+ String.valueOf(index + 1) + ".png");
					if (imgFile.exists()) {

						// if there is a file prepare canvas with loaded image
						Bitmap mutableBitmap = BitmapFactory.decodeFile(imgFile
								.getAbsolutePath());
						Bitmap bmp = mutableBitmap.copy(Bitmap.Config.ARGB_8888, true);
						i.setImageBitmap(bmp);
					}else{
		i.setImageResource(mImageIds[index]);
					}
		i.setLayoutParams(new Gallery.LayoutParams(200, 200));

		i.setScaleType(ImageView.ScaleType.FIT_XY);

		return i;
	}
}
