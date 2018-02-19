package com.gerp83.modelparser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GErP83
 * a class witch can parse json values into class fields
 */
public class ModelParser {

    /**
     * automatically parse json to fields with String
     *
     * @param jsonString JsonObject String to parse
     */
    public static void parseAll(Object object, String jsonString) {

        if (jsonString == null) {
            return;
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        parseJsonObject(object, jsonObject);
    }

    /**
     * automatically parse json to fields
     *
     * @param jsonObject JsonObject to parse
     */
    public static void parseAll(Object object, JSONObject jsonObject) {

        parseJsonObject(object, jsonObject);

    }

    /**
     * parse a key into a field
     *
     * @param key try to parse the key into the field, the field name equals the key
     */
    public static void parse(Object object, JSONObject jsonObject, String key) {

        if (key == null) {
            return;
        }

        parse(object, jsonObject, key, key, null);
    }

    /**
     * parse a key into a field
     *
     * @param fieldName the field name into parse to
     * @param key       the key for search in the JsonObject
     */
    public static void parse(Object object, JSONObject jsonObject, String fieldName, String key) {

        if (fieldName == null || key == null) {
            return;
        }

        parse(object, jsonObject, fieldName, key, null);
    }

    /**
     * parse a key into a field with fallback class, its handy if the server sometimes not gives the same value
     *
     * @param fieldName        the field name into parse to
     * @param key              the key for search in the JsonObject
     * @param mapFallBackClass Class for fallback
     */
    public static void parse(Object object, JSONObject jsonObject, String fieldName, String key, Class mapFallBackClass) {

        if (object == null || jsonObject == null || fieldName == null || key == null) {
            return;
        }

        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            parseKeyToField(object, jsonObject, field, key, mapFallBackClass);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    private static void parseJsonObject(Object object, JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }

        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            parseKeyToField(object, jsonObject, field, field.getName(), null);
        }
    }

    private static void parseKeyToField(Object object, JSONObject jsonObject, Field field, String key, Class mapFallBackClass) {

        try {
            Class classType = field.getType();
            field.setAccessible(true);

            if (ModelClass.class.isAssignableFrom(classType)) {
                if(!hasKey(jsonObject, key)) {
                    field.setAccessible(false);
                    return;
                }
                Object mappedValue = parseValueFromJson(jsonObject, JSONObject.class, key, null);
                Class<?> clazz = Class.forName(classType.getName());
                Constructor<?> constructor = clazz.getConstructor(JSONObject.class);
                Object classObject = constructor.newInstance(mappedValue);
                field.set(object, classObject);

            } else if (List.class.isAssignableFrom(classType)) {
                if(!hasKey(jsonObject, key)) {
                    field.setAccessible(false);
                    return;
                }
                JSONArray mappedValue = (JSONArray) parseValueFromJson(jsonObject, JSONArray.class, key, null);
                ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                if (parameterizedType != null) {
                    Class<?> genericType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                    if (genericType != null) {

                        List arrayList = new ArrayList();
                        int size = mappedValue.length();
                        for (int i = 0; i < size; i++) {

                            if (ModelClass.class.isAssignableFrom(genericType) && mappedValue.getJSONObject(i) != null) {
                                Class<?> clazz = Class.forName(genericType.getName());
                                Constructor<?> constructor = clazz.getConstructor(JSONObject.class);
                                Object classObject = constructor.newInstance(mappedValue.getJSONObject(i));
                                arrayList.add(classObject);

                            } else {
                                Object itemValue = null;
                                if (genericType == int.class || genericType == Integer.class) {
                                    itemValue = mappedValue.getInt(i);

                                } else if (genericType == long.class || genericType == Long.class) {
                                    itemValue = mappedValue.getLong(i);

                                } else if (genericType == String.class) {
                                    itemValue = mappedValue.getString(i);

                                } else if (genericType == double.class || genericType == Double.class) {
                                    itemValue = mappedValue.getDouble(i);

                                } else if (genericType == boolean.class || genericType == Boolean.class) {
                                    itemValue = mappedValue.getBoolean(i);
                                }
                                arrayList.add(itemValue);
                            }

                        }
                        field.set(object, arrayList);
                    }
                }

            } else {
                if(!hasKey(jsonObject, key)) {
                    field.setAccessible(false);
                    return;
                }
                Object mappedValue = parseValueFromJson(jsonObject, classType, key, mapFallBackClass);
                field.set(object, mappedValue);
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static Object parseValueFromJson(JSONObject jsonObject, Class classType, String key, Class mapFallBackClass) {

        Object object = null;
        try {
            if (classType == int.class || classType == Integer.class) {
                object = jsonObject.getInt(key);

            } else if (classType == long.class || classType == Long.class) {
                object = jsonObject.getLong(key);

            } else if (classType == String.class) {
                object = jsonObject.isNull(key) ? null : jsonObject.getString(key);

            } else if (classType == double.class || classType == Double.class) {
                object = jsonObject.getDouble(key);

            } else if (classType == boolean.class || classType == Boolean.class) {
                object = jsonObject.getBoolean(key);

            } else if (classType == JSONObject.class) {
                object = jsonObject.getJSONObject(key);

            } else if (classType == JSONArray.class) {
                object = jsonObject.getJSONArray(key);

            }
        } catch (Throwable e) {
            e.printStackTrace();
            if (mapFallBackClass == null) {
                return getDefaultValue(classType);

            } else {
                return parseValueFromJson(jsonObject, mapFallBackClass, key, null);

            }
        }

        return object;
    }

    private static boolean hasKey(JSONObject jsonObject, String key) {
        return jsonObject.has(key);
    }

    private static Object getDefaultValue(Class classType) {

        if (classType == int.class || classType == Integer.class) {
            return 0;

        } else if (classType == long.class || classType == Long.class) {
            return 0;

        } else if (classType == String.class) {
            return null;

        } else if (classType == double.class || classType == Double.class) {
            return 0;

        } else if (classType == boolean.class || classType == Boolean.class) {
            return false;

        } else if (classType == JSONObject.class) {
            return null;

        } else if (classType == JSONArray.class) {
            return null;

        }
        return null;
    }

}