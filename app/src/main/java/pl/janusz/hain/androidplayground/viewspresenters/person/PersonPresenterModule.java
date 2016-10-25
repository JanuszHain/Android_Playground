package pl.janusz.hain.androidplayground.viewspresenters.person;

import dagger.Module;
import dagger.Provides;

@Module
public class PersonPresenterModule {
    private final PersonFragment personFragment;

    public PersonPresenterModule(PersonFragment personFragment) {
        this.personFragment = personFragment;
    }

    @Provides
    PersonFragment providePersonFragment(){
        return personFragment;
    }
}
