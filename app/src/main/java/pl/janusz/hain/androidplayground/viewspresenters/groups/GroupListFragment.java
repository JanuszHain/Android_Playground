package pl.janusz.hain.androidplayground.viewspresenters.groups;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.janusz.hain.androidplayground.R;
import pl.janusz.hain.androidplayground.models.people.group.Group;
import pl.janusz.hain.androidplayground.models.people.person.Person;

public class GroupListFragment extends Fragment {

    private static final String ARG_GROUPS = "ARG_GROUPS";
    private static final String ARG_PERSON = "ARG_PERSON";
    private ArrayList<Group> groups;
    private Person person;
    private ArrayList<GroupParentObject> groupParentObjects;
    private boolean firstTimeCreated = true;
    private GroupListFragmentInterface mListener;
    private MyGroupExpendableRecycleViewAdapter myGroupExpendableRecycleViewAdapter;

    @BindView(R.id.listOfGroupsTitle)
    protected TextView listOfGroupsTitle;

    @BindView(R.id.list)
    protected RecyclerView recyclerView;

    public GroupListFragment() {
    }

    public static GroupListFragment newInstance(ArrayList<Group> groups, Person person) {
        GroupListFragment fragment = new GroupListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_GROUPS, groups);
        args.putSerializable(ARG_PERSON, person);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            groups = (ArrayList<Group>) getArguments().getSerializable(ARG_GROUPS);
            groupParentObjects = new ArrayList<>(groups.size());
            person = (Person) getArguments().getSerializable(ARG_PERSON);
            initializeObjectsForAdapter();
        }
    }

    private void initializeObjectsForAdapter() {
        if (firstTimeCreated) {
            for (Group group : groups) {
                ArrayList<Person> persons = new ArrayList<>(group.getPeopleInGroup());
                GroupParentObject groupParentObject = new GroupParentObject(persons, group);
                groupParentObjects.add(groupParentObject);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_list, container, false);

        ButterKnife.bind(this, view);

        setRecyclerView(view);

        return view;
    }

    private void setRecyclerView(View view) {

        Context context = view.getContext();

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        if (firstTimeCreated) {
            myGroupExpendableRecycleViewAdapter = new MyGroupExpendableRecycleViewAdapter(context, groupParentObjects, mListener);
        }
        recyclerView.setAdapter(myGroupExpendableRecycleViewAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (firstTimeCreated) {

            firstTimeCreated = false;
        }
        setTitle();
    }


    private void setTitle() {
        listOfGroupsTitle.setText("Groups of " + person + " friends");
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GroupListFragmentInterface) {
            mListener = (GroupListFragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public interface GroupListFragmentInterface {
        void onPersonChosenFromGroup(Person person);
    }
}
