package ru.geekbrains.androidBase.lesson1;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdditionalInfoFragment extends Fragment {


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

        final CitySelectionSingleton presenter = CitySelectionSingleton.getInstance();
        LinearLayout windInfo = getActivity().findViewById(R.id.windInfoLinearLayout);
        LinearLayout pressureInfo = getActivity().findViewById(R.id.pressureInfoLinearLayout);

        windInfo.setVisibility(presenter.isWindSwitchChecked() ? View.VISIBLE : View.GONE);
        pressureInfo.setVisibility(presenter.isPressureSwitchChecked() ? View.VISIBLE : View.GONE);
    }
}
