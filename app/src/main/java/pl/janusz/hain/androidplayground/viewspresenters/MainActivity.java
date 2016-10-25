package pl.janusz.hain.androidplayground.viewspresenters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.janusz.hain.androidplayground.R;
import pl.janusz.hain.androidplayground.models.people.group.Group;
import pl.janusz.hain.androidplayground.models.people.person.Person;
import pl.janusz.hain.androidplayground.util.ActivityUtils;
import pl.janusz.hain.androidplayground.util.FragmentTags;
import pl.janusz.hain.androidplayground.viewspresenters.groups.GroupListFragment;
import pl.janusz.hain.androidplayground.viewspresenters.main.MainMenuFragment;
import pl.janusz.hain.androidplayground.viewspresenters.person.PersonFragment;
import pl.janusz.hain.androidplayground.viewspresenters.persons.PersonListFragment;
import pl.janusz.hain.androidplayground.viewspresenters.persons.knownpersons.KnownPersonListFragment;

public class MainActivity extends AppCompatActivity implements
        PersonListFragment.OnPersonListInteractionListener,
        PersonFragment.OnPersonFragmentInteractionListener,
        KnownPersonListFragment.OnKnownPersonListInteractionListener,
        GroupListFragment.GroupListFragmentInterface {

    @BindView(R.id.mainFrameLayout)
    protected FrameLayout mainFrameLout;

    @Nullable
    @BindView(R.id.secondFrameLayout)
    protected FrameLayout secondFrameLayout;

    @BindBool(R.bool.big_screen)
    protected boolean bigScreen;

    @Inject
    MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        addMainMenuFragment();
        injectPresenter();
        mainPresenter.start();
    }

    private void addMainMenuFragment() {
        Fragment mainMenuFragment = getSupportFragmentManager().findFragmentById(R.id.mainFrameLayout);
        if (mainMenuFragment == null) {
            mainMenuFragment = MainMenuFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mainMenuFragment, R.id.mainFrameLayout);
        }
    }

    private void injectPresenter() {
        DaggerMainComponent.builder()
                .mainPresenterModule(new MainPresenterModule(this)).build()
                .inject(this);
    }

    public void onClickSavedPersons(View v) {
        PersonListFragment personListFragment = PersonListFragment.newInstance(FragmentTags.localPersonList);
        ActivityUtils.replaceFragmentInActivityWithSavingBackstack(getSupportFragmentManager(), personListFragment, R.id.mainFrameLayout);
    }

    public void onClickInternetPersons(View v) {
        PersonListFragment personListFragment = PersonListFragment.newInstance(FragmentTags.internetPersonList);
        ActivityUtils.replaceFragmentInActivityWithSavingBackstack(getSupportFragmentManager(), personListFragment, R.id.mainFrameLayout);
    }

    @Override
    public void onPersonListFragmentInteraction(Person person) {
        PersonFragment personFragment = PersonFragment.newInstance(person);
        if (!bigScreen) {
            ActivityUtils.replaceFragmentInActivityWithSavingBackstack(getSupportFragmentManager(), personFragment, R.id.mainFrameLayout);
        } else {
            ActivityUtils.replaceFragmentInActivityWithSavingBackstack(getSupportFragmentManager(), personFragment, R.id.secondFrameLayout);
        }
    }

    @Override
    public void onPersonSearchRelations(Person person) {
        KnownPersonListFragment knownPersonListFragment = KnownPersonListFragment.newInstance(person);
        if (!bigScreen) {
            ActivityUtils.replaceFragmentInActivityWithSavingBackstack(getSupportFragmentManager(), knownPersonListFragment, R.id.mainFrameLayout);
        } else {
            ActivityUtils.replaceFragmentInActivityWithSavingBackstack(getSupportFragmentManager(), knownPersonListFragment, R.id.secondFrameLayout);
        }
    }

    @Override
    public void onGroupsCreated(ArrayList<Group> groups, Person person) {
        GroupListFragment groupListFragment = GroupListFragment.newInstance(groups, person);
        if (!bigScreen) {
            ActivityUtils.replaceFragmentInActivityWithSavingBackstack(getSupportFragmentManager(), groupListFragment, R.id.mainFrameLayout);
        } else {
            ActivityUtils.replaceFragmentInActivityWithSavingBackstack(getSupportFragmentManager(), groupListFragment, R.id.secondFrameLayout);
        }
    }

    //I decided to make different method for each call from fragment for future possible differences
    @Override
    public void onPersonChosenFromGroup(Person person) {
        PersonFragment personFragment = PersonFragment.newInstance(person);
        if (!bigScreen) {
            ActivityUtils.replaceFragmentInActivityWithSavingBackstack(getSupportFragmentManager(), personFragment, R.id.mainFrameLayout);
        } else {
            ActivityUtils.replaceFragmentInActivityWithSavingBackstack(getSupportFragmentManager(), personFragment, R.id.secondFrameLayout);
        }
    }
}
