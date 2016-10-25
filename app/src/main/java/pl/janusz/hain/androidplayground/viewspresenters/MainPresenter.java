package pl.janusz.hain.androidplayground.viewspresenters;

import javax.inject.Inject;

public class MainPresenter implements MainContract.Presenter {

    private final MainActivity mainActivity;

    @Inject
    public MainPresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void start() {
    }
}
