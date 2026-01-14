package org.example.ui;

import java.util.Scanner;

public class InputValidator {

    public static int getIntInput(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = scanner.nextInt();
                scanner.nextLine();

                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.printf("❌ Måste vara mellan %d och %d. Försök igen.%n", min, max);
                }
            } catch (Exception e) {
                System.out.println("❌ Ogiltigt nummer. Försök igen.");
                scanner.nextLine();
            }
        }
    }

    public static long getLongInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                long value = scanner.nextLong();
                scanner.nextLine();
                return value;
            } catch (Exception e) {
                System.out.println("❌ Ogiltigt nummer. Försök igen.");
                scanner.nextLine();
            }
        }
    }

    public static String getNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("❌ Fältet får inte vara tomt. Försök igen.");
            }
        }
    }
}
