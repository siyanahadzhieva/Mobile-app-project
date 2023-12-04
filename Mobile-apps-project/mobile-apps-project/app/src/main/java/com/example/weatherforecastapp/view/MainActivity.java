package com.example.weatherforecastapp.view;

import static com.example.weatherforecastapp.util.ForecastFactory.createForecastFromApiResponse;
import static com.example.weatherforecastapp.util.ForecastHistoryFactory.createForecastHistory;
import static com.example.weatherforecastapp.util.ForecastHttpClient.getErrorMessage;
import static com.example.weatherforecastapp.util.ForecastHttpClient.getForecast;
import static com.example.weatherforecastapp.util.ImageLoader.loadImage;
import static cz.msebera.android.httpclient.util.TextUtils.isBlank;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherforecastapp.R;
import com.example.weatherforecastapp.model.Forecast;
import com.example.weatherforecastapp.model.ForecastHistory;
import com.example.weatherforecastapp.model.ForecastType;
import com.example.weatherforecastapp.util.DatabaseHelper;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import lombok.SneakyThrows;

public class MainActivity extends BaseActivity {
    private EditText cityNameEditText;
    private Button searchForecastButton;
    private ImageView forecastIconImageView;
    private TextView temperatureTextView;
    private TextView weatherDescriptionTextView;
    private ProgressBar progressBar;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViewItems();
        handleSearchForecastButtonClick();
        hideProgressBar();
        databaseHelper = new DatabaseHelper(this);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void handleSearchForecastButtonClick() {
        searchForecastButton.setOnClickListener(v -> {
            String cityName = cityNameEditText.getText().toString();
            if (isBlank(cityName)) {
                Toast.makeText(this, "City name is required", Toast.LENGTH_LONG)
                        .show();
                return;
            }
            showProgressBar();
            getForecast(cityName, new ForecastJsonResponseHandler());
        });
    }

    private void bindViewItems() {
        cityNameEditText = findViewById(R.id.etCityName);
        searchForecastButton = findViewById(R.id.btnSearch);
        forecastIconImageView = findViewById(R.id.ivListWeatherIcon);
        temperatureTextView = findViewById(R.id.tvTemperature);
        progressBar = findViewById(R.id.progressBar1);
        weatherDescriptionTextView = findViewById(R.id.tvWeatherDescription);
    }

    private void updateTemperatureTextValue(int temperature) {
        temperatureTextView.setText(String.format("%d°C", temperature));
    }

    private void updateForecastIcon(String iconName) {
        Bitmap bitmap = loadImage(iconName);
        forecastIconImageView.setImageBitmap(bitmap);
    }

    private void updateWeatherDescription(String weatherDescription) {
        weatherDescriptionTextView.setText(weatherDescription);
    }

    private void saveSearchHistory() {
        String cityName = cityNameEditText.getText().toString();
        ForecastHistory forecastHistory = createForecastHistory(cityName, ForecastType.TODAY);
        databaseHelper.saveForecast(forecastHistory);
    }

    private class ForecastJsonResponseHandler extends JsonHttpResponseHandler {
        @SneakyThrows
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Forecast forecast = createForecastFromApiResponse(response);
            updateTemperatureTextValue(forecast.getTemperature());
            updateForecastIcon(forecast.getIcon());
            updateWeatherDescription(forecast.getWeatherDescription());
            hideProgressBar();
            saveSearchHistory();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Log.e("CurrentDayForecastFailure", throwable.getMessage(), throwable);
            Log.d("CurrentDayForecastResponse", errorResponse.toString());
            Toast.makeText(MainActivity.this,
                    getErrorMessage(statusCode), Toast.LENGTH_LONG)
                    .show();
            hideProgressBar();
        }
    }
}