package pl.janusz.hain.androidplayground.viewspresenters.persons.knownpersons;

import dagger.Component;
import pl.janusz.hain.androidplayground.util.FragmentScoped;

@FragmentScoped
@Component(modules = KnownPersonsPresenterModule.class)
public interface KnownPersonsComponent {
    void inject(KnownPersonListFragment knownPersonListFragment);
}
