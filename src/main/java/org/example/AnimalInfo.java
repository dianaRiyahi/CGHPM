package org.example;

public class AnimalInfo {
    private String name;
    private double massMale;
    private double massFemale;
    private int lifespanMale;
    private int lifespanFemale;
    private String historicalPopulation;
    private String currentPopulation;
    private double populationGrowth;

    // Constructor
    public AnimalInfo(String name, double massMale, double massFemale, int lifespanMale, int lifespanFemale, String historicalPopulation, String currentPopulation, double populationGrowth) {
        this.name = name;
        this.massMale = massMale;
        this.massFemale = massFemale;
        this.lifespanMale = lifespanMale;
        this.lifespanFemale = lifespanFemale;
        this.historicalPopulation = historicalPopulation;
        this.currentPopulation = currentPopulation;
        this.populationGrowth = populationGrowth;
    }

    // Getters
    public String getName() { return name; }
    public double getMassMale() { return massMale; }
    public double getMassFemale() { return massFemale; }
    public int getLifespanMale() { return lifespanMale; }
    public int getLifespanFemale() { return lifespanFemale; }
    public String getHistoricalPopulation() { return historicalPopulation; }
    public String getCurrentPopulation() { return currentPopulation; }
    public double getPopulationGrowth() { return populationGrowth; }
}

