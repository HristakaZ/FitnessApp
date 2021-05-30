package models;

public class Exercise {

    private String name;
    private FitnessProgram fitnessProgram;

    public Exercise() {

    }

    public Exercise(String name, FitnessProgram fitnessProgram) {
        this.name = name;
        this.fitnessProgram = fitnessProgram;
    }

    public String getName() {
        return this.name;
    }
    public FitnessProgram getFitnessProgram() { return this.fitnessProgram; }
}
