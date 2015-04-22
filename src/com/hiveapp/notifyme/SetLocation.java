package com.hiveapp.notifyme;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SetLocation extends SupportMapFragment implements OnMapLongClickListener,LoaderCallbacks<Cursor> {
	
	GoogleMap mapView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 // Invoke LoaderCallbacks to retrieve and draw already saved locations in map
        getLoaderManager().initLoader(0, null, this); 
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onInflate(Activity activity, AttributeSet attrs,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onInflate(activity, attrs, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		mapView = getMap();
		mapView.setMyLocationEnabled(true);
		mapView.setBuildingsEnabled(true);
		mapView.setOnMapLongClickListener(this);
	}

	@Override
	public void onMapLongClick(LatLng point) {
		// TODO Auto-generated method stub
		mapView.clear();
		mapView.addMarker(new MarkerOptions().position(point).title("college").snippet("subtitle").draggable(true));
	
		
		// Creating an instance of LocationDeleteTask
        LocationDeleteTask deleteTask = new LocationDeleteTask();
        
        // Deleting all the rows from SQLite database table
        deleteTask.execute();
		
		 // Creating an instance of ContentValues
        ContentValues contentValues = new ContentValues();
        
        // Setting latitude in ContentValues
        contentValues.put(LocationsDB.FIELD_LAT, point.latitude );
        
        // Setting longitude in ContentValues
        contentValues.put(LocationsDB.FIELD_LNG, point.longitude);
        
        // Setting zoom in ContentValues
        contentValues.put(LocationsDB.FIELD_ZOOM, mapView.getCameraPosition().zoom);
        
        // Creating an instance of LocationInsertTask
        LocationInsertTask insertTask = new LocationInsertTask();
        
        // Storing the latitude, longitude and zoom level to SQLite database
        insertTask.execute(contentValues);                
                        
	}
	
	private class LocationInsertTask extends AsyncTask<ContentValues, Void, Void>{
        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            
            /** Setting up values to insert the clicked location into SQLite database */           
        	getActivity().getContentResolver().insert(LocationsContentProvider.CONTENT_URI, contentValues[0]);            
            return null;
        }        
    }
    
    private class LocationDeleteTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            
            /** Deleting all the locations stored in SQLite database */
        	getActivity().getContentResolver().delete(LocationsContentProvider.CONTENT_URI, null, null);            
            return null;
        }        
    } 

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		 // Uri to the content provider LocationsContentProvider
        Uri uri = LocationsContentProvider.CONTENT_URI;
        
        // Fetches all the rows from locations table
        return new CursorLoader(getActivity(),uri, null, null, null, null);
        
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		
		 int locationCount = 0;
	        double lat=0;
	        double lng=0;
	        float zoom=0;
	        
	        // Number of locations available in the SQLite database table
	        locationCount = arg1.getCount();
	        
	        // Move the current record pointer to the first row of the table
	        arg1.moveToFirst();
	        
	        for(int i=0;i<locationCount;i++){
	            
	            // Get the latitude
	            lat = arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LAT));
	            
	            // Get the longitude
	            lng = arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LNG));
	            
	            // Get the zoom level
	            zoom = arg1.getFloat(arg1.getColumnIndex(LocationsDB.FIELD_ZOOM));
	            
	            // Creating an instance of LatLng to plot the location in Google Maps
	            LatLng location = new LatLng(lat, lng);
	            
	            // Drawing the marker in the Google Maps
	            mapView.addMarker(new MarkerOptions().position(location).title("college").snippet("subtitle").draggable(true));
	            
	            // Traverse the pointer to the next row
	            arg1.moveToNext();
	        }
	        
	        if(locationCount>0){
	            // Moving CameraPosition to last clicked position
	            mapView.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lng)));
	            
	            // Setting the zoom level in the map on last position  is clicked
	            mapView.animateCamera(CameraUpdateFactory.zoomTo(zoom));  
	            
	        }
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
