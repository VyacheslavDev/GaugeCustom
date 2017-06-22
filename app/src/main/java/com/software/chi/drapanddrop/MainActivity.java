package com.software.chi.drapanddrop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.SeekBar;

import com.github.anastr.speedviewlib.ImageSpeedometer;

import static com.software.chi.drapanddrop.R.id.recyclerView;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ImageSpeedometer mSpeedView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        SeekBar mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        final Gauge mGauge = (Gauge) findViewById(R.id.kek);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mGauge.maxValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mRecyclerView = (RecyclerView) findViewById(recyclerView);
//        mSpeedView = (ImageSpeedometer) findViewById(R.id.speedView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(recyclerViewAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.setAdapter(recyclerViewAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
//
//        new Thread() {
//            public void run() {
//                for (int i = 0; i < 100; i++) {
//                    try {
//                        final int finalI = i;
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mSpeedView.speedTo(finalI);
//                            }
//                        });
//                        Thread.sleep(50);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();
    }
}
