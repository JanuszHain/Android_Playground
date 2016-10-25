package pl.janusz.hain.androidplayground.usecases.person;

import android.support.annotation.Nullable;

import java.util.ArrayList;

import javax.inject.Inject;

import pl.janusz.hain.androidplayground.MyApplication;
import pl.janusz.hain.androidplayground.data.internet.MyApiEndpointInterface;
import pl.janusz.hain.androidplayground.models.people.person.Person;
import pl.janusz.hain.androidplayground.util.MyScheduler;
import retrofit2.Retrofit;
import rx.Observable;

public class LoadPersons {
    private MyApplication application;

    @Inject
    protected Retrofit retrofit;

    public LoadPersons() {
        application = MyApplication.getApplication();
        injectDependencies();
    }


    private void injectDependencies() {
        application.getNetComponent().inject(this);
    }

    public Observable<ArrayList<Person>> execute(@Nullable Person lastPerson, int limit) {
        if (lastPerson != null) {
            MyApiEndpointInterface personObservable = retrofit.create(MyApiEndpointInterface.class);
            System.out.println(lastPerson.getIdNetworkDatabase() + lastPerson.getSecondName());
            return personObservable.getPersons(lastPerson.getIdNetworkDatabase(), limit, lastPerson.getSecondName())
                    .observeOn(MyScheduler.getMainThreadScheduler())
                    .subscribeOn(MyScheduler.getScheduler());
        } else {
            MyApiEndpointInterface personObservable = retrofit.create(MyApiEndpointInterface.class);
            return personObservable.getPersons(limit)
                    .observeOn(MyScheduler.getMainThreadScheduler())
                    .subscribeOn(MyScheduler.getScheduler());
        }
    }

    public Observable<ArrayList<Person>> executeLoadSelectedPersons(ArrayList<Person> persons) {
        ArrayList<Long> personsIds = new ArrayList<>(persons.size());
        for (Person person : persons) {
            personsIds.add(person.getIdNetworkDatabase());
        }
        MyApiEndpointInterface personObservable = retrofit.create(MyApiEndpointInterface.class);
        return personObservable.getSelectedPersons(personsIds)
                .observeOn(MyScheduler.getMainThreadScheduler())
                .subscribeOn(MyScheduler.getScheduler());
    }


}
