package pl.janusz.hain.androidplayground.viewspresenters.persons.knownpersons;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.janusz.hain.androidplayground.R;
import pl.janusz.hain.androidplayground.models.people.group.Group;
import pl.janusz.hain.androidplayground.models.people.person.Person;
import pl.janusz.hain.androidplayground.util.ListenerRecyclerViewScrolledToBottom;
import pl.janusz.hain.androidplayground.viewspresenters.persons.MyPersonRecyclerViewAdapter;

public class KnownPersonListFragment extends Fragment implements KnownPersonsContract.View,
        ListenerRecyclerViewScrolledToBottom.RecyclerViewScrolledToBottomCallback,
        MyPersonRecyclerViewAdapter.OnPersonRecyclerViewInteractionListener {

    private static final String ARG_PERSON = "person";
    private int mColumnCount = 1;
    private OnKnownPersonListInteractionListener mListener;
    private ListenerRecyclerViewScrolledToBottom listenerRecyclerViewScrolledToBottom;
    private MyPersonRecyclerViewAdapter recyclerViewAdapter;

    private boolean firstTimeCreated = true;
    private Person person;

    @BindView(R.id.personKnownPeople)
    protected TextView textViewTitle;

    @BindView(R.id.list)
    protected RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    protected ProgressBar progressBar;

    @Inject
    protected KnownPersonsPresenter knownPersonsPresenter;

    public KnownPersonListFragment() {
    }

    public static KnownPersonListFragment newInstance(Person person) {
        KnownPersonListFragment fragment = new KnownPersonListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PERSON, person);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            person = (Person) getArguments().getSerializable(ARG_PERSON);
        }
        injectPresenter(person);
        setHasOptionsMenu(true);
    }

    private void injectPresenter(Person person) {
        DaggerKnownPersonsComponent.builder()
                .knownPersonsPresenterModule(new KnownPersonsPresenterModule(this, person)).build()
                .inject(this);

        System.out.println(knownPersonsPresenter.getClass().getName());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.knownpersons, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_groups:
                knownPersonsPresenter.searchGroupsOfKnownPeople();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_known_person_list, container, false);

        ButterKnife.bind(this, view);
        setRecyclerView(view);
        return view;
    }

    private void setRecyclerView(View view) {
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        if (firstTimeCreated) {
            ArrayList<Person> persons = new ArrayList<>();
            recyclerViewAdapter = new MyPersonRecyclerViewAdapter(persons, this);
        }
        recyclerView.setAdapter(recyclerViewAdapter);
        listenerRecyclerViewScrolledToBottom = new ListenerRecyclerViewScrolledToBottom(this, recyclerView, recyclerViewAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (firstTimeCreated) {
            knownPersonsPresenter.start();
            firstTimeCreated = false;
        }
        setTitleOfTheList();
    }

    private void setTitleOfTheList() {
        textViewTitle.setText(person + "'s friends");
        textViewTitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void onScrolledToBottom() {
        loadPersons();
    }

    private void loadPersons() {
        knownPersonsPresenter.loadPersons();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnKnownPersonListInteractionListener) {
            mListener = (OnKnownPersonListInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnKnownPersonListInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void addPersons(ArrayList<Person> persons) {
        if (persons.size() > 0) {
            recyclerViewAdapter.addPersons(persons);
        }
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGroupsCreated(ArrayList<Group> groups) {
        mListener.onGroupsCreated(groups, person);
    }

    @Override
    public void setProgressBar(boolean visible) {
        if (visible) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPersonChosen(Person person) {
        mListener.onPersonListFragmentInteraction(person);
    }


    public interface OnKnownPersonListInteractionListener {
        void onPersonListFragmentInteraction(Person person);

        void onGroupsCreated(ArrayList<Group> groups, Person person);
    }
}
