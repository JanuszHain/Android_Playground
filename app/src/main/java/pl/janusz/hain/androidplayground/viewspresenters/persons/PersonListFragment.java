package pl.janusz.hain.androidplayground.viewspresenters.persons;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import pl.janusz.hain.androidplayground.R;
import pl.janusz.hain.androidplayground.models.people.person.Person;
import pl.janusz.hain.androidplayground.util.ListenerRecyclerViewScrolledToBottom;

import java.util.ArrayList;

import javax.inject.Inject;

public class PersonListFragment extends Fragment implements PersonsContract.View,
        ListenerRecyclerViewScrolledToBottom.RecyclerViewScrolledToBottomCallback,
        MyPersonRecyclerViewAdapter.OnPersonRecyclerViewInteractionListener {

    private int mColumnCount = 1;
    private OnPersonListInteractionListener mListener;
    private ListenerRecyclerViewScrolledToBottom listenerRecyclerViewScrolledToBottom;
    private MyPersonRecyclerViewAdapter recyclerViewAdapter;
    private static final String ARG_FRAGMENT_LIST_TYPE_TAG = "ARG_FRAGMENT_LIST_TYPE_TAG";
    private String fragmentListType;

    private boolean firstTimeCreated = true;

    @Inject
    protected PersonsPresenter personsPresenter;

    public PersonListFragment() {
    }

    public static PersonListFragment newInstance(String fragmentListTypeTag) {
        PersonListFragment fragment = new PersonListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FRAGMENT_LIST_TYPE_TAG, fragmentListTypeTag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fragmentListType = getArguments().getString(ARG_FRAGMENT_LIST_TYPE_TAG);
        }
        injectPresenter();
        personsPresenter.setFragmentTag(fragmentListType);

    }

    private void injectPresenter() {
        DaggerPersonsComponent.builder()
                .personsPresenterModule(new PersonsPresenterModule(this)).build()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_list, container, false);

        setRecyclerView(view);


        return view;
    }

    private void setRecyclerView(View view) {
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
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
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (firstTimeCreated) {
            personsPresenter.start();
            firstTimeCreated = false;
        }
    }

    @Override
    public void onScrolledToBottom() {
        loadPersons();
    }

    private void loadPersons() {
        personsPresenter.loadPersonList();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPersonListInteractionListener) {
            mListener = (OnPersonListInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
    public void onPersonChosen(Person person) {

        mListener.onPersonListFragmentInteraction(person);
    }


    public interface OnPersonListInteractionListener {
        void onPersonListFragmentInteraction(Person person);
    }
}
