package com.goloveschenko.calculator.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.goloveschenko.calculator.R
import com.goloveschenko.calculator.activity.MainActivity

class FragmentOct : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_oct, container, false)

        var button: Button
        button = view.findViewById(R.id.button0) as Button
        button.setOnClickListener(activity as MainActivity)
        button = view.findViewById(R.id.button1) as Button
        button.setOnClickListener(activity as MainActivity)
        button = view.findViewById(R.id.button2) as Button
        button.setOnClickListener(activity as MainActivity)
        button = view.findViewById(R.id.button3) as Button
        button.setOnClickListener(activity as MainActivity)
        button = view.findViewById(R.id.button4) as Button
        button.setOnClickListener(activity as MainActivity)
        button = view.findViewById(R.id.button5) as Button
        button.setOnClickListener(activity as MainActivity)
        button = view.findViewById(R.id.button6) as Button
        button.setOnClickListener(activity as MainActivity)
        button = view.findViewById(R.id.button7) as Button
        button.setOnClickListener(activity as MainActivity)
        button = view.findViewById(R.id.buttonDelete) as Button
        button.setOnClickListener(activity as MainActivity)
        button = view.findViewById(R.id.buttonMultiply) as Button
        button.setOnClickListener(activity as MainActivity)
        button = view.findViewById(R.id.buttonDevide) as Button
        button.setOnClickListener(activity as MainActivity)
        button = view.findViewById(R.id.buttonPlus) as Button
        button.setOnClickListener(activity as MainActivity)
        button = view.findViewById(R.id.buttonMinus) as Button
        button.setOnClickListener(activity as MainActivity)
        button = view.findViewById(R.id.buttonPower) as Button
        button.setOnClickListener(activity as MainActivity)
        button = view.findViewById(R.id.buttonResult) as Button
        button.setOnClickListener(activity as MainActivity)

        return view
    }
}
