package pl.janusz.hain.androidplayground.models.people.person;

import java.io.Serializable;
import java.util.Comparator;

public class PersonComparator implements Comparator<Person>, Serializable {

    @Override
    public int compare(Person lPerson, Person rPerson) {
        if (compareSeoondName(lPerson, rPerson) != 0) {
            return compareSeoondName(lPerson, rPerson);
        }
        else{
            if (compareFirstName(lPerson, rPerson)!=0){
                return compareFirstName(lPerson, rPerson);
            }
            else{
                return compareNetworkId(lPerson, rPerson);
            }
        }
    }

    private int compareSeoondName(Person lPerson, Person rPerson) {
        return lPerson.getSecondName().compareTo(rPerson.getSecondName());
    }

    private int compareFirstName(Person lPerson, Person rPerson){
        return lPerson.getFirstName().compareTo(rPerson.getFirstName());
    }

    private int compareNetworkId(Person lPerson, Person rPerson){
        long lPersonId = lPerson.getIdNetworkDatabase();
        long rPersonId = rPerson.getIdNetworkDatabase();

        if(lPersonId > rPersonId){
            return 1;
        }
        if(lPersonId < rPersonId){
            return -1;
        }
        return 0;
    }
}
