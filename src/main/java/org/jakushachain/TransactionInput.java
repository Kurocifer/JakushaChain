package org.jakushachain;

// Transaction inputs are references to previous transaction outputs
public class TransactionInput {
    private String TransactionOutputId; // Reference to TransactionOutputs -> transactionId
    private TransactionOutput UTXO; // Contains the Unspent transaction output

    public TransactionInput(String transactionOutputId) {
        this.TransactionOutputId = transactionOutputId;
    }

    public TransactionOutput getUTXO() {
        return UTXO;
    }

    public void setUTXO(TransactionOutput UTXO) {
        this.UTXO = UTXO;
    }

    public String getTransactionOutputId() {
        return TransactionOutputId;
    }
}