package gym.workoutclasses;

import gym.database.DatabaseConnection;
import gym.database.SQLTemplates;
import gym.database.DBConst;
import gym.users.User;
import gym.users.UserDAO;
import gym.users.childclasses.Trainer;
import gym.users.interfaces.RoleBasedAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public abstract class WorkoutClassDAO {

    public static boolean addNew(WorkoutClass workout_class, Connection conn, boolean exit_on_error) {
        conn = DatabaseConnection.ensureConnection(conn, exit_on_error);
        PreparedStatement ps = null;
        try {
            ps = DatabaseConnection.prepareStatement(conn, SQLTemplates.SQL_INSERT_WORKOUT_CLASS, exit_on_error);
            DatabaseConnection.psSetString(ps, 1, workout_class.getType(), exit_on_error);
            DatabaseConnection.psSetString(ps, 2, workout_class.getDescription(), exit_on_error);
            DatabaseConnection.psSetInt(ps, 3, workout_class.getTrainer().getId(), exit_on_error);

            int rows_inserted = DatabaseConnection.executeUpdate(ps, exit_on_error);
            return rows_inserted > 0;
        } finally {
            DatabaseConnection.closeStatement(ps, exit_on_error);
        }
    }

    public static int addNewReturnId(WorkoutClass workout_class, Connection conn, boolean exit_on_error) {
        conn = DatabaseConnection.ensureConnection(conn, exit_on_error);
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = DatabaseConnection.prepareStatement(conn, SQLTemplates.SQL_INSERT_WORKOUT_CLASS_RETURN_ID, exit_on_error);
            DatabaseConnection.psSetString(ps, 1, workout_class.getType(), exit_on_error);
            DatabaseConnection.psSetString(ps, 2, workout_class.getDescription(), exit_on_error);
            DatabaseConnection.psSetInt(ps, 3, workout_class.getTrainer().getId(), exit_on_error);

            rs = DatabaseConnection.executeQuery(ps, exit_on_error);
            if (DatabaseConnection.rsNext(rs, exit_on_error)) {
                return DatabaseConnection.rsGetInt(rs, DBConst.WorkoutClasses.ID, exit_on_error);
            } else {
                if (exit_on_error) {
                    System.err.println("WorkoutClass insert failed: no ID returned.");
                    System.exit(1);
                }
                return -1;
            }
        } finally {
            DatabaseConnection.closeResultSet(rs, exit_on_error);
            DatabaseConnection.closeStatement(ps, exit_on_error);
        }
    }

    public static boolean updateById(WorkoutClass workout_class, Connection conn, boolean exit_on_error) {
        conn = DatabaseConnection.ensureConnection(conn, exit_on_error);
        PreparedStatement ps = null;

        try {
            ps = DatabaseConnection.prepareStatement(conn, SQLTemplates.SQL_UPDATE_WORKOUT_CLASS_BY_ID, exit_on_error);
            DatabaseConnection.psSetString(ps, 1, workout_class.getType(), exit_on_error);
            DatabaseConnection.psSetString(ps, 2, workout_class.getDescription(), exit_on_error);
            DatabaseConnection.psSetInt(ps, 3, workout_class.getTrainer().getId(), exit_on_error);
            DatabaseConnection.psSetInt(ps, 4, workout_class.getId(), exit_on_error);

            int rows_updated = DatabaseConnection.executeUpdate(ps, exit_on_error);
            return rows_updated > 0;
        } finally {
            DatabaseConnection.closeStatement(ps, exit_on_error);
        }
    }

    public static boolean deleteById(int id, Connection conn, boolean exit_on_error) {
        conn = DatabaseConnection.ensureConnection(conn, exit_on_error);
        PreparedStatement ps = null;

        try {
            ps = DatabaseConnection.prepareStatement(conn, SQLTemplates.SQL_DELETE_WORKOUT_CLASS_BY_ID, exit_on_error);
            DatabaseConnection.psSetInt(ps, 1, id, exit_on_error);

            int rows_deleted = DatabaseConnection.executeUpdate(ps, exit_on_error);
            return rows_deleted > 0;
        } finally {
            DatabaseConnection.closeStatement(ps, exit_on_error);
        }
    }

    public static ArrayList<WorkoutClass> getAll(Connection conn, boolean exit_on_error) {
        conn = DatabaseConnection.ensureConnection(conn, exit_on_error);
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<WorkoutClass> classes = new ArrayList<>();

        try {
            ps = DatabaseConnection.prepareStatement(conn, SQLTemplates.SQL_SELECT_ALL_WORKOUT_CLASSES, exit_on_error);
            rs = DatabaseConnection.executeQuery(ps, exit_on_error);

            while (DatabaseConnection.rsNext(rs, exit_on_error)) {
                WorkoutClass wc = buildFromResultSet(rs, conn, exit_on_error);
                if (wc != null) {
                    classes.add(wc);
                }
            }

            return classes;
        } finally {
            DatabaseConnection.closeResultSet(rs, exit_on_error);
            DatabaseConnection.closeStatement(ps, exit_on_error);
        }
    }

    public static ArrayList<WorkoutClass> getAllByTrainerId(int trainer_id, Connection conn, boolean exit_on_error) {
        conn = DatabaseConnection.ensureConnection(conn, exit_on_error);
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<WorkoutClass> classes = new ArrayList<>();

        try {
            ps = DatabaseConnection.prepareStatement(conn, SQLTemplates.SQL_SELECT_WORKOUT_CLASSES_BY_TRAINER_ID, exit_on_error);
            DatabaseConnection.psSetInt(ps, 1, trainer_id, exit_on_error);
            rs = DatabaseConnection.executeQuery(ps, exit_on_error);

            while (DatabaseConnection.rsNext(rs, exit_on_error)) {
                WorkoutClass wc = buildFromResultSet(rs, conn, exit_on_error);
                if (wc != null) {
                    classes.add(wc);
                }
            }

            return classes;
        } finally {
            DatabaseConnection.closeResultSet(rs, exit_on_error);
            DatabaseConnection.closeStatement(ps, exit_on_error);
        }
    }

    // Overloads
    public static boolean addNew(WorkoutClass wc, Connection conn) {
        return addNew(wc, conn, true);
    }

    public static int addNewReturnId(WorkoutClass wc, Connection conn) {
        return addNewReturnId(wc, conn, true);
    }

    public static boolean updateById(WorkoutClass wc, Connection conn) {
        return updateById(wc, conn, true);
    }

    public static boolean deleteById(int id, Connection conn) {
        return deleteById(id, conn, true);
    }

    public static ArrayList<WorkoutClass> getAll(Connection conn) {
        return getAll(conn, true);
    }

    public static ArrayList<WorkoutClass> getAllByTrainerId(int trainer_id, Connection conn) {
        return getAllByTrainerId(trainer_id, conn, true);
    }

    // Helper
    private static WorkoutClass buildFromResultSet(ResultSet rs, Connection conn, boolean exit_on_error) {
        int id = DatabaseConnection.rsGetInt(rs, DBConst.WorkoutClasses.ID, exit_on_error);
        String type = DatabaseConnection.rsGetString(rs, DBConst.WorkoutClasses.TYPE, exit_on_error);
        String description = DatabaseConnection.rsGetString(rs, DBConst.WorkoutClasses.DESCRIPTION, exit_on_error);
        int trainer_id = DatabaseConnection.rsGetInt(rs, DBConst.WorkoutClasses.TRAINER_ID, exit_on_error);

        // get user object for trainer_id
        User user_by_id = UserDAO.getById(trainer_id, conn, exit_on_error);

        // casting to Trainer
        Trainer trainer = RoleBasedAccess.castToRole(user_by_id, Trainer.class);

        if (trainer == null) {
            if (exit_on_error) {
                String error_msg = (user_by_id == null) 
                    ? "Trainer not found for WorkoutClass ID: " + id
                    : "User is not a trainer for WorkoutClass ID: " + id;
                System.err.println(error_msg);
                System.exit(99);
            }
            return null;
        }

        return new WorkoutClass(id, type, description, trainer);
    }

    // overload method to exit on any error
    public static WorkoutClass buildFromResultSet(ResultSet rs, Connection conn) {
        return buildFromResultSet(rs, conn, true);
    }

}
