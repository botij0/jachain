import java.util.Date;

public class Block {
    public String hash;
    public String previousHash;
    private String data;
    private long timeStamp; //number of milliseconds since 1/1/1970
    private int nonce;

    public Block(String data, String previousHash){
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash() {
        return StringUtil.applySha256(
                previousHash + Long.toString((timeStamp)) + Integer.toString(nonce) + data
        );
    }

    public void mineBlock(int difficulty){
        String target = new String(new char[difficulty]).replace('\0','0');
        while (!hash.substring(0, difficulty).equals(target)){
            nonce ++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!: " + hash);
    }

    @Override
    public String toString(){
        return "Block {" +
                "\n  hash='" + hash + '\'' +
                ",\n  previousHash='" + previousHash + '\'' +
                ",\n  data='" + data + '\'' +
                ",\n  timeStamp=" + timeStamp +
                "\n}";
    }
}
