package com.example.mviexample;

import android.app.Application;
import android.content.Context;
import com.example.mviexample.dependencyinjection.DependencyInjection;

public class SampleApplication  extends Application {


    protected DependencyInjection dependencyInjection = new DependencyInjection();

    public static DependencyInjection getDependencyInjection(Context context) {
        return ((SampleApplication) context.getApplicationContext()).dependencyInjection;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


}
