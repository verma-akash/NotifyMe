package com.hiveapp.notifyme;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * This service is started when an Alarm has been raised
 * 
 * We pop a notification into the status bar for the user to click on
 * When the user clicks the notification a new activity is opened
 * 
 * @author paul.blundell
 */
public class NotifyServiceTuesday extends Service {
	
	/**
	 * Class for clients to access
	 */
	private DbHelper mHelper;
	private SQLiteDatabase dataBase;
	
	public class ServiceBinder extends Binder {
		NotifyServiceTuesday getService() {
			return NotifyServiceTuesday.this;
		}
	}
	
	
	// Unique id to identify the notification.
	//private static final int NOTIFICATION = 3;
	// Name of an intent extra we can use to identify if this service was started to create a notification	
	public static final String INTENT_NOTIFY = "com.hiveapp.notifyme.INTENT_NOTIFY";
	
	public static final String p_id = "com.hiveapp.notifyme.p_id";
	// The system notification manager
	private NotificationManager mNM;

	@Override
	public void onCreate() {
		Log.i("NotifyService", "onCreate()");
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mHelper = new DbHelper(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("LocalService", "Received start id " + startId + ": " + intent);
		
		int c=30000+(intent.getExtras().getInt(p_id));
		
		// If this service was started by out AlarmTask intent then we want to show our notification
		if(intent.getBooleanExtra(INTENT_NOTIFY, false))
			showNotification(c);
		
		// We don't care if this service is stopped as we have already delivered our notification
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	// This is the object that receives interactions from clients
	private final IBinder mBinder = new ServiceBinder();

	/**
	 * Creates a notification and shows it in the OS drag-down status bar
	 */
	private void showNotification(int c) {
		int id=c-30000,itype=0,l;
		int minute=id%100;
		int hhour=id/100;
		Cursor s;
		String n_Title="error", n_class="error", n_room="error";
		dataBase = mHelper.getReadableDatabase();
		String query="Select * from "+DbHelper.TABLE_TUE+" where "+DbHelper.KEY_HOUR+" ="+hhour + " AND " +DbHelper.KEY_MIN+" = "+minute;
		//String query1=
		//String query2=
		s=dataBase.rawQuery(query, null);
		
		if (s.moveToFirst()) {
			
			n_Title=s.getString(s.getColumnIndex(DbHelper.KEY_FNAME));
			n_room=s.getString(s.getColumnIndex(DbHelper.KEY_LNAME));
			n_class=s.getString(s.getColumnIndex(DbHelper.KEY_TNAME));
			itype=s.getInt(s.getColumnIndex(DbHelper.KEY_CTYPE));
			//l=s.getInt(s.getColumnIndex(DbHelper.KEY_HOUR));
		}
		
		//long time = System.currentTimeMillis();
		
		// The PendingIntent to launch our activity if the user selects this notification
		
		
		if(itype==0)
		{
			l=R.drawable.tutorial_1;
		}
		else if(itype==1)
		{
			l=R.drawable.practical_1;	
		}
		else
		{
			l=R.drawable.lecture_1;
		}
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
		
		Notification notification = new Notification.Builder(this)
		
		.setContentTitle(n_Title)
		.setContentText(n_room + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + n_class)
		.setSmallIcon(l)
		.setContentIntent(contentIntent)
		//.setSound(soundUri)
		//.addAction(R.drawable.ninja, "View", pIntent)
		//.addAction(0, "Remind", pIntent)
		.build();

		notification.defaults = Notification.DEFAULT_ALL;
		// The PendingIntent to launch our activity if the user selects this notification
		

		// Set the info for the views that show in the notification panel.
		//notification.setLatestEventInfo(this, title, text, contentIntent);

		// Clear the notification when it is pressed
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		// Send the notification to the system.
		mNM.notify(c, notification);
		
		// Stop the service when we are finished
		stopSelf();
	}
}