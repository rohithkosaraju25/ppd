package com.example.demo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.util.ObjectUtils;

public class OccurenceCount {
    public static void main(String[] args) {
        List<Claim> myList = new ArrayList<>();
        int count = 0;
        LocalDate date1 = LocalDate.of(22, 10, 10);
        LocalDate date2 = LocalDate.of(22, 10, 11);
        LocalDate date3 = LocalDate.of(22, 10, 12);
        LocalDate date4 = LocalDate.of(22, 10, 13);

        myList.add(new Claim(date1, "Ajax", "AZ", "capgemini"));
        myList.add(new Claim(date1, "Ajax", "NY", "hartford")); // duplicate

        myList.add(new Claim(date1, "Carl", "AZ", "capgemini")); // duplicate
        myList.add(new Claim(date1, "Carl", "NY", "capgemini")); // duplicate
        myList.add(new Claim(date1, "Carl", "TX", "hartford")); // duplicate

        myList.add(new Claim(date1, "Bob", "AK", "capgemini"));

        myList.add(new Claim(date2, "Ajax", "AZ", "capgemini"));

        myList.add(new Claim(date3, "Bob", "TX", "hartford"));
        myList.add(new Claim(date3, "Carl", "TX", "hartford")); // duplicate
        myList.add(new Claim(date3, "Carl", "NY", "hartford")); // duplicate

        myList.add(new Claim(date4, "Carl", "NY", null));
        myList.add(new Claim(date4, "Bob", "NY", null));

        Map<LocalDate, List<Claim>> myMap = myList.stream().collect(Collectors.groupingBy(Claim::getAcctDate));

        for (Map.Entry<LocalDate, List<Claim>> entry : myMap.entrySet()) {
            List<Claim> claims = entry.getValue();
            List<String> states = claims.stream().map(i -> i.getState()).distinct().collect(Collectors.toList());
            List<String> names = claims.stream().map(i -> i.getClaimName()).distinct().collect(Collectors.toList());
            List<String> description = claims.stream().map(i -> i.getDesc()).distinct().collect(Collectors.toList());
            System.out.println(names + "  " + states + " " + description);
            for (String name : names) {
                int firstNameCount = 0;
                for (String state : states) {
                    for (String desc : description) {
                        if (ObjectUtils.isEmpty(desc)) {
                            if (checkIfClaimWithoutDescriptionExists(claims, name, state, desc)) {
                                count += 1;
                                break;
                            }
                        }
                        Optional<Claim> checkIfClaim = checkClaimIfPresent(claims, name, state, desc);
                        if (checkIfClaim.isPresent()) {
                            if (names.indexOf(name) != 0) {
                                boolean value = checkForSameStateDifferentCLiam(claims, names, name, state, desc);
                                if (value == true) {
                                    firstNameCount += 1;
                                    break;
                                }
                            }
                            if (firstNameCount != 0) {
                                break;
                            }
                            System.out.println(name + " " + state + " " + desc);
                            firstNameCount += 1;
                            count = count + 1;

                        }
                    }
                }
            }
        }
        System.out.println(count);
    }

    public static boolean checkForSameStateDifferentCLiam(List<Claim> claims, List<String> names, String name,
            String state, String description) {
        List<String> checkNames = new ArrayList<>();
        for (String i : names) {
            checkNames.add(i);
        }
        boolean value = false;
        checkNames = checkNames.subList(0, checkNames.indexOf(name));
        for (String i : checkNames) {
            Optional<Claim> checkClaim = checkClaimIfPresent(claims, i, state, description);
            if (checkClaim.isPresent()) {
                value = true;
                break;
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

    public static boolean checkIfClaimWithoutDescriptionExists(List<Claim> claims, String name, String state,
            String description) {

        return claims.stream()
                .filter(i -> i.getClaimName().equals(name) && i.getState().equals(state)
                        && ObjectUtils.isEmpty(i.getDesc()))
                .findAny().isPresent();
    }
}
