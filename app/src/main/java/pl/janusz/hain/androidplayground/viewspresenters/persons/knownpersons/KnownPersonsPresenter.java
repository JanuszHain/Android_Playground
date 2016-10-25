package pl.janusz.hain.androidplayground.viewspresenters.persons.knownpersons;

import java.util.ArrayList;
import java.util.HashSet;

import javax.inject.Inject;

import pl.janusz.hain.androidplayground.models.people.group.Group;
import pl.janusz.hain.androidplayground.models.people.person.Person;
import pl.janusz.hain.androidplayground.models.people.relation.Relation;
import pl.janusz.hain.androidplayground.models.people.relation.RelationDbForm;
import pl.janusz.hain.androidplayground.usecases.group.SearchForGroupsOfMutualPeople;
import pl.janusz.hain.androidplayground.usecases.person.LoadPersonRelations;
import pl.janusz.hain.androidplayground.usecases.person.LoadPersons;
import rx.Observable;


/*
In this presenter relations are firstly loaded for later use for loading persons.
Next loads are "zipped" by RxJava. Persons to be shown are loaded concurrently to new relations for later use (for next persons load).
This way application doesn't need to wait for relations response before loading persons, so loading times are up to 2 times faster.
 */
public class KnownPersonsPresenter implements KnownPersonsContract.Presenter {

    private final KnownPersonListFragment personListFragment;
    private final Person mainPerson;

    private final int startLimit = 10;
    private final int defaultLimit = 5;

    private ArrayList<Person> personsToBeLoaded;
    private ArrayList<Person> personsInTheGroup;
    private ArrayList<RelationDbForm> currentRelationsFromDb;

    private LoadPersonRelations loadPersonRelations;
    private LoadPersons loadPersons;


    private long lastRelationId = -1;

    private boolean isLoading = false;




    @Inject
    public KnownPersonsPresenter(KnownPersonListFragment personListFragment, Person person) {
        this.personListFragment = personListFragment;
        this.mainPerson = person;
        personsToBeLoaded = new ArrayList<>(startLimit);
        personsInTheGroup = new ArrayList<>(startLimit);
        currentRelationsFromDb = new ArrayList<>(startLimit);

        personsInTheGroup.add(person);

        loadPersonRelations = new LoadPersonRelations(person);
        loadPersons = new LoadPersons();


    }

    @Override
    public void start() {
        isLoading = true;
        loadRelationsOnStart(startLimit);
    }

    private void loadRelationsOnStart(int limit) {
        loadPersonRelations.executeWithLimit(lastRelationId, limit)
                .subscribe(relationDbForms -> {
                            addPersonsToBeLoaded(relationDbForms);
                            setLastIdForRelations(relationDbForms);
                            currentRelationsFromDb.addAll(relationDbForms);

                        },
                        throwable -> {
                            System.err.println(throwable);
                            isLoading = false;
                            personListFragment.showToast("Error occured");
                        }
                        ,
                        () -> {

                            isLoading = false;
                            loadPersons();
                        }
                );
    }


    @Override
    public void loadPersons() {
        if (!isLoading) {
            isLoading = true;
            if (personsToBeLoaded.size() > 0) {
                loadZippedPersonsAndRelations(defaultLimit);
            }
        }
    }

    private void loadZippedPersonsAndRelations(int limit) {
        zippedObservableLoadPersonsAndRelations(limit).subscribe(
                zippedObservableResulstsPersons -> {
                    addPersonsToGroup(zippedObservableResulstsPersons);

                    relationsFromDBToRelationsAndPersons(currentRelationsFromDb);
                    setLastIdForRelations(zippedObservableResulstsPersons.getRelationDbForms());


                    clearPersonsToBeLoaded();
                    addPersonsToBeLoaded(zippedObservableResulstsPersons.getRelationDbForms());

                    clearCurrentRelationsFromDb();
                    currentRelationsFromDb.addAll(zippedObservableResulstsPersons.getRelationDbForms());

                    addPersonsToList(zippedObservableResulstsPersons.getPersons());
                    isLoading = false;
                },
                throwable -> {
                    isLoading = false;
                    System.err.println(throwable);
                    personListFragment.showToast("Error occured");
                }
        );

    }

    private Observable<ZippedObservableResulstsPersons> zippedObservableLoadPersonsAndRelations(int limit) {
        return Observable.zip(loadPersonRelations.executeWithLimit(lastRelationId, limit), loadPersons.executeLoadSelectedPersons(personsToBeLoaded),
                (relationDbForms, persons) -> new ZippedObservableResulstsPersons(persons, relationDbForms)
        );
    }

    private void clearPersonsToBeLoaded() {
        personsToBeLoaded.clear();
    }

    private void clearCurrentRelationsFromDb() {
        currentRelationsFromDb.clear();
    }

    private void addPersonsToBeLoaded(ArrayList<RelationDbForm> relationDbForms) {
        for (RelationDbForm relationDbForm : relationDbForms) {
            Person person = new Person(relationDbForm.getIdPersonA());
            if (!person.equals(mainPerson)) {
                personsToBeLoaded.add(person);
            } else {
                personsToBeLoaded.add(new Person(relationDbForm.getIdPersonB()));
            }
        }
    }

    private void addPersonsToGroup(ZippedObservableResulstsPersons zippedObservableResulstsPersons) {
        personsInTheGroup.addAll(zippedObservableResulstsPersons.getPersons());
    }


    private void relationsFromDBToRelationsAndPersons(ArrayList<RelationDbForm> relationDbForms) {
        for (RelationDbForm relationDbForm : relationDbForms) {
            createRelationAndAddRelationsToPersons(relationDbForm);
        }
    }

    private void createRelationAndAddRelationsToPersons(RelationDbForm relationDbForm) {
        Person personA = new Person(relationDbForm.getIdPersonA());
        Person personB = new Person(relationDbForm.getIdPersonB());

        Person personFromGroupA = getPersonFromGroup(personA);
        Person personFromGroupB = getPersonFromGroup(personB);

        Relation relation = new Relation(personFromGroupA, personFromGroupB);
        personFromGroupA.setRelation(relation);
        personFromGroupB.setRelation(relation);
    }

    private Person getPersonFromGroup(Person person) {
        return personsInTheGroup.get(personsInTheGroup.indexOf(person));
    }

    private void setLastIdForRelations(ArrayList<RelationDbForm> relationDbForms) {
        if (relationDbForms.size() > 0) {
            lastRelationId = relationDbForms.get(relationDbForms.size() - 1).getId();
        }
    }

    private void addPersonsToList(ArrayList<Person> persons) {
        if (persons.size() > 0) {
            personListFragment.addPersons(persons);
        }
    }

    @Override
    public void searchGroupsOfKnownPeople() {
        if (!isLoading) {
            isLoading = true;
            personListFragment.setProgressBar(true);
            loadRestOfRelations();
        }
    }

    private void loadRestOfRelations() {

        loadPersonRelations.executeGetRestOfRelations(lastRelationId)
                .subscribe(
                        relationDbForms -> {
                            addPersonsToBeLoaded(relationDbForms);
                            setLastIdForRelations(relationDbForms);
                            currentRelationsFromDb.addAll(relationDbForms);
                        }
                        ,
                        throwable -> {
                            isLoading = false;
                            personListFragment.setProgressBar(false);
                            personListFragment.showToast("Error occured");
                        },
                        this::loadRestOfPersons
                );
    }

    private void loadRestOfPersons() {
        if (personsToBeLoaded.size() > 0) {
            loadPersons.executeLoadSelectedPersons(personsToBeLoaded)
                    .subscribe(persons ->
                            {
                                personsInTheGroup.addAll(persons);
                                relationsFromDBToRelationsAndPersons(currentRelationsFromDb);

                                clearPersonsToBeLoaded();
                                clearCurrentRelationsFromDb();

                                addPersonsToList(persons);

                            },
                            throwable -> {
                                isLoading = false;
                                personListFragment.setProgressBar(false);
                                personListFragment.showToast("Error occured");
                            },
                            this::loadRelationsForKnownPersons
                    );
        } else {
            loadRelationsForKnownPersons();
        }
    }

    /*

    This is the old way of getting data without much work in PHP and MySQL to retrieve needed data.
    Instead of that all relations were sent to this presenter and then the needed ones were chosen.

    private void loadRelationsForKnownPersons() {
        boolean firstPersonInGroup = true;

        ArrayList<Observable<ArrayList<RelationDbForm>>> observables = new ArrayList<>(personsInTheGroup.size() - 1);

        for (Person person : personsInTheGroup) {
            if (!firstPersonInGroup) {
                LoadPersonRelations loadPersonRelations = new LoadPersonRelations(person);
                observables.add(loadPersonRelations.execute());
            } else {
                firstPersonInGroup = false;
            }
        }

        Observable<ArrayList<RelationDbForm>> mergedObservablesLoadedRelationsDb = Observable.merge(observables);

        mergedObservablesLoadedRelationsDb.subscribe(relationDbForms -> {
                    addRelationsToPersonsIfKnownToMainPerson(relationDbForms);
                },
                throwable -> {
                    System.err.println(throwable);
                    isLoading = false;
                },
                () -> {
                    //TODO creating group and then isLoading = false
                    searchGroupsOfMutualPeople();
                }

        );
    }

    */

    private void loadRelationsForKnownPersons() {
        boolean firstPersonInGroup = true;

        ArrayList<Observable<ArrayList<RelationDbForm>>> observables = new ArrayList<>(personsInTheGroup.size() - 1);

        for (Person person : personsInTheGroup) {
            if (!firstPersonInGroup) {
                LoadPersonRelations loadPersonRelations = new LoadPersonRelations(person);
                observables.add(loadPersonRelations.getRelationsWithSelectedPeople(person, personsInTheGroup));
            } else {
                firstPersonInGroup = false;
            }
        }

        Observable<ArrayList<RelationDbForm>> mergedObservablesLoadedRelationsDb = Observable.merge(observables);

        mergedObservablesLoadedRelationsDb.subscribe(this::addRelationsToPersonsIfKnownToMainPerson,
                throwable -> {
                    System.err.println(throwable);
                    isLoading = false;
                    personListFragment.setProgressBar(false);
                    personListFragment.showToast("Error occured");
                },
                this::searchGroupsOfMutualPeople

        );
    }

    private void addRelationsToPersonsIfKnownToMainPerson(ArrayList<RelationDbForm> relationDbForms) {
        for (RelationDbForm relationDbForm : relationDbForms) {
            Person personA = new Person(relationDbForm.getIdPersonA());
            Person personB = new Person(relationDbForm.getIdPersonB());

            Person personFromGroupA = getPersonFromGroup(personA);
            Person personFromGroupB = getPersonFromGroup(personB);

            Relation relation = new Relation(personFromGroupA, personFromGroupB);
            personFromGroupA.setRelation(relation);
            personFromGroupB.setRelation(relation);
        }
    }

    private void searchGroupsOfMutualPeople() {
        SearchForGroupsOfMutualPeople searchForGroupsOfMutualPeople = new SearchForGroupsOfMutualPeople(createGroupsOutOfRelationsOfMainPerson());

        searchForGroupsOfMutualPeople.searchForGroups()
                .subscribe(groups -> {
                            isLoading = false;
                            personListFragment.setProgressBar(false);
                            onGroupsCreated(groups);
                        },
                        throwable -> {
                            System.err.println(throwable);
                            isLoading = false;
                            personListFragment.setProgressBar(false);
                            personListFragment.showToast("Error occured");
                        }
                );

    }

    private HashSet<Group> createGroupsOutOfRelationsOfMainPerson() {
        HashSet<Relation> mainPersonRelations = mainPerson.getRelations();
        HashSet<Group> groups = new HashSet<>(mainPersonRelations.size());
        for (Relation relation : mainPersonRelations) {
            Group group = new Group(relation);
            groups.add(group);
        }
        return groups;
    }

    private void onGroupsCreated(ArrayList<Group> groups) {
        personListFragment.onGroupsCreated(groups);
    }


    private class ZippedObservableResulstsPersons {
        private ArrayList<Person> persons;
        private ArrayList<RelationDbForm> relationDbForms;

        public ZippedObservableResulstsPersons(ArrayList<Person> persons, ArrayList<RelationDbForm> relationDbForms) {
            this.persons = persons;
            this.relationDbForms = relationDbForms;
        }

        public ArrayList<Person> getPersons() {
            return persons;
        }

        public ArrayList<RelationDbForm> getRelationDbForms() {
            return relationDbForms;
        }
    }
}
