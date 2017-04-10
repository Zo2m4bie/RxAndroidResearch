package com.rxandroid;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yzubritskiy on 4/3/2017.
 */
public class DataManager {
//    private final StringGenerator stringGenerator;
//    private final NumberGenerator numberGenerator;
//    private final Executor jobExecutor;
    private final List<Integer> numbers;
//
//    public DataManager(StringGenerator stringGenerator,
//                       NumberGenerator numberGenerator, Executor jobExecutor) {
//        this.stringGenerator = stringGenerator;
//        this.numberGenerator = numberGenerator;
//        this.jobExecutor = jobExecutor;
//        this.numbers = new ArrayList<>(Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10));
//
//    }

    public DataManager() {
        this.numbers = new ArrayList<>(Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10));

    }

    public Observable<Integer> numbersObservable() {
        Log.d("TAG_", "numbersObservable");

        return Observable.fromIterable( numbers);
    }

//    public Observable<Long> numbers(int upUntil) {
//        return Observable.fromIterable(numberGenerator.numbers(upUntil));
//    }

    public Flowable<Long> milliseconds(int upUntil) {
        return Flowable.interval(0, 1, TimeUnit.MILLISECONDS).take(upUntil);
    }

    public Observable<Integer> squareOfAsync(int number) {
        return Observable.just(number * number)
                .subscribeOn(Schedulers.computation());
    }
//
//    public Observable<String> elements() {
//        return Observable.fromIterable(stringGenerator.randomStringList());
//    }

//    public Observable<String> newElement() {
//        return Observable
//                .just(stringGenerator.nextString())
//                .map((string -> "RandomItem" + string));
//    }

    public List<Integer> getNumbers() {
        return numbers;
    }
}
