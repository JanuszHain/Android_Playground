package pl.janusz.hain.androidplayground.viewspresenters.groups;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;
import java.util.List;

import pl.janusz.hain.androidplayground.models.people.group.Group;
import pl.janusz.hain.androidplayground.models.people.person.Person;

public class GroupParentObject implements ParentListItem {
    private ArrayList<Person> persons;
    private Group group;

    public GroupParentObject(ArrayList<Person> persons, Group group) {
        this.persons = persons;
        this.group = group;
    }

    @Override
    public List<?> getChildItemList() {
        return persons;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public Group getGroup() {
        return group;
    }
}
