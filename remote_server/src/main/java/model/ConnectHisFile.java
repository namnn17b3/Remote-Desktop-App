package model;

import java.io.Serializable;

public class ConnectHisFile implements Serializable{
    private String ipServer;
    private String ipClient;
    private String timeCreated;
    private String timeEnded;
    private String connectedTimePeriod;

    private ConnectHisFile() {
    }
    public static class Builder {
        private String ipServer;
        private String ipClient;
        private String timeCreated;
        private String timeEnded;
        private String connectedTimePeriod;

        public Builder withIpServer(String ipServer) {
            this.ipServer = ipServer;
            return this;
        }

        public Builder withIpClient(String ipClient) {
            this.ipClient = ipClient;
            return this;
        }

        public Builder withTimeCreated(String timeCreated) {
            this.timeCreated = timeCreated;
            return this;
        }

        public Builder withTimeEnded(String timeEnded) {
            this.timeEnded = timeEnded;
            return this;
        }

        public Builder withConnectedTimePeriod(String connectedTimePeriod) {
            this.connectedTimePeriod = connectedTimePeriod;
            return this;
        }

        public ConnectHisFile build() {
            ConnectHisFile connectHisFile = new ConnectHisFile();
            connectHisFile.ipServer = this.ipServer;
            connectHisFile.ipClient = this.ipClient;
            connectHisFile.timeCreated = this.timeCreated;
            connectHisFile.timeEnded = this.timeEnded;
            connectHisFile.connectedTimePeriod = this.connectedTimePeriod;
            return connectHisFile;
        }
    }

    public String getIpServer() {
        return ipServer;
    }

    public String getIpClient() {
        return ipClient;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public String getTimeEnded() {
        return timeEnded;
    }

    public String getConnectedTimePeriod() {
        return connectedTimePeriod;
    }


    @Override
    public String toString() {
        return "ConnectHisFile{" +
                "ipServer='" + ipServer + '\'' +
                ", ipClient='" + ipClient + '\'' +
                ", timeCreated='" + timeCreated + '\'' +
                ", timeEnded='" + timeEnded + '\'' +
                ", connectedTimePeriod='" + connectedTimePeriod + '\'' +
                '}';
    }
    
    public Object[] toObject(){
        return new Object[]{
                ipServer,ipClient,timeCreated,timeEnded
        };
    }
}
