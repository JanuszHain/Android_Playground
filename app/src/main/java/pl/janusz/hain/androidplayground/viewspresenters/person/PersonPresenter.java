package pl.janusz.hain.androidplayground.viewspresenters.person;

import javax.inject.Inject;

public class PersonPresenter {
    private final PersonFragment personFragment;

    @Inject
    public PersonPresenter(PersonFragment personFragment) {
        this.personFragment = personFragment;
    }
}
