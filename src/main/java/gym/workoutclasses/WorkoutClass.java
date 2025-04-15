package gym.workoutclasses;

import gym.users.childclasses.Trainer;

public class WorkoutClass {

    private final int id;
    private String type;
    private String description;
    private Trainer trainer;

    public WorkoutClass(int id, String type, String description, Trainer trainer) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.trainer = trainer;
    }

    // Constructor without ID, for new classes
    public WorkoutClass(String type, String description, Trainer trainer) {
        this(0, type, description, trainer); 
    }

    // Getters
    public int getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public String getDescription() {
        return this.description;
    }

    public Trainer getTrainer() {
        return this.trainer;
    }

    // Setters
    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    @Override
    public String toString() {
        return "(CID:" + this.id + ") " + this.type + ": " +
               this.description + " (by " + this.trainer.getFullName() + ")";
    }

    public String toStringNoId() {
        return this.type + ": " +
               this.description + " (by " + this.trainer.getFullName() + ")";
    }

    public String toStringNoName() {
        return "(CID:" + this.id + ") " + this.type + ": " + this.description;
    }




}
