package com.example.lenovo.selphies;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * This class redirect the user to the Google Map class
 */

public class AroundyouFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_aroundyou, container, false);
        final Activity activity = getActivity();

        startActivity(new Intent(getContext(), MapsActivity.class));

        return view;
    }
}
