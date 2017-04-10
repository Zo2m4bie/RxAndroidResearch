package com.rxandroid.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rxandroid.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by yzubritskiy on 4/9/2017.
 */

public class ZipAndMergeFragment extends Fragment {

    private Button mZipButton;
    private Button mMergeButton;
    private LinearLayout mZipContainer;
    private LinearLayout mMergeContainer;

    public ZipAndMergeFragment() {}


    public static ZipAndMergeFragment newInstance() {
        ZipAndMergeFragment fragment = new ZipAndMergeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_zip, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mZipButton = (Button) view.findViewById(R.id.zip_btn);
        mMergeButton = (Button) view.findViewById(R.id.merge_btn);
        mZipContainer = (LinearLayout) view.findViewById(R.id.container_zip);
        mMergeContainer = (LinearLayout) view.findViewById(R.id.container_merge);
        mZipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doZip();
            }
        });
        mMergeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doMerge();
            }
        });
    }

    private void doMerge(){
        long currentTime = System.currentTimeMillis();
        Observable
                .merge(getYellowObservable(), getRedObservable())
                .take(10)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Circle>() {
                    @Override
                    public void accept(Circle circle) throws Exception {
                        if(circle!=null){
                            mMergeContainer.addView(circle.getCircle(getContext()));
                            TextView time = new TextView(getContext());
                            time.setText((System.currentTimeMillis()-currentTime)+"");
                            mMergeContainer.addView(time);
                        }

                    }
                });
    }



    private void doZip(){
        long currentTime = System.currentTimeMillis();
        Observable
                .zip(getYellowObservable(), getRedObservable(), new BiFunction<Circle, Circle, Circle>() {
                    @Override
                    public Circle apply(Circle circle, Circle circle2) throws Exception {
                        if(circle.getType().equals("yellow") && circle2.getType().equals("red")){
                            return new GreenCircle();
                        }
                        return null;
                    }
                })
                .take(10)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Circle>() {
                    @Override
                    public void accept(Circle circle) throws Exception {
                        if(circle!=null){
                            mZipContainer.addView(circle.getCircle(getContext()));
                            TextView time = new TextView(getContext());
                            time.setText((System.currentTimeMillis()-currentTime)+"");
                            mZipContainer.addView(time);
                        }
                    }
                });

    }

    private Observable<Circle> getYellowObservable(){
        return Observable.interval(250, TimeUnit.MILLISECONDS).map(new Function<Long, Circle>() {
            @Override
            public YellowCircle apply(Long aLong) throws Exception {
                return new YellowCircle();
            }
        });
    }

    private Observable<Circle> getRedObservable(){
        return Observable.interval(150, TimeUnit.MILLISECONDS).map(new Function<Long, Circle>() {
            @Override
            public RedCircle apply(Long aLong) throws Exception {
                return new RedCircle();
            }
        });
    }

    abstract class Circle{
        abstract ImageView getCircle(Context context);
        abstract String getType();
    }

    private class YellowCircle extends  Circle{

        public YellowCircle() {
        }

        @Override
        ImageView getCircle(Context context) {
            ImageView yellowCircle = new ImageView(context);
            yellowCircle.setImageDrawable(context.getResources().getDrawable(R.drawable.yellow_circle));
            return  yellowCircle;
        }

        @Override
        String getType() {
            return "yellow";
        }
    }

    private class RedCircle extends  Circle{

        public RedCircle() {
        }

        @Override
        ImageView getCircle(Context context) {
            ImageView redCircle = new ImageView(getContext());
            redCircle.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.red_circle));
            return  redCircle;
        }

        @Override
        String getType() {
            return "red";
        }
    }

    private class GreenCircle extends  Circle{

        public GreenCircle() {
        }

        @Override
        ImageView getCircle(Context context) {
            ImageView redCircle = new ImageView(getContext());
            redCircle.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.green_circle));
            return  redCircle;
        }

        @Override
        String getType() {
            return "red";
        }
    }
}
