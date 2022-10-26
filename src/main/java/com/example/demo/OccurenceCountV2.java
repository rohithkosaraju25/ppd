package com.example.demo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class OccurenceCountV2 {
    public static void main(String[] args) {
        List<Claim> myList = new ArrayList<>();
        AtomicInteger count = new AtomicInteger(0);
        System.out.println(count.get());
        LocalDate date1 = LocalDate.of(22, 10, 10);
        LocalDate date2 = LocalDate.of(22, 10, 11);
        LocalDate date3 = LocalDate.of(22, 10, 12);

        myList.add(new Claim(date1, "Ajax", "AZ", "capgemini"));
        myList.add(new Claim(date1, "Ajax", "NY", "hartford")); // duplicate

        myList.add(new Claim(date1, "Carl", "AZ", "capgemini")); // duplicate
        myList.add(new Claim(date1, "Carl", "NY", "capgemini"));
        myList.add(new Claim(date1, "Carl", "TX", "hartford"));

        myList.add(new Claim(date1, "Bob", "AK", "capgemini"));

        myList.add(new Claim(date2, "Ajax", "AZ", "capgemini"));

        myList.add(new Claim(date3, "Carl", "TX", "hartford"));

        Map<LocalDate, List<Claim>> myMap = myList.stream().collect(Collectors.groupingBy(Claim::getAcctDate));

        for (Map.Entry<LocalDate, List<Claim>> entry : myMap.entrySet()) {
            List<Claim> claims = entry.getValue();
            List<String> states = claims.stream().map(i -> i.getState()).distinct().collect(Collectors.toList());
            List<String> names = claims.stream().map(i -> i.getClaimName()).distinct().collect(Collectors.toList());
            List<String> descriptions = claims.stream().map(i -> i.getDesc()).distinct().collect(Collectors.toList());
            // System.out.println(names + " " + states);
            int firstNameCount = 0;

            Flux.fromIterable(names).flatMap(name -> Flux.fromIterable(states)
                    .flatMap(state -> Flux.fromIterable(descriptions)
                            .flatMap(description -> {
                                AtomicInteger c = countOccurence(claims, names, name, state, description,
                                        firstNameCount, count);
                                System.out.println(c.get() + " ---");
                                count.addAndGet(c.get());
                                System.out.println(count + " ----");
                                System.out.println();
                                return Mono.empty();
                            })))
                    .subscribe();

        }
        System.out.println(count.get());
    }

    public static AtomicInteger countOccurence(List<Claim> claims, List<String> names, String name,
            String state, String description, int firstNameCount, AtomicInteger count) {
        Optional<Claim> checkIfClaim = checkClaimIfPresent(claims, name, state, description);
        if (names.indexOf(name) != 0) {
            boolean value = checkForSameStateDifferentCLiam(claims, names, name, state, description);
            if (value == true) {
                return new AtomicInteger(0);
            }
        }
        if (checkIfClaim.isPresent()) {
            if (names.indexOf(name) == 0 && firstNameCount != 0) {
                return new AtomicInteger(0);
            }
            // System.out.println(name + " " + state + " " + description);
            firstNameCount += 1;
            System.out.println(count.get() + " -");
            count.getAndIncrement();
            System.out.println(name + " " + state + " " + description);
            System.out.println(count.get() + " --");
        }
        return count;
    }

    public static boolean checkForSameStateDifferentCLiam(List<Claim> claims, List<String> names, String name,
            String state, String description) {
        Optional<Claim> checkIfClaim = checkClaimIfPresent(claims, name, state, description);
        List<String> checkNames = new ArrayList<>();
        for (String i : names) {
            checkNames.add(i);
        }
        boolean value = false;
        if (checkIfClaim.isPresent()) {
            checkNames = checkNames.subList(0, checkNames.indexOf(name));
            for (String i : checkNames) {
                Optional<Claim> checkClaim = checkClaimIfPresent(claims, i, state, description);
                if (checkClaim.isPresent()) {
                    value = true;
                    break;
                }
            }
        }

        return value;
    }

    private static Optional<Claim> checkClaimIfPresent(List<Claim> claims, String name, String state,
            String description) {
        return claims.stream()
                .filter(i -> i.getClaimName().equals(name) && i.getState().equals(state)
                        && i.getDesc().equals(description))
                .findAny();
    }
}
