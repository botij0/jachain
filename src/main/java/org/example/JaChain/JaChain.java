package org.example.JaChain;

import java.security.Security;
import java.util.ArrayList;

public class JaChain {

    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static int difficulty = 5;
    public static Wallet walletA;
    public static Wallet walletB;

    public static void main(String[] args){
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        walletA = new Wallet();
        walletB = new Wallet();

        System.out.println("Private Key: " + StringUtil.getStringFromKey(walletA.privateKey));
        System.out.println("Public Key: " + StringUtil.getStringFromKey(walletA.publicKey));

        Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 5, null);
        transaction.generateSignature(walletA.privateKey);

        System.out.println("Is signature verified:");
        System.out.println(transaction.verifySignature());
    }

    public static void displayBasicChain() {
        blockchain.add(new Block("First org.example.JaChain.Block", "0"));
        System.out.println("Trying to Mine org.example.JaChain.Block 1...");
        blockchain.getFirst().mineBlock(difficulty);

        blockchain.add(new Block("Second org.example.JaChain.Block", blockchain.getLast().hash));
        System.out.println("Trying to Mine org.example.JaChain.Block 2...");
        blockchain.getLast().mineBlock(difficulty);

        blockchain.add(new Block("Third org.example.JaChain.Block", blockchain.getLast().hash));
        System.out.println("Trying to Mine org.example.JaChain.Block 3...");
        blockchain.getLast().mineBlock(difficulty);

        System.out.println("Validation Chain: " + isChainValid());
        System.out.println("The Blockchain: ");
        System.out.println(blockchain);
    }

    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0','0');

        for (int i = 1; i< blockchain.size(); i++){
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);

            if (!currentBlock.hash.equals(currentBlock.calculateHash())){
                System.out.println("Hashes not equal");
                return false;
            }

            if (!previousBlock.hash.equals(currentBlock.previousHash)){
                System.out.println("Previous Hashes not equal");
                return false;
            }

            if (!currentBlock.hash.substring(0,difficulty).equals(hashTarget)){
                System.out.println("This block hasn't been mined");
                return false;
            }
        }
        return true;
    }
}
