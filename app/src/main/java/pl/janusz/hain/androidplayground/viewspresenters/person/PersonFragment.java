package pl.janusz.hain.androidplayground.viewspresenters.person;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.janusz.hain.androidplayground.R;
import pl.janusz.hain.androidplayground.models.people.person.Person;
import pl.janusz.hain.androidplayground.util.ImageUtils;

public class PersonFragment extends Fragment {
    private static final String ARG_PERSON = "person";
    private Person person;

    private OnPersonFragmentInteractionListener mListener;

    private Target target;

    @BindView(R.id.photo)
    protected ImageView imageView;

    @BindView(R.id.firstName)
    protected TextView textViewFirstName;

    @BindView(R.id.secondName)
    protected TextView textViewSecondName;

    @BindView(R.id.birthDate)
    protected TextView textViewBirthDate;

    @BindView(R.id.city)
    protected TextView textViewCity;

    @BindView(R.id.progressBar)
    protected ContentLoadingProgressBar contentLoadingProgressBar;


    public PersonFragment() {
    }

    public static PersonFragment newInstance(Person person) {
        PersonFragment fragment = new PersonFragment();
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
        injectPresenter();
    }

    private void injectPresenter() {
        DaggerPersonComponent.builder()
                .personPresenterModule(new PersonPresenterModule(this)).build()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTextViews();
        target = createNewTarget();
        setImage();

    }

    private void setTextViews() {
        if (person.getFirstName() != null && !person.getFirstName().equals("")) {
            textViewFirstName.setVisibility(View.VISIBLE);
            textViewFirstName.setText(person.getFirstName());
        }

        if (person.getSecondName() != null && !person.getSecondName().equals("")) {
            textViewSecondName.setVisibility(View.VISIBLE);
            textViewSecondName.setText(person.getSecondName());
        }

        if (person.getBirthDay() != null && !person.getBirthDay().equals("")) {
            textViewBirthDate.setVisibility(View.VISIBLE);
            textViewBirthDate.setText(person.getBirthDay());
        }

        if (person.getCity() != null && !person.getCity().equals("")) {
            textViewCity.setVisibility(View.VISIBLE);
            textViewCity.setText(person.getCity());
        }

    }

    private void setImage() {
        if (getContext() != null) {
            if (person.getPhotoUrl() != null && !person.getPhotoUrl().equals("")) {
                Picasso.with(getContext()).load(person.getPhotoUrl()).into(target);
            }
        }
    }

    private Target createNewTarget() {
        return new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Bitmap scaledBitmap = ImageUtils.scaleDown(bitmap, 200, true);
                imageView.setImageBitmap(scaledBitmap);
                contentLoadingProgressBar.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                contentLoadingProgressBar.setVisibility(View.VISIBLE);
            }
        };
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPersonFragmentInteractionListener) {
            mListener = (OnPersonFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPersonFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.personViewRelations)
    public void searchRelations() {
        mListener.onPersonSearchRelations(person);
    }

    public interface OnPersonFragmentInteractionListener {

        void onPersonSearchRelations(Person person);

    }
}
