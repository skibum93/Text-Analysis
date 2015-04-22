/*Will Richmond
11-24-2015
*/

//Importing for inputing files, scanner, array and arraylist classes
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

/**
	This program analyzes .txt files for ease of reading, calculating the Flesch Index, amount and types of punctuation, word length, and writing tone.
	User output is customized depending on the depth of analysis requested.
	The included sample txt file is a public domain Federalist Paper.
*/
	
public class TextAnalysis
{
	public static void main(String[] args) throws FileNotFoundException
	{
		//Input a txt file
		File input = new File("sample_federalist_paper.txt");
		Scanner scan = new Scanner(input);

      	//Text Analysis Information
      	double[] sentence= new double[3]; //Sentences ending in: EXCLAMATION POINTS!, question marks? and periods.
      	double[] syllable= new double[3]; //Vowel placement in the word: not the last letter, the last letter but not an e, the last letter and is an e
      	double[] wordLength= new double[4]; //Word character length: 1-4, 5-8, 9-12, 13+
      	String[] eEndSyllable = {"ble","gle","zle","gle","fle","dle","tle","ple","cle","kle"}; //The possible vowel syllable endings for last letter e
      	ArrayList<String> emphaSIS = new ArrayList<String>(); //Words in ALL CAPS

      	double caps=0; //The number of words that begin with a capital letter
      	double properNoun; // The number of proper nouns
      	double punctuation=0; //The number of non-ending sentence punctuation

      	double percentExclaim; //Percent of sentences ending in a exclaimation point!
      	double percentQuestion; //Percent of sentences ending in a question mark?
      	double percentPeriod; //Percent of sentences ending in a period.

      	double percentShortWord; //Percentage of 1-4 character words
      	double percentMediumWord; //Percentage of 5-8 character words
      	double percentLongWord; //Percentage of 9-12 character words
      	double percentDollarWord; //Percentage of 13+ character words

      	double percentVowelNotEnd; //Percent of syllables with vowels not at the end of the word
      	double percentVowelEnd; //Percent of syllables with vowels at the end of the word

      	double sentencePunctuation; //The number of punctuation marks in the sentence

      	double totalSentences; //Total number of sentences
      	double totalSyllables; //Total number of syllables
      	double totalWords; //Total number of words
      	double fleschIndex; //The index of the content's ease of reading

		//Read the input word by word
		while(scan.hasNext())
		{
			//Getting the word, it's length and the last letter
			String word=scan.next();
			int length= word.length();
			String lastLetter=word.substring(length-1,length);

			//Checking for punctuation, removing if found
         if((lastLetter.equals(","))||(lastLetter.equals("-"))||(lastLetter.equals(":"))||(lastLetter.equals(";")))
			{
				punctuation++;
				word=word.substring(0,length-1);
				length--;
			}

			//Checking for a sentence ending (!?.), removing if found
			else if ((lastLetter.equals("!")))
			{
				sentence[0]++;
				word=word.substring(0,length-1);
				length--;
			}
			else if ((lastLetter.equals("?")))
			{
				sentence[1]++;
				word=word.substring(0,length-1);
				length--;
			}
			else if ((lastLetter.equals(".")))
			{
				sentence[2]++;
				word=word.substring(0,length-1);
				length--;
			}

			//Recording the word length
			if(length<5)
			{
				wordLength[0]++;
			}
			else if((length<9)&&(length>4))
			{
				wordLength[1]++;
			}
			else if((length<13)&&(length>8))
			{
				wordLength[2]++;
			}
			else 
			{
				wordLength[3]++;
			}

			//Turning the word into a char array, and recording the first letter
			char[] wordChar = word.toCharArray();
			char firstLetter= wordChar[0];

			//Checking the capitalization for both the first letter and the entire word
			if((length>=2)&&(Character.isUpperCase(firstLetter)))
			{
					if(Character.isUpperCase(wordChar[1]))
					{
						emphaSIS.add(word); //With TWo initial capital letters, I assume the entire word is capitalized
					}
					else
					{
						caps++; //To later calculate proper nouns
					}
			}

			//Recording the syllables based on vowel placement
			boolean letterOne= VowelTest.getVowel(firstLetter);
			if (length==1)
			{
				//Checking capitalization for one letter words
				if(Character.isUpperCase(firstLetter))
				{
					caps++; //To later calculate proper nouns
				}
			
				syllable[1]++; //The word must be either A or I, assuming correct English
			}
			else if (length==2)
			{
				boolean letterTwo= VowelTest.getVowel(wordChar[1]);
            
            if(letterTwo==true)
				{
					if ((wordChar[1]=='e')||(wordChar[1]=='E'))
					{
						syllable[2]++;

						if(letterOne==true)
						{
							syllable[0]++; //Two lettered words can possibly have two syllables
						}
					}
					else
					{
						syllable[1]++;

						if(letterOne==true)
						{
							syllable[0]++; //Two lettered words can possibly have two syllables
						}
					}
				}
				else
				{
					syllable[0]++;
				}	
			}
			else if (length==3)
			{
			
			//Checks the vowel cases for three letter words
			boolean letterTwo= VowelTest.getVowel(wordChar[1]);
            boolean letterThree= VowelTest.getVowel(wordChar[2]);
            
				if( (letterOne==true&&letterTwo==false)||(letterTwo==true&&letterThree==false))
				{
					syllable[0]++;
				}
				if((letterTwo==false)&&((letterThree==true)))
				{
					if( (wordChar[2]=='e')|| (wordChar[2]=='E'))
					{
						syllable[2]++;
					}
					else
					{
						syllable[1]++;
					}
				}
			}
			else
			{
				//The vowel analysis for 3+ character words

				//Checking for syllables with a vowel followed by a consanant
				for(int i=0; i<length-1; i++)
				{
					boolean vowelLetterOne= VowelTest.getVowel(wordChar[i]);
					boolean vowelLetterTwo= VowelTest.getVowel(wordChar[i+1]);

					if((vowelLetterOne==true)&&(vowelLetterTwo==false))
					{
						syllable[0]++;
					}
				}

				//Checking for a final syllable if the last letter is a vowel
				boolean lastLetterCheck=VowelTest.getVowel(wordChar[length-1]);
				if(lastLetterCheck==true)
				{
					if((wordChar[length-1]!='e')||(wordChar[length-1]!='E'))
					{
						syllable[1]++; //All vowels besides e at the end of a word always cause a syllable 
					}
					else
					{
						//e can cause a syllable at the end of the word, with very specific endings 
						word=word.substring(length-4,length);
						if(((word.equalsIgnoreCase("stle"))||(word.equalsIgnoreCase("ckle"))))
						{
							syllable[2]++;
						}
						else
						{
							//checking for three letter e endings
							word=word.substring(1,4);
							int i=0;
							boolean _eEndSyllable=false;
							while ((i<eEndSyllable.length)&&(_eEndSyllable==false))
							{
								if(word.equalsIgnoreCase(eEndSyllable[i]))
								{
									syllable[2]++;
									_eEndSyllable=true;
								}
								i++;
							}
						}
					}	
				}	
			}
		}

		//Close reading scanner
		scan.close();

		//Analysis Calculations
		totalSentences=sentence[0]+sentence[1]+sentence[2]; 
      	totalSyllables=syllable[0]+syllable[1]+syllable[2];
      	totalWords=wordLength[0]+wordLength[1]+wordLength[2]+wordLength[3]; 
      	fleschIndex=206.835-84.6*(totalSyllables/totalWords)-1.015*(totalWords/totalSentences); //Provided formula

		properNoun=caps-totalSentences; //ALL CAPS words are excluded from proper nouns
		sentencePunctuation=punctuation/totalSentences;

		percentExclaim=sentence[0]/totalSentences*100;
      	percentQuestion=sentence[1]/totalSentences*100;
      	percentPeriod=100-percentExclaim-percentQuestion;

      	percentShortWord=wordLength[0]/totalWords*100; 
      	percentMediumWord=wordLength[1]/totalWords*100; 
      	percentLongWord=wordLength[2]/totalWords*100; 
      	percentDollarWord=100-percentShortWord-percentMediumWord-percentLongWord;

      	percentVowelNotEnd=syllable[0]/totalSyllables*100; 
      	percentVowelEnd=100-percentVowelNotEnd; 


      	//Customizable User Output

      	//Flesch Index, displayed to everyone
      	System.out.printf("Your writing has a readability index of %.2f, suitable for", fleschIndex);
      	if (fleschIndex>=90)
      	{
      		System.out.println(" anyone 10 or younger.");
      	}
      	else if ((fleschIndex<90)&&(fleschIndex>=80))
      	{
      		System.out.println(" an 11-year old.");
      	}
      	else if ((fleschIndex<80)&&(fleschIndex>=70))
      	{
      		System.out.println(" an 12-year old.");
      	}
      	else if ((fleschIndex<70)&&(fleschIndex>=60))
      	{
      		System.out.println(" an middle school or early high schooler.");
      	}
      	else if ((fleschIndex<60)&&(fleschIndex>=50))
      	{
      		System.out.println(" an high achieving high school senior.");
      	}
      	else if ((fleschIndex<50)&&(fleschIndex>=30))
      	{
      		System.out.println(" an competitent undergraduate college student.");
      	}
      	else
      	{
      		System.out.println(" for graduate students only. \nConsider editing your work.");
      	}

      	//Creates a second scanner and asks if the user wants more input
      	Scanner user = new Scanner(System.in);
      	System.out.println("\nWould you like to know more about the writing?\nType 'yes' if interested, or any key to close the program.");
      	String response= user.next();

      	//The Additional text analysis, if requested
      	if((response.equalsIgnoreCase("yes"))||(response.equalsIgnoreCase("\'yes\'"))||(response.equalsIgnoreCase("\"yes\"")))
      	{
      		//General text information
      		System.out.printf("The work was composed of %.0f words, made up of %.0f syllables, creating %.0f sentences.",totalWords,totalSyllables,totalSentences);
      		System.out.printf("\nThere were %.0f proper nouns, and a sentences averaged %.2f pieces of punctuation.",properNoun,sentencePunctuation);
      		System.out.printf("\n%.2f-percent of the syllables were from the beginning and middle of the word, and %.2f were from the end",percentVowelNotEnd,percentVowelEnd);
      		
      		//Word length
      		System.out.println("\n\nHere is the percentage of you word use length:");
      		System.out.printf("Short words (1-4 characters)- %.2f", percentShortWord);
      		System.out.printf("\nMedium words (5-8 characters)- %.2f", percentMediumWord);
      		System.out.printf("\nLong words (9-12 characters)- %.2f", percentLongWord);
      		System.out.printf("\nDollar words (13+ characters)- %.2f", percentDollarWord);
      		
      		//Displays tone information if exclaimation points or question marks were used
      		if(percentPeriod!=100)
      		{
      			System.out.println("\nRegarding tone, here is the percent on how you ended your sentences.");
      			System.out.printf("EXCLAIMING with a !- %.2f",percentExclaim);
      			System.out.printf("\nquestioning with a ?- %.2f",percentQuestion);
      			System.out.printf("\nNormally with a .- %.2f",percentPeriod);
      		}

      		//Displays the ALL CAPS words if any exist
      		if(emphaSIS.size()>0)
      		{
      			System.out.println("\n\nHere are the words you emphasized in all caps: "+emphaSIS);
      		}
      		
      		System.out.println("\nHave a great day!");
      	}
      	else
      	{
      		System.out.print("\nHave a great day!"); //Everyone should always have a great day :)
      	}
	
	}
}