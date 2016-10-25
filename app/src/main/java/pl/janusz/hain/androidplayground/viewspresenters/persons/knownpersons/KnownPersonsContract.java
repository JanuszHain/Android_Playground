package pl.janusz.hain.androidplayground.viewspresenters.persons.knownpersons;

import java.util.ArrayList;

import pl.janusz.hain.androidplayground.BasePresenter;
import pl.janusz.hain.androidplayground.models.people.group.Group;
import pl.janusz.hain.androidplayground.models.people.person.Person;

public interface KnownPersonsContract {

    interface View{
        void addPersons(ArrayList<Person> persons);

        void showToast(String text);

        void onGroupsCreated(ArrayList<Group> groups);

        void setProgressBar(boolean visible);
    }

    interface Presenter extends BasePresenter {

        void loadPersons();

        void searchGroupsOfKnownPeople();



    }
}
