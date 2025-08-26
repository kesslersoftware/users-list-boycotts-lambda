package com.boycottpro.userboycotts.model;

import java.util.Objects;

public class CompanySummary {

    private String company_id;
    private String company_name;

    public CompanySummary() {
    }

    public CompanySummary(String company_id, String company_name) {
        this.company_id = company_id;
        this.company_name = company_name;
    }

    public String getCompany_id() {
        return company_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompanySummary)) return false;
        CompanySummary that = (CompanySummary) o;
        return Objects.equals(company_id, that.company_id) &&
                Objects.equals(company_name, that.company_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(company_id, company_name);
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }
}
