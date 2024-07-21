package org.jakushachain;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;

public class Transaction {
    private String transactionId;
    private PublicKey sender;
    private PublicKey recipient;
    public float value;
    public byte[] signature;

    public ArrayList<TransactionInput> inputs = new ArrayList<>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<>();

    private static int sequence = 0;

    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
    }

    private String calculateHash() {
        sequence++;
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender) +
                      StringUtil.getStringFromKey(recipient) +
                      Float.toString(value) + sequence
        );
    }

    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.
                getStringFromKey(sender) +
                StringUtil.getStringFromKey(recipient) +
                Float.toString(value);

        signature = StringUtil.applyECDSASig(privateKey, data);
    }

    public boolean verifySignature() {
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + Float.toString(value);
        return StringUtil.verifyECDSASig(sender, data, signature);
    }

    public boolean processTransaction() {
        if(!verifySignature()) {
            System.out.println("#Transaction Signature failed to verify");
            return false;
        }

        // gather transaction inputs
        for(TransactionInput i : inputs) {
            i.setUTXO(JakushaChain.UTXOs.get(i.getTransactionOutputId()));
        }

        if(getInputValue() < JakushaChain.minimumTransaction) {
            System.out.println("Transaction Inputs to small: " + getInputValue());
            return false;
        }

        float leftOver = getInputValue() - value;
        transactionId = calculateHash();
        outputs.add(new TransactionOutput(this.recipient, value, transactionId));
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionId));

        for(TransactionOutput o : outputs)
            JakushaChain.UTXOs.put(o.getId(), o);

        for(TransactionInput i : inputs) {
            if(i.getUTXO() == null)
                continue;
            JakushaChain.UTXOs.remove(i.getUTXO().getId());
        }

        return true;
    }

    public float getInputValue() {
        float total = 0;

        for(TransactionInput i : inputs) {
            if(i.getUTXO() == null)
                continue;
            value += i.getUTXO().getValue();
        }
        return value;
    }

    public float getOutputsValue() {
        float total = 0;

        for(TransactionOutput o : outputs)
            total += o.getValue();

        return total;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
