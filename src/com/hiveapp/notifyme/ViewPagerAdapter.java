package com.hiveapp.notifyme;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

	// Declare the number of ViewPager pages
	final int PAGE_COUNT = 6;
	private String titles[] = new String[] { "Monday", "Tuesday","Wednesday","Thursday","Friday","Saturday" };

	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {

			// Open FragmentTab1.java
		case 0:
			Monday fragmenttab1 = new Monday();
			return fragmenttab1;

			// Open FragmentTab2.java
		case 1:
			Tuesday fragmenttab2 = new Tuesday();
			return fragmenttab2;
			// Open FragmentTab3.java
		case 2:
			Wednesday fragmenttab3 = new Wednesday();
			return fragmenttab3;
		case 3:
			Thursday fragmenttab4 = new Thursday();
			return fragmenttab4;
		case 4:
			Friday fragmenttab5 = new Friday();
			return fragmenttab5;
		case 5:
			Saturday fragmenttab6 = new Saturday();
			return fragmenttab6;
		}
		return null;
	}

	public CharSequence getPageTitle(int position) {
		return titles[position];
	}

	@Override
	public int getCount() {
		return PAGE_COUNT;
	}

}