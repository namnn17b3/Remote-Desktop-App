package utils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MapUtils {
    public static <T> T mapRowFromTableToClass(T object, ResultSet resultSet){
        Field[] fields = object.getClass().getDeclaredFields(); // lay ra tat ca properties cua class
        // duyet de set du lieu cho cac field
        for (Field field: fields
        ) {
            field.setAccessible(true);
            Object valueFromDb = null;
            try {
                // lay du lieu tu ResultSet trong database
                valueFromDb = resultSet.getObject(field.getName(),field.getType());
                // set du lieu cho field
                field.set(object,valueFromDb);
            } catch (SQLException | IllegalAccessException e) {
                e.getMessage();
                e.printStackTrace();
            }
        }
        return object;
    }
}
