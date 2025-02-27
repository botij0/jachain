import java.util.ArrayList;

public class JaChain {

    public static ArrayList<Block> blockchain = new ArrayList<Block>();

    public static void main(String[] args){
        blockchain.add(new Block("First Block", "0"));
        blockchain.add(new Block("Second Block", blockchain.getLast().hash));
        blockchain.add(new Block("Third Block", blockchain.getLast().hash));

        System.out.println(blockchain);
        System.out.println("Validation Chain: " + isChainValid());

    }

    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;

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
        }
        return true;
    }
}
