package com.rxandroid.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.rxandroid.R;

/**
 *io(): Creates and returns a Scheduler intended for IO-bound work.
 * The implementation is backed by an Executor thread-pool that will grow as needed.
 *  This can be used for asynchronously performing blocking IO.
 *  Do not perform computational work on this scheduler. Use Schedulers.computation() instead,
 *  because io() is unbounded and if you schedule a thousand computational tasks on io() in parallel
 *  then each of those thousand tasks will each have their own thread and be competing for CPU incurring context switching costs.
 *  Use, for example, for interaction with the file system, interaction with databases or services on a different host
 */

public class IoFragment extends Fragment {



    public IoFragment() {}


    public static IoFragment newInstance() {
        IoFragment fragment = new IoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_io, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


}
