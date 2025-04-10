package gym.memberships;

import gym.database.DatabaseConnection;
import gym.database.SQLTemplates;
import gym.database.DBConst;
import gym.users.User;
import gym.users.UserDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

public abstract class MembershipDAO {

    // add new membership to the database
    public static boolean addNew(Membership membership, boolean exit_on_error) {
        DatabaseConnection.getConnection(exit_on_error);
        PreparedStatement ps = null;
        try {
            ps = DatabaseConnection.prepareStatement(SQLTemplates.SQL_INSERT_MEMBERSHIP, exit_on_error);
            DatabaseConnection.psSetInt(ps, 1, membership.getType().getId(), exit_on_error);
            DatabaseConnection.psSetInt(ps, 2, membership.getUser().getId(), exit_on_error);
            DatabaseConnection.psSetDate(ps, 3, java.sql.Date.valueOf(membership.getPurchaseDate()), exit_on_error); 

            int rows_inserted = DatabaseConnection.executeUpdate(ps, exit_on_error);
            return rows_inserted > 0;
        } finally {
            DatabaseConnection.closeStatement(ps, exit_on_error);
        }
    }

    // overloads to exit on any error
    public static boolean addNew(Membership membership) {
        return addNew(membership, true);
    }

    // add new membership to the database and return the ID of the new membership
    public static int addNewReturnId(Membership membership, boolean exit_on_error) {
        DatabaseConnection.getConnection(exit_on_error);
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = DatabaseConnection.prepareStatement(SQLTemplates.SQL_INSERT_MEMBERSHIP_RETURN_ID, exit_on_error);
            DatabaseConnection.psSetInt(ps, 1, membership.getType().getId(), exit_on_error);
            DatabaseConnection.psSetInt(ps, 2, membership.getUser().getId(), exit_on_error);
            DatabaseConnection.psSetDate(ps, 3, java.sql.Date.valueOf(membership.getPurchaseDate()), exit_on_error);

            rs = DatabaseConnection.executeQuery(ps, exit_on_error);
            if (DatabaseConnection.rsNext(rs, exit_on_error)) {
                return DatabaseConnection.rsGetInt(rs, DBConst.Memberships.ID, exit_on_error);
            } else {
                if (exit_on_error) {
                    System.err.println("Membership insert failed: no ID returned.");
                    System.exit(1);
                }
                return -1;
            }
        } finally {
            DatabaseConnection.closeResultSet(rs, exit_on_error);
            DatabaseConnection.closeStatement(ps, exit_on_error);
        }
    }

    // overloads to exit on any error
    public static int addNewReturnId(Membership membership) {
        return addNewReturnId(membership, true);
    }

    // get all memberships from the database
    public static ArrayList<Membership> getAll(boolean exit_on_error) {
        DatabaseConnection.getConnection(exit_on_error);
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Membership> memberships = new ArrayList<>();

        try {
            ps = DatabaseConnection.prepareStatement(SQLTemplates.SQL_SELECT_ALL_MEMBERSHIPS, exit_on_error);
            rs = DatabaseConnection.executeQuery(ps, exit_on_error);

            while (DatabaseConnection.rsNext(rs, exit_on_error)) {
                Membership m = buildFromResultSet(rs, exit_on_error);
                if (m != null) {
                    memberships.add(m);
                }
            }

            return memberships;
        } finally {
            DatabaseConnection.closeResultSet(rs, exit_on_error);
            DatabaseConnection.closeStatement(ps, exit_on_error);
        }
    }

    // overloads to exit on any error
    public static ArrayList<Membership> getAll() {
        return getAll(true);
    }

    // get all memberships by user ID from the database
    public static ArrayList<Membership> getAllByUserId(int user_id, boolean exit_on_error) {
        DatabaseConnection.getConnection(exit_on_error);
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Membership> memberships = new ArrayList<>();

        try {
            ps = DatabaseConnection.prepareStatement(SQLTemplates.SQL_SELECT_MEMBERSHIPS_BY_USER_ID, exit_on_error);
            DatabaseConnection.psSetInt(ps, 1, user_id, exit_on_error);
            rs = DatabaseConnection.executeQuery(ps, exit_on_error);

            while (DatabaseConnection.rsNext(rs, exit_on_error)) {
                Membership m = buildFromResultSet(rs, exit_on_error);
                if (m != null) {
                    memberships.add(m);
                }
            }

            return memberships;
        } finally {
            DatabaseConnection.closeResultSet(rs, exit_on_error);
            DatabaseConnection.closeStatement(ps, exit_on_error);
        }
    }

    // overloads to exit on any error
    public static ArrayList<Membership> getAllByUserId(int user_id) {
        return getAllByUserId(user_id, true);
    }

    // private method to build Membership from ResultSet
    private static Membership buildFromResultSet(ResultSet rs, boolean exit_on_error) {
        int id = DatabaseConnection.rsGetInt(rs, DBConst.Memberships.ID, exit_on_error);
        int type_id = DatabaseConnection.rsGetInt(rs, DBConst.Memberships.TYPE_ID, exit_on_error);
        int user_id = DatabaseConnection.rsGetInt(rs, DBConst.Memberships.USER_ID, exit_on_error);
        LocalDate purchase_date = DatabaseConnection.rsGetDate(rs, DBConst.Memberships.PURCHASE_DATE, exit_on_error).toLocalDate();

        MembershipType type = MembershipTypeDAO.getById(type_id, exit_on_error);
        User user = UserDAO.getById(user_id, exit_on_error);

        return new Membership(id, type, user, purchase_date);
    }

    // overloads to exit on any error
    public static Membership buildFromResultSet(ResultSet rs) {
        return buildFromResultSet(rs, true);
    }

}
