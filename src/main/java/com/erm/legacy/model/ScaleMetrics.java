package com.erm.legacy.model;

/**
 * DTO for Complexity-table scale metrics shown on the dashboard.
 * Demo is capped at &lt;5K LOC vs production ~2.2M LOC.
 */
public class ScaleMetrics {

    private final String productAge;
    private final String linesOfCodeDemo;
    private final String linesOfCodeProduction;
    private final int productModules;
    private final String microservicesNote;
    private final String apisNote;
    private final String tablesNote;
    private final String frameworksNote;
    private final String integrationsNote;
    private final String regionsNote;

    public ScaleMetrics(String productAge, String linesOfCodeDemo, String linesOfCodeProduction,
                        int productModules, String microservicesNote, String apisNote,
                        String tablesNote, String frameworksNote, String integrationsNote,
                        String regionsNote) {
        this.productAge = productAge;
        this.linesOfCodeDemo = linesOfCodeDemo;
        this.linesOfCodeProduction = linesOfCodeProduction;
        this.productModules = productModules;
        this.microservicesNote = microservicesNote;
        this.apisNote = apisNote;
        this.tablesNote = tablesNote;
        this.frameworksNote = frameworksNote;
        this.integrationsNote = integrationsNote;
        this.regionsNote = regionsNote;
    }

    public String getProductAge() { return productAge; }
    public String getLinesOfCodeDemo() { return linesOfCodeDemo; }
    public String getLinesOfCodeProduction() { return linesOfCodeProduction; }
    public int getProductModules() { return productModules; }
    public String getMicroservicesNote() { return microservicesNote; }
    public String getApisNote() { return apisNote; }
    public String getTablesNote() { return tablesNote; }
    public String getFrameworksNote() { return frameworksNote; }
    public String getIntegrationsNote() { return integrationsNote; }
    public String getRegionsNote() { return regionsNote; }
}
