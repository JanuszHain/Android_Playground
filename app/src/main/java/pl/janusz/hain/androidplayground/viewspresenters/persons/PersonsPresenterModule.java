package pl.janusz.hain.androidplayground.viewspresenters.persons;

import dagger.Module;
import dagger.Provides;

@Module
public class PersonsPresenterModule {
    private final PersonListFragment personListFragment;

    public PersonsPresenterModule(PersonListFragment personListFragment) {
        this.personListFragment = personListFragment;
    }

    @Provides
    PersonListFragment providePersonListFragment(){
        return personListFragment;
    }
}
