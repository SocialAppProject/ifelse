package com.socialappproject.ifelse;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class StatisticFragment extends Fragment {
    private static final String TAG = "StatisticFragment";

    private DatabaseReference userRef = DatabaseManager.databaseReference.child("USER");


    public StatisticFragment() {
        // Required empty public constructor
    }

    public static StatisticFragment newInstance() {
        StatisticFragment fragment = new StatisticFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);

        BarChart chart = (BarChart) view.findViewById(R.id.chart);



        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);

        chart.setDrawValuesForWholeStack(false);
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);

        chart.setHighlightEnabled(false);
        chart.setDrawGridBackground(true);
        chart.setDescription("");

        XAxis x = chart.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yLabels_left = chart.getAxisLeft();
        yLabels_left.setDrawLabels(false);
        yLabels_left.setAxisMinValue(0);
        yLabels_left.setAxisMaxValue(100);
        YAxis yLabels_right = chart.getAxisRight();
        yLabels_right.setDrawLabels(false);

        Legend legend = chart.getLegend();
        legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);

        chart.animateXY(2000, 2000);
        chart.invalidate();

        return view;
    }

    private ArrayList<BarDataSet> getDataSet() {

        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        ArrayList<BarEntry> valueSet0 = new ArrayList<>();
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();

        //int women_sum = 0, men_sum = 0;

        final BarEntry v0e0 = new BarEntry(0, 0); // 10대 미만 여
        final BarEntry v0e1 = new BarEntry(0, 1); // 10대 여
        final BarEntry v0e2 = new BarEntry(0, 2); // 20대 여
        final BarEntry v0e3 = new BarEntry(0, 3); // 30대 여
        final BarEntry v0e4 = new BarEntry(0, 4); // 40대 여
        final BarEntry v0e5 = new BarEntry(0, 5); // 50대 여
        final BarEntry v0e6 = new BarEntry(0, 6); // 60대 이상 여

        final BarEntry v1e0 = new BarEntry(0, 0); // 10대 미만 남
        final BarEntry v1e1 = new BarEntry(0, 1); // 10대 남
        final BarEntry v1e2 = new BarEntry(0, 2); // 20대 남
        final BarEntry v1e3 = new BarEntry(0, 3); // 30대 남
        final BarEntry v1e4 = new BarEntry(0, 4); // 40대 남
        final BarEntry v1e5 = new BarEntry(0, 5); // 50대 남
        final BarEntry v1e6 = new BarEntry(0, 6); // 60대 이상 남


        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    String snaped_gender = postSnapshot.child("gender").getValue().toString();
                    int snaped_old = Integer.parseInt((postSnapshot.child("old").getValue().toString()));

                    switch (snaped_gender) {
                        case "0": {
                            if (snaped_old < 10) {
                                v0e0.setVal(v0e0.getVal() + 1);
                            } else if(snaped_old < 20) {
                                v0e1.setVal(v0e1.getVal() + 1);
                            } else if(snaped_old < 30) {
                                v0e2.setVal(v0e2.getVal() + 1);
                            } else if(snaped_old < 40) {
                                v0e3.setVal(v0e3.getVal() + 1);
                            } else if(snaped_old < 50) {
                                v0e4.setVal(v0e4.getVal() + 1);
                            } else if(snaped_old < 60) {
                                v0e5.setVal(v0e5.getVal() + 1);
                            } else {
                                v0e6.setVal(v0e6.getVal() + 1);
                            }
                            break;
                        }
                        case "1": {
                            if (snaped_old < 10) {
                                v1e0.setVal(v1e0.getVal() + 1);
                            } else if(snaped_old < 20) {
                                v1e1.setVal(v1e1.getVal() + 1);
                            } else if(snaped_old < 30) {
                                v1e2.setVal(v1e2.getVal() + 1);
                            } else if(snaped_old < 40) {
                                v1e3.setVal(v1e3.getVal() + 1);
                            } else if(snaped_old < 50) {
                                v1e4.setVal(v1e4.getVal() + 1);
                            } else if(snaped_old < 60) {
                                v1e5.setVal(v1e5.getVal() + 1);
                            } else {
                                v1e6.setVal(v1e6.getVal() + 1);
                            }
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                }
                float women_count = v0e0.getVal() + v0e1.getVal() + v0e2.getVal() + v0e3.getVal() + v0e4.getVal() + v0e5.getVal() +v0e6.getVal();
                float men_count = v1e0.getVal() + v1e1.getVal() + v1e2.getVal() + v1e3.getVal() + v1e4.getVal() + v1e5.getVal() +v1e6.getVal();

                v0e0.setVal(v0e0.getVal()/women_count*100);
                v0e1.setVal(v0e1.getVal()/women_count*100);
                v0e2.setVal(v0e2.getVal()/women_count*100);
                v0e3.setVal(v0e3.getVal()/women_count*100);
                v0e4.setVal(v0e4.getVal()/women_count*100);
                v0e5.setVal(v0e5.getVal()/women_count*100);
                v0e6.setVal(v0e6.getVal()/women_count*100);

                v1e0.setVal(v1e0.getVal()/men_count*100);
                v1e1.setVal(v1e1.getVal()/men_count*100);
                v1e2.setVal(v1e2.getVal()/men_count*100);
                v1e3.setVal(v1e3.getVal()/men_count*100);
                v1e4.setVal(v1e4.getVal()/men_count*100);
                v1e5.setVal(v1e5.getVal()/men_count*100);
                v1e6.setVal(v1e6.getVal()/men_count*100);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });

        valueSet0.add(v0e0);
        valueSet0.add(v0e1);
        valueSet0.add(v0e2);
        valueSet0.add(v0e3);
        valueSet0.add(v0e4);
        valueSet0.add(v0e5);
        valueSet0.add(v0e6);

        valueSet1.add(v1e0);
        valueSet1.add(v1e1);
        valueSet1.add(v1e2);
        valueSet1.add(v1e3);
        valueSet1.add(v1e4);
        valueSet1.add(v1e5);
        valueSet1.add(v1e6);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "남성");
        barDataSet1.setDrawValues(false);
        barDataSet1.setColor(ContextCompat.getColor(this.getContext(), R.color.men));
        BarDataSet barDataSet2 = new BarDataSet(valueSet0, "여성");
        barDataSet2.setDrawValues(false);
        barDataSet2.setColor(ContextCompat.getColor(this.getContext(), R.color.women));

        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);

        return dataSets;

    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("10대 미만");
        xAxis.add("10대");
        xAxis.add("20대");
        xAxis.add("30대");
        xAxis.add("40대");
        xAxis.add("50대");
        xAxis.add("60대 이상");
        return xAxis;
    }

}
