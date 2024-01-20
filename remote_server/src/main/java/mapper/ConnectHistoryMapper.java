package mapper;

import model.ConnectHistoryEntity;
import utils.MapUtils;

import java.sql.ResultSet;

public class ConnectHistoryMapper implements IRowMapper<ConnectHistoryEntity> {
    @Override
    public ConnectHistoryEntity mapFromDbToClass(ResultSet resultSet) {
        return MapUtils.mapRowFromTableToClass(new ConnectHistoryEntity(),resultSet);
    }
}