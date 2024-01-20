package dao.impl;



import dao.IGenericDao;
import mapper.IRowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public abstract class AbstractDao<T> implements IGenericDao<T> {

    private PreparedStatement preparedStatement = null;

    ResourceBundle resourceBundle = ResourceBundle.getBundle("db");
    public Connection getConnection() {
        String url = resourceBundle.getString("url");
        String username = resourceBundle.getString("username");
        String password = resourceBundle.getString("password");
        try {
            Class.forName(resourceBundle.getString("driverName"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<T> findByProperties(String sql, IRowMapper<T> mapper, Object... params){
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<T> data = new ArrayList<T>();
        try {
            preparedStatement = connection.prepareStatement(sql);
//            setParam(preparedStatement,params);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                T object = mapper.mapFromDbToClass(resultSet);
                data.add(object);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            disconectSafe(connection, preparedStatement);
        }
        return data;
    }

    public Integer countTotalRecords(String sql, Object... params ){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Integer totalRecord = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                totalRecord = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
           disconectSafe(connection, preparedStatement);
        }
        return totalRecord;

    }


    public T findOne(String sql, IRowMapper<T> mapper, Object...params){
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        T data = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            setParam(preparedStatement,params);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                data = mapper.mapFromDbToClass(resultSet);
                return data;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            disconectSafe(connection, preparedStatement);
        }
        return data;
    }

    private static void disconectSafe(Connection connection, PreparedStatement preparedStatement) {
        if (connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (preparedStatement != null){
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertOrUpdateOrDelete(String sql,Object... param){
        PreparedStatement preparedStatement = null;
        Connection connection = getConnection();

        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            setParam(preparedStatement,param);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }finally {
                disconectSafe(connection, preparedStatement);
            }
        }
    }

    public boolean update(String sql,Object... param){
        PreparedStatement preparedStatement = null;
        Connection connection = getConnection();

        try {
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            setParam(preparedStatement,param);
            preparedStatement.executeUpdate();
            connection.commit();
            return true;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        }finally {
            disconectSafe(connection, preparedStatement);
        }
    }


    private void setParam(PreparedStatement preparedStatement,Object... param) throws SQLException {
        for (int i = 0; i < param.length; i++) {
            int index = i + 1;
            Object value = param[i];
            if (value instanceof String){
                preparedStatement.setString(index,(String) value);
            }else if (value instanceof Integer){
                preparedStatement.setInt(index, (Integer) value);
            }else if (value instanceof Boolean){
                preparedStatement.setBoolean(index,(Boolean) value);
            }else if (value instanceof Timestamp){
                preparedStatement.setTimestamp(index,(Timestamp) value);
            }else if (value instanceof Long){
                preparedStatement.setLong(index,(Long)value);
            }else if (value instanceof Double){
                preparedStatement.setDouble(index,(Double)value);
            }
        }
    }
}
