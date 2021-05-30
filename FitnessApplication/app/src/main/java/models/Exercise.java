package models;

public class Exercise {

    private String name;
    private String fitnessProgram;

    public Exercise() {

    }

    public Exercise(String name, String fitnessProgram) {
        this.name = name;
        this.fitnessProgram = fitnessProgram;
    }

    public String getName() {
        return this.name;
    }
    public String getFitnessProgram() { return this.fitnessProgram; }
}
