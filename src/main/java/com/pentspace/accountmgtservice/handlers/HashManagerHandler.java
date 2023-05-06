package com.pentspace.accountmgtservice.handlers;

public interface HashManagerHandler {
    String hashData(String data);
    boolean validateData(String passwordPlainText, String databasePassword);

}
