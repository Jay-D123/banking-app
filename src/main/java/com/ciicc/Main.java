package com.ciicc;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()){
            System.out.println("Connected to the database");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}