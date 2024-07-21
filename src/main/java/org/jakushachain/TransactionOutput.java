package org.jakushachain;

import java.security.PublicKey;

// A wallet's balance is the sum of all unspent transaction outputs addressed to
// the owner of the wallet
public class TransactionOutput {
    private String id;
    private PublicKey recipient; // The new owner of these coins
    private float value; // The amount of coins they own
    private String parentTransactionId; // The id of the transaction this output was created in

    public TransactionOutput(PublicKey recipient, float value, String parentTransactionId) {
        this.recipient = recipient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = StringUtil.applySha256(
                StringUtil.getStringFromKey(recipient) +
                Float.toString(value) + parentTransactionId);
    }

    public boolean isMine(PublicKey publicKey) {
        return publicKey == recipient;
    }

    public float getValue() {
        return value;
    }

    public String getId() {
        return id;
    }

    public PublicKey getRecipient() {
        return recipient;
    }
}
