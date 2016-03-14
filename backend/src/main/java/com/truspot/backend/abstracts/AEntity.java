package com.truspot.backend.abstracts;


import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.truspot.backend.interfaces.Identifiable;
import com.truspot.backend.interfaces.Validateable;
import java.util.List;
import static com.truspot.backend.OfyService.ofy;

/**
 * Created by yavoryordanov on 2/25/16.
 */
public abstract class AEntity<T> implements Identifiable, Validateable {

    // public methods
    public void save() throws Exception {
        checkValidation();

        ofy().save().entity(this).now();
    }

    public void remove() {
        ofy().delete().entity(this).now();
    }

    // public static methods
    public static <T> Key<T> createKey(Class<T> clazz, long id) {
        return Key.create(clazz, id);
    }

    public static <T> List<Key<T>> findKeysByParent(Class<T> clazz, Key parent) {
        return ofy().load().type(clazz).ancestor(parent).keys().list();
    }

    public static <T> T findFirst(Class<T> clazz) {
        return ofy().load().type(clazz).first().now();
    }

    public static <T> T findFirstSafe(Class<T> clazz) {
        return ofy().load().type(clazz).first().safe();
    }

    public static <T> List<T> findByParent(Class<T> clazz, Key parent) {
        return ofy().load().type(clazz).ancestor(parent).list();
    }

    public static <T> T findByKey(Key<T> key) {
        return ofy().load().key(key).now();
    }

    public static <T> T findByKeySafe(Key<T> key) {
        return ofy().load().key(key).safe();
    }

    public static <T> T findById(Class<T> clazz, long id) {
        return ofy().load().type(clazz).id(id).now();
    }

    public static <T> T findByIdSafe(Class<T> clazz, long id) {
        return ofy().load().type(clazz).id(id).safe();
    }

    public static <T> List<T> findAll(Class<T> clazz) {
        return ofy().load().type(clazz).list();
    }

    public static <T> void deleteByKey(Key<T> key) {
        ofy().delete().key(key).now();
    }

    public static <T> void deleteById(Class<T> clazz, long id) {
        ofy().delete().type(clazz).id(id).now();
    }

    public static <T> void deleteByIds(Class<T> clazz, long... ids) {
        ofy().delete().type(clazz).ids(ids).now();
    }

    public static <T> void deleteByParent(Class<T> clazz, Key parent) {
        List<Key<T>> keys = findKeysByParent(clazz, parent);

        if (keys != null && !keys.isEmpty()) {
            ofy().delete().keys(keys).now();
        }
    }
}