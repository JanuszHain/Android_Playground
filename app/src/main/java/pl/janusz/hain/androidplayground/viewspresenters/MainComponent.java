package pl.janusz.hain.androidplayground.viewspresenters;

import dagger.Component;
import pl.janusz.hain.androidplayground.util.FragmentScoped;

@FragmentScoped
@Component(modules = MainPresenterModule.class)
public interface MainComponent {
    void inject(MainActivity mainActivity);
}
