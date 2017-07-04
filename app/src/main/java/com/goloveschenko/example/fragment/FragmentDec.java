package com.goloveschenko.example.fragment;

import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.goloveschenko.example.R;
import com.goloveschenko.example.activity.MainActivity;

public class FragmentDec extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dec, container, false);

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
        button = (Button) view.findViewById(R.id.button8);
        button.setOnClickListener((MainActivity) getActivity());
        button = (Button) view.findViewById(R.id.button9);
        button.setOnClickListener((MainActivity) getActivity());
        button = (Button) view.findViewById(R.id.buttonDevider);
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

        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return view;
    }
}
