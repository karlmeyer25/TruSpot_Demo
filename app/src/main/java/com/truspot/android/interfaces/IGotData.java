package com.truspot.android.interfaces;

import com.truspot.backend.api.model.MapSettings;
import com.truspot.backend.api.model.VenueFull;

import java.util.List;

/**
 * Created by yavoryordanov on 3/15/16.
 */
public interface IGotData {
    List<VenueFull> getVenues();
    MapSettings getMapSettings();
}
