package com.hiveapp.notifyme;

import java.util.Calendar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
//import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class Add_Note extends Activity implements OnClickListener, android.view.View.OnClickListener {
	 	Button btnCalendar, btnTimePicker;
	    TextView txtDate, txtTime;
	    private DbHelper mHelper;
	    private SQLiteDatabase dataBase;
	    private EditText edit_first;
	    private String id,nname;
	    private boolean isUpdate;
	    
	    private int mYear, mMonth, mDay, mHour, mMinute;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
				super.onCreate(savedInstanceState);
				setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				setContentView(R.layout.add_notes);
				
				btnCalendar = (Button) findViewById(R.id.date);
		        btnTimePicker = (Button) findViewById(R.id.time);
		 
		        txtDate = (TextView) findViewById(R.id.txt_date);
		        txtTime = (TextView) findViewById(R.id.txt_time);
		        edit_first=(EditText)findViewById(R.id.editText1);
		 
		        btnCalendar.setOnClickListener(this);
		        btnTimePicker.setOnClickListener(this);
		        
		        
		        isUpdate=getIntent().getExtras().getBoolean("update");
		        if(isUpdate)
		        {
		        	id=getIntent().getExtras().getString("ID");
		        	nname=getIntent().getExtras().getString("Nname");
		        	//lname=getIntent().getExtras().getString("Lname");
		        	edit_first.setText(nname);
		        	//edit_last.setText(lname);
		        	
		        }
		        mHelper=new DbHelper(this);
	}
	//***************************************************************************************
	public void onClick(View v) {
 
        if (v == btnCalendar) {
 
            // Process to get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
 
            // Launch Date Picker Dialog
            DatePickerDialog dpd = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
 
           public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                   // Display Selected date in textbox
                   txtDate.setText(dayOfMonth + "-"  + (monthOfYear + 1) + "-" + year);}
            		}, mYear, mMonth, mDay);
            dpd.show();
        }
        if (v == btnTimePicker) {
 
            // Process to get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
 
            // Launch Time Picker Dialog
            TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener(){
 
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                int minute) {
                        	//tpd.setIs24HourView(true);
                            // Display Selected time in textbox
                            txtTime.setText(hourOfDay + ":" + minute);
                        }

						
                    }, mHour, mMinute,  DateFormat.is24HourFormat(this));
            tpd.show();
        }
        
    }
	
	//*************************************************************************************
	/**
	 * save data into SQLite
	 */
	private void saveData(){
		dataBase=mHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		
		values.put(DbHelper.KEY_NNAME,nname);
		//values.put(DbHelper.KEY_LNAME,lname );
		
		System.out.println("");
		if(isUpdate)
		{    
			//update database with new data 
			dataBase.update(DbHelper.TABLE_NOTE, values, DbHelper.KEY_ID+"="+id, null);
		}
		else
		{
			//insert data into database
			dataBase.insert(DbHelper.TABLE_NOTE, null, values);
		}
		//close database
		dataBase.close();
		finish();
		
		
	}
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
			nname=edit_first.getText().toString().trim();
			//lname=edit_last.getText().toString().trim();
			if(nname.length()>0 )
			{
				saveData();
			}
			else
			{
				AlertDialog.Builder alertBuilder=new AlertDialog.Builder(Add_Note.this);
				alertBuilder.setTitle("Invalid Data");
				alertBuilder.setMessage("Please, Enter valid data");
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

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}