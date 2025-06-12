package myBlockchain.Transactions;

import java.security.*;
import java.security.spec.ECGenParameterSpec; //Elliptic-curve cryptography
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import myBlockchain.myBlockchain;


public class Wallet 
{
    public PrivateKey privateKey; //To sign our transactions
    public PublicKey publicKey; //To verify that our signature is valid and data has not been tampered with

    // Local copy of UTXOs owned by this wallet
    public HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();


    public Wallet()
    {
        generateKeyPair();
    }
    // Generates a new key pair using ECDSA cryptography
    public void generateKeyPair()
    {
        try 
        {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");

            keyGen.initialize(ecSpec,random);
            KeyPair keyPair = keyGen.generateKeyPair();

            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } 
        catch (Exception e) 
        {
            throw new RuntimeException(e);
        }
    }
    // Calculates and returns the wallet's current balance
    public float getBalance() 
    {
	    float total = 0;	
        // Check all UTXOs in the blockchain for ones belonging to this wallet
        for (Map.Entry<String, TransactionOutput> item: myBlockchain.UTXOs.entrySet())
        {
        	TransactionOutput UTXO = item.getValue();
            if(UTXO.isMine(publicKey)) 
            {
                // Add to local UTXO collection and total balance
            	UTXOs.put(UTXO.id,UTXO);
            	total += UTXO.value ; 
            }
        }  
		return total;
	}
    // Creates and signs a new transaction to send funds
    public Transaction sendFunds(PublicKey _reciever,float value ) 
    {
        // Check if wallet has sufficient funds
		if(getBalance() < value) 
        {
			System.err.println("#Not Enough funds to send transaction. Transaction Discarded.");
			return null;
		}
        // Gather inputs to cover the transaction amount
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
		float total = 0;
		for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet())
        {
			TransactionOutput UTXO = item.getValue();
			total += UTXO.value;
			inputs.add(new TransactionInput(UTXO.id));
            // Stop when we have enough to cover the transaction
			if(total > value) break;
		}
        // Create and sign the new transaction
		Transaction newTransaction = new Transaction(publicKey, _reciever , value, inputs);
		newTransaction.generateSignature(privateKey);
        // Remove used UTXOs from wallet's local collection
		for(TransactionInput input: inputs)
        {
			UTXOs.remove(input.transactionOutputId);
		}
		return newTransaction;
	}
}
