package pl.janusz.hain.androidplayground.viewspresenters.persons;

import java.util.ArrayList;

import javax.inject.Inject;

import pl.janusz.hain.androidplayground.models.people.person.Person;
import pl.janusz.hain.androidplayground.usecases.person.LoadPersons;
import pl.janusz.hain.androidplayground.util.FragmentTags;

public class PersonsPresenter implements PersonsContract.Presenter {

    private final PersonListFragment personListFragment;
    private LoadPersons loadPersons;
    private Person lastPerson;
    private String fragmentTag = "";

    private final int startLimit = 10;
    private final int defaultLimit = 5;

    private boolean isLoading = false;


    @Inject
    public PersonsPresenter(PersonListFragment personListFragment) {
        this.personListFragment = personListFragment;
        loadPersons = new LoadPersons();
    }

    @Override
    public void loadPersonList() {
        loadPersonList(defaultLimit);
    }

    @Override
    public void start() {
        loadPersonList(startLimit);
    }

    @Override
    public void loadPersonList(int limit) {
        if(!isLoading) {
            isLoading = true;
            switch (fragmentTag) {
                case FragmentTags.internetPersonList:
                    loadPersonsFromRemote(limit);
                    break;
                default:
                    break;
            }
        }
    }

    private void loadPersonsFromRemote(int limit) {
        loadPersons.execute(lastPerson, limit).subscribe(
                this::onPersonsLoaded
                , throwable -> {
                    System.err.println(throwable);
                    isLoading = false;
                    personListFragment.showToast("Couldn't load persons");
                }
        );
    }

    private void onPersonsLoaded(ArrayList<Person> persons) {
        if (persons.size() > 0) {
            lastPerson = getLastPersonFromArrayList(persons);
            if (personListFragment != null) {

                personListFragment.addPersons(persons);
            }
        }
        isLoading = false;
    }

    private Person getLastPersonFromArrayList(ArrayList<Person> persons) {
        return persons.get(persons.size() - 1);
    }

    public void setFragmentTag(String fragmentTag) {
        this.fragmentTag = fragmentTag;
    }
}
