package myBlockchain;

import java.util.ArrayList;
import com.google.gson.GsonBuilder; //Objects to JSON

public class myBlockchain
{
    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static void main(String[] args) 
    {
        blockchain.add(new Block("First", "0"));
        blockchain.add(new Block("Second", blockchain.get(blockchain.size()-1).hash));
        blockchain.add(new Block("Third", blockchain.get(blockchain.size()-1).hash));
        
        String blockChainJSON = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println(blockChainJSON);
    }
}
