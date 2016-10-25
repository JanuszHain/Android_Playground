package pl.janusz.hain.androidplayground.viewspresenters.persons;

import java.util.ArrayList;

import pl.janusz.hain.androidplayground.BasePresenter;
import pl.janusz.hain.androidplayground.models.people.person.Person;

public interface PersonsContract {

    interface View{
        void addPersons(ArrayList<Person> persons);

        void showToast(String text);
    }

    interface Presenter extends BasePresenter {

        void loadPersonList();

        void loadPersonList(int limit);

    }
}
