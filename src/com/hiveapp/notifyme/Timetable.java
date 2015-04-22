package com.hiveapp.notifyme;

import java.lang.reflect.Field;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import com.hiveapp.notifyme.R;

import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
//import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

public class Timetable extends SherlockFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		

		View view = inflater.inflate(R.layout.viewpager_main, container, false);
		// Locate the ViewPager in viewpager_main.xml
		ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
		// Set the ViewPagerAdapter into ViewPager
		mViewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
		return view;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	
	   inflater.inflate(R.menu.timetable_settings, menu);
	
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int Itemid = item.getItemId();
		if(Itemid == R.id.add_item)
		{
			Intent i = new Intent(getActivity().getApplicationContext(),AddClass.class);
			i.putExtra("update", false);
			startActivity(i);
		}
		
		return super.onOptionsItemSelected(item);
	}
}
