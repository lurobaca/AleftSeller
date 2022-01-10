package com.essco.seller.Clases;


import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;


public class Class_GPS extends Service implements LocationListener {

	private final Context context;
	boolean isGPSEnable = false;
	boolean isNetworkEnable=false;
	boolean canGetLocation=false;
	
	
	Location location;
	double latitude;
	double longitude;
	
	private static final long MIN_DISTANCE_CHANCE_FOR_UPDATE=10;
	private static final long MIN_TIME_BW_UPDATE=1000*60*1;
	
	protected android.location.LocationManager LocationManager;
	
	public Class_GPS(Context context)
	{
		this.context=context;
		getlocation();
	}
	
	public Location getlocation()
	{
		try
		{
			LocationManager = (android.location.LocationManager) context.getSystemService(LOCATION_SERVICE);
			isGPSEnable=LocationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
			isNetworkEnable=LocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			
			if(!isGPSEnable && !isNetworkEnable){
				
			}
			else{
				this.canGetLocation=true;
				if(isNetworkEnable)
				{
                 LocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
										MIN_TIME_BW_UPDATE,
		                                MIN_DISTANCE_CHANCE_FOR_UPDATE, 
		                                this);
					
					
				
				
				
				if(LocationManager != null ) {
					location=LocationManager.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER);
					
					if(location!= null){
						latitude=location.getLatitude();
						longitude=location.getLongitude();
					}
				  }
				}
				
				if(!isGPSEnable){
					if(location==null){
						LocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
																MIN_TIME_BW_UPDATE,
																 MIN_DISTANCE_CHANCE_FOR_UPDATE,this);
						if(LocationManager != null){
							location=LocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if(location != null){
								latitude=location.getLatitude();
								longitude=location.getLongitude();
							}
						}
					}
						
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return location;
	}
	
	
	
	public void stopUsingGps(){
		if(LocationManager != null){
			LocationManager.removeUpdates(Class_GPS.this);
		}
	}
	
	public double getlatitude(){
		if(location!= null){
			latitude=location.getLatitude();
		}
		return latitude;
	}
	
	public double getlongitude(){
		if(location!= null){
			longitude=location.getLongitude();
		}
			return longitude;
	}
	
	public boolean canGetLocation(){
		return this.canGetLocation;
	}
	
	public void shoeSettingsAlert(){
		
		AlertDialog.Builder alertDialog= new AlertDialog.Builder(context);
		alertDialog.setTitle("GPS ID SETTING");
		alertDialog.setMessage("GPS IS NOT ENABLE , DO YOU WANT TO GO SETTINGS MENNU?");
		alertDialog.setPositiveButton("Settings",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
	
				Intent viewIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(viewIntent);
			}
			}); 
		
		alertDialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		
		
		alertDialog.show();
		
	}
			
		
	
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}

