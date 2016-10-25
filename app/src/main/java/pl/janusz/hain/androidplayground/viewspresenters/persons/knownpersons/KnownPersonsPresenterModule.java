package pl.janusz.hain.androidplayground.viewspresenters.persons.knownpersons;

import dagger.Module;
import dagger.Provides;
import pl.janusz.hain.androidplayground.models.people.person.Person;

@Module
public class KnownPersonsPresenterModule {
    private final KnownPersonListFragment personListFragment;
    private final Person person;

    public KnownPersonsPresenterModule(KnownPersonListFragment personListFragment, Person person) {
        this.personListFragment = personListFragment;
        this.person = person;
    }

    @Provides
    KnownPersonListFragment provideKnownPersonListFragment(){
        return personListFragment;
    }

    @Provides
    Person providePerson(){
        return person;
    }
}
