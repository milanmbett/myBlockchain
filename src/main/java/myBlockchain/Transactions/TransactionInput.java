package myBlockchain.Transactions;

public class TransactionInput 
{
	public String transactionOutputId; // Reference to the UTXO being spent
	public TransactionOutput UTXO; // The actual UTXO being spent
	
	public TransactionInput(String transactionOutputId) 
    {
		this.transactionOutputId = transactionOutputId;
    }
}

