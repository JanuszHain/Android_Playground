package pl.janusz.hain.androidplayground.models.people.relation;

import java.io.Serializable;

import pl.janusz.hain.androidplayground.models.people.person.Person;

public class Relation implements  Serializable {
    private Person personA;
    private Person personB;

    public Relation(Person lPerson, Person rPerson) {
        this.personA = lPerson;
        this.personB = rPerson;
    }

    public Person getPersonA() {
        return personA;
    }

    public Person getPersonB() {
        return personB;
    }

    public Person getAnotherPerson(Person person) {
        if (person.equals(personA)) {
            return personB;
        } else {
            return personA;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Relation relation = (Relation) o;

        if (personA.equals(relation.personA) || personA.equals(relation.personB)) {
            if (personB.equals(relation.personA) || personB.equals(relation.personB)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = personA.hashCode();
        result = result + personB.hashCode();
        return result;
    }

}
