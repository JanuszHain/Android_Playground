package pl.janusz.hain.androidplayground.viewspresenters.persons;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.janusz.hain.androidplayground.R;
import pl.janusz.hain.androidplayground.models.people.person.Person;

import java.util.ArrayList;

public class MyPersonRecyclerViewAdapter extends RecyclerView.Adapter<MyPersonRecyclerViewAdapter.ViewHolder> implements MyPersonRecyclerViewAdapterInterface {

    private final ArrayList<Person> persons;
    private OnPersonRecyclerViewInteractionListener onPersonRecyclerViewInteractionListener;

    public MyPersonRecyclerViewAdapter(ArrayList<Person> persons, Fragment fragment) {
        this.persons = persons;
        setFragmentListener(fragment);
    }

    private void setFragmentListener(Fragment fragment) {
        if (fragment instanceof OnPersonRecyclerViewInteractionListener) {
            onPersonRecyclerViewInteractionListener = (OnPersonRecyclerViewInteractionListener) fragment;
        } else {
            throw new RuntimeException(fragment.toString()
                    + " must implement OnPersonRecyclerViewInteractionListener");
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_person_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.person = persons.get(position);
        holder.mContentView.setText(holder.person.toString());

        holder.mView.setOnClickListener(v -> onPersonRecyclerViewInteractionListener.onPersonChosen(holder.person));
    }


    @Override
    public int getItemCount() {
        return persons.size();
    }

    @Override
    public void addPersons(ArrayList<Person> persons) {
        System.out.println("addPersons()");
        this.persons.addAll(persons);
        notifyDataSetChangedUsingHandler();

    }

    private void notifyDataSetChangedUsingHandler() {

        MyPersonRecyclerViewAdapter myPersonRecyclerViewAdapter = this;

        Handler handler = new Handler();

        final Runnable r = myPersonRecyclerViewAdapter::notifyDataSetChanged;

        handler.post(r);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        @BindView(R.id.content)
        protected TextView mContentView;
        protected Person person;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, mView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public interface OnPersonRecyclerViewInteractionListener {
        void onPersonChosen(Person person);
    }
}
