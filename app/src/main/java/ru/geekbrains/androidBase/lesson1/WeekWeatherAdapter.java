package ru.geekbrains.androidBase.lesson1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class WeekWeatherAdapter extends RecyclerView.Adapter<WeekWeatherAdapter.ViewHolder> {
    private String[] weekDays;

    public WeekWeatherAdapter(String[] data){
        weekDays = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.weather_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String temperatureValue = "+24 C";
        String windValue = "5 m/s (SW)";
        String pressureValue = "1000 hpa";

        holder.fillItemView(weekDays[position], temperatureValue, windValue, pressureValue);
    }

    @Override
    public int getItemCount() {
        return weekDays.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView weekDay;
        TextView temperatureValue;
        TextView windValue;
        TextView pressureValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            weekDay = itemView.findViewById(R.id.wi_weekDay);
            temperatureValue = itemView.findViewById(R.id.wi_temperatureValue);
            windValue = itemView.findViewById(R.id.wi_windValue);
            pressureValue = itemView.findViewById(R.id.wi_pressureValue);
        }

        void fillItemView(String weekDay, String temperatureValue, String windValue, String pressureValue){
            this.weekDay.setText(weekDay);
            this.temperatureValue.setText(temperatureValue);
            this.windValue.setText(windValue);
            this.pressureValue.setText(pressureValue);
        }
    }
}
