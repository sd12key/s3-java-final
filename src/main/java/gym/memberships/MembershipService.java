package gym.memberships;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import gym.users.User;
import gym.utilities.Utils;

public class MembershipService {

    public static boolean addNewMembership(Membership membership) {
        return MembershipDAO.addNew(membership, true);
    }

    public static List<Membership> getMembershipsByUser(User user) {
        return MembershipDAO.getAllByUserId(user.getId());
    }

    public static List<Membership> getMembershipsByUser(int id) {
        return MembershipDAO.getAllByUserId(id);
    }

    public static List<Membership> getAllMemberships() {
        return MembershipDAO.getAll();
    }
    
    public static List<String> getAllMembershipsReport() {

        List<Membership> memberships = getAllMemberships();
        
        // Create lists to separate active and expired memberships
        List<Membership> active_memberships = new ArrayList<>();
        List<Membership> expired_memberships = new ArrayList<>();
        
        // Split memberships into active and expired based on isExpired() method
        for (Membership membership : memberships) {
            if (membership.isExpired()) {
                expired_memberships.add(membership);
            } else {
                active_memberships.add(membership);
            }
        }

        // Create a List of Strings for reporting
        List<String> report = new ArrayList<>();
        
        // Add summary line for active memberships
        String active_summary = ">>> Active: " + (active_memberships.isEmpty() ? "None" : active_memberships.size());
        report.add(active_summary);
        
        // Add active memberships to the report
        for (Membership membership : active_memberships) {
            report.add(membership.toString());
        }
        
        report.add(" ");
        // Add summary line for expired memberships
        String expired_summary = ">>> Expired: " + (expired_memberships.isEmpty() ? "None" : expired_memberships.size());
        report.add(expired_summary);
        
        // Add expired memberships to the report
        for (Membership membership : expired_memberships) {
            report.add(membership.toString());
        }
        
        return report;
    }

    public static List<String> getMembershipsByUserReport(int user_id) {
        List<String> report = new ArrayList<>();
        List<Membership> memberships = getMembershipsByUser(user_id);
        if (memberships == null || memberships.isEmpty()) {
            report.add(">>> No memberships found");
            return report;
        }
        report.add(">>> Memberships:");
        for (Membership membership : memberships) {
            report.add(membership.toString());
        }
        return report;
    }
    
    public static List<String> getMembershipsByUserReport(User user) {
        return getMembershipsByUserReport(user.getId());
    }

    public static List<String> getRevenueReport() {

        List<Membership> memberships = getAllMemberships();

        // == counters =
        double total_revenue = 0.0;
        double total_active_revenue = 0.0;
        double total_expired_revenue = 0.0;
        double total_revenue_this_year = 0.0;
        double total_active_revenue_this_year = 0.0;
        double total_expired_revenue_this_year = 0.0;

        int total_num = 0;
        int exp_num = 0;
        int active_num = 0;
        int total_this_year_num = 0;
        int exp_this_year_num = 0;
        int active_this_year_num = 0;

        int current_year = LocalDate.now().getYear();

        // Loop through all memberships to calculate total revenue and sort them
        for (Membership membership : memberships) {
            // Increment total counter for all memberships
            total_num++;
            total_revenue += membership.getType().getCost(); 

            // Check if membership is expired or active, and update counters and totals
            boolean is_current_year = membership.getPurchaseDate().getYear() == current_year;

            if (membership.isExpired()) {
                exp_num++;
                total_expired_revenue += membership.getType().getCost();

                if (is_current_year) {
                    exp_this_year_num++;
                    total_expired_revenue_this_year += membership.getType().getCost();
                }
            } else {
                active_num++;
                total_active_revenue += membership.getType().getCost();

                if (is_current_year) {
                    active_this_year_num++;
                    total_active_revenue_this_year += membership.getType().getCost();
                }
            }

            // For both expired and active memberships, update the total for this year
            if (is_current_year) {
                total_this_year_num++;
                total_revenue_this_year += membership.getType().getCost();
            }
        }
        
        // Create the earnings report as a list of strings
        List<String> report = new ArrayList<>();
        
        int num_len = 10;
        // Add total earnings up to date
        String grand_total_str = "Total as of " + LocalDate.now() + " (" + total_num + "): ";
        int align_to = grand_total_str.length() + 4;
        report.add("==> " + grand_total_str + Utils.align_right("$" + Utils.double_to_str(total_revenue), num_len));

        report.add(Utils.align_right("Incl. Active (" + active_num + "): ", align_to) 
            + Utils.align_right("$" + Utils.double_to_str(total_active_revenue), num_len));
        report.add(Utils.align_right("Inactive (" + exp_num + "): ", align_to) 
            + Utils.align_right("$" + Utils.double_to_str(total_expired_revenue), num_len));

        report.add(" ");
        // Add earnings for memberships purchased in the current year
        report.add("==> " + Utils.align_right("Purchased in " + current_year + " (" + total_this_year_num + "): ", align_to-4) 
            + Utils.align_right("$" + Utils.double_to_str(total_revenue_this_year), num_len));
        report.add(Utils.align_right("Incl. Active (" + active_this_year_num + "): ", align_to) 
            + Utils.align_right("$" + Utils.double_to_str(total_active_revenue_this_year), num_len));
        report.add(Utils.align_right("Inactive (" + exp_this_year_num + "): ", align_to) 
            + Utils.align_right("$" + Utils.double_to_str(total_expired_revenue_this_year), num_len));

        return report;
    }
    
    // Get active memberships by user
    public static List<Membership> getActiveMembershipsByUser(User user) {
        List<Membership> memberships = getMembershipsByUser(user);
        List<Membership> active_memberships = new ArrayList<>();
        for (Membership membership : memberships) {
            if (!membership.isExpired()) {
                active_memberships.add(membership);
            }
        }
        return active_memberships;
    }

    public static List<String> getMembershipsAndExpencesReport(User user){
        List<String> report = new ArrayList<>();
        List<Membership> memberships = getMembershipsByUser(user);
        if (memberships == null || memberships.isEmpty()) {
            report.add(">>> No memberships found");
            return report;
        }
        report.add(">>> Memberships:");
        for (Membership membership : memberships) {
            report.add(membership.toString());
        }
        report.add(" ");
        
        // Calculate total revenue from memberships list
        double[] totals = calculateMembershipTotals(memberships);
        int[] counts = calculateMembershipCounts(memberships);
        double active_expences = totals[0];
        double expired_expences = totals[1];
        double total_expences = active_expences + expired_expences;
        int active_num = counts[0];
        int exp_num = counts[1];
        int total_num = active_num + exp_num;

        int num_len = 10;
        // Add total earnings up to date
        String grand_total_str = ">>> Total Expences as of " + LocalDate.now() + " (" + total_num + "): ";
        int align_to = grand_total_str.length() + 4;
        report.add("==> " + grand_total_str + Utils.align_right("$" + Utils.double_to_str(total_expences), num_len));

        report.add(Utils.align_right("Incl. Active (" + active_num + "): ", align_to) 
            + Utils.align_right("$" + Utils.double_to_str(active_expences), num_len));
        report.add(Utils.align_right("Inactive (" + exp_num + "): ", align_to) 
            + Utils.align_right("$" + Utils.double_to_str(expired_expences), num_len));
        
        return report;
    }


    // Calculate total revenue from memberships list
    public static double[] calculateMembershipTotals(List<Membership> memberships) {
        double activeTotals = 0.0;
        double expiredTotals = 0.0;
        for (Membership membership : memberships) {
            if (membership.isExpired()) {
                expiredTotals += membership.getType().getCost();
            } else {
                activeTotals += membership.getType().getCost();
            }
        }
        return new double[] {activeTotals, expiredTotals};
    }
    
    // Calculate total active and expired memberships
    public static int[] calculateMembershipCounts(List<Membership> memberships) {
        int activeCount = 0;
        int expiredCount = 0;
        for (Membership membership : memberships) {
            if (membership.isExpired()) {
                expiredCount++;
            } else {
                activeCount++;
            }
        }
        return new int[] {activeCount, expiredCount};    
    }



}
