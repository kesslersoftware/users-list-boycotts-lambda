package com.boycottpro.userboycotts.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompanySummaryTest {

    @Test
    void getCompany_id() {
        CompanySummary companySummary = new CompanySummary("123", "Test Company");
        assertEquals("123", companySummary.getCompany_id());
    }

    @Test
    void setCompany_id() {
        CompanySummary companySummary = new CompanySummary();
        companySummary.setCompany_id("123");
        assertEquals("123", companySummary.getCompany_id());
    }

    @Test
    void getCompany_name() {
        CompanySummary companySummary = new CompanySummary("123", "Test Company");
        assertEquals("Test Company", companySummary.getCompany_name());
    }

    @Test
    void setCompany_name() {
        CompanySummary companySummary = new CompanySummary();
        companySummary.setCompany_name("Test Company");
        assertEquals("Test Company", companySummary.getCompany_name());
    }
}