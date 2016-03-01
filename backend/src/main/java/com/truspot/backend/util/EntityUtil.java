package com.truspot.backend.util;

import com.googlecode.objectify.Ref;
import com.truspot.backend.interfaces.Identifiable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yavoryordanov on 2/24/16.
 */
public class EntityUtil {

    public static <T extends Identifiable> void addRefToList(boolean add, List<Ref<T>> list, T element) {
        int position = -1;

        for (int i = 0, z = list.size(); i < z; i++) {
            Ref<T> obj = list.get(i);

            if (obj.key().getId() == element.getId()) {
                position = i;

                break;
            }
        }

        if (add) {
            if (position == -1) {
                list.add(Ref.create(element));
            }
        } else {
            if (position != -1) {
                list.remove(position);
            }
        }
    }

    public static <T> List<T> getElements(List<Ref<T>> refs) {
        List<T> res = new ArrayList<>();

        for (Ref<T> ref : refs) {
            res.add(ref.get());
        }

        return res;
    }
}
