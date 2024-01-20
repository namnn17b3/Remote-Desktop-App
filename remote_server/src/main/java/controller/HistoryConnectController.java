package controller;

import dao.impl.AbstractDao;
import mapper.ConnectHistoryMapper;
import model.ConnectHistoryEntity;

import java.util.List;

public class HistoryConnectController extends AbstractDao<ConnectHistoryEntity> {

    public List<ConnectHistoryEntity> findAll() {
        String sql="SELECT idserver, idclient, dayconnect, daydisconnect FROM connect";

        List<ConnectHistoryEntity> list = findByProperties(sql, new ConnectHistoryMapper());

        return list.isEmpty() ? null : list;
    }

}
