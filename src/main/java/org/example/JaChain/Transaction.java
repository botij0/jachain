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

        // Gather transaction inputs (Make sure they are unspent)
        for(TransactionInput i : inputs){
            i.UTXO = JaChain.UTXOs.get(i.transactionOutputId);
        }


        if (getInputsValue() < JaChain.minimumTransaction) {
            System.out.println("#Transacation Inputs to small: " + getInputsValue());
            return false;
        }

        float leftOver = getInputsValue() - value;
        transactionId = calculateHash();

        // send value to recipient
        outputs.add(new TransactionOutput(this.reciepient, value, transactionId));
        // send the left over 'change' back to sender
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionId));

        // Add outputs to Unspent List
        for (TransactionOutput o : outputs){
            JaChain.UTXOs.put(o.id, o);
        }

        //Remove transaction inputs from UTXO lists as spent
        for(TransactionInput i : inputs){
            if(i.UTXO == null) continue;
            JaChain.UTXOs.remove(i.UTXO.id);
        }


        return true;
    }

    public float getInputsValue(){
        float total = 0;
        for(TransactionInput i : inputs){
            if(i.UTXO == null) continue;
            total += i.UTXO.value;
        }
        return total;
    }

    public float getOutputsValue(){
        float total = 0;
        for(TransactionOutput o : outputs){
            total += o.value;
        }
        return total;
    }

}
