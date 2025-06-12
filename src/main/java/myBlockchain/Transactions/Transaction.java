package myBlockchain.Transactions;

import java.security.*;
import java.util.ArrayList;

import myBlockchain.Util.StringUtil;
import myBlockchain.myBlockchain;

public class Transaction
{
    public String transactionId; // Unique identifier for this transaction
    public PublicKey sender; // Who is sending the funds
    public PublicKey reciever; // Who is receiving the funds
    public float value; // Amount being sent
    public byte[] signature; // Digital signature to prove ownership

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    private static int sequence = 0; // Counter for unique transaction IDs

    public Transaction(PublicKey sender, PublicKey reciever, float value,  ArrayList<TransactionInput> inputs)
    {
        this.sender = sender;
        this.reciever = reciever;
        this.value = value;
        this.inputs = inputs;
    }

    // Calculates a unique hash for this transaction
	private String calulateHash() 
    {
		sequence++;
		return StringUtil.applySHA256(
				StringUtil.getStringFromKey(sender) +
				StringUtil.getStringFromKey(reciever) +
				Float.toString(value) + sequence
				);
	}
    // Signs the transaction with the sender's private key
    public void generateSignature(PrivateKey privateKey) 
    {
	    String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciever) + Float.toString(value)	;
	    signature = StringUtil.applyECDSASig(privateKey,data);		
    }
    // Verifies the transaction signature is valid
    public boolean verifiySignature() 
    {
	    String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(reciever) + Float.toString(value)	;
	    return StringUtil.verifyECDSASig(sender, data, signature);
    }
    // Processes the transaction and updates the UTXO set
    public boolean processTransaction() 
    {
		// Verify the transaction signature
		if(verifiySignature() == false) {
			System.out.println("#Transaction Signature failed to verify");
			return false;
		}
		
        // Gather transaction inputs from UTXO set
		for(TransactionInput i : inputs) 
        {
			i.UTXO = myBlockchain.UTXOs.get(i.transactionOutputId);
		}
        // Check minimum transaction amount
		if(getInputsValue() < myBlockchain.minimumTransaction) 
        {
			System.out.println("#Transaction Inputs to small: " + getInputsValue());
			return false;
		}
		
		//generate transaction outputs:
		float leftOver = getInputsValue() - value; //get value of inputs then the left over change:
		transactionId = calulateHash();
		outputs.add(new TransactionOutput( this.reciever, value,transactionId)); //send value to recipient
		outputs.add(new TransactionOutput( this.sender, leftOver,transactionId)); //send the left over 'change' back to sender		
		
        // Add new outputs to global UTXO set
		for(TransactionOutput o : outputs) 
        {
			myBlockchain.UTXOs.put(o.id , o);
		}
        // Remove spent UTXOs from global set
		for(TransactionInput i : inputs) 
        {
			if(i.UTXO == null) continue; //if Transaction can't be found skip it 
			myBlockchain.UTXOs.remove(i.UTXO.id);
		}
		
		return true;
	}
    // Calculates total value of all inputs
	public float getInputsValue() 
    {
		float total = 0;
		for(TransactionInput i : inputs) 
        {
			if(i.UTXO == null) continue;
			total += i.UTXO.value;
		}
		return total;
	}
    // Calculates total value of all outputs
	public float getOutputsValue() 
    {
		float total = 0;
		for(TransactionOutput o : outputs) {
			total += o.value;
		}
		return total;
	}
}
