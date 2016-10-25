package pl.janusz.hain.androidplayground;

import android.app.Application;

import pl.janusz.hain.androidplayground.data.internet.DaggerNetComponent;
import pl.janusz.hain.androidplayground.data.internet.NetComponent;
import pl.janusz.hain.androidplayground.data.internet.NetModule;

/**
 * https://github.com/googlesamples/android-architecture/blob/todo-mvp-dagger/todoapp/app/src/main/java/com/example/android/architecture/blueprints/todoapp/ToDoApplication.java
 */
public class MyApplication extends Application {

    private NetComponent netComponent;
    private static MyApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        injectNetComponent();
    }

    private void injectNetComponent(){
        netComponent = DaggerNetComponent.builder()
                .netModule(new NetModule())
                .build();
    }

    public NetComponent getNetComponent() {
        return netComponent;
    }

    public static MyApplication getApplication() {
        return application;
    }
}
