package com.tracker.app.model;

public class ReportingLocationStatistics implements Comparable<ReportingLocationStatistics> {

    private Disease disease;
    private String state;
    private String country;
    private long latestTotalCases;
    private Integer diffFromPrevPeriod;
    private String asOfDate;

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public Integer getDiffFromPrevPeriod() {
        return diffFromPrevPeriod;
    }

    public void setDiffFromPrevPeriod(Integer diffFromPrevPeriod) {
        this.diffFromPrevPeriod = diffFromPrevPeriod;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getLatestTotalCases() {
        return latestTotalCases;
    }

    public void setLatestTotalCases(long latestTotalCases) {
        this.latestTotalCases = latestTotalCases;
    }

    public String getAsOfDate() {
        return asOfDate;
    }

    public void setAsOfDate(String asOfDate) {
        this.asOfDate = asOfDate;
    }

    @Override
    public String toString() {
        return "LocationStats{" +
                "disease=" + disease +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", latestTotalCases=" + latestTotalCases +
                '}';
    }

    @Override
    public int compareTo(ReportingLocationStatistics o) {
        int otherDiff = o.diffFromPrevPeriod == null ? 0 : o.diffFromPrevPeriod;
        int thisDiff = this.diffFromPrevPeriod == null ? 0 : this.diffFromPrevPeriod;
        return otherDiff - thisDiff;
    }
}
