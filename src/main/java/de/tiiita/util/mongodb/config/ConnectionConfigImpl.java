package de.tiiita.util.mongodb.config;

public class ConnectionConfigImpl implements ConnectionConfig {

    private String user;
    private char[] password;
    private String host;
    private int port;
    private String database;

    ConnectionConfigImpl(String user, char[] password, String host, int port, String database) {
        this.user = user;
        this.password = password;
        this.host = host;
        this.port = port;
        this.database = database;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public char[] getPassword() {
        return password;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getDatabase() {
        return database;
    }

}
