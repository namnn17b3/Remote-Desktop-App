/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mapper;

import java.sql.ResultSet;
import model.ConnectHistoryEntity;
import model.ServerEntity;
import utils.MapUtils;

/**
 *
 * @author trinh
 */
public class ServerMapper implements IRowMapper<ServerEntity>{

    @Override
    public ServerEntity mapFromDbToClass(ResultSet resultSet) {
        return MapUtils.mapRowFromTableToClass(new ServerEntity(),resultSet);
    }

}