/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.impl.AbstractDao;
import mapper.ServerMapper;
import model.ConnectHistoryEntity;
import model.ServerEntity;

/**
 *
 * @author trinh
 */
public class ServerController extends AbstractDao<ServerController> {
    
    public void create(ServerEntity newServer){
        String sql="INSERT INTO public.server ( ip, password) VALUES ( ?, ?)";
        insertOrUpdateOrDelete(sql, newServer.getIp(),newServer.getPassword());
    }
    
    
}
