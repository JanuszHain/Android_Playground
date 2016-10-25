package pl.janusz.hain.androidplayground.usecases.group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import pl.janusz.hain.androidplayground.models.people.group.Group;
import pl.janusz.hain.androidplayground.models.people.group.GroupComparator;
import pl.janusz.hain.androidplayground.util.MyScheduler;
import rx.Observable;
import rx.Subscriber;


public class SearchForGroupsOfMutualPeople {

    private ArrayList<Group> groups;
    private HashSet<Group> groupsToBeGeneratedMore;


    public SearchForGroupsOfMutualPeople(HashSet<Group> groups) {
        this.groups = new ArrayList<>(groups.size());
        groupsToBeGeneratedMore = new HashSet<>(groups.size());
        groupsToBeGeneratedMore.addAll(groups);
    }

    public Observable<ArrayList<Group>> searchForGroups() {
        Observable<ArrayList<Group>> observable;
        observable = Observable.create(subscriber -> {
            generateGroups()
                    .doOnCompleted(() -> {
                        sortGroups();
                        subscriber.onNext(groups);
                        subscriber.onCompleted();
                    })
                    .subscribe();
        });
        return observable
                .observeOn(MyScheduler.getMainThreadScheduler())
                .subscribeOn(MyScheduler.getScheduler());
    }

    private Observable generateGroups() {
        return Observable.create(this::createMergedGroupGeneration);
    }

    private void createMergedGroupGeneration(Subscriber subscriber) {
        HashSet<Group> groupsToBeAdded = new HashSet<>(1);
        Observable<Group> mergedGroupGeneration = mergedObservablesGenerateGroups();
        groupsToBeGeneratedMore.clear();
        mergedGroupGeneration
                .doOnNext(group -> {
                    if (!group.generateMore()) {
                        groupsToBeAdded.add(group);
                    } else {
                        groupsToBeGeneratedMore.add(group);
                    }
                })
                .doOnCompleted(() -> {
                    groups.addAll(groupsToBeAdded);
                    if (!groupsToBeGeneratedMore.isEmpty()) {
                        createMergedGroupGeneration(subscriber);
                    } else {
                        subscriber.onCompleted();
                    }
                })
                .subscribe();
    }

    private Observable<Group> mergedObservablesGenerateGroups() {
        HashSet<Group> tmpGroupsToBeGeneratedMore = new HashSet<>(groupsToBeGeneratedMore.size());
        tmpGroupsToBeGeneratedMore.addAll(groupsToBeGeneratedMore);
        ArrayList<Observable<Group>> observables = new ArrayList<>(groupsToBeGeneratedMore.size());

        for (Group group : tmpGroupsToBeGeneratedMore) {
            Observable<Group> observable = observableGenerateGroup(group);
            observable = observable.subscribeOn(MyScheduler.getScheduler());
            observables.add(observable);
        }

        return Observable.merge(observables);
    }

    private Observable<Group> observableGenerateGroup(Group group) {
        return Observable.create(subscriber -> {
            group.generateGroup();
            subscriber.onNext(group);
            subscriber.onCompleted();
        });
    }

    private void sortGroups() {
        Collections.sort(groups, new GroupComparator());
    }


}
