package com.hiveapp.notifyme;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class DisplayAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<String> id;
	private ArrayList<String> firstName;
	private ArrayList<String> lastName;
	private ArrayList<String> teacherName;
	private ArrayList<String> classType;
	private ArrayList<String> hour;
	private ArrayList<String> min;
	

	public DisplayAdapter(Context c, ArrayList<String> id,ArrayList<String> fname, ArrayList<String> lname, ArrayList<String> tname, ArrayList<String> ctype, ArrayList<String> thour, ArrayList<String> tmin) {
		this.mContext = c;

		this.id = id;
		this.firstName = fname;
		this.lastName = lname;
		this.teacherName= tname;
		this.classType = ctype;
		this.hour= thour;
		this.min=tmin;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return id.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int pos, View child, ViewGroup parent) {
		Holder mHolder;
		LayoutInflater layoutInflater;
		if (child == null) {
			layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			child = layoutInflater.inflate(R.layout.listcell, null);
			mHolder = new Holder();
			mHolder.txt_id = (TextView) child.findViewById(R.id.txt_id);
			mHolder.txt_fName = (TextView) child.findViewById(R.id.txt_fName);
			mHolder.txt_lName = (TextView) child.findViewById(R.id.txt_lName);
			mHolder.imageView1 = (ImageView)child.findViewById(R.id.imageView1);
			mHolder.txt_hour = (TextView) child.findViewById(R.id.txt_hour);
			mHolder.txt_min = (TextView) child.findViewById(R.id.txt_min);
			child.setTag(mHolder);
		} else {
			mHolder = (Holder) child.getTag();
		}
		mHolder.txt_id.setText(teacherName.get(pos));
		mHolder.txt_fName.setText(firstName.get(pos));
		mHolder.txt_lName.setText(lastName.get(pos));
		mHolder.txt_hour.setText(hour.get(pos));
		mHolder.txt_min.setText(min.get(pos));
		if((classType.get(pos).equals("0")))
		{
			mHolder.imageView1.setImageResource(R.drawable.tutorial_1);
		}
		else if((classType.get(pos).equals("1")))
		{
			mHolder.imageView1.setImageResource(R.drawable.practical_1);
		}
		else if((classType.get(pos).equals("2")))
		{
			mHolder.imageView1.setImageResource(R.drawable.lecture_1);
		}
		return child;
	}

	public class Holder {
		TextView txt_id;
		TextView txt_fName;
		TextView txt_lName;
		TextView txt_hour;
		TextView txt_min;
		ImageView imageView1;
	}

}
