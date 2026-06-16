package bank;

public class Tools {
    public static void manipulate(String command, Assistant app) {
        String[] parts = command.split(" ");
        if (parts.length == 0) {
            return;
        }

        if (parts[0].equals("create") && parts.length >= 2) {
            String name = parts[1];
            Account account = new Account(app.nextAccountId++, name);
            app.accounts.add(account);
            System.out.println("Created account " + account.id);
            return;
        }

        if (parts[0].equals("list")) {
            for (Account account : app.accounts) {
                System.out.println(account.id + ": " + account.name + " - " + account.balance);
            }
            return;
        }

        if ((parts[0].equals("deposit") || parts[0].equals("withdraw")) && parts.length >= 3) {
            int accountId;
            double amount;
            try {
                accountId = Integer.parseInt(parts[1]);
                amount = parseAmount(parts[2]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid account id or amount. Use dot or comma for decimals (e.g. 49.99 or 49,99).");
                return;
            }

            if (amount <= 0) {
                System.out.println("Amount must be positive.");
                return;
            }

            Account account = app.findAccount(accountId);
            if (account == null) {
                System.out.println("Account not found.");
                return;
            }

            if (parts[0].equals("deposit")) {
                account.deposit(amount);
            } else {
                account.withdraw(amount);
            }

            Operation operation = new Operation(
                    app.nextOperationId++,
                    accountId,
                    parts[0],
                    amount);
            app.operations.add(operation);

            System.out.println("Done. Balance: " + account.balance);
        }

        if (parts[0].equals("transfer")) {
            int firstAccountId;
            int secondAccountId;
            double amount;
            try {
                firstAccountId = Integer.parseInt(parts[1]);
                secondAccountId = Integer.parseInt(parts[2]);
                amount = parseAmount(parts[3]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid account id or amount. Use dot or comma for decimals (e.g. 49.99 or 49,99).");
                return;
            }

            if (amount <= 0) {
                System.out.println("Amount must be positive.");
                return;
            }

            Account account1 = app.findAccount(firstAccountId);
            Account account2 = app.findAccount(secondAccountId);
            if ((account1 == null)||(account2 == null)) {
                System.out.println("Account not found.");
                return;
            }
            account1.withdraw(amount);
            account2.deposit(amount);

            Operation operation = new Operation(
                    app.nextOperationId++,
                    firstAccountId,
                    "withdraw",
                    amount);
            app.operations.add(operation);

            operation = new Operation(
                    app.nextOperationId++,
                    secondAccountId,
                    "deposit",
                    amount);
            app.operations.add(operation);

            System.out.println("Done.\nBalance 1: " + account1.balance + "\nBalance 2: " + account2.balance);
        }
    }

    static double parseAmount(String text) {
        return Double.parseDouble(text.replace(',', '.'));
    }
}
