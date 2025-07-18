
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {
    protected PublicKey publicKey;
    protected PrivateKey privateKey;


    public HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();//Only the utxos that are owned by this wallet





    public Wallet() {
        generateKeyPair();

    }


    public void generateKeyPair(){
        try{

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            //Generation of new keypair
            keyGen.initialize(ecSpec,random);
            KeyPair keyPair = keyGen.generateKeyPair();
            //Setting the public and the private keypairs from the keypairs that are geenrated
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    //returns the balance and store the  utxos owned by this wallet in the this.UTXOs
    public float  getBalance(){
        float total = 0;
        for(Map.Entry<String,TransactionOutput> item: Main.UTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();
            if(UTXO.idMine(publicKey)){
                //if the coins belong to this wallet
                UTXOs.put(UTXO.id, UTXO); //then add it to the lisst of unspent transactions
                total+= UTXO.value;
            }
        }
        return total;
    }
    //Generates and returns new transaction from this wallet
    public Transaction sendFunds(PublicKey reciever,float value){
        if(getBalance()<value){
            //Initiating the transaction only when there is sufficient balance
            System.out.println("Not enough funds . Transaction discarded");
            return null;
        }
        //Creating the array list of inputs
        ArrayList<TransactionInput> inputs = new ArrayList<>();
        float total =0;
        for(Map.Entry<String,TransactionOutput> item:UTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();
            total+= UTXO.value;
            inputs.add(new TransactionInput(UTXO.id));
            if(total>value) break;

        }
        Transaction newTransaction = new Transaction(publicKey,reciever,value,inputs);
        newTransaction.generateSignature(privateKey);
        for(TransactionInput input:inputs){
            UTXOs.remove(input.transactionOutputId);
        }
        return newTransaction;
    }

    protected PublicKey getPublicKey() {
        return this.publicKey;
    }
    protected  PrivateKey getPrivateKey(){
        return  this.privateKey;
    }
}