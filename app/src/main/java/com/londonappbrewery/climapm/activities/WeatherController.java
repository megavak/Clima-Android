package com.londonappbrewery.climapm.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.londonappbrewery.climapm.R;
import com.londonappbrewery.climapm.model.WeatherDataModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class WeatherController extends AppCompatActivity {

    // Constants:
    final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    // App ID to use OpenWeather data
    final String APP_ID = "e72ca729af228beabd5d20e3b7749713";
    private final String MY_APP_ID = "605168367ec3977d8d017d62b4d249ba";
    // Time between location updates (5000 milliseconds or 5 seconds)
    final long MIN_TIME = 5000;
    // Distance between location updates (1000m or 1km)
    final float MIN_DISTANCE = 1000;
    private final static int REQUEST_CODE = 1234;

    // TODO: Set LOCATION_PROVIDER here:
    private final String GPS_LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;
    private final String NETWORK_LOCATION_PROVIDER = LocationManager.NETWORK_PROVIDER;


    // Member Variables:
    TextView mCityLabel;
    ImageView mWeatherImage;
    TextView mTemperatureLabel;

    // TODO: Declare a LocationManager and a LocationListener here:
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_controller_layout);

        // Linking the elements in the layout to Java code
        mCityLabel = (TextView) findViewById(R.id.locationTV);
        mWeatherImage = (ImageView) findViewById(R.id.weatherSymbolIV);
        mTemperatureLabel = (TextView) findViewById(R.id.tempTV);
        ImageButton changeCityButton = (ImageButton) findViewById(R.id.changeCityButton);


        // TODO: Add an OnClickListener to the changeCityButton here:
        changeCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ChangeCityController.class);
                startActivity(intent);
            }
        });

    }


    // TODO: Add onResume() here:

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Clima", "onResume called");
       /* Bundle bundle = getIntent().getExtras();
        String cityName = bundle.getString("cityName");*/
       Intent myIntent = getIntent();
        String cityName = myIntent.getStringExtra("cityName");
        if (cityName != null){
           getWeatherForNewCity(cityName);
        }else {
            Log.d("Clima", "Getting weather for current location");
            getWeatherForCurrentLocation();
        }

    }


    // TODO: Add getWeatherForNewCity(String city) here:
       private  void getWeatherForNewCity(String cityName){
                  RequestParams params = new RequestParams();
           params.put("q",cityName);
           params.put("appid",MY_APP_ID);
           letsDoSomeNetworking(params);
       }

    // TODO: Add getWeatherForCurrentLocation() here:
    private void getWeatherForCurrentLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Clima", "onLocationChanged called() callback received.");
                String longitude = String.valueOf(location.getLongitude());
                String latitude = String.valueOf(location.getLatitude());
                Log.d("Clima", "lon: " +longitude);
                Log.d("Clima", "lat: "+latitude);

                RequestParams params = new RequestParams();
                params.put("lon",longitude);
                params.put("lat",latitude);
                params.put("appid",MY_APP_ID);
                letsDoSomeNetworking( params);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("Clima", "onStatusChanged called() callback received.");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("Clima", "onProviderEnabled() callback received.");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("Clima", "onProviderDisabled() callback received.");
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            String[] requestedPermission = {Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this,requestedPermission,REQUEST_CODE);
            return;
        }
        locationManager.requestLocationUpdates(NETWORK_LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
             }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Clima","permission granted");
                getWeatherForCurrentLocation();
            }else {
                Log.d("Clima","permission denied");
            }
        }
    }

    // TODO: Add letsDoSomeNetworking(RequestParams params) here:
        private  void letsDoSomeNetworking(RequestParams params) {
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.get(WEATHER_URL,params,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                      Log.d("Clima","Success! JSON" + response.toString());
                    WeatherDataModel weather = WeatherDataModel.fromJson(response);
                    updateUI(weather);
                }
                 @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                      JSONObject errorResponse) {
                     Log.e("Clima","Fail " + throwable.toString());
                     Log.d("Clima","Status code " + statusCode);
                      makeToast("Request Failed");
                }

            });
        }

    private void makeToast(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }


    // TODO: Add updateUI() here:
        private void updateUI(WeatherDataModel weatherDataModel){

            mCityLabel.setText(weatherDataModel.getCity());
            mTemperatureLabel.setText(weatherDataModel.getTemperature());
            int resourceID = getResources().getIdentifier(weatherDataModel.getIconName(),"drawable",getPackageName());
              mWeatherImage.setImageResource(resourceID);
        }


    // TODO: Add onPause() here:


    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager !=null){
            locationManager.removeUpdates(locationListener);
        }
    }
}
