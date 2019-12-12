package ru.geekbrains.androidBase.lesson1;


import android.os.Bundle;

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
    public void onResume() {
        super.onResume();
        WeatherProvider.getInstance().addListener(this);

        final AppSettingsSingleton presenter = AppSettingsSingleton.getInstance();
        LinearLayout windInfo = getActivity().findViewById(R.id.windInfoLinearLayout);
        LinearLayout pressureInfo = getActivity().findViewById(R.id.pressureInfoLinearLayout);

        windInfo.setVisibility(presenter.isWindSwitchChecked() ? View.VISIBLE : View.GONE);
        pressureInfo.setVisibility(presenter.isPressureSwitchChecked() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void updateWeather(WeatherModel model) {
        ((TextView) getActivity().findViewById(R.id.windInfo)).setText(String.format("%d m/s", model.getWind().getSpeed()));
        ((TextView) getActivity().findViewById(R.id.pressureInfo)).setText(String.format("%d hpa", model.getMain().getPressure()));
    }

    @Override
    public void onPause() {
        WeatherProvider.getInstance().removeListener(this);
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
