package dao;

import mapper.IRowMapper;

import java.util.List;

public interface IGenericDao<T> {
    List<T> findByProperties(String sql, IRowMapper<T> mapper, Object... params);

}
