package org.example;

import java.util.Locale;

public class Writer {

    public void mainMenu(){
        String select;

        String menu = """
            CREATE BOOKING
            UPDATE BOOKING
            READ BOOKING
            DELETE BOOKING

            CREATE TABLES*
            CREATE GUESTS*
            """;
        select = IO.readln(menu).toLowerCase();

        switch (select) {
            case "create booking", "cb" -> createMenu();
            case "update booking", "ub" -> updateMenu();
            case "read booking", "rb" -> readMenu();
            case "delete booking", "db" -> deleteMenu();
            default -> mainMenu();
        }




    }

    private void deleteMenu() {

    }

    private void readMenu() {
    }

    private void updateMenu() {
    }

    private void createMenu() {
    }


}
