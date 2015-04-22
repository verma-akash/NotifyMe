package com.hiveapp.notifyme;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
//import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.hiveapp.notifyme.R;



public class AddClass extends Activity {
	
	private EditText edit_first,edit_last,edit_teacher;
	private DbHelper mHelper;
	private SQLiteDatabase dataBase;
	private String id,fname,lname,tname,classtype,h1,m1;
	private int ctype,h2,m2;
	private boolean isUpdate;
	ToggleButton one; 
	ToggleButton two;
	ToggleButton three;
	ToggleButton four;
	ToggleButton five;
	ToggleButton six;
	ToggleButton lec;
	ToggleButton tut;
	ToggleButton prac;
	
	// This is a handle so that we can call methods on our service
    private ScheduleClient scheduleClient;
    // This is the date picker used to select the date for our notification
	private TimePicker picker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.addclass);
		one = (ToggleButton) findViewById(R.id.mon);
	    two = (ToggleButton) findViewById(R.id.tue);
	    three = (ToggleButton) findViewById(R.id.wed);
	    four = (ToggleButton) findViewById(R.id.thu);
	    five = (ToggleButton) findViewById(R.id.fri);
	    six = (ToggleButton) findViewById(R.id.sat);
	    lec = (ToggleButton) findViewById(R.id.lec);
	    tut = (ToggleButton) findViewById(R.id.tut);
	    prac = (ToggleButton) findViewById(R.id.prac);
	    //timePicker = (TimePicker) findViewById(R.id.timePicker1);
	    //timePicker.setIs24HourView(true);
	    
	    picker = (TimePicker) findViewById(R.id.timePicker1);
        picker.setIs24HourView(true);
        
	    one.setOnCheckedChangeListener(changeChecker);
	    two.setOnCheckedChangeListener(changeChecker);
	    three.setOnCheckedChangeListener(changeChecker);
	    four.setOnCheckedChangeListener(changeChecker);
	    five.setOnCheckedChangeListener(changeChecker);
	    six.setOnCheckedChangeListener(changeChecker);
	    lec.setOnCheckedChangeListener(changeChecker);
	    tut.setOnCheckedChangeListener(changeChecker);
	    prac.setOnCheckedChangeListener(changeChecker);
	    
	    
        edit_first=(EditText)findViewById(R.id.editText1);
        edit_last=(EditText)findViewById(R.id.editText3);
        edit_teacher=(EditText)findViewById(R.id.editText2);
        isUpdate=getIntent().getExtras().getBoolean("update");
        if(isUpdate)
        {
        	id=getIntent().getExtras().getString("ID");
        	fname=getIntent().getExtras().getString("Fname");
        	lname=getIntent().getExtras().getString("Lname");
        	tname=getIntent().getExtras().getString("Tname");
        	h1=getIntent().getExtras().getString("Hour");
        	m1=getIntent().getExtras().getString("Min");
        	classtype=getIntent().getExtras().getString("Ctype");
        	h2=Integer.parseInt(h1);
        	m2=Integer.parseInt(m1);
        	edit_first.setText(fname);
        	edit_last.setText(lname);
        	edit_teacher.setText(tname);
        	picker.setCurrentHour(h2);
        	picker.setCurrentMinute(m2);
        	if(classtype.equals("0"))
        	{
        		tut.setChecked(true);
        	}
        	else if(classtype.equals("1"))
        	{
        		prac.setChecked(true);
        	}
        	else if(classtype.equals("2"))
        	{
        		lec.setChecked(true);
        	}
        	}
        	
        
         
         
        // Create a new service client and bind our activity to this service
        scheduleClient = new ScheduleClient(this);
        scheduleClient.doBindService();

     // Get a reference to our date picker
        
         mHelper=new DbHelper(this);
        
    

	}
    
	
		/**
		 * save data into SQLite
		 */
		private void saveData(){
			int h=picker.getCurrentHour();
	    	int m=picker.getCurrentMinute();
	    	int n_id=h*100+m;
			dataBase=mHelper.getWritableDatabase();
			ContentValues values=new ContentValues();
			if(lec.isChecked())
			{
				ctype=2;
			}
			else if(prac.isChecked())
			{
				ctype=1;
			}
			else if(tut.isChecked())
			{
				ctype=0;
			}
			
			values.put(DbHelper.KEY_FNAME,fname);
			values.put(DbHelper.KEY_LNAME,lname );
			values.put(DbHelper.KEY_TNAME,tname );
			values.put(DbHelper.KEY_CTYPE,ctype );
			values.put(DbHelper.KEY_HOUR,h);
			values.put(DbHelper.KEY_MIN,m );
			
			System.out.println("");
			int day = 0;
			
			if(one.isChecked())
			{
				day = 2;
			if(isUpdate)
			{    Cursor s;
			int ho = 0,mi = 0,c_id;/********************************/
			
			dataBase = mHelper.getReadableDatabase();
			String query="Select * from "+DbHelper.TABLE_MON+" where "+DbHelper.KEY_ID + "="
					+ id;
		
			s=dataBase.rawQuery(query, null);
			
			if (s.moveToFirst()) {
				
				ho=s.getInt(s.getColumnIndex(DbHelper.KEY_HOUR));
				mi=s.getInt(s.getColumnIndex(DbHelper.KEY_MIN));

			}
			
			c_id=ho*100+mi;
			
			Calendar c = Calendar.getInstance();
			c.set(Calendar.DAY_OF_WEEK,2);
	    	c.set(Calendar.HOUR_OF_DAY,ho);
	    	c.set(Calendar.MINUTE,mi);
	    	c.set(Calendar.SECOND, 0);
	    	
	    	scheduleClient.cancelAlarmForNotification(c,2,c_id);
				//update database with new data 
				dataBase.update(DbHelper.TABLE_MON, values, DbHelper.KEY_ID+"="+id, null);
			}
			else
			{
				//insert data into database
				dataBase.insert(DbHelper.TABLE_MON, null, values);
			}
			//close database
			dataBase.close();
			}
			else if(two.isChecked())
			{
				day = 3;
				if(isUpdate)
				{    
					Cursor s;
					int ho = 0,mi = 0,c_id;/********************************/
					
					dataBase = mHelper.getReadableDatabase();
					String query="Select * from "+DbHelper.TABLE_TUE+" where "+DbHelper.KEY_ID + "="
							+ id;
				
					s=dataBase.rawQuery(query, null);
					
					if (s.moveToFirst()) {
						
						ho=s.getInt(s.getColumnIndex(DbHelper.KEY_HOUR));
						mi=s.getInt(s.getColumnIndex(DbHelper.KEY_MIN));
	
					}
					
					c_id=ho*100+mi;
					
					Calendar c = Calendar.getInstance();
					c.set(Calendar.DAY_OF_WEEK,3);
			    	c.set(Calendar.HOUR_OF_DAY,ho);
			    	c.set(Calendar.MINUTE,mi);
			    	c.set(Calendar.SECOND, 0);
			    	
			    	scheduleClient.cancelAlarmForNotification(c,3,c_id);
					//update database with new data 
					dataBase.update(DbHelper.TABLE_TUE, values, DbHelper.KEY_ID+"="+id, null);
				}
				else
				{
					//insert data into database
					dataBase.insert(DbHelper.TABLE_TUE, null, values);
				}
				//close database
				dataBase.close();
			}
			else if(three.isChecked())
			{
				day = 4;
				if(isUpdate)
				{    
					Cursor s;
					int ho = 0,mi = 0,c_id;/********************************/
					
					dataBase = mHelper.getReadableDatabase();
					String query="Select * from "+DbHelper.TABLE_WED+" where "+DbHelper.KEY_ID + "="
							+ id;
				
					s=dataBase.rawQuery(query, null);
					
					if (s.moveToFirst()) {
						
						ho=s.getInt(s.getColumnIndex(DbHelper.KEY_HOUR));
						mi=s.getInt(s.getColumnIndex(DbHelper.KEY_MIN));
	
					}
					
					c_id=ho*100+mi;
					
					Calendar c = Calendar.getInstance();
					c.set(Calendar.DAY_OF_WEEK,4);
			    	c.set(Calendar.HOUR_OF_DAY,ho);
			    	c.set(Calendar.MINUTE,mi);
			    	c.set(Calendar.SECOND, 0);
			    	
			    	scheduleClient.cancelAlarmForNotification(c,4,c_id);
					//update database with new data 
					dataBase.update(DbHelper.TABLE_WED, values, DbHelper.KEY_ID+"="+id, null);
				}
				else
				{
					//insert data into database
					dataBase.insert(DbHelper.TABLE_WED, null, values);
				}
				//close database
				dataBase.close();
			}
			else if(four.isChecked())
			{
				day = 5;
				if(isUpdate)
				{    
					Cursor s;
					int ho = 0,mi = 0,c_id;/********************************/
					
					dataBase = mHelper.getReadableDatabase();
					String query="Select * from "+DbHelper.TABLE_THU+" where "+DbHelper.KEY_ID + "="
							+ id;
				
					s=dataBase.rawQuery(query, null);
					
					if (s.moveToFirst()) {
						
						ho=s.getInt(s.getColumnIndex(DbHelper.KEY_HOUR));
						mi=s.getInt(s.getColumnIndex(DbHelper.KEY_MIN));
	
					}
					
					c_id=ho*100+mi;
					
					Calendar c = Calendar.getInstance();
					c.set(Calendar.DAY_OF_WEEK,5);
			    	c.set(Calendar.HOUR_OF_DAY,ho);
			    	c.set(Calendar.MINUTE,mi);
			    	c.set(Calendar.SECOND, 0);
			    	
			    	scheduleClient.cancelAlarmForNotification(c,5,c_id);
					//update database with new data 
					dataBase.update(DbHelper.TABLE_THU, values, DbHelper.KEY_ID+"="+id, null);
				}
				else
				{
					//insert data into database
					dataBase.insert(DbHelper.TABLE_THU, null, values);
				}
				//close database
				dataBase.close();
			}
			else if(five.isChecked())
			{
				day = 6;
				if(isUpdate)
				{    
					Cursor s;
					int ho = 0,mi = 0,c_id;/********************************/
					
					dataBase = mHelper.getReadableDatabase();
					String query="Select * from "+DbHelper.TABLE_FRI+" where "+DbHelper.KEY_ID + "="
							+ id;
				
					s=dataBase.rawQuery(query, null);
					
					if (s.moveToFirst()) {
						
						ho=s.getInt(s.getColumnIndex(DbHelper.KEY_HOUR));
						mi=s.getInt(s.getColumnIndex(DbHelper.KEY_MIN));
	
					}
					
					c_id=ho*100+mi;
					
					Calendar c = Calendar.getInstance();
					c.set(Calendar.DAY_OF_WEEK,6);
			    	c.set(Calendar.HOUR_OF_DAY,ho);
			    	c.set(Calendar.MINUTE,mi);
			    	c.set(Calendar.SECOND, 0);
			    	
			    	scheduleClient.cancelAlarmForNotification(c,6,c_id);
					//update database with new data 
					dataBase.update(DbHelper.TABLE_FRI, values, DbHelper.KEY_ID+"="+id, null);
				}
				else
				{
					//insert data into database
					dataBase.insert(DbHelper.TABLE_FRI, null, values);
				}
				//close database
				dataBase.close();
			}
			else if(six.isChecked())
			{
				day = 7;
				if(isUpdate)
				{    
					Cursor s;
					int ho = 0,mi = 0,c_id;/********************************/
					
					dataBase = mHelper.getReadableDatabase();
					String query="Select * from "+DbHelper.TABLE_SAT+" where "+DbHelper.KEY_ID + "="
							+ id;
				
					s=dataBase.rawQuery(query, null);
					
					if (s.moveToFirst()) {
						
						ho=s.getInt(s.getColumnIndex(DbHelper.KEY_HOUR));
						mi=s.getInt(s.getColumnIndex(DbHelper.KEY_MIN));
	
					}
					
					c_id=ho*100+mi;
					
					Calendar c = Calendar.getInstance();
					c.set(Calendar.DAY_OF_WEEK,7);
			    	c.set(Calendar.HOUR_OF_DAY,ho);
			    	c.set(Calendar.MINUTE,mi);
			    	c.set(Calendar.SECOND, 0);
			    	
			    	scheduleClient.cancelAlarmForNotification(c,7,c_id);
					//update database with new data 
					dataBase.update(DbHelper.TABLE_SAT, values, DbHelper.KEY_ID+"="+id, null);
				}
				else
				{
					//insert data into database
					dataBase.insert(DbHelper.TABLE_SAT, null, values);
				}
				//close database
				dataBase.close();
				
			}
			// Create a new calendar set to the date chosen
	    	// we set the time to midnight (i.e. the first minute of that day)
	    	Calendar c = Calendar.getInstance();
	    	//c.set(year, 11, 29);
	    	c.set(Calendar.DAY_OF_WEEK,day);
	    	c.set(Calendar.HOUR_OF_DAY,picker.getCurrentHour());
	    	c.set(Calendar.MINUTE, picker.getCurrentMinute());
	    	c.set(Calendar.SECOND, 0);
	    		    	
	    	if(c.getTimeInMillis()<System.currentTimeMillis() || c.before(new GregorianCalendar()))
	    	{
	    		c.set(Calendar.HOUR_OF_DAY, 7);
	    		c.add(GregorianCalendar.DAY_OF_MONTH, 7);
	    	}
	    	
	    	/*int h=picker.getCurrentHour();
	    	int m=picker.getCurrentMinute();
	    	int n_id=h*100+m;*/
	    	
	    	
	    	// Ask our service to set an alarm for that date, this activity talks to the client that talks to the service
	    	scheduleClient.setAlarmForNotification(c,day,n_id);
	    	
	    	// Notify the user what they just did
	    	if(day==2)
	    	{
	    		// Notify the user what they just did
	    		Toast.makeText(this, "Notification set for: Monday", Toast.LENGTH_SHORT).show();
	    	}
	    	
	    	else if(day==3)
	    	{
	    		// Notify the user what they just did
	    		Toast.makeText(this, "Notification set for: Tuesday", Toast.LENGTH_SHORT).show();
	    	}
	    	
	    	else if(day==4)
	    	{
	    		// Notify the user what they just did
	    		Toast.makeText(this, "Notification set for: Wednesday", Toast.LENGTH_SHORT).show();
	    	}
	    	
	    	else if(day==5)
	    	{
	    		// Notify the user what they just did
	    		Toast.makeText(this, "Notification set for: Thursday", Toast.LENGTH_SHORT).show();
	    	}
	    	
	    	else if(day==6)
	    	{
	    		// Notify the user what they just did
	    		Toast.makeText(this, "Notification set for: Friday", Toast.LENGTH_SHORT).show();
	    	}
	    	
	    	else if(day==7)
	    	{
	    		// Notify the user what they just did
	    		Toast.makeText(this, "Notification set for: Saturday", Toast.LENGTH_SHORT).show();
	    	}
			finish();
			
			
		}
	//@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		android.view.MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.addclass_settings, menu);

		return super.onCreateOptionsMenu(menu);
		
	}
	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		int Itemid = item.getItemId();
		if(Itemid == R.id.back)
		{
			this.finish();
	        return true;
		}
		else if(Itemid==R.id.done)
		{
			fname=edit_first.getText().toString().trim();
			lname=edit_last.getText().toString().trim();
			tname=edit_teacher.getText().toString().trim();
			if(fname.length()>0 && lname.length()>0 && tname.length()>0 && (one.isChecked() || two.isChecked() || three.isChecked() || four.isChecked() || five.isChecked() || six.isChecked() ) && (lec.isChecked() || tut.isChecked() || prac.isChecked()))
			{
				saveData();
			}
			else
			{
				AlertDialog.Builder alertBuilder=new AlertDialog.Builder(AddClass.this);
				alertBuilder.setTitle("Invalid Data");
				alertBuilder.setMessage("Please, Enter valid data or day not selected");
				alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
						
					}
				});
				alertBuilder.create().show();
			}
			
		}
		return super.onOptionsItemSelected(item);
	}

	OnCheckedChangeListener changeChecker = new OnCheckedChangeListener() {

	    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	        if (isChecked){
	            if (buttonView == one) {
	                two.setChecked(false);
	                three.setChecked(false);
	                four.setChecked(false);
	                five.setChecked(false);
	                six.setChecked(false);
	            }
	            if (buttonView == two) {
	                one.setChecked(false);
	                three.setChecked(false);
	                four.setChecked(false);
	                five.setChecked(false);
	                six.setChecked(false);
	            }
	            if (buttonView == three) {
	                two.setChecked(false);
	                one.setChecked(false);
	                four.setChecked(false);
	                five.setChecked(false);
	                six.setChecked(false);
	            }
	            if (buttonView == four) {
	                two.setChecked(false);
	                three.setChecked(false);
	                one.setChecked(false);
	                five.setChecked(false);
	                six.setChecked(false);
	            }
	            if (buttonView == five) {
	                two.setChecked(false);
	                three.setChecked(false);
	                one.setChecked(false);
	                four.setChecked(false);
	                six.setChecked(false);
	            }
	            if (buttonView == six) {
	                two.setChecked(false);
	                three.setChecked(false);
	                one.setChecked(false);
	                four.setChecked(false);
	                five.setChecked(false);
	            }
	            if (buttonView == tut) {
	                prac.setChecked(false);
	                lec.setChecked(false);
	                
	            }
	            if (buttonView == lec) {
	                tut.setChecked(false);
	                prac.setChecked(false);
	                
	            }
	            if (buttonView == prac) {
	                tut.setChecked(false);
	                lec.setChecked(false);
	                
	            }
	        }
	    }
	};
	
	@Override
    protected void onStop() {
    	// When our activity is stopped ensure we also stop the connection to the service
    	// this stops us leaking our activity into the system *bad*
    	if(scheduleClient != null)
    		scheduleClient.doUnbindService();
    	super.onStop();
    }
	 
}