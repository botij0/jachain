package org.example.JaChain;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Transaction {

    public String transactionId; // hash of the transaction
    public PublicKey sender;
    public PublicKey reciepient;
    public float value;
    public byte[] signature;

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    private static int sequence = 0; //number of transactions generated

    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.reciepient = to;
        this.value = value;
        this.inputs = inputs;
    }

    private String calculateHash() {
        sequence ++;
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender) +
                        StringUtil.getStringFromKey(reciepient) +
                        value + sequence
        );
    }

    public void generateSignature(PrivateKey privateKey){
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + value;
        signature = StringUtil.applyECDSAig(privateKey,data);
    }

    public boolean verifySignature(){
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciepient) + value;
        return StringUtil.verifyECDSASig(sender, data, signature);
    }

    public boolean processTransaction() {
        if(!verifySignature()){
            System.out.println("#Transaction Signature failed to verify");
        }

        for(TransactionInput i : inputs){
            i.UTXO = JaChain.UTXOs.get(i.transactionOutputId);
        }

        // TODO: continue here

        return true;
    }

}
