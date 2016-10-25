package pl.janusz.hain.androidplayground.viewspresenters.groups;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import pl.janusz.hain.androidplayground.R;
import pl.janusz.hain.androidplayground.models.people.person.Person;

public class PersonViewHolder extends ChildViewHolder {

    private TextView textViewPerson;
    private Person person;

    public PersonViewHolder(View itemView) {
        super(itemView);
        textViewPerson = (TextView) itemView.findViewById(R.id.personName);
    }

    public void bind(Person person) {
        this.person = person;
        textViewPerson.setText(person.toString());
    }

    public void setOnClickListener(GroupListFragment.GroupListFragmentInterface mListener){
        textViewPerson.setOnClickListener(view -> {
            mListener.onPersonChosenFromGroup(person);
        });
    }
}
