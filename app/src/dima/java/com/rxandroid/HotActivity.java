package com.rxandroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Created by dima on 4/3/17.
 */

public class HotActivity extends Activity {

    private Observable<Integer> mSimpluObserver;
    private Observable<Integer> mSimpluObserver2;

    private TextView mFirst, mSecond;
    private Scheduler mUiThread;
    private Scheduler mIoThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cold_activity);
        mUiThread = AndroidSchedulers.mainThread();
        mIoThread = Schedulers.newThread();
        mSimpluObserver = Observable.just(1);
        mSimpluObserver2 = Observable.fromCallable(new Func0<Integer>() {
            @Override
            public Integer call() {
                Log.d("TEST", "Start second " + this.toString());
                synchronized (this){
                    try {
                        wait(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                int result = new Random().nextInt();
                Log.d("TEST", "Start second end " + result);
                return result;
            }
        }).subscribeOn(mIoThread).observeOn(mUiThread);
        mFirst = (TextView) findViewById(R.id.result_first);
        mSecond = (TextView) findViewById(R.id.result_secind);
        findViewById(R.id.start_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSimpluObserver.subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        mFirst.setText(String.valueOf(integer));
                    }
                });
            }
        });
        findViewById(R.id.start_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSimpluObserver2.subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d("TEST", "Start second subscribe " + this.toString());
                        mSecond.setText(String.valueOf(integer));
                    }
                });

            }
        });
    }
}
