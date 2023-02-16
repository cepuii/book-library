package ua.od.cepuii.library.dto;

import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
@Getter
public class Report {

    private final Map<String, String> reports;
    private boolean hasError;

    private Report() {
        this.reports = new HashMap<>();
        hasError = false;
    }

    private Report(String key, String value) {
        this.reports = new HashMap<>();
        reports.put(key, value);
        hasError = false;
    }

    private Report(String key, String value, boolean hasError) {
        this.reports = new HashMap<>();
        reports.put(key, value);
        this.hasError = hasError;
    }


    public static Report newInstance() {
        return new Report();
    }

    public static Report newInstance(String key, String value) {
        return new Report(key, value);
    }

    public static Report newInstance(String key, String value, boolean hasError) {
        return new Report(key, value, hasError);
    }

    public void mergeReports(Report report) {
        reports.putAll(report.getReports());
    }

    public Map<String, String> getReports() {
        return reports;
    }

    public void addReport(String key, String value) {
        reports.put(key, value);
    }

    public void addError(String key, String value) {
        hasError = true;
        reports.put(key, value);
    }


    public boolean hasReports() {
        return !reports.isEmpty();
    }

    public boolean hasErrors() {
        return hasError;
    }

    public String getReport(String key) {
        return reports.get(key);
    }


}
