package bank;

public class Operation {
    int id;
    int accountId;
    String type;
    double amount;

    public Operation(int id, int accountId, String type, double amount) {
        this.id = id;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
    }
}
