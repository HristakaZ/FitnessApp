package models;

public class FitnessProgram {
    private String name;
    private String user;

    public FitnessProgram() {

    }
    public FitnessProgram(String name, String user) {
        this.name = name;
        this.user = user;
    }
    public String getName() {
        return this.name;
    }
    public String getUser() { return this.user; }
}
