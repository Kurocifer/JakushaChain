package org.jakushachain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class JakushaChain {

    public static ArrayList<Block> blockchain = new ArrayList<>();
    public static final int difficulty = 5;

    public static void main(String[] args) {
        blockchain.add(new Block("Soy el primer bloque", "0" ));
        System.out.println("Trying to Mine block 1...");
        blockchain.getFirst().mineBlock(difficulty);

        blockchain.add(new Block("Segundo bloque list", blockchain.getLast().getHash()));
        System.out.println("Trying to Mine block 2...");
        blockchain.get(1).mineBlock(difficulty);

        blockchain.add(new Block("Oye, soy el terver bloque", blockchain.getLast().getHash()));
        System.out.println("Trying to Mine block 3...");
        blockchain.get(2).mineBlock(difficulty);

        System.out.println("\nBlockchain is valid: " + isChainValid());

        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println(blockchainJson);
    }

    public static boolean isChainValid() {
        Block currentBlock, previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        for(int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);

            // in case data in a block altered
            if(!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                System.out.println("Current hashes don't match");
                return false;
            }
            // in case an unknown block is added to the chain
            if(!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                System.out.println("Previous hashes don't match");
                return false;
            }

            if(!currentBlock.getHash().substring(0, difficulty).equals(hashTarget)) {
                System.out.printf("Block %d hasn't been mined yet", i);
                return false;
            }
        }
        return true;
    }


}