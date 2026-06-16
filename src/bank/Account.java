package bank;

public class Account {
    int id;
    String name;
    double balance = 0;

    public Account(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        balance -= amount;
    }
}
