package com.example.weatherforecastapp.view;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.weatherforecastapp.R;
import com.example.weatherforecastapp.model.ForecastHistory;
import com.example.weatherforecastapp.util.DatabaseHelper;

import java.util.List;

public class HistoryActivity extends BaseActivity {
    private ListView listView;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        listView = findViewById(R.id.lvHistory);
        databaseHelper = new DatabaseHelper(this);
        refreshListView();
        setListViewItemClickHandler();
    }

    private void setListViewItemClickHandler() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete form history")
                    .setMessage("Do you want to delete this item from history?")
                    .setIcon(R.drawable.ic_delete)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        ForecastHistory forecastHistory = (ForecastHistory)
                                listView.getAdapter().getItem(position);
                        databaseHelper.deleteForecast(forecastHistory.getId());
                        refreshListView();
                    })
                    .setNegativeButton("No", (dialog, which) -> { })
                    .create()
                    .show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshListView();
    }

    private void refreshListView() {
        List<ForecastHistory> historyActivities = databaseHelper.loadAllForecastHistory();
        ArrayAdapter<ForecastHistory> forecastHistoryArrayAdapter =
                new ArrayAdapter<>(this, R.layout.history_row_item, R.id.tvHistoryItem, historyActivities);
        listView.setAdapter(forecastHistoryArrayAdapter);
    }

}