
import java.security.*;
import java.util.ArrayList;
import java.util.Base64;

public class HashUtil {
    //Applies Sha256 to a string and returns the result.
    public static String applySha256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            //Applies sha256 to our input,
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer(); // This will contain hash as hexidecimal
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
   //Applies the edsa signature and returns the result  in the bytes
    public static byte[] applyECDASig(PrivateKey privateKey,String input){
        Signature dsa;
        byte[] output = new byte[0];
        try{
            dsa = Signature.getInstance("ECDSA","BC");
            dsa.initSign(privateKey);
            byte[] strbyte = input.getBytes();
            dsa.update(strbyte);
            byte[] realSig = dsa.sign();
            output = realSig;
        }catch(Exception e ){
            throw new RuntimeException(e);
        }
        return output;
    }
    //To verify the String type signature
    public static boolean verifyECDSASig(PublicKey publicKey,String data,byte[] signature){
        try{
            Signature ecdsaVerify = Signature.getInstance("ECDSA","BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    //To get the String type of the key
    public static String getStringFromKey(Key key){
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    //A helper method which summarises the transactions of the user without downloading it
    //Mainly known as Merkle root
    public static String getMerkleRoot(ArrayList<Transaction> transactions){
        int count = transactions.size();
        ArrayList<String> previousTreeLayer = new ArrayList<String>();
        for(Transaction transaction : transactions){
            previousTreeLayer.add(transaction.transactionId);
        }
        ArrayList<String> treeLayer = previousTreeLayer;
        while(count>1){
            treeLayer = new ArrayList<String>();
            for(int i=1;i<previousTreeLayer.size();i++){
                treeLayer.add(applySha256(previousTreeLayer.get(i-1)+previousTreeLayer.get(i)));

            }
            count = treeLayer.size();
            previousTreeLayer=treeLayer;

        }
        String markleRoot = (treeLayer.size()==1)? treeLayer.get(0) :"";
        return markleRoot;
    }
}