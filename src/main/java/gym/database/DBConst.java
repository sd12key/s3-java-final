package gym.database;

public class DBConst {
    public static final String SCHEMA = "gym_app";

    public static class Users {
        public static final String TABLE = "users";
        public static final String ID = "id";
        public static final String USERNAME = "username";
        public static final String PASSWORD_HASH = "password_hash";
        public static final String EMAIL = "email";
        public static final String PHONE_NUMBER = "phone_number";
        public static final String ADDRESS = "address";
        public static final String ROLE = "role"; // "admin", "trainer", "member"
    }

    public static class MembershipTypes {
        public static final String TABLE = "membership_types";
        public static final String ID = "id";
        public static final String USER_ROLE = "user_role"; // "member" or "trainer"
        public static final String TYPE = "type";
        public static final String DESCRIPTION = "description";
        public static final String DURATION_MONTHS = "duration_in_months";
        public static final String COST = "cost";
    }

    public static class Memberships {
        public static final String TABLE = "memberships";
        public static final String ID = "id";
        public static final String TYPE_ID = "type_id";       // FK → membership_types.id
        public static final String MEMBER_ID = "member_id";   // FK → users.id
        public static final String PURCHASE_DATE = "purchase_date";
    }

    public static class WorkoutClasses {
        public static final String TABLE = "workout_classes";
        public static final String ID = "id";
        public static final String TYPE = "type";
        public static final String DESCRIPTION = "description";
        public static final String TRAINER_ID = "trainer_id"; // FK → users.id
    }
}
