package TerminalCalculator;

import java.util.Scanner;

public class terminalCalculator {
	//create the scanner object for the user input
	static Scanner userInput = new Scanner(System.in);
	//create a boolean variable to store if the calc. mode is in degrees.
	static boolean isDegrees = true;
	
	public static void main(String[] args) {
		System.out.println("========= PARSING CALCULATOR v1.0 ==========");
		System.out.println("Calculator set to degrees.");
		//call the main calculator command interface.
		commandInterface();
	}
	
	public static void commandInterface() {
		//Create a string variable to store the input returns from the command line input method
		String input = commandLineInput();	
		
		//if the input is "exit" then close the program
		if(input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("close")) {
			System.out.println("[alart] Closing program");
			System.exit(0);
		}
		//if the input is "help" then print the help interface
		else if(input.equalsIgnoreCase("help")) {
			System.out.println("============= CALCULATOR HELP  =============");
			System.out.println("= TO USE: enter in arithmetic expression   =");
			System.out.println("= into the '>' terminal prompt and select  =");
			System.out.println("= enter to see the result. Incorrect       =");
			System.out.println("= statements will either return \"NaN\" or   =");
			System.out.println("= \"Error.\" To change the mode trig of the  =");
			System.out.println("= calculator either type \"degree\" or       =");
			System.out.println("= \"radians\" into the prompt.               =");
			System.out.println("============================================");
			//restart the command interface
			commandInterface();
		}
		//if the input is "degrees" or "degree"
		else if(input.equalsIgnoreCase("degrees") || input.equalsIgnoreCase("degree")) {
			//if the mode is in degree print a error message
			if(isDegrees) {
				System.out.println("[error] Already set to degrees.");

			}
			//update the mode to degrees
			else {
				System.out.println("[alart] Updated to degrees.");
				isDegrees = true;
			}
			//restart the command interface
			commandInterface();
		}
		//if the input is "radians" or "radian"
		else if(input.equalsIgnoreCase("radians") || input.equalsIgnoreCase("radian")) {
			//if the mode is in degree print a error message
			if(!isDegrees) {
				System.out.println("[error] Already set to radians.");

			}
			//update the mode to radians
			else {
				System.out.println("[alart] Updated to radians.");
				isDegrees = false;
			}
			//restart the command interface
			commandInterface();
		}
		else {
			//try to evaluate the the arithmetic statement 
			try {
				System.out.println(calculateArithmeticStatement(input,isDegrees));
			}
			//if the statement won't evaluate then print an error statement
			catch(Exception e) {
				System.out.println("Error");
			}
			//restart the command interface
			commandInterface();
		}
	}
	

	public static String commandLineInput() {	
		System.out.print("> ");
		String userInputData = userInput.nextLine();	
		return userInputData;

	}
	

    /*
    * This is a recursive descent algorithm for solving for 
    * mathematical parsing operations.
    */
   public static double calculateArithmeticStatement(final String arithmeticStatement,
   	boolean inDegrees) {
       //return an object for the answer for this function analysis
   	//This allows for exceptions to pass though
   	return new Object() {
           //crconsumeChare the integer variables for parsing and iteration purposes
   		int charPosition = -1;
           int character;

           //return the next character for the descent
           void nextCharacter() {
           	character = (++charPosition < arithmeticStatement.length()) 
           		? arithmeticStatement.charAt(charPosition) : -1;
           }

           //process the parsing grammar rules with respect to position and exceptions
           double parse() {
           	//recursively call to push the process forward
           	nextCharacter();
               double x = expressionParse();
               //throw an exception error
               if(charPosition < arithmeticStatement.length()) { 
               	throw new RuntimeException
               	("Unexpected/Incorrect Input: " + (char) character);
               }
               return x;
               
           }
           
           //parse the term with respect to the multiplication and division operations
           double parseGivenTerm() {
               //call the parseFactor
           	double x = parseGivenFactor();
               //for the length of the parsing order
           	for(;;) {
           		//multiplication function
                   if(consumeChar('*')) {
                   	x = x * parseGivenFactor();
                   }
                   //division function
                   else if(consumeChar('/')) {
                   	x = x / parseGivenFactor();
                   }
                   else return x;
               }
           }
           
         //parse the term with respect to the addition and subtraction operations
           double expressionParse() {
               double x = parseGivenTerm();
               //for the length of the parsing order
               for(;;) {
               	//addition
                   if(consumeChar('+')) {
                   	x = x + parseGivenTerm();
                   }
                   //subtraction
                   else if(consumeChar('-')) {
                   	x = x - parseGivenTerm();
                   }
                   else return x;
               }
           }
           
           //consume the token in the process
           boolean consumeChar(int charToconsumeChar) {
           	//while the character is a space
               while (character == ' ') {
               	//update the character forward
               	nextCharacter();
               }
               if (character == charToconsumeChar) {
               	//recursively call to push the process forward
               	nextCharacter();
                   return true;
               }
               return false;
           }

           /*
            * perform the mathematical parsing operation 
            * on the characters and respective operators with
            * the operator grammars.
            */
           double parseGivenFactor() {
               //unary plus
           	if(consumeChar('+')) {
               	return + parseGivenFactor();
               }
           	// unary minus
               if(consumeChar('-')) {
               	return - parseGivenFactor(); 
               }
               //crconsumeChare variables for processing
               double x;
               int startcharPosition = this.charPosition;
               //process parentheses
               if(consumeChar('(')) { 
                   x = expressionParse();
                   if(!consumeChar(')')) {
                   	throw new RuntimeException("Missing ')'");
                   }
               }
               //process numbers
               else if((character >= '0' && character <= '9') || character == '.') {
                   while ((character >= '0' && character <= '9') || character == '.') {
                   	nextCharacter();
                   }
                   x = Double.parseDouble(arithmeticStatement.substring(startcharPosition, this.charPosition));
               } 
               //process trig/general math function
               else if(character >= 'a' && character <= 'z') { 
                   while (character >= 'a' && character <= 'z') {
                   	nextCharacter();
                   }
                   //crconsumeChare a string to process the grammar substring
                   String functionString = arithmeticStatement.substring(startcharPosition, this.charPosition);
                   if(consumeChar('(')) {
                       x = expressionParse();
                       //process missing closing parenthesis and throw exceptions
                       if(!consumeChar(')')) {
                       	throw new RuntimeException("Missing ')' after argument to " + functionString);
                       }
                   }
                   //process the result
                   else {
                       x = parseGivenFactor();
                   }
                   //process the result for square root
                   if(functionString.equals("sqrt")) {
                   	x = Math.sqrt(x);
                   }
                   //process the result for sine 
                   else if(functionString.equals("sin")) {
                   	//if the calculator is set to degrees and NOT radians 
                   	if(inDegrees) {
                   		x = Math.sin(Math.toRadians(x));
                   	}
                   	//if set to radians 
                   	else {
                   		x = Math.sin(x);
                   	}
                   }
                   //process the result for cosine 
                   else if(functionString.equals("cos")) {
                   	//if the calculator is set to degrees and NOT radians 
                   	if(inDegrees) {
                   		x = Math.cos(Math.toRadians(x));
                   	}
                   	//if set to radians 
                   	else {
                   		x = Math.cos(x);
                   	}
                   }
                   //process the result for tangent 
                   else if(functionString.equals("tan")) {
                   	//if the calculator is set to degrees and NOT radians 
                   	if(inDegrees) {
                   		x = Math.tan(Math.toRadians(x));
                   	}
                   	//if set to radians
                   	else {
                   		x = Math.tan(x);
                   	}
                   }
                   //if the functionString is not recognized
                   else throw new RuntimeException("functiontiontiontion is Unknown: " + functionString);
               }
               //throw an exception if the char is not excepted
               else {
                   throw new RuntimeException("Unexpected: " + (char) character);
               }
               //compute exponents
               if(consumeChar('^')) {
               	x = Math.pow(x, parseGivenFactor());
               }
               //return the final result
               return x;
           }
       //parse final result and return
       }.parse();
   }	
	
	
}
