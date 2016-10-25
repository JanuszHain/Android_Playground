package pl.janusz.hain.androidplayground.models.people.person;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashSet;

import pl.janusz.hain.androidplayground.models.people.relation.Relation;

public final class Person implements Serializable {
    private transient long idLocal;
    @SerializedName("id")
    private long idNetworkDatabase;
    private String firstName;
    private String secondName;
    private String birthDate;
    private String city;
    private String photoUrl;

    private transient HashSet<Relation> relations;


    public Person(long idNetworkDatabase) {
        this.idNetworkDatabase = idNetworkDatabase;
    }

    public Person(long idNetworkDatabase, String firstName, String secondName, String age, String city, String photoUrl) {
        this.idNetworkDatabase = idNetworkDatabase;
        this.firstName = firstName;
        this.secondName = secondName;
        this.birthDate = age;
        this.city = city;
        this.photoUrl = photoUrl;

    }

    public synchronized  void setRelation(Relation relation) {

        if(relations==null){
            relations = new HashSet<>();
        }

        relations.add(relation);
    }

    public String getFirstName() {
        return firstName;
    }

    public long getIdNetworkDatabase() {
        return idNetworkDatabase;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getBirthDay() {
        return birthDate;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return firstName + " " + secondName;
    }

    public void setIdLocal(long idLocal) {
        this.idLocal = idLocal;
    }

    public long getIdLocal() {
        return idLocal;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public HashSet<Relation> getRelations() {
        return relations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        return getIdNetworkDatabase() == person.getIdNetworkDatabase();

    }

    @Override
    public int hashCode() {
        return (int) (getIdNetworkDatabase() ^ (getIdNetworkDatabase() >>> 32));
    }
}
