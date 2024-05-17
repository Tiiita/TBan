package de.tiiita.util.mongodb.config;

public interface ConnectionConfig {

    int MONGODB_DEFAULT_PORT = 27017;

    String getUser();
    char[] getPassword();
    String getHost();
    int getPort();
    String getDatabase();

    static ConnectionConfig create(String user, char[] password, String host, int port, String database) {
        return new ConnectionConfigImpl(user, password, host, port, database);
    }
}
