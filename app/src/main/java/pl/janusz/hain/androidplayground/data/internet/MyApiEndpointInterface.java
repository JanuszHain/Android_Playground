package pl.janusz.hain.androidplayground.data.internet;

import java.util.ArrayList;

import pl.janusz.hain.androidplayground.models.people.person.Person;
import pl.janusz.hain.androidplayground.models.people.relation.RelationDbForm;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface MyApiEndpointInterface {

    @GET("persons.php")
    Observable<ArrayList<Person>> getPersons(@Query("lastId") long lastId,
                                        @Query("limit") int limit,
                                        @Query("lastSecName") String lastSecondName
    );

    @GET("persons.php?")
    Observable<ArrayList<Person>> getPersons(@Query("limit") int limit);

    @GET("person.php?")
    Observable<Person> getPerson(@Query("id") long id);

    @GET("selected_persons.php")
    Observable<ArrayList<Person>> getSelectedPersons(
                                       @Query("personsIds[]") ArrayList<Long> personIds
    );

    @GET("relations.php?")
    Observable<ArrayList<RelationDbForm>> getRelations(@Query("personId") long id);

    @GET("relations.php?")
    Observable<ArrayList<RelationDbForm>> getRelations(@Query("personId") long id,
                                      @Query("limit") int limit
    );

    @GET("relations.php?")
    Observable<ArrayList<RelationDbForm>> getRelations(@Query("personId") long id,
                                      @Query("lastId") long lastId,
                                      @Query("limit") int limit
    );

    @GET("rest_of_relations.php?")
    Observable<ArrayList<RelationDbForm>> getRestOfRelations(@Query("personId") long id,
                                                  @Query("lastId") long lastId
    );

    @GET("relations_for_selected_persons.php?")
    Observable<ArrayList<RelationDbForm>> getRelationsWithSelectedPeople(@Query("idMainPerson") long id,
                                                                         @Query("personsIds[]") ArrayList<Long> personIds
    );
}
