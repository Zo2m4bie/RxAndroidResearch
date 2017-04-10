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

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yzubritskiy on 4/9/2017.
 */

public class ConcatMapFragment extends Fragment {

    private TextView mResultFlatMapTextView;
    private TextView mResultConcatMapTextView;
    private Button mFlatMapButton;
    private Button mConcatMapButton;
    private DataManager mDataManager;

    public ConcatMapFragment() {}


    public static ConcatMapFragment newInstance() {
        ConcatMapFragment fragment = new ConcatMapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_concatmap, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mResultFlatMapTextView = (TextView) view.findViewById(R.id.result_faltmap_tv);
        mResultConcatMapTextView = (TextView) view.findViewById(R.id.result_concatmap_tv);
        mFlatMapButton = (Button) view.findViewById(R.id.do_flatmap_btn);
        mConcatMapButton = (Button) view.findViewById(R.id.do_concatmap_btn);
        view.findViewById(R.id.link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriUrl = Uri.parse("https://fernandocejas.com/2015/01/11/rxjava-observable-tranformation-concatmap-vs-flatmap/");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });

        mDataManager = new DataManager();
        Log.d("TAG_", "onViewCreated");
        JobExecutor executor = JobExecutor.getInstance();

        mFlatMapButton.setOnClickListener(v -> doFlatMap(executor));
        mConcatMapButton.setOnClickListener(v -> doConcatMap(executor));
    }

    private void doFlatMap(JobExecutor executor){
        StringBuilder sourceBuilder = new StringBuilder("source -> ");
        emit(mDataManager.getNumbers())
                .flatMap(new Function<Integer, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> apply(Integer integer) throws Exception {
                        sourceBuilder.append(integer+", ");
                        return Observable.just(integer*integer)
                                 .subscribeOn(Schedulers.io());

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .map(new Function<List<Integer>, String>() {
                    @Override
                    public String apply(List<Integer> integers) throws Exception {
                        return listNumbersToString(integers);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String result) throws Exception {
                        mResultFlatMapTextView.setText(sourceBuilder+"\n"+result);
                    }
                });

    }

    private void doConcatMap(JobExecutor executor){
        StringBuilder sourceBuilder = new StringBuilder("source -> ");
        emit(mDataManager.getNumbers())
                .concatMap(new Function<Integer, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> apply(Integer integer) throws Exception {
                        sourceBuilder.append(integer+", ");
                        return Observable.just(integer*integer)
                                .subscribeOn(Schedulers.io());

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .map(new Function<List<Integer>, String>() {
                    @Override
                    public String apply(List<Integer> integers) throws Exception {
                        return listNumbersToString(integers);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String result) throws Exception {
                        mResultConcatMapTextView.setText(sourceBuilder+"\n"+result);
                    }
                });
    }

    private Observable<Integer> emit(List<Integer> integers){
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for(Integer integer: integers){
                    e.onNext(integer);
                }
                e.onComplete();
            }
        });
    }

    private String listNumbersToString(List<Integer> integers){
        StringBuilder builder = new StringBuilder("result -> ");
        for(Integer integer: integers){
            builder.append(integer+", ");
        }
        return builder.toString();
    }
}
