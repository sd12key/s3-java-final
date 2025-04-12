package gym.memberships;

import java.util.List;

public class MembershipTypeService {
    
    public static List<MembershipType> getMembershipTypesByRole(String role) {
        return MembershipTypeDAO.getByUserRole(role);
    }

}
