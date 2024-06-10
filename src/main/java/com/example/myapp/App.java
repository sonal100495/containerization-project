package com.example.myapp;

public class App {
    public static void main(String[] args) {
        while (true) {
            System.out.println("Hello, World!");
            try {
                Thread.sleep(1000); // Sleep for 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
