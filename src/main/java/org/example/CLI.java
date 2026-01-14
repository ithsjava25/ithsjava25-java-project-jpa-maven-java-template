package org.example;

public class CLI {
    DirectorService directorService;
    FilmService filmService;
    SeriesService seriesService;

    void cliStart(DirectorService directorService, FilmService filmService, SeriesService seriesService){
        this.directorService = directorService;
        this.filmService = filmService;
        this.seriesService = seriesService;

        mainMenu();
    }

    void mainMenu() {
        while(true) {

            System.out.println("""
                Welcome to the Film Database!

                Please select a category to view below:

                1. Directors
                2. Films
                3. Series

                4. Exit

                """);
            switch (IO.readln()) {
                case "1" -> directorMenu();
                case "2" -> filmMenu();
                case "3" -> seriesMenu();
                case "4" -> System.exit(0);
                default -> invalidInput();
            }
        }
    }

    private static void invalidInput() {
        System.out.println("Invalid input!" +
            "\nPlease enter the number of the option you wish to choose");
    }

    private void directorMenu() {
        System.out.println("""
            You are in the Director Menu.

            Please select an action below:

            1. Create new entry
            2. List entries
            3. List specific entry
            4. Update existing entry

            5. Exit

            """);
        switch (IO.readln()){
            case "1" -> createDirector();
            case "2" -> listDirectors();
            case "3" -> listSpecificDirector();
            case "4" -> updateDirector();
            case "5" -> mainMenu();
            default -> invalidInput();
        }
    }

    private void createDirector() {
        String name;
        String country;
        int birthYear;
        Integer yearOfDeath = null;
        try {
            name = IO.readln("Enter the full name of the Director: ");
            country = IO.readln("Enter the country of the Director: ");
            birthYear = Integer.parseInt(IO.readln("Enter the birth year of the Director: "));
            try {
                yearOfDeath = Integer.valueOf(IO.readln("Enter the year of death of the Director." +
                    "\nIf they're alive, leave blank: "));
            } catch (NumberFormatException _) {
            }

            Director newDirector = new Director();
            newDirector.setName(name);
            newDirector.setCountry(country);
            newDirector.setBirthYear(birthYear);
            newDirector.setYearOfDeath(yearOfDeath);
            directorService.create(newDirector);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
        }
    }

    private void listDirectors() {
        IO.println(directorService.findAll());
    }

    private void listSpecificDirector() {
        String input = IO.readln("Enter the ID or name of the Director: ");
        Director d;
        try{
            d = directorService.findDirectorId(Long.valueOf(input));
        } catch (NumberFormatException e) {
            d = directorService.findDirectorName(input);
        }


        IO.println("Name: " + d.getName() +
            "\nCountry: " +  d.getCountry() +
            "\nYear of birth: " + d.getBirthYear() +
            "\nYear of death: " + d.getYearOfDeath() +
            "Films:\n" +
            d.getFilms() +
            "\n\nSeries:\n" +
            d.getSeries());
    }

    private void updateDirector() {
        Long id = Long.valueOf(IO.readln("Enter the ID of the Director: "));

        IO.println("When prompted, enter the value you wish to update." +
            "\nIf you don't want to change it, leave the input blank.");

        String name = IO.readln("Enter the full name of the Director: ");
        String country = IO.readln("Enter the country of the Director: ");
        Integer birthYear = null;
        Integer yearOfDeath = null;
        try {
            birthYear = Integer.valueOf(IO.readln("Enter the birth year of the Director: "));
        } catch (NumberFormatException _) {
        }
        try {
            yearOfDeath = Integer.valueOf(IO.readln("Enter the year of death of the Director: "));
        } catch (NumberFormatException _) {
        }

        Director updatedDirector = directorService.findDirectorId(id);
        if(name != null && !name.isEmpty())
            updatedDirector.setName(name);
        if(country != null && !country.isEmpty())
            updatedDirector.setCountry(country);
        if(birthYear != null)
            updatedDirector.setBirthYear(birthYear);
        if(yearOfDeath != null)
            updatedDirector.setYearOfDeath(yearOfDeath);
        directorService.update(updatedDirector);

    }

    private void filmMenu() {
        System.out.println("""
            You are in the Film Menu.

            Please select an action below:

            1. Create new entry
            2. List entries
            3. List specific entry
            4. Update existing entry

            5. Exit

            """);
        switch (IO.readln()){
            case "1" -> createFilm();
            case "2" -> listFilms();
            case "3" -> listSpecificFilm();
            case "4" -> updateFilm();
            case "5" -> mainMenu();
            default -> invalidInput();
        }
    }

    private void createFilm() {
        String title = IO.readln("Enter the title of the Film: ");

        try {
            Film film = new Film();
            film.setTitle(title);
            film.setDirector(directorService.findDirectorId(Long.valueOf(IO.readln("Enter the ID of the Director: "))));
            filmService.create(film);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
        }
    }

    private void listFilms() {
        IO.println(filmService.findAll());
    }

    private void listSpecificFilm() {
        String input = IO.readln("Enter the ID or title of the Film: ");
        Film f;
        try{
            f = filmService.findFilmId(Long.valueOf(input));
        } catch (NumberFormatException e) {
            f = filmService.findFilmTitle(input);
        }

        IO.println("Title: " + f.getTitle() +
            "\nDirector: " + f.getDirector());
    }

    private void updateFilm() {
        try {
            IO.println("When prompted, enter the value you wish to update." +
                "\nIf you don't want to change it, leave the input blank.");
            Film film = filmService.findFilmId(Long.valueOf(IO.readln("Enter the ID of the Film: ")));
            String title = IO.readln("Enter the title of the Film: ");
            if(title != null && !title.isEmpty())
                film.setTitle(title);
            try {
                Director director = directorService.findDirectorId(Long.valueOf(IO.readln("Enter the ID of the Director: ")));
                film.setDirector(director);
            } catch (NumberFormatException _) {
            }

            filmService.update(film);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
        }
    }

    private void seriesMenu() {
        System.out.println("""
            You are in the Series Menu.

            Please select an action below:

            1. Create new entry
            2. List entries
            3. List specific entry
            4. Update existing entry

            5. Exit

            """);
        switch (IO.readln()){
            case "1" -> createSeries();
            case "2" -> listSeries();
            case "3" -> listSpecificSeries();
            case "4" -> updateSeries();
            case "5" -> mainMenu();
            default -> invalidInput();
        }
    }

    private void createSeries() {
        try {
            String title = IO.readln("Enter the title of the Series: ");
            int episodes = Integer.parseInt(IO.readln("Enter the number of episodes in the Series: "));
            int firstAired = Integer.parseInt(IO.readln("Enter the year the Series was first released: "));
            Integer lastAired = null;
            try {
                lastAired = Integer.valueOf(IO.readln("Enter the year the Series ended." +
                    "\nLeave blank if not yet finished: "));
            } catch (NumberFormatException _) {
            }
            String starActors = IO.readln("Enter the star actors of the Series: ");

            Series series = new Series();
            series.setTitle(title);
            series.setEpisodes(episodes);
            series.setFirstAired(firstAired);
            series.setLastAired(lastAired);
            series.setStarActors(starActors);

            seriesService.create(series);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
        }
    }

    private void listSeries() {
        IO.println(seriesService.findAll());
    }

    private void listSpecificSeries() {
        String title = IO.readln("Enter the ID or title of the Series: ");
        Series s;

        try {
            s = seriesService.findSeriesId(Long.valueOf(title));
        } catch (NumberFormatException e) {
            s= seriesService.findSeriesTitle(title);
        }

        IO.println("Title: " + s.getTitle() +
            "\nDirector: " + s.getDirectors() +
            "\nEpisodes: " + s.getEpisodes() +
            "\nFirst Aired: " + s.getFirstAired() +
            "\nLast Aired: " + s.getLastAired() +
            "\nStar Actors: " + s.getStarActors());

    }

    private void updateSeries() {
        IO.println("When prompted, enter the value you wish to update." +
            "\nIf you don't want to change it, leave the input blank.");
        try {
            Series series = seriesService.findSeriesId(Long.valueOf(IO.readln("Enter the ID of the Series: ")));
            String title = IO.readln("Enter the title of the Series: ");
            if (title != null && !title.isEmpty())
                series.setTitle(title);
            try {
                int episodes = Integer.parseInt(IO.readln("Enter the number of episodes in the Series: "));
                series.setEpisodes(episodes);
            } catch (NumberFormatException _) {
            }
            try {
                int firstAired = Integer.parseInt(IO.readln("Enter the year the Series was first released: "));
                series.setFirstAired(firstAired);
            } catch (NumberFormatException _) {
            }
            try {
                Integer lastAired = Integer.valueOf(IO.readln("Enter the year the Series ended: "));
                series.setLastAired(lastAired);
            } catch (NumberFormatException _) {
            }
            String starActors = IO.readln("Enter the star actors of the Series: ");
            if (starActors != null && !starActors.isEmpty())
                series.setStarActors(starActors);

            seriesService.update(series);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
        }

    }

}
