package mapper;

import java.sql.ResultSet;

public interface IRowMapper<T>{
    T mapFromDbToClass(ResultSet resultSet);
}