package gym.memberships;

import gym.users.User;
import gym.utilities.Utils;

import java.time.LocalDate;

public class Membership {
    private int id;
    private MembershipType type;
    private User user;
    private LocalDate purchase_date;

    // Constructor for full object
    public Membership(int id, MembershipType type, User user, LocalDate purchase_date) {
        this.id = id;
        this.type = type;
        this.user = user;
        this.purchase_date = purchase_date;
    }

    // Constructor for new membership (id is preset to 0)
    public Membership(MembershipType type, User user, LocalDate purchaseDate) {
        this(0, type, user, purchaseDate); 
    }

    // Getters
    public int getId() {
        return this.id;
    }

    public MembershipType getType() {
        return this.type;
    }

    public User getUser() {
        return this.user;
    }

    public LocalDate getPurchaseDate() {
        return this.purchase_date;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setType(MembershipType type) {
        this.type = type;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPurchaseDate(LocalDate purchase_date) {
        this.purchase_date = purchase_date;
    }

    public LocalDate getExpirationDate() {
        if (this.type == null || this.purchase_date == null) {
            return null;
        }
        return this.purchase_date.plusMonths(this.type.getDurationInMonths());
    }

    public boolean isExpired() {
        LocalDate expirationDate = this.getExpirationDate();
        return expirationDate != null && expirationDate.isBefore(LocalDate.now());
    }

    @Override
    public String toString() {
        String status;
        LocalDate expiration = this.getExpirationDate();
        if (expiration == null) {
            status = "exp.unknown";
        } else if (expiration.isBefore(LocalDate.now())) {
            status = "expired";
        } else {
            status = "exp. " + expiration;
        }
    
        return "[" + this.id +
                ", " + Utils.FirstCharToUpperCase(this.type.getUserRole()) +
                "/" + this.type.getType() + "/" + "$" + Utils.double_to_str(this.type.getCost()) + "] " +
                this.user.getFullName() + "(id:" + this.user.getId() + ")" +
                ", purchased " + this.purchase_date + " (" + status + ")";
    }
        
}

