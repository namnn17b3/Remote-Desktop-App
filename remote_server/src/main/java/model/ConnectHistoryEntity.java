package model;

import java.sql.Timestamp;
import java.util.Date;

public class ConnectHistoryEntity {
    Integer idserver;
    Integer idclient;
    Timestamp dayconnect;
    Timestamp daydisconnect;

    public Integer getIdserver() {
        return idserver;
    }

    public void setIdserver(Integer idserver) {
        this.idserver = idserver;
    }

    public Integer getIdclient() {
        return idclient;
    }

    public void setIdclient(Integer idclient) {
        this.idclient = idclient;
    }

    public Timestamp getDayconnect() {
        return dayconnect;
    }

    public void setDayconnect(Timestamp dayconnect) {
        this.dayconnect = dayconnect;
    }

    public Timestamp getDaydisconnect() {
        return daydisconnect;
    }

    public void setDaydisconnect(Timestamp daydisconnect) {
        this.daydisconnect = daydisconnect;
    }
    
    public Object[] toObject(){
        return new Object[]{
                idserver,idclient,dayconnect,daydisconnect
        };
    }

}
