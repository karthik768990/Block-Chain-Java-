import java.util.*;
import java.security.*;

public class TransactionOutput {
    public String id;
    public PublicKey reciever; //the new pwner of the coins
    public float value; //the amount of coins that they actually own
    public String parentTransactionId;//the id of  the transaction where this output was being created
    //Constructor of the TransactionOutput class
    public TransactionOutput(PublicKey reciever,float value,String parentTransactionId){
        this.reciever = reciever;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = HashUtil.applySha256(HashUtil.getStringFromKey(reciever)+Float.toString(value)+parentTransactionId);

    }
    //Check if the coin belongs to you
    public boolean idMine(PublicKey publicKey){
        return (publicKey==reciever);
    }

}