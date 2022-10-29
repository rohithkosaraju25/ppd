package com.example.demo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.springframework.util.ObjectUtils;

public class OccurrenceCount6 {
    public static void main(String[] args) throws IOException {
        List<Claim> myList = new ArrayList<>();
        List<Claim> nonDupClaims = new ArrayList<>();

        FileWriter fw = new FileWriter("C:\\Users\\rohit\\OneDrive\\out.txt");
        BufferedWriter writer = new BufferedWriter(fw);
        LocalDate date1 = LocalDate.of(22, 10, 10);
        LocalDate date2 = LocalDate.of(22, 10, 11);
        LocalDate date3 = LocalDate.of(22, 10, 12);
        LocalDate date4 = LocalDate.of(22, 10, 13);
        LocalDate date5 = LocalDate.of(22, 10, 14);
        LocalDate date6 = LocalDate.of(22, 10, 15);
        myList.add(new Claim(date1, "Ajax", "AZ", "capgemini"));
        myList.add(new Claim(date1, "Ajax", "NY", "hartford")); // duplicate

        myList.add(new Claim(date1, "Carl", "AZ", "hartford"));
        myList.add(new Claim(date1, "Carl", "NY", "capgemini")); // duplicate
        myList.add(new Claim(date1, "Carl", "TX", "hartford")); // duplicate

        myList.add(new Claim(date1, "Bob", "AK", "capgemini"));

        myList.add(new Claim(date2, "Ajax", "AZ", "capgemini"));
        myList.add(new Claim(date2, "Bob", "AK", "capgemini"));

        myList.add(new Claim(date3, "Bob", "TX", "hartford"));
        myList.add(new Claim(date3, "Carl", "TX", "hartford")); // duplicate
        myList.add(new Claim(date3, "Carl", "NY", "hartford")); // duplicate

        myList.add(new Claim(date4, "Carl", "NY", null));
        myList.add(new Claim(date4, "Bob", "NY", null));
        myList.add(new Claim(date4, "Derrek", "AK", "hartford"));
        myList.add(new Claim(date4, null, "AK", "Hartford")); // duplicate
        myList.add(new Claim(date4, null, "AK", null));
        myList.add(new Claim(date4, null, "NY", null));
        myList.add(new Claim(date4, null, "NY", null));

        myList.add(new Claim(date5, null, "NY", "capgemini"));
        myList.add(new Claim(date5, null, "AK", "hartford"));

        myList.add(new Claim(date5, null, "NY", "capgemini")); // duplicate
        myList.add(new Claim(date5, null, "NY", "capgemini"));// duplicate

        myList.add(new Claim(date5, null, "NY", null));
        myList.add(new Claim(date5, null, "NY", "hartford"));

        myList.add(new Claim(date6, "Carl", null, "capgemini"));
        myList.add(new Claim(date6, "Bob", null, "capgemini"));
        Map<LocalDate, List<Claim>> myMap = myList.stream().collect(Collectors.groupingBy(Claim::getAcctDate));

        myMap.forEach((date, claims) -> {
            claims.forEach(checkClaim -> {
                if (claims.indexOf(checkClaim) == 0 && Collections.frequency(claims, checkClaim) == 1) {
                    nonDupClaims.add(checkClaim);
                } else if (nonDupClaims.contains(checkClaim)
                        && !claimNameCheck(nonDupClaims.get(nonDupClaims.indexOf(checkClaim)), checkClaim)
                        && ObjectUtils.isEmpty(checkClaim.getDesc())) {
                    nonDupClaims.add(checkClaim);
                } else {
                    if (!nonDupClaims.contains(checkClaim) &&
                            !checkForDuplicateClaims(claims, checkClaim, claims.indexOf(checkClaim))) {
                        nonDupClaims.add(checkClaim);
                    }
                }
            });
        });
        for (Claim claim : nonDupClaims) {
            writer.write(claim.toString());
            writer.write("\n");
            System.out.println(claim);
        }
        writer.write(String.valueOf(nonDupClaims.size()));
        System.out.println(nonDupClaims.size());
        writer.close();
    }

    private static boolean checkForDuplicateClaims(List<Claim> claims, Claim checkClaim,
            int index) {
        AtomicBoolean duplicateFound = new AtomicBoolean(false);
        for (int i = 0; i < index; i++) {
            Claim copyClaim = claims.get(i);
            if (copyClaim.getAcctDate().equals(checkClaim.getAcctDate()) &&
                    (claimNameCheck(copyClaim, checkClaim) ||
                            stateAndDescriptionCheck(copyClaim, checkClaim))) {
                duplicateFound.set(true);
            }
        }
        return duplicateFound.get();
    }

    private static boolean stateAndDescriptionCheck(Claim claim, Claim checkClaim) {
        boolean value = !ObjectUtils.isEmpty(claim.getState()) && !ObjectUtils.isEmpty(checkClaim.getState())
                && checkClaim.getState().equalsIgnoreCase(claim.getState())
                && !ObjectUtils.isEmpty(claim.getDesc()) && !ObjectUtils.isEmpty(checkClaim.getDesc())
                && checkClaim.getDesc().equalsIgnoreCase(claim.getDesc());
        return value;
    }

    private static boolean claimNameCheck(Claim claim, Claim checkClaim) {
        boolean value = !ObjectUtils.isEmpty(claim.getClaimName()) && !ObjectUtils.isEmpty(checkClaim.getClaimName())
                && checkClaim.getClaimName().equalsIgnoreCase(claim.getClaimName());
        return value;
    }

}
