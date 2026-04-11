package com.itandcstech.modernqrscanner;

public class Path {
    public static void main(String[] args) {
        // Retrieve the PATH environment variable
        String pathValue = System.getenv("PATH");

        System.out.println("--- Operating System PATH Variable ---");

        if (pathValue != null) {
            // Split the path into individual directories
            // Windows uses ';', while macOS/Linux use ':'
            String delimiter = System.getProperty("path.separator");
            String[] directories = pathValue.split(delimiter);

            System.out.println("Number of directories in PATH: " + directories.length);
            System.out.println("\nList of directories:");

            for (int i = 0; i < directories.length; i++) {
                System.out.println((i + 1) + ". " + directories[i]);
            }
        } else {
            System.out.println("The PATH variable is not defined.");
        }
    }
}