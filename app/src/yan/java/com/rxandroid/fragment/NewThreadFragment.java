package com.rxandroid.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rxandroid.DataManager;
import com.rxandroid.JobExecutor;
import com.rxandroid.R;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * newThread(): Creates and returns a Scheduler that
 * creates a new Thread for each unit of work.
 */

public class NewThreadFragment extends Fragment {

    private StringBuilder mBuilder = new StringBuilder();
    private Button mExample1;
    private TextView mResultTextView;

    public NewThreadFragment() {}


    public static NewThreadFragment newInstance() {
        NewThreadFragment fragment = new NewThreadFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_thread, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mExample1 = (Button) view.findViewById(R.id.example1);
        mResultTextView = (TextView) view.findViewById(R.id.result_tv);
        mExample1.setOnClickListener(v -> example1());

        Button example2Button = (Button) view.findViewById(R.id.example2);
        example2Button.setOnClickListener(v -> example7());

    }


    /**
     * What happens if I have multiple subscribeOn declarations?

     In case of multiple declaration of subscribeOns, only the first
     declaration takes preference. This is because the Observable computation can be executed only on one thread.
     So, in this example app will crash by ViewRootImpl$CalledFromWrongThreadException
     */
    private void example1(){
        mBuilder = new StringBuilder();
        Observable.fromCallable(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        mBuilder.append("Observable thread: " + Thread.currentThread().getName()+"\n");
                        return 1;
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        mBuilder.append("Operator  thread: " + Thread.currentThread().getName()+"\n");
                        return String.valueOf(integer);
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        mBuilder.append("Subscriber  thread: " + Thread.currentThread().getName()+"\n");
                        mResultTextView.setText(mBuilder);
                    }
                });
    }


    private void example7(){
        mBuilder = new StringBuilder();
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
                                .subscribeOn(Schedulers.newThread());
                    }
                })
                .flatMap(new Function<String, Observable<String>>() {
                    @Override
                    public Observable<String> apply(String s) throws Exception {
                        mBuilder.append("Operator3  thread: " + Thread.currentThread().getName()+"\n");
                        return Observable.just(s)
                                .subscribeOn(Schedulers.newThread());
                    }
                })
                .flatMap(new Function<String, Observable<String>>() {
                    @Override
                    public Observable<String> apply(String s) throws Exception {
                        mBuilder.append("Operator4  thread: " + Thread.currentThread().getName()+"\n");
                        return Observable.just(s)
                                .subscribeOn(Schedulers.newThread());
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .flatMap(new Function<String, Observable<String>>() {
                    @Override
                    public Observable<String> apply(String s) throws Exception {
                        mBuilder.append("Operator5  thread: " + Thread.currentThread().getName()+"\n");
                        return Observable.just(s)
                                .subscribeOn(Schedulers.newThread());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        mBuilder.append("Subscriber  thread: " + Thread.currentThread().getName()+"\n");
                        Log.d("TAG", mBuilder.toString());
                        mResultTextView.setText(mBuilder);
                    }
                });
    }
}
