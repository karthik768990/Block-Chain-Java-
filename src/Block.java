import java.util.*;
public class Block {
    private  String hash;
    String previousHash ;
    public String merkleRoot;
    public ArrayList<Transaction> transactions = new ArrayList<>();//a llist to store all the transactions of the block

    private long timeStamp; //intialised by the date and the time wheen it was being generated
    private int nonce;

    //A constructor for the block
    public Block(String previousHash){

        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }
    protected String calculateHash(){
        String calculatedHash = HashUtil.applySha256(previousHash+Long.toString(timeStamp)+
                Integer.toString(nonce)+merkleRoot);
        return calculatedHash;
    }

    protected String getHash(){return this.hash;}

    public void mineBlock(int difficulty){
         merkleRoot = HashUtil.getMerkleRoot(transactions);
        String target = new String(new char[difficulty]).replace('\0','0');
        while(!hash.substring(0,difficulty).equals(target)){
            nonce++;
            this.hash = calculateHash();
        }
        System.out.println("Block mined !"+this.hash);
    }

    //A method to add the transactions to the block
    public boolean addTransaction(Transaction transaction){
        //To process the transaction and check if valid,untill the block is a genesis(first) block then ignore
        if(transaction==null){return false;}
        if((previousHash!="0")){
            if((transaction.processTransaction()!=true)){
                System.out.println("Transaction failed to process.Discarded. ");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Transaction successfully added to the block");
        return true;
    }
}