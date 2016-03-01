package com.truspot.backend.abstracts;


import com.truspot.backend.interfaces.Identifiable;
import com.truspot.backend.interfaces.Validateable;
import static com.truspot.backend.OfyService.ofy;

/**
 * Created by yavoryordanov on 2/25/16.
 */
public abstract class AEntity<T> implements Identifiable, Validateable {
    public void save() throws Exception {
        checkValidation();

        ofy().save().entity(this).now();
    }

    public void remove() {
        ofy().delete().entity(this).now();
    }
}