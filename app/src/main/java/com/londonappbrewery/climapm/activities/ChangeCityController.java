package com.londonappbrewery.climapm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.londonappbrewery.climapm.R;

public class ChangeCityController extends AppCompatActivity {
    private EditText cityEditText;
    private ImageButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_city_layout);
        cityEditText = (EditText)findViewById(R.id.queryET);
        btn = (ImageButton)findViewById(R.id.backButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cityEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String cityName = cityEditText.getText().toString();
                Intent intent = new Intent(getApplicationContext(),WeatherController.class);
                intent.putExtra("cityName",cityName);
                startActivity(intent);
                return false;
            }
        });

    }

}
