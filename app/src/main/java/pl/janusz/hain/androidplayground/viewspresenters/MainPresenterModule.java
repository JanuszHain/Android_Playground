package pl.janusz.hain.androidplayground.viewspresenters;

import dagger.Module;
import dagger.Provides;

@Module
public class MainPresenterModule {
    private final MainActivity mainActivity;

    public MainPresenterModule(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Provides
    MainActivity provideMainActivity(){
        return mainActivity;
    }
}
