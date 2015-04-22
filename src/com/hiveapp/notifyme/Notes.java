package com.hiveapp.notifyme;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
//import com.actionbarsherlock.view.MenuItem;
import com.hiveapp.notifyme.R;


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

@SuppressLint("ShowToast")
public class Notes extends SherlockFragment {
	private DbHelper mHelper;
	private SQLiteDatabase dataBase;

	private ArrayList<String> userId = new ArrayList<String>();
	private ArrayList<String> user_nName = new ArrayList<String>();
	private ListView userList;
	private AlertDialog.Builder build;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.notes, container, false);
		
		
		userList = (ListView) rootView.findViewById(R.id.listView1);

		mHelper = new DbHelper(getActivity());
		
	
	
	//click to update data
			userList.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {

					Intent i = new Intent(getActivity().getApplicationContext(),
							Add_Note.class);
					i.putExtra("Nname", user_nName.get(arg2));
					//i.putExtra("Lname", user_lName.get(arg2));
					//i.putExtra("ID", userId.get(arg2));
					i.putExtra("update", true);
					startActivity(i);

				}
			});
			
			//long click to delete data
			userList.setOnItemLongClickListener(new OnItemLongClickListener() {

				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						final int arg2, long arg3) {

					build = new AlertDialog.Builder(getActivity());
					build.setTitle("Delete " + user_nName.get(arg2) + " "
							);
					build.setMessage("Do you want to delete ?");
					build.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {

									Toast.makeText(
											getActivity().getApplicationContext(),
											user_nName.get(arg2) + " "
													+ " is deleted.", 3000).show();

									dataBase.delete(
											DbHelper.TABLE_NOTE,
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
			return rootView;
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
					+ DbHelper.TABLE_NOTE, null);

			userId.clear();
			user_nName.clear();
			//user_lName.clear();
			if (mCursor.moveToFirst()) {
				do {
					userId.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_ID)));
					user_nName.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_NNAME)));
					//user_lName.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.KEY_LNAME)));

				} while (mCursor.moveToNext());
			}
			DisplayAdapterNote disadpt = new DisplayAdapterNote(getActivity(),userId, user_nName);
			userList.setAdapter(disadpt);
			userList.setEmptyView(getActivity().findViewById(R.id.notetext));
			mCursor.close();
		}
		
		
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	
	   inflater.inflate(R.menu.notes_settings, menu);
	
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int Itemid = item.getItemId();
		if(Itemid == R.id.add_note)
		{
			Intent i = new Intent(getActivity().getApplicationContext(),Add_Note.class);
			i.putExtra("update", false);
			startActivity(i);
		}
		
		return super.onOptionsItemSelected(item);
	}
}
