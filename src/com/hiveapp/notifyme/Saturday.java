package com.hiveapp.notifyme;

import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.hiveapp.notifyme.R;

@SuppressLint("ShowToast")
public class Saturday extends SherlockFragment {
	private DbHelper mHelper;
	private SQLiteDatabase dataBase;

	private ArrayList<String> userId = new ArrayList<String>();
	private ArrayList<String> user_fName = new ArrayList<String>();
	private ArrayList<String> user_lName = new ArrayList<String>();
	private ArrayList<String> user_tName = new ArrayList<String>();
	private ArrayList<String> user_cType = new ArrayList<String>();
	private ArrayList<String> user_tHour = new ArrayList<String>();
	private ArrayList<String> user_tMin = new ArrayList<String>();


	private ListView userList;
	private AlertDialog.Builder build;
	private ScheduleClient scheduleClient;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fragmenttab3.xml
		View view = inflater.inflate(R.layout.saturday, container, false);
		userList = (ListView) view.findViewById(R.id.contentlist5);
		
		// Create a new service client and bind our activity to this service
        scheduleClient = new ScheduleClient(getActivity());
        scheduleClient.doBindService();

		mHelper = new DbHelper(getActivity());
		
		//click to update data
				userList.setOnItemClickListener(new OnItemClickListener() {

					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {

						Intent i = new Intent(getActivity().getApplicationContext(),
								AddClass.class);
						i.putExtra("Fname", user_fName.get(arg2));
						i.putExtra("Lname", user_lName.get(arg2));
						i.putExtra("Tname", user_tName.get(arg2));
						i.putExtra("ID", userId.get(arg2));
						i.putExtra("Ctype", user_cType.get(arg2));
						i.putExtra("Hour", user_tHour.get(arg2));
						i.putExtra("Min", user_tMin.get(arg2));
						i.putExtra("update", true);
						startActivity(i);

					}
				});
				
				//long click to delete data
				userList.setOnItemLongClickListener(new OnItemLongClickListener() {

					int ho,mi,c_id;
					public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
							final int arg2, long arg3) {

						build = new AlertDialog.Builder(getActivity());
						build.setTitle("Delete " + user_fName.get(arg2));
						build.setMessage("Do you want to delete ?");
						build.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {

										Toast.makeText(
												getActivity().getApplicationContext(),
												user_fName.get(arg2) + " is deleted.", 3000).show();

										Cursor s;
					
										dataBase = mHelper.getReadableDatabase();
										String query="Select * from "+DbHelper.TABLE_SAT+" where "+DbHelper.KEY_ID + "="
												+ userId.get(arg2);
									
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
								    	

										dataBase.delete(
												DbHelper.TABLE_SAT,
												DbHelper.KEY_ID + "="
														+ userId.get(arg2), null);
										displayData();
										dialog.cancel();
									}
								});

						build.setNegativeButton("No",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
									}
								});
						AlertDialog alert = build.create();
						alert.show();

						return true;
					}
				});
		return view;
	}
	@Override
	public void onResume() {
		displayData();
		super.onResume();
	}

	/**
	 * displays data from SQLite
	 */
	private void displayData() {
		dataBase = mHelper.getWritableDatabase();
		Cursor mCursor = dataBase.rawQuery("SELECT * FROM "
				+ DbHelper.TABLE_SAT, null);

		userId.clear();
		user_fName.clear();
		user_lName.clear();
		user_tName.clear();
		user_cType.clear();
		user_tHour.clear();
		user_tMin.clear();
		if (mCursor.moveToFirst()) {
			do {
				userId.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_ID)));
				user_fName.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_FNAME)));
				user_lName.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_LNAME)));
				user_tName.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_TNAME)));
				user_cType.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_CTYPE)));
				user_tHour.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_HOUR)));
				user_tMin.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_MIN)));
				
			} while (mCursor.moveToNext());
		}
		DisplayAdapter disadpt = new DisplayAdapter(getActivity(),userId, user_fName, user_lName, user_tName, user_cType,user_tHour,user_tMin);
		userList.setAdapter(disadpt);
		userList.setEmptyView(getActivity().findViewById(R.id.saturday_text));
		mCursor.close();
	}

	  
	
}
