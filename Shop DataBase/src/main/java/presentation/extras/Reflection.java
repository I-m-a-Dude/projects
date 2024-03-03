package presentation.extras;

import java.lang.reflect.Field;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class Reflection {

    public static List<Entry<String, Object>> retrieveProperties(Object object) {
        List<Entry<String, Object>> fieldEntries = new ArrayList<>();
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(object);
                fieldEntries.add(new SimpleEntry<>(field.getName(), value));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return fieldEntries;
    }
}