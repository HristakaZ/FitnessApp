package models;

public class Exercise {

    private String name;
    private String cloudImagePath;
    private String fitnessProgram;

    public Exercise() {

    }

    public Exercise(String name, String cloudImagePath, String fitnessProgram) {
        this.name = name;
        this.cloudImagePath = cloudImagePath;
        this.fitnessProgram = fitnessProgram;
    }

    public String getName() {
        return this.name;
    }
    public String getCloudImagePath() { return this.cloudImagePath; }
    public String getFitnessProgram() { return this.fitnessProgram; }
}
