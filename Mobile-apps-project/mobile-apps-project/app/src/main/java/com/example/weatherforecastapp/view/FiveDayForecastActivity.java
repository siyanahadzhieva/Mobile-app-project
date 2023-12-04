package com.example.weatherforecastapp.view;

import static com.example.weatherforecastapp.util.ForecastFactory.createForecastsFromApiResponse;
import static com.example.weatherforecastapp.util.ForecastHttpClient.getErrorMessage;
import static com.example.weatherforecastapp.util.ForecastHttpClient.getFiveDayForecast;
import static cz.msebera.android.httpclient.util.TextUtils.isBlank;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.weatherforecastapp.R;
import com.example.weatherforecastapp.model.ForecastHistory;
import com.example.weatherforecastapp.model.ForecastType;
import com.example.weatherforecastapp.model.MultiDayForecast;
import com.example.weatherforecastapp.util.DatabaseHelper;
import com.example.weatherforecastapp.util.ForecastHistoryFactory;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import lombok.SneakyThrows;

public class FiveDayForecastActivity extends BaseActivity {
    private EditText cityNameEditText;
    private Button searchButton;
    private ProgressBar progressBar;
    private ListView listView;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five_day_forecast);
        bindViewItems();
        handleSearchButtonClick();
        hideProgressBar();
        databaseHelper = new DatabaseHelper(this);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void handleSearchButtonClick() {
        searchButton.setOnClickListener(v -> {
            String cityName = cityNameEditText.getText().toString();
            if (isBlank(cityName)) {
                Toast.makeText(this, "City name is required!", Toast.LENGTH_LONG)
                        .show();
                return;
            }
            showProgressBar();
            getFiveDayForecast(cityName, new FiveDayForecastJsonResponseHandler());
        });
    }

    private void bindViewItems() {
        cityNameEditText = findViewById(R.id.etCityNameFiveDayForecast);
        searchButton = findViewById(R.id.btnFiveDayForecastSearch);
        progressBar = findViewById(R.id.progressBar);
        listView = findViewById(R.id.lvFiveDayForecast);
    }

    private void saveCurrentSearchIntoHistory() {
        String cityName = cityNameEditText.getText().toString();
        ForecastHistory forecastHistory = ForecastHistoryFactory.createForecastHistory(cityName, ForecastType.FIVE_DAY);
        databaseHelper.saveForecast(forecastHistory);
    }

    private class FiveDayForecastJsonResponseHandler extends JsonHttpResponseHandler {
        @SneakyThrows
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            List<MultiDayForecast> forecasts = createForecastsFromApiResponse(response);
            setListViewAdapter(forecasts);
            hideProgressBar();
            saveCurrentSearchIntoHistory();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            Log.e("FiveDayForecastError", throwable.getMessage(), throwable);
            Log.d("FiveDayForecastResponse", errorResponse.toString());
            hideProgressBar();
            Toast.makeText(FiveDayForecastActivity.this,
                    getErrorMessage(statusCode), Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void setListViewAdapter(List<MultiDayForecast> forecasts) {
        ArrayAdapter<MultiDayForecast> multiDayForecastArrayAdapter =
                new WeatherForecastListAdapter(this, R.id.list_item, forecasts);
        listView.setAdapter(multiDayForecastArrayAdapter);
    }
}