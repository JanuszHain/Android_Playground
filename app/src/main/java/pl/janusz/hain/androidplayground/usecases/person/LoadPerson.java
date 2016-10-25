package pl.janusz.hain.androidplayground.usecases.person;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import pl.janusz.hain.androidplayground.MyApplication;
import pl.janusz.hain.androidplayground.data.internet.MyApiEndpointInterface;
import pl.janusz.hain.androidplayground.models.people.person.Person;
import pl.janusz.hain.androidplayground.util.MyScheduler;
import retrofit2.Retrofit;
import rx.Observable;

public class LoadPerson {
    private Person person;
    private MyApplication application;

    @Inject
    protected Retrofit retrofit;

    public LoadPerson() {
        application = MyApplication.getApplication();
        injectDependencies();
    }

    private void injectDependencies() {
        application.getNetComponent().inject(this);
    }

    public Observable<Person> execute(@NonNull Person person) {
        this.person = person;
        MyApiEndpointInterface relationObservable = retrofit.create(MyApiEndpointInterface.class);
        return relationObservable.getPerson(person.getIdNetworkDatabase())
                .observeOn(MyScheduler.getMainThreadScheduler())
                .subscribeOn(MyScheduler.getScheduler());
    }

}
