package gym.memberships;

import gym.database.DBConst;
import gym.database.DatabaseConnection;
import gym.database.SQLTemplates;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// I make this class to be a utility class with static methods only
// make it abstract to prevent instantiation
// make all methods static
public abstract class MembershipTypeDAO {

    // add new membership type to the database
    // returns true if the membership type was added successfully, false otherwise
    public static boolean addNew(MembershipType type, Connection conn, boolean exit_on_error) {
        conn = DatabaseConnection.ensureConnection(conn, exit_on_error);
        PreparedStatement ps = null;
        try {
            ps = DatabaseConnection.prepareStatement(conn, SQLTemplates.SQL_INSERT_MEMBERSHIP_TYPE, exit_on_error);
            DatabaseConnection.psSetString(ps, 1, type.getUserRole(), exit_on_error);
            DatabaseConnection.psSetString(ps, 2, type.getType(), exit_on_error);
            DatabaseConnection.psSetString(ps, 3, type.getDescription(), exit_on_error);
            DatabaseConnection.psSetInt(ps, 4, type.getDurationInMonths(), exit_on_error);
            DatabaseConnection.psSetDouble(ps, 5, type.getCost(), exit_on_error);
            int rows_updated = DatabaseConnection.executeUpdate(ps, exit_on_error);
            return rows_updated > 0;
        } finally {
            DatabaseConnection.closeStatement(ps, exit_on_error);
        }
    }
    
    // overload method to exit on any error
    public static boolean addNew(MembershipType type, Connection conn) {
        return addNew(type, conn, true);
    }

    // get membership type by id from the database
    public static MembershipType getById(int id, Connection conn, boolean exit_on_error) {
        conn = DatabaseConnection.ensureConnection(conn, exit_on_error);
        PreparedStatement ps = null;
        ResultSet rs = null;
    
        try {
            ps = DatabaseConnection.prepareStatement(conn, SQLTemplates.SQL_SELECT_MEMBERSHIP_TYPE_BY_ID, exit_on_error);
            DatabaseConnection.psSetInt(ps, 1, id, exit_on_error);
            rs = DatabaseConnection.executeQuery(ps, exit_on_error);
    
            MembershipType type = null;
            if (DatabaseConnection.rsNext(rs, exit_on_error)) {
                type = buildFromResultSet(rs, exit_on_error);
            }
            return type;
    
        } finally {
            DatabaseConnection.closeResultSet(rs, exit_on_error);
            DatabaseConnection.closeStatement(ps, exit_on_error);
        }
    }
        
    // overload method to exit on any error
    public static MembershipType getById(int id, Connection conn) {
        return getById(id, conn, true);
    }

    // get all membership types by specific user role
    public static List<MembershipType> getByUserRole(String role, Connection conn, boolean exit_on_error) {
        conn = DatabaseConnection.ensureConnection(conn, exit_on_error);
        PreparedStatement ps = null;
        ResultSet rs = null;
    
        try {
            ps = DatabaseConnection.prepareStatement(conn, SQLTemplates.SQL_SELECT_MEMBERSHIP_TYPE_BY_USER_ROLE, exit_on_error);
            DatabaseConnection.psSetString(ps, 1, role, exit_on_error);
            rs = DatabaseConnection.executeQuery(ps, exit_on_error);
    
            List<MembershipType> types = new ArrayList<>();
            while (DatabaseConnection.rsNext(rs, exit_on_error)) {
                types.add(buildFromResultSet(rs, exit_on_error));
            }
            return types;
    
        } finally {
            DatabaseConnection.closeResultSet(rs, exit_on_error);
            DatabaseConnection.closeStatement(ps, exit_on_error);
        }
    }
    
    // overload method to exit on any error
    public static List<MembershipType> getByUserRole(String role, Connection conn) {
        return getByUserRole(role, conn, true);
    }


    // get all membership types from the database
    public static List<MembershipType> getAll(Connection conn, boolean exit_on_error) {
        conn = DatabaseConnection.ensureConnection(conn, exit_on_error);
        Statement stmt = null;
        ResultSet rs = null;
    
        try {
            stmt = DatabaseConnection.createStatement(conn, exit_on_error);
            rs = DatabaseConnection.executeQuery(stmt, SQLTemplates.SQL_SELECT_ALL_MEMBERSHIP_TYPES, exit_on_error);
    
            List<MembershipType> types = new ArrayList<>();
            while (DatabaseConnection.rsNext(rs, exit_on_error)) {
                types.add(buildFromResultSet(rs, exit_on_error));
            }
    
            return types;
    
        } finally {
            DatabaseConnection.closeResultSet(rs, exit_on_error);
            DatabaseConnection.closeStatement(stmt, exit_on_error);
        }
    }
    
    // overload method to exit on any error
    public static List<MembershipType> getAll(Connection conn) {
        return getAll(conn, true);
    }

    // private method to build a MembershipType object from a ResultSet
    private static MembershipType buildFromResultSet(ResultSet rs, boolean exit_on_error) {
        int id = DatabaseConnection.rsGetInt(rs, DBConst.MembershipTypes.ID, exit_on_error);
        String user_role = DatabaseConnection.rsGetString(rs, DBConst.MembershipTypes.USER_ROLE, exit_on_error);
        String type = DatabaseConnection.rsGetString(rs, DBConst.MembershipTypes.TYPE, exit_on_error);
        String description = DatabaseConnection.rsGetString(rs, DBConst.MembershipTypes.DESCRIPTION, exit_on_error);
        int duration = DatabaseConnection.rsGetInt(rs, DBConst.MembershipTypes.DURATION_MONTHS, exit_on_error);
        double cost = DatabaseConnection.rsGetDouble(rs, DBConst.MembershipTypes.COST, exit_on_error);
        return new MembershipType(id, user_role, type, description, duration, cost);
    }
}
