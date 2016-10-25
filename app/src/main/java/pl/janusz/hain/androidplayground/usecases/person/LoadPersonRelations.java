package pl.janusz.hain.androidplayground.usecases.person;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import javax.inject.Inject;

import pl.janusz.hain.androidplayground.MyApplication;
import pl.janusz.hain.androidplayground.data.internet.MyApiEndpointInterface;
import pl.janusz.hain.androidplayground.models.people.person.Person;
import pl.janusz.hain.androidplayground.models.people.relation.RelationDbForm;
import pl.janusz.hain.androidplayground.util.MyScheduler;
import retrofit2.Retrofit;
import rx.Observable;

public class LoadPersonRelations {
    private Person person;
    private MyApplication application;

    @Inject
    protected Retrofit retrofit;

    public LoadPersonRelations(@NonNull Person person) {
        this.person = person;
        application = MyApplication.getApplication();
        injectDependencies();
    }

    private void injectDependencies() {
        application.getNetComponent().inject(this);
    }

    public Observable<ArrayList<RelationDbForm>> execute() {
        MyApiEndpointInterface relationObservable = retrofit.create(MyApiEndpointInterface.class);
        return relationObservable.getRelations(person.getIdNetworkDatabase())
                .observeOn(MyScheduler.getMainThreadScheduler())
                .subscribeOn(MyScheduler.getScheduler());
    }

    public Observable<ArrayList<RelationDbForm>> executeWithLimit(long lastId, int limit) {

        if (lastId != -1) {
            MyApiEndpointInterface relationObservable = retrofit.create(MyApiEndpointInterface.class);
            return relationObservable.getRelations(person.getIdNetworkDatabase(), lastId, limit)
                    .observeOn(MyScheduler.getMainThreadScheduler())
                    .subscribeOn(MyScheduler.getScheduler());
        } else {
            MyApiEndpointInterface relationObservable = retrofit.create(MyApiEndpointInterface.class);
            return relationObservable.getRelations(person.getIdNetworkDatabase(), limit)
                    .observeOn(MyScheduler.getMainThreadScheduler())
                    .subscribeOn(MyScheduler.getScheduler());
        }
    }

    public Observable<ArrayList<RelationDbForm>> executeGetRestOfRelations(long lastId) {
        MyApiEndpointInterface relationObservable = retrofit.create(MyApiEndpointInterface.class);
        return relationObservable.getRestOfRelations(person.getIdNetworkDatabase(), lastId)
                .observeOn(MyScheduler.getMainThreadScheduler())
                .subscribeOn(MyScheduler.getScheduler());
    }


    public Observable<ArrayList<RelationDbForm>> getRelationsWithSelectedPeople(Person mainPerson, ArrayList<Person> persons) {
        ArrayList<Long> personsIds = new ArrayList<>(persons.size());
        for (Person person : persons) {
            personsIds.add(person.getIdNetworkDatabase());
        }
        MyApiEndpointInterface relationObservable = retrofit.create(MyApiEndpointInterface.class);
        return relationObservable.getRelationsWithSelectedPeople(mainPerson.getIdNetworkDatabase(), personsIds)
                .observeOn(MyScheduler.getMainThreadScheduler())
                .subscribeOn(MyScheduler.getScheduler());

    }

}
