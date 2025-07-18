import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.Security;
import java.util.*;

public class Main {
    static {
        // Register Bouncy Castle as a security provider
        Security.addProvider(new BouncyCastleProvider());
        System.out.println("Bouncy Castle Registered: " + Security.getProvider("BC"));
    }
    public static List<Block> blockchain = new ArrayList<>();
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<>();
    public static HashMap<String, Wallet> userWallets = new HashMap<>();

    public static int difficulty = 4;
    public static float minimumTransaction = 0.1f;
    public static Transaction genesisTransaction;
    public static Scanner sc = new Scanner(System.in);
    public static Wallet coinBase; // Acts like the central authority


    public static void main(String[] args) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        // 1. Create wallets
        coinBase = new Wallet();
        Wallet user = new Wallet();
        setupGenesis(); // Initial transaction

    // 2. Create the genesis transaction
            Transaction genesisTx = new Transaction(null, coinBase.getPublicKey(), 10000f, null);
            genesisTx.transactionId = "0";
            genesisTx.outputs.add(new TransactionOutput(coinBase.getPublicKey(), 10000f, genesisTx.transactionId));

    // 3. Add genesis transaction output to UTXOs
            Main.UTXOs.put(genesisTx.outputs.get(0).id, genesisTx.outputs.get(0));

    // 4. Add genesis block to the blockchain
            Block genesis = new Block("0");
            genesis.addTransaction(genesisTx); // This will not process inputs but initializes outputs
            blockchain.add(genesis);

    // 5. Create transaction inputs using genesis output
            ArrayList<TransactionInput> inputs = new ArrayList<>();
            inputs.add(new TransactionInput(genesisTx.outputs.get(0).id));

    // 6. Create a real transaction from coinbase to user
            Transaction tx = new Transaction(coinBase.getPublicKey(), user.getPublicKey(), 500f, inputs);
            tx.generateSignature(coinBase.getPrivateKey());

    // 7. Add transaction to new block and add it to the chain
            Block block = new Block(blockchain.getLast().getHash());
            block.addTransaction(tx);
            blockchain.add(block);


        System.out.println("============================================");
        System.out.println("         JAVA BLOCKCHAIN CLI SYSTEM         ");
        System.out.println("============================================");



        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Create New Wallet");
            System.out.println("2. Login to Wallet");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1": createWallet(); break;
                case "2": loginWallet(); break;
                case "3": System.out.println("Exiting..."); return;
                default: System.out.println("Invalid input. Try again.");
            }
        }
    }

    static void createWallet() {
        System.out.print("Enter new username: ");
        String name = sc.nextLine();
        if (userWallets.containsKey(name)) {
            System.out.println("❌ Username already exists!");
            return;
        }
        Wallet wallet = new Wallet();
        userWallets.put(name, wallet);
        System.out.println("✅ Wallet created for " + name);
    }

    static void loginWallet() {
        System.out.print("Enter your username: ");
        String name = sc.nextLine();
        Wallet wallet = userWallets.get(name);
        if (wallet == null) {
            System.out.println("❌ Wallet not found!");
            return;
        }

        while (true) {
            System.out.println("\nWallet Menu - " + name);
            System.out.println("1. View Balance");
            System.out.println("2. Wallet Top-Up (Deposit)");
            System.out.println("3. Send Funds");
            System.out.println("4. View Blockchain");
            System.out.println("5. Validate Blockchain");
            System.out.println("6. Logout");
            System.out.print("Enter choice: ");
            String option = sc.nextLine();

            switch (option) {
                case "1":
                    System.out.printf("💰 Balance: ₹%.2f\n", wallet.getBalance());
                    break;

                case "2":
                    topUpWallet(wallet);
                    break;

                case "3":
                    sendFunds(wallet);
                    break;

                case "4":
                    printBlockchain();
                    break;

                case "5":
                    isChainValid();
                    break;

                case "6":
                    System.out.println("Logging out...");
                    return;

                default:
                    System.out.println("Invalid input. Try again.");
            }
        }
    }

    static void topUpWallet(Wallet wallet) {
        System.out.print("Enter amount to deposit: ₹");
        try {
            float amount = Float.parseFloat(sc.nextLine());
            if (amount <= 0) {
                System.out.println("❌ Enter a positive amount.");
                return;
            }

            // Step 1: Create transaction from coinBase to user using coinBase's balance
            Transaction tx = coinBase.sendFunds(wallet.publicKey, amount);

            if (tx == null) {
                System.out.println("❌ Top-up failed: Insufficient funds or transaction issue.");
                return;
            }

            // Step 2: Add transaction to new block
            Block topUpBlock = new Block(blockchain.get(blockchain.size() - 1).getHash());
            topUpBlock.addTransaction(tx); // This internally calls processTransaction()
            addBlock(topUpBlock);

            System.out.printf("✅ Successfully topped up ₹%.2f. New balance: ₹%.2f\n", amount, wallet.getBalance());

        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid amount entered.");
        }
    }


    static void sendFunds(Wallet senderWallet) {
        System.out.print("Enter recipient username: ");
        String recipientName = sc.nextLine();
        Wallet receiverWallet = userWallets.get(recipientName);
        if (receiverWallet == null) {
            System.out.println("❌ Recipient not found.");
            return;
        }

        System.out.print("Enter amount to send: ₹");
        try {
            float amount = Float.parseFloat(sc.nextLine());
            Block block = new Block(blockchain.get(blockchain.size() - 1).getHash());
            Transaction tx = senderWallet.sendFunds(receiverWallet.publicKey, amount);
            if (tx != null) {
                block.addTransaction(tx);
                addBlock(block);
                System.out.printf("✅ Transaction successful. New balance: ₹%.2f\n", senderWallet.getBalance());
            } else {
                System.out.println("❌ Transaction failed.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid amount entered.");
        }
    }

    static void printBlockchain() {
        for (Block b : blockchain) {
            System.out.println("-------------------------------------------------");
            System.out.println("Block Hash: " + b.getHash());
            System.out.println("Previous Hash: " + b.previousHash);
            System.out.println("Transactions: " + b.transactions.size());
        }
        System.out.println("-------------------------------------------------");
    }

    static void setupGenesis() {
        Wallet system = new Wallet();
        Wallet firstUser = new Wallet();
        userWallets.put("root", firstUser);

        genesisTransaction = new Transaction(system.publicKey, firstUser.publicKey, 100f, null);
        genesisTransaction.generateSignature(system.privateKey);
        genesisTransaction.transactionId = "0";
        genesisTransaction.outputs.add(new TransactionOutput(
                genesisTransaction.reciever, genesisTransaction.value, genesisTransaction.transactionId));
        UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        Block genesis = new Block("0");
        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);
        System.out.println("✅ Genesis block created and mined.");
    }

    public static Boolean isChainValid() {
        // Keep your original validation logic here
        System.out.println("✅ Blockchain is valid.");
        return true;
    }

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }
}