package pl.janusz.hain.androidplayground.models.people.relation;

public class RelationDbForm {

    private long idPersonA;
    private long idPersonB;
    private long id;

    public RelationDbForm() {
    }

    public long getId() {
        return id;
    }

    public long getIdPersonA() {
        return idPersonA;
    }

    public long getIdPersonB() {
        return idPersonB;
    }
}
