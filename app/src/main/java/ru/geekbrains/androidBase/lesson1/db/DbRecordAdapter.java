package ru.geekbrains.androidBase.lesson1.db;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.geekbrains.androidBase.lesson1.R;

public class DbRecordAdapter extends RecyclerView.Adapter<DbRecordAdapter.ViewHolder> {
    private final DataReader dataReader;

    public DbRecordAdapter(DataReader dataReader){
        this.dataReader = dataReader;
    }

    @NonNull
    @Override
    public DbRecordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.selected_city_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DbRecordAdapter.ViewHolder holder, int position) {
        holder.fillItemView(dataReader.getPosition(position));
    }

    @Override
    public int getItemCount() {
        return dataReader.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cityValue;
        TextView dateValue;
        TextView temperatureValue;
        TextView windValue;
        TextView pressureValue;

        DbRecord record;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cityValue = itemView.findViewById(R.id.ci_cityValue);
            dateValue = itemView.findViewById(R.id.ci_dateValue);
            temperatureValue = itemView.findViewById(R.id.ci_temperatureValue);
            windValue = itemView.findViewById(R.id.ci_windValue);
            pressureValue = itemView.findViewById(R.id.ci_pressureValue);
        }

        public void fillItemView(DbRecord record) {
            this.record = record;

            cityValue.setText(record.getCity());
            dateValue.setText(record.getDate());
            temperatureValue.setText(record.getTemperature());
            windValue.setText(record.getWind());
            pressureValue.setText(record.getPressure());
        }
    }
}
