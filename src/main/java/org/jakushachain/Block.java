package org.jakushachain;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Block {

    private String hash;
    private String previousHash;
    private String data;
    private long timeStamp;
    private int nonce;
    private String merkleRoot;

    public ArrayList<Transaction> transactions = new ArrayList<>();

    public Block(String previousHash ) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();

        this.hash = calculateHash();
    }

    public String calculateHash() {
        return StringUtil.applySha256(
                previousHash + Long.toString(timeStamp) +
                        data + Integer.toString(nonce) + merkleRoot);
    }

    public void mineBlock(int difficulty) {
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = StringUtil.getDificultyString(difficulty);
        while(!hash.substring( 0, difficulty).equals(target)) {
            nonce ++;
            hash = calculateHash();
        }
        System.out.println("Block Mined: " + hash);
    }

    public boolean addTransaction(Transaction transaction) {
        if(transaction == null)
            return false;

        if((!Objects.equals(previousHash, "0"))) { // if it is not the genesis block
            if((!transaction.processTransaction())) {
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Transaction Successfully added to Block");
        return true;
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }
}