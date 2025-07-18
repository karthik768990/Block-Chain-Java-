import java.util.*;
import java.security.*;
public class Transaction {
    public String transactionId; //the hash of the transaction
    public PublicKey sender; //the public key of the sender
    public PublicKey reciever;//the public key of the reciever
    public float value; //the values that  is being sent
    public byte[] signature; //the prevention of others to tamper the transaction

    public ArrayList<TransactionInput> inputs = new ArrayList<>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<>();

    private static int sequence = 0;//A count of number of transactions that have been done

    //Constructor of the transaction class
    public Transaction(PublicKey from,PublicKey to,float value,ArrayList<TransactionInput> input){
        this.sender = from;
        this.reciever = to;
        this.value = value;
        this.inputs = input;


    }
    //to calculate the hash for the transaction which is going to be the transaction id so far
    private String calculateHash(){
        sequence++;//to avouid the overlapping of 2 or more transactions with the same id
        return HashUtil.applySha256(
                HashUtil.getStringFromKey(sender)+
                        HashUtil.getStringFromKey(reciever)+
                        Float.toString(value)+sequence
        );
    }

    //Signs all the data that we donot wish to be tampered with
    public void generateSignature(PrivateKey privateKey) {
        String data = HashUtil.getStringFromKey(sender)+HashUtil.getStringFromKey(reciever)+Float.toString(value);
        signature = HashUtil.applyECDASig(privateKey,data);
    }

    //Verification of the data that we have signed
    public boolean verifySignature(){
        String data = HashUtil.getStringFromKey(sender)+HashUtil.getStringFromKey(reciever)+Float.toString(value);
        return HashUtil.verifyECDSASig(sender,data,signature);
    }

    //A boolean method which is to be expected to return true if a new Transacton was created
    public boolean processTransaction(){
        if(verifySignature()==false){
            System.out.println("#Transaction signature failed ");
            return false;
        }
        //making sure that the transaction inputs are unspent
        for(TransactionInput i: inputs){
            i.UTXO = Main.UTXOs.get(i.transactionOutputId);
        }

        //To check if the transaction is valid
        if(getInputsValue()<Main.minimumTransaction){
            System.out.println("#Transaction inputs too small "+getInputsValue());
            return false;

        }
        //Generation of the transaction outputs
        float leftOver = getInputsValue()-value; //Getting the left over with the simple substraction
        transactionId = calculateHash();
        outputs.add(new TransactionOutput(this.reciever,value,transactionId));
        outputs.add(new TransactionOutput(this.sender,leftOver,transactionId));

        //Adding the outputs to unspent list
        for(TransactionOutput o:outputs){
            Main.UTXOs.put(o.id,o);
        }

        //remove the transaction inputs from the utxo lists as spent
        for(TransactionInput i:inputs){
            if(i.UTXO == null){
                continue; //if the transaction could not found then skip it  }
            }
            Main.UTXOs.remove(i.UTXO.id);
        }
        return true;
    }
    //To return the sum of the input(UTXOs)  values
    public float  getInputsValue(){
        float total =0;
        for(TransactionInput i:inputs){
            if(i.UTXO==null) continue;
            total+=i.UTXO.value;

        }
        return total;
    }
    //To return the sum of the output(UTXOs) values
    public float getOutputsValue(){
        float total=0;
        for(TransactionOutput o:outputs){
            total+=o.value;
        }
        return total;
    }
}