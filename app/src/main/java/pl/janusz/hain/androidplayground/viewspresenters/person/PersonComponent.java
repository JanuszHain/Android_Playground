package pl.janusz.hain.androidplayground.viewspresenters.person;

import dagger.Component;
import pl.janusz.hain.androidplayground.util.FragmentScoped;

@FragmentScoped
@Component(modules = PersonPresenterModule.class)
public interface PersonComponent {
    void inject(PersonFragment personFragment);
}
