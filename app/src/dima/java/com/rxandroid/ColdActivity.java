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
import rx.subjects.AsyncSubject;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;

/**
 * Created by dima on 4/3/17.
 */

public class ColdActivity extends Activity {

    private PublishSubject mPublishSubject;
    private AsyncSubject mAsyncSubject;
    private BehaviorSubject mBehaviorSubject;
    private ReplaySubject mReplaySubject;

    private TextView mFirst, mSecond, mThird, mForth;
    private Scheduler mUiThread;
    private Scheduler mIoThread;
    private Observable<Integer> mSimpluObserver2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hot_activity);
        mUiThread = AndroidSchedulers.mainThread();
        mIoThread = Schedulers.newThread();
        mPublishSubject = PublishSubject.create();
        mAsyncSubject = AsyncSubject.create();
        mBehaviorSubject = BehaviorSubject.create();
        mReplaySubject = ReplaySubject.create();

        mFirst = (TextView) findViewById(R.id.result_first);
        mSecond = (TextView) findViewById(R.id.result_secind);
        mThird = (TextView) findViewById(R.id.result_third);
        mForth = (TextView) findViewById(R.id.result_fourth);
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

        findViewById(R.id.start_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int result = new Random().nextInt();
                Log.d("TEST", "In subject " + result);
                mPublishSubject.onNext(result);
                    mAsyncSubject.onNext(result);
                mBehaviorSubject.onNext(result);
                mReplaySubject.onNext(result);
            }
        });
        findViewById(R.id.start_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPublishSubject.subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer o) {
                        Log.d("TEST", "mPublishSubject receive " + o);
                        mFirst.setText(String.valueOf(o));
                    }
                });

            }
        });
        findViewById(R.id.subject_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAsyncSubject.subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer o) {
                        Log.d("TEST", "mAsyncSubject receive " + o);
                        mSecond.setText(String.valueOf(o));
                    }
                });

            }
        });
        findViewById(R.id.start_third).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBehaviorSubject.subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer o) {
                        Log.d("TEST", "mBehaviorSubject receive " + o);
                        mThird.setText(String.valueOf(o));
                    }
                });

            }
        });
        findViewById(R.id.start_forth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mReplaySubject.subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer o) {
                        Log.d("TEST", "mReplaySubject receive " + o);
                        mForth.setText(String.valueOf(o));
                    }
                });

            }
        });
    }
}
