public class TransactionInput {
    public String transactionOutputId;
    public TransactionOutput UTXO;

    public TransactionInput(String transactionInputId){
        this.transactionOutputId = transactionInputId;
    }
}