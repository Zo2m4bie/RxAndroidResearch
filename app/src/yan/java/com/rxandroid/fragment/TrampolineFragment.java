package com.rxandroid.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rxandroid.R;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * trampoline(): Creates and returns a Scheduler that queues work on the current thread
 * to be executed after the current work completes.
 */

public class TrampolineFragment extends Fragment {
    private StringBuilder mBuilder = new StringBuilder();
    private Button mExample2;
    private Button mExample3;
    private TextView mResultTextView;
    private TextView mResultTextView2;

    public TrampolineFragment() {}


    public static TrampolineFragment newInstance() {
        TrampolineFragment fragment = new TrampolineFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tempoline, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mExample2 = (Button) view.findViewById(R.id.example_trampoline);
        mExample3 = (Button) view.findViewById(R.id.example_trampoline3);
        mResultTextView = (TextView) view.findViewById(R.id.result_trampoline_tv);
        mResultTextView2 = (TextView) view.findViewById(R.id.result_trampoline_tv2);
        mExample2.setOnClickListener(v -> example2());
        mExample3.setOnClickListener(v -> example3());
    }

    private void example(){
        mBuilder = new StringBuilder();
        Scheduler schedulerTrampoline = Schedulers.trampoline();
        Scheduler schedulerNewThread = Schedulers.newThread();

        Observable
                .create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> observableEmitter) throws Exception {
                        mBuilder.append("Observable  thread: " + Thread.currentThread().getName()+"\n");
                        observableEmitter.onNext(1);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        mBuilder.append("Operator1  thread: " + Thread.currentThread().getName()+"\n");
                        return String.valueOf(integer);
                    }
                })
                .flatMap(new Function<String, Observable<String>>() {
                    @Override
                    public Observable<String> apply(String s) throws Exception {
                        mBuilder.append("Operator2  thread: " + Thread.currentThread().getName()+"\n");
                        return Observable.just(s)
                                .subscribeOn(schedulerNewThread);
                    }
                })
                .flatMap(new Function<String, Observable<String>>() {
                    @Override
                    public Observable<String> apply(String s) throws Exception {
                        mBuilder.append("Operator3  thread: " + Thread.currentThread().getName()+"\n");
                        return Observable.just(s)
                                .subscribeOn(schedulerTrampoline);
                    }
                })
                .flatMap(new Function<String, Observable<String>>() {
                    @Override
                    public Observable<String> apply(String s) throws Exception {
                        mBuilder.append("Operator4  thread: " + Thread.currentThread().getName()+"\n");
                        return Observable.just(s)
                                .observeOn(schedulerTrampoline)
                                .subscribeOn(schedulerNewThread);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread() )
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        mBuilder.append("Subscriber  thread: " + Thread.currentThread().getName()+"\n");
                        Log.d("TAG", mBuilder.toString());
                        mResultTextView.setText(mBuilder);
                    }
                });
    }

    private void example2(){
        mBuilder = new StringBuilder();
        Consumer<Integer> onNext = new Consumer<Integer>() {
            @Override public void accept(Integer integer)  throws Exception{
                mBuilder.append("integer =  " + integer + "\n");
            }
        };
        Observable.just(2, 4, 6, 8, 10)
                .subscribeOn(Schedulers.io())
                .subscribe(onNext);
        Observable.just(1, 3, 5, 7, 9)
                .subscribeOn(Schedulers.io())
                .subscribe(onNext);

        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                mResultTextView.setText(mBuilder);
            }
        };
        handler.postDelayed(r, 1000);

    }
    private void example3(){
        mBuilder = new StringBuilder();
        Consumer<Integer> onNext = new Consumer<Integer>() {
            @Override public void accept(Integer integer)  throws Exception{
                mBuilder.append("integer =  " + integer + "\n");
            }
        };
        Observable.just(2, 4, 6, 8, 10)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(onNext);
        Observable.just(1, 3, 5, 7, 9)
                .subscribeOn(Schedulers.trampoline())
                .subscribe(onNext);

        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                mResultTextView2.setText(mBuilder);
            }
        };
        handler.postDelayed(r, 1000);
    }
}
