package com.hiveapp.notifyme;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
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
public class NotifyServiceSaturday extends Service implements LocationListener{
	
	/**
	 * Class for clients to access
	 */
	private DbHelper mHelper;
	
	/**********************************************/
	private LocationsDB ldb;
	private SQLiteDatabase dataBase,database1;
	
    String lat="error", lng="error";
    double lng2,lat2,lat1,lng1;
	LocationManager locationManager;
	Location location;
	float[] distance=new float[2];
	/**********************************************/
	
	public class ServiceBinder extends Binder {
		NotifyServiceSaturday getService() {
			return NotifyServiceSaturday.this;
		}
	}
	
	
	// Unique id to identify the notification.
	
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
		
		/*******************************************************************************/
		ldb = new LocationsDB(this);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		// set criteria for location provider:
		Criteria criteria = new Criteria(); 
		// fine accuracy
	    criteria.setAccuracy(Criteria.ACCURACY_FINE);  
	    criteria.setCostAllowed(false);
	    String bestProvider = locationManager.getBestProvider(criteria, false);
	    Location location = locationManager.getLastKnownLocation(bestProvider);
	    
	    lng2=location.getLongitude();
	    lat2=location.getLatitude();
	   
	    /***************************************************/
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("LocalService", "Received start id " + startId + ": " + intent);
		
		/******************************************************************/
		
		Cursor s1;
		database1 = ldb.getReadableDatabase();
		String query="Select * from "+LocationsDB.DATABASE_TABLE;
		s1=database1.rawQuery(query, null);
		
		if (s1.moveToFirst()) {
			
			lat=s1.getString(s1.getColumnIndex(LocationsDB.FIELD_LAT));
			lng=s1.getString(s1.getColumnIndex(LocationsDB.FIELD_LNG));
			
			lat1=Double.parseDouble(lat);
			lng1=Double.parseDouble(lng);
			
		}
		
		Location.distanceBetween( lat1, lng1,lat2,lng2, distance);
		/***************************************************************/
		
		int c=70000+(intent.getExtras().getInt(p_id));
		
		// If this service was started by out AlarmTask intent then we want to show our notification
		if(intent.getBooleanExtra(INTENT_NOTIFY, false))
			if( distance[0] < 1000 )/********************************************/
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
		int id=c-70000,itype=0,l;
		int minute=id%100;
		int hhour=id/100;
		Cursor s;
		String n_Title="error", n_class="error", n_room="error";
		dataBase = mHelper.getReadableDatabase();
		String query="Select * from "+DbHelper.TABLE_SAT+" where "+DbHelper.KEY_HOUR+" ="+hhour + " AND " +DbHelper.KEY_MIN+" = "+minute;
		s=dataBase.rawQuery(query, null);
		
		if (s.moveToFirst()) {
			
			n_Title=s.getString(s.getColumnIndex(DbHelper.KEY_FNAME));
			n_room=s.getString(s.getColumnIndex(DbHelper.KEY_LNAME));
			n_class=s.getString(s.getColumnIndex(DbHelper.KEY_TNAME));
			itype=s.getInt(s.getColumnIndex(DbHelper.KEY_CTYPE));
		}
		
		
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
		.build();

		notification.defaults = Notification.DEFAULT_ALL;

		// Clear the notification when it is pressed
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		// Send the notification to the system.
		mNM.notify(c, notification);
		
		// Stop the service when we are finished
		stopSelf();
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
}