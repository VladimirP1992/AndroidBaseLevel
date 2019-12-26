package ru.geekbrains.androidBase.lesson1;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.geekbrains.androidBase.lesson1.model.WeatherModel;
import ru.geekbrains.androidBase.lesson1.model.WeatherProvider;
import ru.geekbrains.androidBase.lesson1.model.WeatherProviderListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdditionalInfoFragment extends Fragment implements WeatherProviderListener {

    private TextView windInfoTextView;
    private TextView pressureInfoTextView;
    private LinearLayout windInfoLinearLayout;
    private LinearLayout pressureInfoLinearLayout;

    public AdditionalInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_additional_info, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews(){
        windInfoTextView =  getActivity().findViewById(R.id.windInfo);
        pressureInfoTextView = getActivity().findViewById(R.id.pressureInfo);
        windInfoLinearLayout = getActivity().findViewById(R.id.windInfoLinearLayout);
        pressureInfoLinearLayout = getActivity().findViewById(R.id.pressureInfoLinearLayout);
    }

    @Override
    public void onResume() {
        super.onResume();
        WeatherProvider.getInstance(getActivity()).addListener(this);

        final AppSettingsSingleton presenter = AppSettingsSingleton.getInstance();

        windInfoLinearLayout.setVisibility(presenter.isWindSwitchChecked() ? View.VISIBLE : View.GONE);
        pressureInfoLinearLayout.setVisibility(presenter.isPressureSwitchChecked() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void updateWeather(WeatherModel model) {
        double wind = model.getWind().getSpeed();
        double pressure = WeatherProvider.hectopascalTOMmOfMercury(model.getMain().getPressure());

        windInfoTextView.setText(String.format("%.1f %s", wind, getResources().getString(R.string.meters_per_second)));
        pressureInfoTextView.setText(String.format("%.1f %s", pressure, getResources().getString(R.string.millimeters_of_mercury)));
    }

    @Override
    public void onPause() {
        WeatherProvider.getInstance(getActivity()).removeListener(this);
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
