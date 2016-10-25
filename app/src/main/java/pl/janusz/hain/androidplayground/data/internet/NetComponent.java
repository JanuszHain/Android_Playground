package pl.janusz.hain.androidplayground.data.internet;

import javax.inject.Singleton;

import dagger.Component;
import pl.janusz.hain.androidplayground.usecases.person.LoadPerson;
import pl.janusz.hain.androidplayground.usecases.person.LoadPersonRelations;
import pl.janusz.hain.androidplayground.usecases.person.LoadPersons;

@Singleton
@Component(modules={NetModule.class})
public interface NetComponent {
    void inject(LoadPersons loadPersons);
    void inject(LoadPersonRelations loadPersonRelations);
    void inject(LoadPerson loadPerson);
}
