package org.example.JaChain;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Block {
    public String hash;
    public String previousHash;
    public String merkleRoot;
    public ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private long timeStamp; //number of milliseconds since 1/1/1970
    private int nonce;

    public Block(String previousHash){
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();

        this.hash = calculateHash();
    }

    public String calculateHash() {
        return StringUtil.applySha256(
                previousHash +
                        timeStamp +
                        nonce +
                        merkleRoot
        );
    }

    public void mineBlock(int difficulty){
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = StringUtil.getDifficultyString(difficulty);
        while (!hash.substring(0, difficulty).equals(target)){
            nonce ++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!: " + hash);
    }

    // Process transaction and chekc if valid, unless genesis block then ignore.
    public boolean addTransaction(Transaction transaction){
        if (transaction == null) return false;
        if(!Objects.equals(previousHash, "0")){
            if (!transaction.processTransaction()){
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Transaction Succesfully added to Block");
        return true;
    }

    @Override
    public String toString(){
        return "Block {" +
                "\n  hash='" + hash + '\'' +
                ",\n  previousHash='" + previousHash + '\'' +
                ",\n  data='" + merkleRoot + '\'' +
                ",\n  timeStamp=" + timeStamp +
                "\n}";
    }
}
