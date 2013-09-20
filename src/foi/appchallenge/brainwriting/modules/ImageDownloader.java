package foi.appchallenge.brainwriting.modules;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import foi.appchallenge.brainwriting.interfaces.ImageLoaderListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

/*
 * here we are going to use an AsyncTask to download the image 
 *      in background while showing the download progress
 * */

public class ImageDownloader extends AsyncTask<Void, Void, Void> {


	private String url;
	private Button save;


	private ImageView img;
	private Bitmap bmp;
	private ImageLoaderListener listener;



	/*--- constructor ---*/
	public ImageDownloader(String url, Bitmap bmp,ImageLoaderListener imageLoaderListener) {
	/*--- we need to pass some objects we are going to work with ---*/
		this.url = url;



		this.bmp = bmp;
		this.listener = imageLoaderListener;
	}
	
	


	@Override
	protected Void doInBackground(Void... arg0) {

		bmp = getBitmapFromURL(url);

	

		return null;
	}

	

	@Override
	protected void onPostExecute(Void result) {

		if (listener != null) {
			listener.onImageDownloaded(bmp);
			}
		img.setImageBitmap(bmp);
		save.setEnabled(true);


		super.onPostExecute(result);
	}

	public static Bitmap getBitmapFromURL(String link) {
		/*--- this method downloads an Image from the given URL, 
		 *  then decodes and returns a Bitmap object
		 ---*/
		try {
			URL url = new URL(link);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);

			return myBitmap;

		} catch (IOException e) {
			e.printStackTrace();
			Log.e("getBmpFromUrl error: ", e.getMessage().toString());
			return null;
		}
	}

}
