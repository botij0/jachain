package org.example.JaChain;

public class TransactionInput {
    public String transactionOutputId; //Reference to Transaction Outputs
    public TransactionOutput UTXO; // Contains the Unspent transaction output

    public TransactionInput(String transactionOutputId){
        this.transactionOutputId = transactionOutputId;
    }
}
