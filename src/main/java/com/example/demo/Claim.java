package com.example.demo;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Claim {
    LocalDate AcctDate;
    String claimName;
    String state;
    String desc;
}
