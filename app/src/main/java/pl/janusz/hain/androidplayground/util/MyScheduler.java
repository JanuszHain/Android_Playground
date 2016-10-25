package pl.janusz.hain.androidplayground.util;

import java.util.concurrent.Executors;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class MyScheduler {

    private static Scheduler scheduler;

    private MyScheduler() {
    }

    public static synchronized Scheduler getScheduler() {
        if(scheduler == null){
            scheduler = Schedulers.from(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));
        }
        return scheduler;
    }

    public static synchronized  Scheduler getMainThreadScheduler(){
        return AndroidSchedulers.mainThread();
    }
}
