package com.example.weatherforecastapp.view;

import static com.example.weatherforecastapp.util.DateFormatUtil.formatDate;
import static com.example.weatherforecastapp.util.ImageLoader.loadImage;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.weatherforecastapp.R;
import com.example.weatherforecastapp.model.MultiDayForecast;

import java.util.List;

public class WeatherForecastListAdapter extends ArrayAdapter<MultiDayForecast> {
    public WeatherForecastListAdapter(@NonNull Context context, int resource, @NonNull List<MultiDayForecast> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
        if(convertView == null) {
            row = inflater.inflate(R.layout.row_item, null, true);
        }

        ImageView weatherIcon =  row.findViewById(R.id.ivListItemWeatherIcon);
        TextView temperatureTextView = row.findViewById(R.id.tvListItemTemperature);
        TextView dateTextView = row.findViewById(R.id.tvListItemDate);
        TextView weatherDescriptionTextView = row.findViewById(R.id.tvListItemWeatherDescription);
        MultiDayForecast multiDayForecast = getItem(position);

        temperatureTextView.setText(multiDayForecast.getTemperature() + "°C");
        dateTextView.setText(formatDate(multiDayForecast.getDate()));
        weatherIcon.setImageBitmap(loadImage(multiDayForecast.getIcon()));
        weatherDescriptionTextView.setText(multiDayForecast.getWeatherDescription());
        return  row;
    }

}
