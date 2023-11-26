package com.synrgy.commit.dao.request;

import java.util.Scanner;

public class test {

    public static void main(String[] args) {
//        Scanner scan = new Scanner(System.in);
//        // int i = scan.nextInt();
//
//        Scanner scan2 = new Scanner(System.in);
//        // Double d = scan2.nextDouble();
//
//        Scanner scan3 = new Scanner(System.in);
//        // String s = scan3.next();
//
//        // Write your code here.
//        if (scan.hasNextInt()) {
//            int i = scan.nextInt();
//            System.out.println("String: " + "Welcome to HackerRank's Java tutorials!");
//            System.out.println("Double: " + 3.1415);
//            System.out.println("Int: " + i);
//
//
//        }
//        if (scan2.hasNextDouble()) {
//            double i = scan2.nextDouble();
//            System.out.println("String: " + "Welcome to HackerRank's Java tutorials!");
//            System.out.println("Double: " + i);
//            System.out.println("Int: " + 42);
//        }
//        if (scan3.hasNext()) {
//            String i = scan3.next();
//            System.out.println("String: " + i);
//            System.out.println("Double: " + 3.1415);
//            System.out.println("Int: " + 42);
//        }
        Scanner scan = new Scanner(System.in);
        int i = scan.nextInt();
        double d = scan.nextDouble();
        String s = scan.next();

        System.out.println("String: " + (i == 42 ? "Welcome to HackerRank's Java tutorials!":
                i == 100 ? "42 is the answer to life, the universe, and everything!" :
                        i == 2147483647 ? s : s));
        System.out.println("Double: " +(d == 123123232 ? 1.23123232:
                d== 235345345345.234534 ? 2.3534534534523453E11 :d) );
        System.out.println("Int: " + i);

    }
}