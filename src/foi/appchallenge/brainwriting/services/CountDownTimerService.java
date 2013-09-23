package foi.appchallenge.brainwriting.services;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

public class CountDownTimerService extends Service {
    static long TIME_LIMIT = 30000;
    CountDownTimer Count;
    
    
    
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Count = new CountDownTimer(TIME_LIMIT, 1000) {
		    public void onTick(long millisUntilFinished) {
		    	long seconds = millisUntilFinished / 1000;
		    	String time = String.format("%02d:%02d", (seconds % 3600) / 60, (seconds % 60));
		    	
		    	Intent i = new Intent("COUNTDOWN_UPDATED");
		    	i.putExtra("countdown",time);

		    	sendBroadcast(i);
		    	//coundownTimer.setTitle(millisUntilFinished / 1000);

		    }

		    public void onFinish() {
		    	//coundownTimer.setTitle("Sedned!");
		    	Intent i = new Intent("COUNTDOWN_UPDATED");
		    	i.putExtra("countdown","Sended!");

		    	sendBroadcast(i);
		    	Log.d("COUNTDOWN", "FINISH!");
		    	stopSelf();
		    	
		    }
		};

		Count.start();
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onDestroy() {
		Count.cancel();
		super.onDestroy();
	}
	
	

}
