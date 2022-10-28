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

public class OccurrenceCount5 {
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
            claims.forEach(claim -> {
                Claim checkClaim = getACopyOfClaim(claim);
                if (claims.indexOf(claim) == 0 && Collections.frequency(claims, claim) == 1) {
                    nonDupClaims.add(checkClaim);
                } else if (nonDupClaims.contains(checkClaim)
                        && !claimNameCheck(claim, checkClaim)
                        && ObjectUtils.isEmpty(claim.getDesc())) {
                    nonDupClaims.add(checkClaim);
                } else {
                    if (!nonDupClaims.contains(checkClaim) &&
                            !checkForDuplicateClaims(nonDupClaims, claims, checkClaim, claims.indexOf(claim))) {
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

    private static boolean checkForDuplicateClaims(List<Claim> nonDupClaims, List<Claim> claims, Claim checkClaim,
            int index) {
        AtomicBoolean duplicateFound = new AtomicBoolean(false);
        System.out.println("index" + index);
        for (int i = 0; i < index; i++) {
            Claim copyClaim = getACopyOfClaim(claims.get(i));
            if (copyClaim.getAcctDate().equals(checkClaim.getAcctDate()) &&
                    (claimNameCheck(copyClaim, checkClaim) ||
                            stateAndDescriptionCheck(copyClaim, checkClaim))) {
                duplicateFound.set(true);
            }
        }

        return duplicateFound.get();
    }

    private static boolean stateAndDescriptionCheck(Claim claim, Claim checkClaim) {
        boolean value = checkClaim.getState().equalsIgnoreCase(claim.getState())
                && !claim.getState().equalsIgnoreCase("") &&
                checkClaim.getDesc().equalsIgnoreCase(claim.getDesc()) && !claim.getDesc().equalsIgnoreCase("");
        return value;
    }

    private static boolean claimNameCheck(Claim claim, Claim checkClaim) {
        boolean value = checkClaim.getClaimName().equalsIgnoreCase(claim.getClaimName())
                && !claim.getClaimName().equalsIgnoreCase("");
        return value;
    }

    private static Claim getACopyOfClaim(Claim claim) {
        Claim claimCopy = new Claim();
        claimCopy.setAcctDate(claim.getAcctDate());
        String claimantName = ObjectUtils.isEmpty(claim.getClaimName()) ? "" : claim.getClaimName();
        claimCopy.setClaimName(claimantName);
        String state = ObjectUtils.isEmpty(claim.getState()) ? "" : claim.getState();
        claimCopy.setState(state);
        String desc = ObjectUtils.isEmpty(claim.getDesc()) ? "" : claim.getDesc();
        claimCopy.setDesc(desc);
        return claimCopy;
    }

}
