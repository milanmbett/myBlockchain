package myBlockchain.Transactions;

import java.security.PublicKey;
import myBlockchain.Util.StringUtil;

public class TransactionOutput 
{
	public String id; // Unique identifier for this output
	public PublicKey reciever; // Who can spend this output
	public float value; // Amount of funds in this output
	public String parentTransactionId; // ID of the transaction that created this output
	
	//Constructor
	public TransactionOutput(PublicKey reciever, float value, String parentTransactionId) 
    {
		this.reciever = reciever;
		this.value = value;
		this.parentTransactionId = parentTransactionId;
        // Generate unique ID based on recipient, value, and parent transaction
		this.id = StringUtil.applySHA256(StringUtil.getStringFromKey(reciever)+Float.toString(value)+parentTransactionId);
	}
	
    // Checks if this output belongs to the given public key
	public boolean isMine(PublicKey publicKey) 
    {
		return (publicKey == reciever);
	}
	
}
