package com.example.demo;

import java.util.*;

public class Atom {
    public static void main(String[] args) {
        // AtomicInteger a = new AtomicInteger(0);
        // a.incrementAndGet();
        // System.out.println(a);
        List<String> list = Arrays.asList("Fire", "fire", "water", "Water");
        long count = list.stream().map(String::toLowerCase).distinct().count();
        System.out.println(list);
        System.out.println(count);
    }
}
