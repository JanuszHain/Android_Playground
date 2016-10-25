package pl.janusz.hain.androidplayground.viewspresenters.groups;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

import pl.janusz.hain.androidplayground.R;
import pl.janusz.hain.androidplayground.models.people.person.Person;

public class MyGroupExpendableRecycleViewAdapter extends ExpandableRecyclerAdapter<GroupViewHolder, PersonViewHolder> {

    private LayoutInflater mInflator;
    private GroupListFragment.GroupListFragmentInterface mListener;

    public MyGroupExpendableRecycleViewAdapter(Context context, @NonNull List<? extends ParentListItem> parentItemList, GroupListFragment.GroupListFragmentInterface mListener) {
        super(parentItemList);
        mInflator = LayoutInflater.from(context);
        this.mListener = mListener;
    }

    @Override
    public GroupViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View groupView = mInflator.inflate(R.layout.group_item, parentViewGroup, false);
        return new GroupViewHolder(groupView);
    }

    @Override
    public PersonViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View personView = mInflator.inflate(R.layout.person_small_item, childViewGroup, false);
        return new PersonViewHolder(personView);
    }

    @Override
    public void onBindParentViewHolder(GroupViewHolder parentViewHolder, int position, ParentListItem parentListItem) {
        GroupParentObject group = (GroupParentObject) parentListItem;
        parentViewHolder.bind(group);
    }

    @Override
    public void onBindChildViewHolder(PersonViewHolder childViewHolder, int position, Object childListItem) {
        Person person = (Person) childListItem;
        childViewHolder.bind(person);
        childViewHolder.setOnClickListener(mListener);
    }
}
