package com.example.kyoui.rhythm;

import android.content.Intent;
import android.service.autofill.TextValueSanitizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.kyoui.kyoro.R;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private ArrayList<Integer> mTapTimeList1;
    private ArrayList<Integer> mTapTimeList2;

    TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultTextView = (TextView) findViewById(R.id.resultTextView);

        Bundle extras = getIntent().getExtras();
        mTapTimeList1 = extras.getIntegerArrayList("1");
        mTapTimeList2 = extras.getIntegerArrayList("2");

        for (int i = 0; i < mTapTimeList1.size(); i++) {
            Log.d("mTapTimeList1", i + ":" + mTapTimeList1.get(i));
        }

        for (int i = 0; i < mTapTimeList2.size(); i++) {
            Log.d("mTapTimeList2", i + ":" + mTapTimeList2.get(i));
        }

        double result = dtw();
        resultTextView.setText(result + "%");

    }

    private double dtw() {
        int len1 = mTapTimeList1.size() + 1;
        int len2 = mTapTimeList2.size() + 1;
        int inf = 999999;
        int sum1 = 0;
        int sum2 = 0;
        int count1 = 0;
        int count2 = 0;
        for (int i = 0; i < len1 - 1; i++) {
            if (mTapTimeList1.get(i) != 0) {
                sum1 += mTapTimeList1.get(i);
                count1++;
            }
        }
        for (int i = 0; i < len2 - 1; i++) {
            if (mTapTimeList2.get(i) != 0) {
                sum2 += mTapTimeList2.get(i);
                count2++;
            }
        }
        double ave1 = count1 != 0 ? sum1 / count1 : 0;
        double ave2 = count2 != 0 ? sum2 / count2 : 0;

        // 縦軸正規化
        double ratio = ave2 / ave1;
        ratio = ratio > 1.3 && ratio < 0.7 ? ratio : 1.0;

        double[][] matrix = new double[len1][len2];

        // 初期化
        for (int i = 0; i < len1; i++) {
            for (int j = 0; j < len2; j++) {
                matrix[i][j] = 0;
            }
        }

        //縦横をinfで埋める
        for (int i = 0; i < len2; i++) {
            matrix[0][i] = inf;
        }
        for (int i = 0; i < len1; i++) {
            matrix[i][0] = inf;
        }
        matrix[0][0] = 0;

        //costの計算
        for (int i = 0; i < len1 - 1; i++) {
            for (int j = 0; j < len2 - 1; j++) {
                double cost = Math.sqrt((double) mTapTimeList1.get(i) * ratio - (double) mTapTimeList2.get(j));
                double value = Math.min(matrix[i][j + 1], matrix[i + 1][j]);
                double min = Math.min(value, matrix[i][j]);
                matrix[i + 1][j + 1] = cost + min;
            }
        }
        double cost2 = matrix[len1 - 1][len2 - 1];
        return cost2;
    }
}
