package foi.appchallenge.brainwriting.interfaces;

import android.graphics.Bitmap;

/**
 * Interface za uspje�nost obavljene zada�e
 * @author Mario Or�oli�
 * @version 1.0 - 16.01.2013.
 */
public interface ImageLoaderListener {
	/**
	 * Succsessful response method handler
	 * @param data String of data returned
	 * @return 
	 */
	void onImageDownloaded(Bitmap bmp);
}
