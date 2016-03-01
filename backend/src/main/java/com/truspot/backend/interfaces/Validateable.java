package com.truspot.backend.interfaces;

/**
 * Created by yavoryordanov on 2/25/16.
 */
public interface Validateable {
    /**
     * If object is not valid throws Exception.
     * */
    public void checkValidation() throws Exception;
}
