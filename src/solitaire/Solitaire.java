package solitaire;

import java.io.IOException;
import java.util.Scanner;

import java.util.Random;

/**
 * This class implements a simplified version of Bruce Schneier's Solitaire Encryption algorithm.
 * 
 * @author RU NB CS112
 */
public class Solitaire {
	
	/**
	 * Circular linked list that is the deck of cards for encryption
	 */
	CardNode deckRear;
	
	/**
	 * Makes a shuffled deck of cards for encryption. The deck is stored in a circular
	 * linked list, whose last node is pointed to by the field deckRear
	 */
	public void makeDeck() {
		// start with an array of 1..28 for easy shuffling
		int[] cardValues = new int[28];
		// assign values from 1 to 28
		for (int i=0; i < cardValues.length; i++) {
			cardValues[i] = i+1;
		}
		
		// shuffle the cards
		Random randgen = new Random();
 	        for (int i = 0; i < cardValues.length; i++) {
	            int other = randgen.nextInt(28);
	            int temp = cardValues[i];
	            cardValues[i] = cardValues[other];
	            cardValues[other] = temp;
	        }
	     
	    // create a circular linked list from this deck and make deckRear point to its last node
	    CardNode cn = new CardNode();
	    cn.cardValue = cardValues[0];
	    cn.next = cn;
	    deckRear = cn;
	    for (int i=1; i < cardValues.length; i++) {
	    	cn = new CardNode();
	    	cn.cardValue = cardValues[i];
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
	    }
	}
	
	/**
	 * Makes a circular linked list deck out of values read from scanner.
	 */
	public void makeDeck(Scanner scanner) 
	throws IOException {
		CardNode cn = null;
		if (scanner.hasNextInt()) {
			cn = new CardNode();
		    cn.cardValue = scanner.nextInt();
		    cn.next = cn;
		    deckRear = cn;
		}
		while (scanner.hasNextInt()) {
			cn = new CardNode();
	    	cn.cardValue = scanner.nextInt();
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
		}
	}
	
	/**
	 * Implements Step 1 - Joker A - on the deck.
	 */
	void jokerA() {
		CardNode ptr = deckRear.next;
		CardNode prev = null;
		int joker1 = 27;
		while(ptr!=null && ptr.cardValue!=27)
		{
			
			prev = ptr;
			ptr = ptr.next;
		}
			prev = ptr.next;
			ptr.cardValue = prev.cardValue;
			prev.cardValue = joker1;	
	}
	
	/**
	 * Implements Step 2 - Joker B - on the deck.
	 */
	void jokerB() {
	    CardNode ptr = deckRear.next;
	    CardNode prev = null;
	    CardNode other = null;
	    int joker2 = 28;
	    
	    while(ptr!=null && ptr.cardValue!=28)
	    {
	    	other = prev;
	    	prev = ptr;
	    	ptr = ptr.next;
	    }
	    prev = ptr.next;
	    other=ptr.next.next;
	    ptr.cardValue = prev.cardValue;
	    prev.cardValue = other.cardValue;
	    other.cardValue = joker2;
	}
	
	/**
	 * Implements Step 3 - Triple Cut - on the deck.
	 */
	void tripleCut() {
		CardNode ptr = deckRear.next;
		CardNode temp = deckRear;
		int FirstJ = 0, SecondJ = 0, count=0;
		
		while(ptr!=deckRear)
		{
			if(ptr.cardValue==27 || ptr.cardValue==28)
			{
				FirstJ = ptr.cardValue;
				break;	
			}
			ptr=ptr.next;
		}
		while(ptr!=deckRear)
		{
			ptr=ptr.next;
			if(ptr.cardValue==27 || ptr.cardValue==28)
			{	
				SecondJ = ptr.next.cardValue;
				break;
			}
			
		}
		
		CardNode Front;
		do{
		Front = new CardNode();
		Front.cardValue = ptr.cardValue;
		Front.next = deckRear.next;
		deckRear.next = Front;
		for(int i=0; i<28+count; i++)
		{
			ptr=ptr.next;
		}
		count++;
		}while(Front.cardValue!=FirstJ);
		
		CardNode nextFront;
		do{
		if(deckRear.cardValue==28)
		{
			break;
		}
		if(deckRear.cardValue==27)
		{
			break;
		}
		nextFront = new CardNode();
		nextFront.cardValue = temp.cardValue;
		nextFront.next = deckRear.next;
		deckRear.next = nextFront;
		for(int k=0; k<28+count; k++)
		{
			temp=temp.next;
		}
	
		count++;
		}while(nextFront.cardValue!=SecondJ);
		
		ptr.next=deckRear.next;
		deckRear=ptr;	
	}
	
	/**
	 * Implements Step 4 - Count Cut - on the deck.
	 */
	void countCut() {	
		CardNode ptr = deckRear.next;
		CardNode prev = ptr;
		CardNode temp = deckRear.next;
		CardNode last = deckRear;
		int next=0;
		
		int count = 27-deckRear.cardValue;
		int FirstNum = ptr.cardValue;
		
		if(deckRear.cardValue==27 || deckRear.cardValue==28)
		{
			return;
		}
		for(int h=0; h<deckRear.cardValue+1; h++)
		{
			last=last.next;
		}
		
		int LastNum = last.cardValue;

		for(int i=0; i<count; i++)
		{
			ptr=ptr.next;
		}
		
		do{
			int saved = ptr.cardValue;
			ptr.cardValue = prev.cardValue;
			prev.cardValue = saved;
			prev=prev.next;
			ptr=ptr.next;
			if(prev.cardValue==FirstNum)
			{
				prev=deckRear.next;
			}
		}while(ptr!=deckRear);
		
		while(prev.next.cardValue!=FirstNum)
		{
			temp=prev;
			prev=prev.next;
		}
		CardNode Front;
		do{
		Front = new CardNode();
		Front.cardValue = prev.cardValue;
		Front.next = deckRear.next;
		deckRear.next = Front;
		for(int k=0; k<28+next; k++)
		{
			prev=prev.next;
		}
		temp=prev.next;
		next++;
		}while(Front.cardValue!=LastNum);
 
		while(temp.cardValue!=FirstNum)
		{
		temp=temp.next;
		temp = prev.next.next;
		prev.next = temp;
		}
	}
	

	
	
	
	/**
	 * Gets a key. Calls the four steps - Joker A, Joker B, Triple Cut, Count Cut, then
	 * counts down based on the value of the first card and extracts the next card value 
	 * as key. But if that value is 27 or 28, repeats the whole process (Joker A through Count Cut)
	 * on the latest (current) deck, until a value less than or equal to 26 is found, which is then returned.
	 * 
	 * @return Key between 1 and 26
	 */
	int getKey() {
		jokerA();
		jokerB();
		tripleCut();
		countCut();
		CardNode ptr=deckRear.next;
		int FirstNum = ptr.cardValue;
		 
		if(FirstNum==28)
		{
			FirstNum=27;
		}
		
		for(int i=0; i<FirstNum; i++)
		{
			ptr=ptr.next;
		}
		while(ptr.cardValue>26)
		{
			getKey();
		}
		
		int key = ptr.cardValue;
				
		return key;
	}
	
	/**
	 * Utility method that prints a circular linked list, given its rear pointer
	 * 
	 * @param rear Rear pointer
	 */
	private static void printList(CardNode rear) {
		if (rear == null) { 
			return;
		}
		System.out.print(rear.next.cardValue);
		CardNode ptr = rear.next;
		do {
			ptr = ptr.next;
			System.out.print("," + ptr.cardValue);
		} while (ptr != rear);
		System.out.println("\n");
	}
	
	/**
	 * Encrypts a message, ignores all characters except upper case letters
	 * 
	 * @param message Message to be encrypted
	 * @return Encrypted message, a sequence of upper case letters only
	 */
	public String encrypt(String message) {
		message = message.toUpperCase();
		StringBuilder newMessage = new StringBuilder();
		for(int i=0; i<message.length(); i++)
		{
			if(Character.isLetter(message.charAt(i)))
			{
				int key = getKey();
				int c = message.charAt(i)-'A'+1;
				int add = c+key;
				if(add>26)
				{
					add=add-26;
				}
				char newChar = (char)(add-1+'A');
				newMessage.append(newChar);
			}
		}
		
	    return newMessage.toString();
	}
	
	/**
	 * Decrypts a message, which consists of upper case letters only
	 * 
	 * @param message Message to be decrypted
	 * @return Decrypted message, a sequence of upper case letters only
	 */
	public String decrypt(String message) {
		message = message.toUpperCase();
		StringBuilder newMessage = new StringBuilder();
		for(int i=0; i<message.length(); i++)
		{
			if(Character.isLetter(message.charAt(i)))
			{
				int key = getKey();
				int c = message.charAt(i)-'A'+1;
				int sub = c-key;
				if(sub<=0)
				{
					sub = c+26-key;
				}
				char newChar = (char)(sub-1+'A');
				newMessage.append(newChar);
			}
		}
			
	    return newMessage.toString();
	}
}
