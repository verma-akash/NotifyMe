package com.hiveapp.notifyme;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


/**
 * Set an alarm for the date passed into the constructor
 * When the alarm is raised it will start the NotifyService
 * 
 * This uses the android build in alarm manager *NOTE* if the phone is turned off this alarm will be cancelled
 * 
 * This will run on it's own thread.
 * 
 * @author paul.blundell
 */
public class AlarmTask implements Runnable{
	// The date selected for the alarm
	private final Calendar date;
	// The android system alarm manager
	private final AlarmManager am;
	// Your context to retrieve the alarm manager from
	private final Context context;
	
	private final int day;
	
	public final int n_id;

	public AlarmTask(Context context, Calendar date ,int d ,int n_id) {
		this.context = context;
		this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		this.date = date;
		this.day=d;
		this.n_id=n_id;
	}
	
	@Override
	public void run() {
		
		Intent intent=null;
		PendingIntent pendingIntent = null;
		
		if(day==2)
		{
			int c=20000+n_id;
			intent = new Intent(context, NotifyServiceMonday.class);
			intent.putExtra(NotifyServiceMonday.p_id, n_id);
			intent.putExtra(NotifyServiceMonday.INTENT_NOTIFY, true);
			pendingIntent = PendingIntent.getService(context, c, intent, 0);
		    
		}
		
		else if(day==3)
			
		{
			int c=30000+n_id;
			intent = new Intent(context, NotifyServiceTuesday.class);
			intent.putExtra(NotifyServiceTuesday.p_id, n_id);
			intent.putExtra(NotifyServiceTuesday.INTENT_NOTIFY, true);
			pendingIntent = PendingIntent.getService(context, c, intent, 0);
		}
		
		else if(day==4)
		{
			int c=40000+n_id;
			intent = new Intent(context, NotifyServiceWednesday.class);
			intent.putExtra(NotifyServiceWednesday.p_id, n_id);
			intent.putExtra(NotifyServiceWednesday.INTENT_NOTIFY, true);
			pendingIntent = PendingIntent.getService(context, c, intent, 0);
			
		}
		
		else if(day==5)
		{
			int c=50000+n_id;
			intent = new Intent(context, NotifyServiceThursday.class);
			intent.putExtra(NotifyServiceThursday.p_id, n_id);
			intent.putExtra(NotifyServiceThursday.INTENT_NOTIFY, true);
			pendingIntent = PendingIntent.getService(context, c, intent, 0);
			//am.cancel(pendingIntent);
		}
		
		else if(day==6)
		{
			int c=60000+n_id;
			intent = new Intent(context, NotifyServiceFriday.class);
			intent.putExtra(NotifyServiceFriday.p_id, n_id);
			intent.putExtra(NotifyServiceFriday.INTENT_NOTIFY, true);
			pendingIntent = PendingIntent.getService(context, c, intent, 0);
		}
		
		else if(day==7)
		{
			int c=70000+n_id;
			intent = new Intent(context, NotifyServiceSaturday.class);
			intent.putExtra(NotifyServiceSaturday.p_id, n_id);
			intent.putExtra(NotifyServiceSaturday.INTENT_NOTIFY, true);
			pendingIntent = PendingIntent.getService(context, c, intent, 0);
			
		}
		// Request to start are service when the alarm date is upon us
		// We don't start an activity as we just want to pop up a notification into the system bar not a full activity
		
		
		// Sets an alarm - note this alarm will be lost if the phone is turned off and on again
		am.set(AlarmManager.RTC, date.getTimeInMillis(), pendingIntent);
		//am.cancel(pendingIntent);
		am.setRepeating(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), 604800000 , pendingIntent);
		//am.cancel(pendingIntent);
	}
}
