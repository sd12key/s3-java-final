package gym.workoutclasses;

import java.util.ArrayList;
import java.util.List;

import gym.users.childclasses.Trainer;

public class WorkoutClassService {

    public static boolean addNewWorkoutClass(WorkoutClass workout_class) {
        return WorkoutClassDAO.addNew(workout_class, true);
    }

    // overloaded method to add a new workout class without an ID
    public static boolean addWorkoutClass(String type, String description, Trainer trainer) {
        WorkoutClass new_wc = new WorkoutClass(type, description, trainer);
        return WorkoutClassDAO.addNew(new_wc);
    }

    public static boolean updateWorkoutClass(WorkoutClass workout_class) {
        return WorkoutClassDAO.updateWorkoutClass(workout_class);
    }

    public static boolean deleteWorkoutClass(int workoutclass_id) {
        return WorkoutClassDAO.deleteById(workoutclass_id);
    }

    public static boolean deleteWorkoutClass(WorkoutClass workout_class) {
        return WorkoutClassDAO.deleteById(workout_class.getId());
    }

    // overload to add checking that the trainer is the same as the one in the workout class
    public static boolean deleteWorkoutClass(int workoutclass_id, Trainer trainer) {
        WorkoutClass wc = getWorkoutClassById(workoutclass_id);
        if (wc != null && wc.getTrainer().getId() == trainer.getId()) {
            return deleteWorkoutClass(workoutclass_id);
        }
        return false;
    }

    public static WorkoutClass getWorkoutClassById(int workoutclass_id) {
        return WorkoutClassDAO.getById(workoutclass_id, true);
    }

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
            report.add(workout_class.toStringNoName());
        }
        return report;
    }

    public static List<String> getTrainerWorkoutClassesReport(Trainer trainer) {
        return getTrainerWorkoutClassesReport(trainer.getId());
    }


}
