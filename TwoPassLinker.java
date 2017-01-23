/** 
 * @author Haley Danylchuk 
 * @version September 20, 2016 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class TwoPassLinker {

	public static void main(String[] args) throws FileNotFoundException {

		//The file name is passed as the first and only argument 
		if (args.length < 1) {
			System.err.println("File name missing");
			System.exit(0);
		}
		
		//creates a new file
		File fileName = new File(args[0]);
		
		//prints an error if the file cannot be read from 
		if (!fileName.canRead()) {
			System.err.printf("Cannot read from file %s\n.", fileName.getAbsolutePath());
			System.exit(0);
		}

		//New scanner is created for the first pass 
		Scanner fin = new Scanner(fileName);
		//ArrayList that will hold the first line of each module, which is symbols defined in that module 
		ArrayList<String> symbols = new ArrayList<String>();
		//ArrayList that will hold the value of the symbols defined 
		ArrayList<Integer> values = new ArrayList<Integer>();
		//ArrayList that will hold all of the addresses in each module 
		ArrayList<Integer> address = new ArrayList<Integer>();
		//ArrayList that holds the values of final address, as well as errors
		ArrayList<String> finalAddress = new ArrayList<String>();
		//ArrayList that holds the information from the second line of each module; the symbols used in that module 
		ArrayList<String> secondLine = new ArrayList<String>();
		//ArrayList that holds all of the used symbols of all of the modules to then check if one of the 
		//symbols was used but never defined 
		ArrayList<String> accumulated = new ArrayList<String>();
		//ArrayList for holding the last digit of each address for each module to see if a symbol was in the use 
		//list but never used 
		ArrayList<String> digit = new ArrayList<String>();
		
		int numberMods = fin.nextInt(); 
		int offset = 0;
		for(int x = 0; x < numberMods; x ++ ){
			int numberSymbols = fin.nextInt(); 
			
			for(int i = 0; i < numberSymbols; i++){ 
				String symbol = fin.next(); 
				int value = (fin.nextInt()) + offset; 
				
				//Checks if a symbol is multiply defined or not 
				if(!symbols.contains(symbol)){
					symbols.add(symbol);
					values.add(value); 
					} else{ 
						System.out.println("Error! " + symbol + " is multiply defined; first value used"); 
					}
			} 
			int numSymbols = fin.nextInt(); 
			for(int n = 0; n < numSymbols; n++){ 
				fin.next();
				}	
			int numAddresses = fin.nextInt(); 
			for(int m = 0; m< numAddresses; m++){ 
				fin.nextInt(); 
				
			}
			offset += numAddresses; 
		}
		
		//end of the first pass 
		fin.close(); 
		//boolean [] ifUsed = new boolean [symbols.size()]; 
		 
		
		//beginning of second pass 
		//create a new scanner 
		fin = new Scanner(fileName);

		 numberMods = fin.nextInt(); 
		 offset = 0;
	
		for(int x = 0; x < numberMods; x ++ ){
			int numberSymbols = fin.nextInt(); 
			for(int i = 0; i < numberSymbols; i++){ 
				fin.next(); 
				fin.nextInt(); 

			} 
			int numSymbols = fin.nextInt(); 
			secondLine = new ArrayList<String>(); 
			for(int n = 0; n < numSymbols; n++){ 
				String b = fin.next(); 
				secondLine.add(b); 
				accumulated.add(b);
			}	
			
			int numAdd = fin.nextInt(); 
			for(int m = 0; m< numAdd; m++){ 
				
				int number = fin.nextInt(); 
				address.add(number); 
				int oneAddress = address.get(m);
				String str = Integer.toString(oneAddress); 
				String [] split = str.split("");
				
				//adding the last digit of each address to digit to later check 
				//if a 4 exists and use list is not empty 
				digit.add(split[4]);
				
				//What to do for each type of address 
				if(split[4].equals("1")){
					String s = str.substring(0, 4); 
					finalAddress.add(s); 

				}else if(split[4].equals("2")) {
					String s = str.substring(1, 4); 
					int ss = Integer.parseInt(s);
					//checks if absolute address exceeds machine size 
					if(ss > 600){ 
						String tooLarge = str.substring(0, 1) + "000 " + "Error: Absolute address exceeds machine size; zero used." ; 
						finalAddress.add(tooLarge); 
						
					} else { 
						String addThis = str.substring(0, 4);
						finalAddress.add(addThis); 
					}
				}
				else if(split[4].equals("3")){ 
					String s = str.substring(1, 4); 
					int num = Integer.parseInt(s); 
					//checks if relative address exceeds module size 
					if(num > numAdd){ 
						String useZero = str.substring(0,1); 
						String errorString = useZero + "000 " + "Error: Relative address exceeds module size; zero used."; 
						finalAddress.add(errorString); 
					}
					num = num + offset; 

					String finalNum = Integer.toString(num); 
					int size = finalNum.length(); 
					if(size == 1){ 
						finalNum = str.substring(0, 1) +  "00" + finalNum; 
					} else if(size == 2){ 
						finalNum = str.substring(0, 1) + "0" + finalNum;
					}else{ 
						finalNum = str.substring(0, 1) + finalNum;
					}
					
					finalAddress.add(finalNum); 
				}else {

					String s = str.substring(1, 4); 
					int index = Integer.parseInt(s); 
					//checks if external address exceeds length of use list 
					if(index >= secondLine.size()){ 
						String newString = str.substring(0, 4); 
						String veryNewString = newString + " Error! External address exceeds length of use list; treat as immediate."; 
						finalAddress.add(veryNewString); 
					} 
					else { 
						
					String thisSymbol = secondLine.get(index);
			
					
					//This is for the error where a symbol is defined in a use list 
					//but isn't used in that module. My code doesn't work, but 
					//my logic is for each module, go through the ArrayList called address and 
					//put the last digit of each address in a new ArrayList called digit. 
					//If digit does not contain a "4" and the secondLine ArrayList (basically the use list) 
					//is not empty, then we know that a symbol was defined but never used, or in other words, 
					//referred to in an external address 
//					for(int v = 0; v < address.size(); v++){ 
//						if(!digit.contains("4")){
//							System.out.println("Error: " + address.get(v) + " Symbol is on use list but never used."); 
//							 
//						} else{ 
//							System.out.println("Symbol ");
//							System.out.println("this index was found " + v);
//						}
//					}
//					
					
				
				//checks for symbols used but not defined
				if(symbols.contains(thisSymbol)){
					int thisIndex = symbols.indexOf(thisSymbol); 
					int thisValue = values.get(thisIndex); 
					
					//ifUsed[symbols.indexOf(thisSymbol)] = true; 
				
					String val = Integer.toString(thisValue); 
					while(val.length() != 3 ){ 
						val = "0"+val; 
					}
					String y = str.substring(0, 1);
					String finalString = y + val; 
					finalAddress.add(finalString);
			
			} else { 
				
				String revisedAddress = str.substring(0, 1) + "000" + " Error: " + thisSymbol + " is used but not defined; zero used."; 
				finalAddress.add(revisedAddress); 
				}
			}
		}
	}
			address = new ArrayList<Integer>();
			digit = new ArrayList<String>(); 
			offset += numAdd; 
			}
	
		//Prints the symbol table and the memory map 
		System.out.println("Symbol Table"); 
		for(int r = 0; r < symbols.size(); r++){ 
			System.out.println(symbols.get(r) + "=" + values.get(r));
			
		} System.out.println(" "); 
		System.out.println("Memory Map"); 
		for(int w = 0; w < offset; w++){ 
			System.out.println(w + ": " + finalAddress.get(w) ); 
		}
		
		System.out.println(""); 
		
		//Warns that a symbol was defined but never used 
		for(int h = 0; h < symbols.size(); h++){ 
			String thisSym = symbols.get(h); 
			if(!accumulated.contains(thisSym)){ 
				//figure out what module it was defined in 
				System.out.println("Warning: " + thisSym + " was defined but never used." ); 
				
			}
		}
		
		//end of second pass 
		fin.close(); 
		}
	
	}
