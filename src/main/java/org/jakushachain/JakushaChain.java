package org.jakushachain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

public class JakushaChain {

    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<>();
    public static int difficulty = 5;
    public static Wallet walletA;
    public static Wallet walletB;
    public static float minimumTransaction = 0.1f;


    public static void main(String[] args) {
        // Setup Bouncey castle as a security provider
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        walletA = new Wallet();
        walletB = new Wallet();

        System.out.println("Private and public keys:");
        System.out.println(StringUtil.getStringFromKey(walletA.getPrivateKey()));
        System.out.println(StringUtil.getStringFromKey(walletA.getPublicKey()));

        Transaction transaction = new Transaction(walletA.getPublicKey(), walletB.getPublicKey(), 5, null);
        transaction.generateSignature(walletA.getPrivateKey());

        System.out.println("Is signature verified");
        System.out.println(transaction.verifySignature());

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