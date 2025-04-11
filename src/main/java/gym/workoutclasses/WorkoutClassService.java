package gym.workoutclasses;

import java.util.ArrayList;
import java.util.List;

import gym.users.childclasses.Trainer;

public class WorkoutClassService {

    public static List<WorkoutClass> getAllWorkoutClasses() {
        return WorkoutClassDAO.getAll();
    }

    public static List<WorkoutClass> getWorkoutClassesByTrainer(Trainer trainer) {
        return WorkoutClassDAO.getAllByTrainerId(trainer.getId());
    }

    public static List<WorkoutClass> getWorkoutClassesByTrainer(int trainer_id) {
        return WorkoutClassDAO.getAllByTrainerId(trainer_id);
    }

    public static List<String> getAllWorkoutClassesReport() {
        List<String> report = new ArrayList<>();
        List<WorkoutClass> workout_classes = getAllWorkoutClasses();
        if (workout_classes == null || workout_classes.isEmpty()) {
            report.add("No workout classes found.");
            return report;
        }
        for (WorkoutClass workout_class : workout_classes) {
            report.add(workout_class.toString());
        }
        return report;
    }

    public static List<String> getTrainerWorkoutClassesReport(int trainer_id) {
        List<String> report = new ArrayList<>();
        List<WorkoutClass> workout_classes = getWorkoutClassesByTrainer(trainer_id);
        if (workout_classes == null || workout_classes.isEmpty()) {
            report.add(">>> No workout classes found.");
            return report;
        }
        report.add(">>> Workout Classes:");
        for (WorkoutClass workout_class : workout_classes) {
            report.add(workout_class.toString());
        }
        return report;
    }

    public static List<String> getTrainerWorkoutClassesReport(Trainer trainer) {
        return getTrainerWorkoutClassesReport(trainer.getId());
    }


}
