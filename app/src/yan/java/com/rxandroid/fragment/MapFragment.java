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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yzubritskiy on 4/9/2017.
 */

public class MapFragment extends Fragment {

    public MapFragment() {}
    private TextView mResultMapTextView;
    private Button mMapButton;
    private DataManager mDataManager;

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mResultMapTextView = (TextView) view.findViewById(R.id.result_map_tv);
        mMapButton = (Button) view.findViewById(R.id.do_map_btn);
        mDataManager = new DataManager();
        mMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doMap();
            }
        });
    }

    private void doMap(){
       Observable.just(mDataManager.getNumbers())
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .map(new Function<List<Integer>, List<String>>() {
                   @Override
                   public List<String> apply(List<Integer> integers) throws Exception {
                       List<String> strings = new ArrayList<String>();
                       for(int i: integers){
                           strings.add("string->"+i);
                       }
                       return strings;
                   }
               })
               .subscribe(new Consumer<List<String>>() {
                   @Override
                   public void accept(List<String> strings) throws Exception {
                       mResultMapTextView.setText(mResultMapTextView.getText()+strings.toString()+",\n");

                   }
               });
    }
}
