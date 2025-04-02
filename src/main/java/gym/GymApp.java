package gym;

import gym.users.childclasses.Admin;
import gym.users.childclasses.Member;
import gym.users.childclasses.Trainer;

public class GymApp {
    public static void main(String[] args)  {
        System.out.println("Welcome to the Gym Management System!");
        Admin admin_1 = new Admin(1,"aaa","aaa","aaa","aaa","aaa");
        Trainer trainer_1 = new Trainer(2,"bbb","bbb","bbb","bbb","bbb");
        Member member_1 = new Member(3,"ccc","ccc","ccc","ccc","ccc");
        System.out.println(admin_1);
        admin_1.showMenu();
        System.out.println();
        System.out.println(trainer_1);
        trainer_1.showMenu();
        System.out.println();
        System.out.println(member_1);
        member_1.showMenu();
        System.out.println();
    }

}
