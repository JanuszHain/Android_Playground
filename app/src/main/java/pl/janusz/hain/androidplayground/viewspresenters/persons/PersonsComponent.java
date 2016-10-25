package pl.janusz.hain.androidplayground.viewspresenters.persons;

import dagger.Component;
import pl.janusz.hain.androidplayground.util.FragmentScoped;

@FragmentScoped
@Component(modules = PersonsPresenterModule.class)
public interface PersonsComponent {
    void inject(PersonListFragment personListFragment);
}
