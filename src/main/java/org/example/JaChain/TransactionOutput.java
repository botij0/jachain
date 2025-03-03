package org.example.JaChain;

import java.security.PublicKey;

public class TransactionOutput {
    public String id;
    public PublicKey recipient; // The new owner of these coins.
    public float value; // amount of coins
    public String parentTransactionId; // id of the transaction this output was created in

    public TransactionOutput(PublicKey recipient, float value, String parentTransactionId){
        this.recipient = recipient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = StringUtil.applySha256(
                StringUtil.getStringFromKey(recipient)
                        + value
                        + parentTransactionId
        );
    }


    public boolean isMine(PublicKey publicKey){
        return (publicKey == recipient);
    }
}
