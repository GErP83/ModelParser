package com.gerp83.modelparser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by GErP83
 * converts custom Class to JSONObject
 */

public class ModelJsonUtil {

    /**
     * Class to JSONObject conversion
     *
     * @param mainObject Class from create JSONObject
     */
    public static JSONObject toJson(Object mainObject) {
        return toJson(mainObject, null);
    }

    /**
     * Class to JSONObject conversion
     *
     * @param mainObject Class from create JSONObject
     * @param fieldKeys class fields which will be not added to JSON, this
     *                  only works pn the parent class
     */
    public static JSONObject toJson(Object mainObject, ArrayList<String> fieldKeys) {

        JSONObject jsonObject = new JSONObject();
        try {

            Field[] fields = mainObject.getClass().getDeclaredFields();
            for (Field field : fields) {

                if(fieldKeys != null && fieldKeys.contains(field.getName())) {
                    continue;
                }
                Class classType = field.getType();
                field.setAccessible(true);

                if (isPrimitiveOrSimpleClass(classType)) {
                    jsonObject.put(field.getName(), field.get(mainObject));

                } else if (AbstractMap.class.isAssignableFrom(classType)) {
                    AbstractMap classObject = (AbstractMap) field.get(mainObject);
                    if (classObject != null && classObject.size() > 0) {
                        JSONObject mapObject = new JSONObject();
                        Set keys = classObject.keySet();

                        for (Object key : keys) {
                            Object value = classObject.get(key);
                            Class itemClassType = value.getClass();
                            if (isPrimitiveOrSimpleClass(itemClassType)) {
                                mapObject.put(key.toString(), value);
                            } else {
                                mapObject.put(key.toString(), toJson(value));
                            }
                        }
                        jsonObject.put(field.getName(), mapObject);
                    }

                } else if (List.class.isAssignableFrom(classType)) {
                    List classObject = (List) field.get(mainObject);
                    if (classObject != null && classObject.size() > 0) {
                        JSONArray array = new JSONArray();
                        int size = classObject.size();
                        for (int i = 0; i < size; i++) {
                            Object itemObject = classObject.get(i);
                            Class itemClassType = itemObject.getClass();
                            if (isPrimitiveOrSimpleClass(itemClassType)) {
                                array.put(itemObject);
                            } else {
                                array.put(toJson(itemObject));
                            }
                        }
                        jsonObject.put(field.getName(), array);
                    }

                } else {
                    Object classObject = field.get(mainObject);
                    if (classObject != null) {
                        jsonObject.put(field.getName(), toJson(classObject));
                    }

                }
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    private static boolean isPrimitiveOrSimpleClass(Class classType) {
        return classType.isPrimitive() ||
                classType == String.class ||
                classType == Integer.class ||
                classType == Long.class ||
                classType == Double.class ||
                classType == Boolean.class;
    }

}
