import java.util.ArrayList;

public class JaChain {

    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static int difficulty = 6;

    public static void main(String[] args){
        blockchain.add(new Block("First Block", "0"));
        System.out.println("Trying to Mine Block 1...");
        blockchain.getFirst().mineBlock(difficulty);

        blockchain.add(new Block("Second Block", blockchain.getLast().hash));
        System.out.println("Trying to Mine Block 2...");
        blockchain.getLast().mineBlock(difficulty);

        blockchain.add(new Block("Third Block", blockchain.getLast().hash));
        System.out.println("Trying to Mine Block 3...");
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
