package com.e_eduspace.forms.model.constants;

import com.e_eduspace.identify.IDenConstants;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017-06-08.
 */

public class FormCons implements IDenConstants {
    public static LinkedHashMap<String, Map<String, Integer>> generate() {
        LinkedHashMap<String, Map<String, Integer>> map = new LinkedHashMap<>();
        for (int i = 0; i < APPLY_FORM_ONE.length; i++) {
            String name = APPLY_FORM_ONE[i];
            String[] elementNames = AREA_ONE_ELEMENT[i];
            Integer[] elementTypes = AREA_ONE_ELEMENT_TYPE[i];
            LinkedHashMap<String, Integer> elements = new LinkedHashMap<>();
            for (int j = 0; j < elementNames.length; j++) {
                elements.put(elementNames[j],elementTypes[j]);
            }
            map.put(name, elements);
        }
        return map;
    }
}
