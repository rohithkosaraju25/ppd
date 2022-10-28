package com.example.demo;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.time.LocalDate;

public class Atom {
    public static void main(String[] args) {
        List<Claim> myList = new ArrayList<>();
        LocalDate date1 = LocalDate.of(22, 10, 10);
        myList.add(new Claim(date1, "Ajax", "AZ", "capgemini"));
        myList.add(new Claim(date1, "Ajax", "NY", "Hartford")); // duplicate

        myList.add(new Claim(date1, "Carl", "AZ", "hartford"));
        myList.add(new Claim(date1, "Carl", "NY", "Capgemini")); // duplicate
        myList.add(new Claim(date1, "Carl", "TX", "hartFord")); // duplicate

        myList.add(new Claim(date1, "Bob", "AK", "capgemini"));

        long count = myList.stream()
                .filter(distinctByKey(claim -> Arrays.asList(claim.getAcctDate(), claim.getDesc()))).count();
        System.out.println(
                myList.stream()
                        .filter(distinctByKey(claim -> Arrays.asList(claim.getAcctDate(), claim.getDesc())))
                        .collect(Collectors.toList()));
        long newcount = myList.stream()
                .filter(distinctByKey(claim -> Arrays.asList(claim.getAcctDate(), claim.getDesc().toLowerCase())))
                .count();
        System.out.println(
                myList.stream()
                        .filter(distinctByKey(
                                claim -> Arrays.asList(claim.getAcctDate(), claim.getDesc().toLowerCase())))
                        .collect(Collectors.toList()));
        System.out.println(count);
        System.out.println(newcount);

    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> key = ConcurrentHashMap.newKeySet();
        return t -> key.add(keyExtractor.apply(t));
    }
}
