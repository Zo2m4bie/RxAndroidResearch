package com.rxandroid.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rxandroid.DataManager;
import com.rxandroid.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yzubritskiy on 4/9/2017.
 */

public class FlatMapFragment extends Fragment {
    private TextView mResultFlatMapTextView;
    private Button mFlatMapButton;
    private DataManager mDataManager;

    public FlatMapFragment() {}


    public static FlatMapFragment newInstance() {
        FlatMapFragment fragment = new FlatMapFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flatmap, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mResultFlatMapTextView = (TextView) view.findViewById(R.id.result_flatmap_tv);
        mFlatMapButton = (Button) view.findViewById(R.id.do_flatmap_btn);
        mDataManager = new DataManager();
        mFlatMapButton.setOnClickListener(v -> doFlatMap());
    }

    private void doFlatMap(){
        Observable.just(mDataManager.getNumbers())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<List<Integer>, Observable<String>>() {
                    @Override
                    public Observable<String> apply(List<Integer> integers) throws Exception {
                        List<String> strings = new ArrayList<String>();
                        for(int i: integers){
                            strings.add("string->"+i);
                        }

                        return Observable.fromIterable(strings);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        mResultFlatMapTextView.setText(mResultFlatMapTextView.getText()+"\n"+s.toString()+",");
                    }
                });
    }
}
