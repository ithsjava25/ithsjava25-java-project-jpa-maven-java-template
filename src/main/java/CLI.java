public class CLI {

    void mainMenu(){
        System.out.println("""
            Welcome to the Film Database!

            Please select a category to view below:

            1. Directors
            2. Films
            3. Series

            4. Exit

            """);
        switch (System.console().readLine()){
            case "1" -> directorMenu();
            case "2" -> filmMenu();
            case "3" -> seriesMenu();
            case "4" -> System.exit(0);
            default -> invalidInput();
        }
    }

    private static void invalidInput() {
        System.out.println("Invalid input!" +
            "PLease enter the number of the option you wish to choose");
    }

    private void directorMenu() {
        System.out.println("""
            You are in the Director Menu.

            Please select an action below:

            1. Create new entry
            2. List entries
            3. Update existing entry

            4. Exit

            """);
        switch (System.console().readLine()){
            case "1" -> createDirector();
            case "2" -> listDirectors();
            case "3" -> updateDirector();
            case "4" -> mainMenu();
            default -> invalidInput();
        }
    }

    private void createDirector() {
    }

    private void listDirectors() {
    }

    private void updateDirector() {
    }

    private void filmMenu() {
        System.out.println("""
            You are in the Film Menu.

            Please select an action below:

            1. Create new entry
            2. List entries
            3. Update existing entry

            4. Exit

            """);
        switch (System.console().readLine()){
            case "1" -> createFilm();
            case "2" -> listFilms();
            case "3" -> updateFilm();
            case "4" -> mainMenu();
            default -> invalidInput();
        }
    }

    private void createFilm() {
    }

    private void listFilms() {
    }

    private void updateFilm() {
    }

    private void seriesMenu() {
        System.out.println("""
            You are in the Series Menu.

            Please select an action below:

            1. Create new entry
            2. List entries
            3. Update existing entry

            4. Exit

            """);
        switch (System.console().readLine()){
            case "1" -> createSeries();
            case "2" -> listSeries();
            case "3" -> updateSeries();
            case "4" -> mainMenu();
            default -> invalidInput();
        }
    }

    private void createSeries() {
    }

    private void listSeries() {
    }

    private void updateSeries() {
    }

}
