package com.goloveschenko.example.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.goloveschenko.example.R;
import com.goloveschenko.example.activity.MainActivity;

public class FragmentOct extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oct, container, false);

        Button button;
        button = (Button) view.findViewById(R.id.button0);
        button.setOnClickListener((MainActivity) getActivity());
        button = (Button) view.findViewById(R.id.button1);
        button.setOnClickListener((MainActivity) getActivity());
        button = (Button) view.findViewById(R.id.button2);
        button.setOnClickListener((MainActivity) getActivity());
        button = (Button) view.findViewById(R.id.button3);
        button.setOnClickListener((MainActivity) getActivity());
        button = (Button) view.findViewById(R.id.button4);
        button.setOnClickListener((MainActivity) getActivity());
        button = (Button) view.findViewById(R.id.button5);
        button.setOnClickListener((MainActivity) getActivity());
        button = (Button) view.findViewById(R.id.button6);
        button.setOnClickListener((MainActivity) getActivity());
        button = (Button) view.findViewById(R.id.button7);
        button.setOnClickListener((MainActivity) getActivity());
        button = (Button) view.findViewById(R.id.buttonDelete);
        button.setOnClickListener((MainActivity) getActivity());
        button = (Button) view.findViewById(R.id.buttonMultiply);
        button.setOnClickListener((MainActivity) getActivity());
        button = (Button) view.findViewById(R.id.buttonDevide);
        button.setOnClickListener((MainActivity) getActivity());
        button = (Button) view.findViewById(R.id.buttonPlus);
        button.setOnClickListener((MainActivity) getActivity());
        button = (Button) view.findViewById(R.id.buttonMinus);
        button.setOnClickListener((MainActivity) getActivity());
        button = (Button) view.findViewById(R.id.buttonPower);
        button.setOnClickListener((MainActivity) getActivity());
        button = (Button) view.findViewById(R.id.buttonResult);
        button.setOnClickListener((MainActivity) getActivity());

        return view;
    }
}
