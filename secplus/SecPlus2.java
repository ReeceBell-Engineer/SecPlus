import java.io.BufferedReader;
import java.lang.StringBuffer;
import java.lang.StringBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class SecPlus2 {
	
	// test bank has all questions
	ArrayList<String> testBank;
	// Answer list 
	ArrayList<String> answerList = new ArrayList<>();
	// acronym list 
	ArrayList<String> acronymList;
	// flashcard list 
	ArrayList<String> FCBank = new ArrayList<>();
	
	String acronymTopic = "Acronyms";
	String questionTopic = "Questions";
	boolean isTesting;
	String userInput;
	int questionCount = 0;
	int questionCorrect = 0;
	int tickCount = 0;
	int tickCorrect = 0;
	
	public SecPlus2() {
		testBank = generateTestQuestionBank();
		acronymList = generateAcronymBank(acronymTopic);
		displayTitle();
		displayMainMenu();
	}
	
	private void displayMainMenu() {
		StringBuffer buf = new StringBuffer();
		buf.append("\t| 1. Take Test | 2. Flashcards | 3. Acronyms | 4. Exit |\n");
		System.out.println(buf.toString());
		String input = getUserInput();
		if (input.equals("1") || input.equals("test") || input.equals("take test")) {
			List<Integer> testQ = new ArrayList<>();
			int numQ = 1;
			try{
				System.out.println("How many Questions? (1 - " + testBank.size() + ")");
				numQ = Integer.parseInt(getUserInput());
			}catch (NumberFormatException ex) {
				System.out.println("Enter a number 1 through " + testBank.size());
				displayMainMenu();
			}
			
			testQ = genRandQuestions(numQ);
			isTesting = true;
			runQ(testQ);
		} else if (input.equals("2") || input.equals("flash") || input.equals("flashcards")) {
			displayMainMenu();
		} else if (input.equals("3") || input.equals("acronyms")) {
			displayMainMenu();
		} else if (input.equals("4") || input.equals("exit")) {
			System.exit(0);
		} else if (input.contains("help")) {
			String[] help = input.split(" ");
			if (help.length > 1) {
				displayAcronyms(help[1]);
			} else {
				System.out.println("You did not enter an acronym to look up.");
				System.out.println("Example input: help rsa\n");
			}
			displayMainMenu();
		}
	}
	
	private List<Integer> genRandQuestions(int num) {
		// System.out.println("Entered genRandQuestions");
		List<Integer> uniqueNums = new ArrayList<>();
		
		// pick 90 random numbers between 1 and the length of testBank
		for (int i = 0; i < num; i++) {
			// get random number
			int qNdx = pickRandomQuestionIndex();
			// if uniqueNums has an entry then check all entries for already existing 
			// value equal to new random number. Else there must be no values so add the first one.
			if (uniqueNums.size() > 1) {
				boolean numValid = true;
				for (int j = 0; j < uniqueNums.size(); j ++) {
					if (uniqueNums.get(j).equals(qNdx)) {
						numValid = false;
						break;
					}
				}
				if (numValid) {
					uniqueNums.add(qNdx);
				} else {
					i--;
				}
			} else {
				uniqueNums.add(qNdx);
			}
		}
		return uniqueNums;
	}

	private int pickRandomQuestionIndex() {
		// System.out.println("Entered pickRandomQuestionIndex");
        int ndx = -1;
        Random rand = new Random();
        ndx = rand.nextInt(testBank.size());
        return ndx;
    }
	
	private void displayTitle() {
		StringBuffer buf = new StringBuffer();
		buf.append("\t ____  _____ ____        _____ ___  _\n");
		buf.append("\t/ ___|| ____/ ___| _    |___  / _ \\/ |\n");
		buf.append("\t\\___ \\|  _|| |   _| |_     / / | | | |\n");
		buf.append("\t ___) | |__| |__|_   _|   / /| |_| | |\n");
		buf.append("\t|____/|_____\\____||_|    /_/  \\___/|_|\n\n\n");
		System.out.println(buf.toString());
	}
	
	private void resetMetrics() {
		questionCount = 0;
		questionCorrect = 0;
		tickCount = 0;
		tickCorrect = 0;
	}
	
	private void runQ(List<Integer> testQ) {
		// System.out.println("Running");
		resetMetrics();
		long timeStart = System.currentTimeMillis();
        for (int i = 0; i < testQ.size(); i++) {
			System.out.println("");
			System.out.println("# " + (i + 1));
			String answers = setUpQuestionDisplay(testQ.get(i));
			System.out.println(answers);
			userInput = getUserInput();
			if (userInput.equals("exit")) {
				isTesting = false;
				break;
			}
			boolean valid = false;
			while (!valid) {
				if (userInput.contains("help")) {
					String[] help = userInput.split(" ");
					if (help.length > 1) {
						displayAcronyms(help[1]);
					} else {
						System.out.println("You did not enter an acronym to look up.");
						System.out.println("Example input: help rsa");
					}
					setUpQuestionDisplay(testQ.get(i));
					userInput = getUserInput();
				} else {
					valid = true;
					break;
				}
			}
			boolean correct = checkAnswers(userInput, testQ.get(i));
			if (!correct) {
				System.out.println("\n");
				displayTestMenu(testQ.get(i));
			}
        }
		long timeStop = System.currentTimeMillis();
		long timeSec = (timeStop - timeStart) / 1000;
		double timeMin = (double) timeSec / 60.0;
		System.out.print("\n\n\tYou Have Finished in " + timeMin + " minutes\n\n");
		
		displayMetrics();
		
		System.out.print("\n\n\t============ TRY AGAIN? =============\n\n");
		// playAgain
		displayTitle();
		displayMainMenu();
    }

	private void displayTestMenu(int q) {
		StringBuffer buf = new StringBuffer();
		buf.append("\t| 1. Next | 2. Flashcard | 3. Quit |\n");
		System.out.println(buf.toString());
		String input = getUserInput();
		if (input.equals("1") || input.equals("next")) {
			
		} else if (input.equals("2") || input.equals("flash") || input.equals("flashcard")) {
			// get flashcard for the current question
			displayFlashcard(q);
			displayTestMenu(q);
		} else if (input.equals("3") || input.equals("quit")) {
			displayTitle();
			displayMainMenu();
		} else if (input.contains("help")) {
			String[] help = input.split(" ");
			if (help.length > 1) {
				displayAcronyms(help[1]);
			} else {
				System.out.println("You did not enter an acronym to look up.");
				System.out.println("Example input: help rsa\n");
			}
			displayTestMenu(q);
		} else {
			System.out.println("You entered an invalid option!");
			displayTestMenu(q);
		}
	}

	private void displayFlashcard(int q) {
		String fc = FCBank.get(q);
		System.out.print(fc);
		System.out.print("\n\n");
	}

	private boolean checkAnswers(String ui, int ans) {
		// keep track of how many questions have passed
		questionCount++;
		boolean correct = false;
		char[] uiChar = ui.toLowerCase().toCharArray();
		String fullAnswers = answerList.get(ans);
		String[] answers = fullAnswers.split(":");
		char[] ansChar = answers[1].toLowerCase().trim().toCharArray();
		System.out.print("\tYour reponse: ");
		for (int i = 0; i < uiChar.length; i++) {
			System.out.print(uiChar[i]);
		}
		System.out.print("\n\tThe answer(s): ");
		for (int i = 0; i < ansChar.length; i++) {
			System.out.print(ansChar[i]);
		}
		System.out.println("\n");
		int qTick = 0;
		// for (char a : uiChar) {System.out.print("answer " + a + " ");};
		for (int i = 0; i < ansChar.length; i++) {
			tickCount++;
			for (int j = 0; j < uiChar.length; j++) {
				if (ansChar[i]== uiChar[j]) {
					tickCorrect++;
					qTick++;
				}
			}				
		}
		if (qTick == ansChar.length) {
			System.out.println("\tCorrect");
			correct = true;
			questionCorrect++;
		} else if (qTick >= 1 && qTick < ansChar.length) {
			System.out.println("\tPartial Correct");
		} else {
			System.out.println("\tIncorrect!!!");
		}

		return correct;
	}
	
	private void displayAcronyms(String acr) {
		boolean found = true;
		String sResult = null;
		for (int i = 0; i < acronymList.size(); i++) {
			String[] result = acronymList.get(i).split(",");
			if (result[0].equalsIgnoreCase(acr)) {
				if (result.length > 1) {
					sResult = result[1];
				} else {
					System.out.println("Acronym found but the meaning has not been added.");
				}
				found = true;
				break;
			}
			found = false;
		}
		if (found && sResult != null) {
			System.out.println(sResult + "\n");
		} else if (!found) {
			System.out.println("Acronym not found!");
		}
	}
	
	private double calcPercent() {
		//System.out.println(tickCorrect + " / " + tickCount);
		double total = (double) tickCorrect * 100 / (double) tickCount;
		return total;
	}
	
	private void displayMetrics() {
		System.out.println("\n");
		double total = calcPercent();
		StringBuffer buf = new StringBuffer();
		buf.append("|# of Questions |").append(" " + questionCount + "\n");
		buf.append("|     # Correct |").append(" " + questionCorrect + "\n");
		buf.append("|    # of Ticks |").append(" " + tickCount + "\n");
		buf.append("|     # Correct |").append(" " + tickCorrect + "\n");
		buf.append("|       % Score |").append(" " + total + " %");
		System.out.println(buf.toString());
	}
	

	
	private String setUpQuestionDisplay(int qNdx) {
		StringBuffer buf = new StringBuffer();
		String question = testBank.get(qNdx);
		// String[] qa = testBank.get(qNdx).split("%,");
		// StringBuffer buf = new StringBuffer();
		// System.out.println("");
		// for (int i = 0; i < qa.length; i++) {
		// 	if (i == 0) {
		// 		System.out.println("\t" + qa[i]);
		// 		System.out.println("");
		// 	} else {
		// 		if (qa[i].contains("(Correct)")) {
		// 			String temp = qa[i].replace("(Correct)", "");
		// 			buf.append(i + ",");
		// 			System.out.println("\t\t" + i + ": " + temp);
		// 		} else {
		// 			System.out.println("\t\t" + i + ": " + qa[i]);
		// 		}
		// 	}
		// }
		// StringBuffer sb = new StringBuffer(buf.toString());
		// sb.deleteCharAt(sb.length() - 1);
		// System.out.println("\n\n\n");
		return question;
	}
	
	private String getUserInput() {
        String tempWord;
        Scanner sc = new Scanner(System.in);
        System.out.print("Answer> ");
        String input = sc.nextLine();
        tempWord = input.toLowerCase();
        System.out.println("");
        return tempWord;
    }
		
	private ArrayList<String> generateAcronymBank(String topic) {
		ArrayList<String> bank = new ArrayList<>();
		File testFile = new File(".//SecPlus_Acronyms.txt");
		if(testFile.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(testFile));
				String line = br.readLine();
				while(line != null) {
					bank.add(line);
					line = br.readLine();
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        } else {
            System.out.println("File DOES NOT Exists");
        }
		return bank;
	}
	
	private ArrayList<String> generateTestQuestionBank() {
		ArrayList<String> bank = new ArrayList<>();
		File testFile = new File(".//SecPlus_MultiQuestions_Final.txt");
		if(testFile.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(testFile));
				String line = br.readLine();
				int ndx = 0;
				boolean isTopic = false;
				boolean isFlashcard = false;
				boolean isAnswer = false;
				StringBuffer bufQ = new StringBuffer();
				StringBuffer bufFC = new StringBuffer();
				while(line != null) {
					if (line.startsWith("QUESTION")) {
						// System.out.println("QUESTION Line Found");
						isTopic = true;
						// line = br.readLine();
					} else if (line.startsWith("Answer")) {
						isAnswer = true;
					} else if (line.startsWith("END")) {
						// System.out.println("END Line Found");
						isTopic = false;
						isFlashcard = false;
						isAnswer = false;
					}
					if (isTopic && !isFlashcard && !isAnswer) {
						// System.out.println(line);
						if (line.startsWith("QUESTION")) {
							bufQ.append("\t" + line + "\n");
						} else if (line.startsWith("A.")) {
							bufQ.append("\n\t" + line);
						} else if (line.startsWith("B.")) {
							bufQ.append("\n\t" + line);
						} else if (line.startsWith("C.")) {
							bufQ.append("\n\t" + line);
						} else if (line.startsWith("D.")) {
							bufQ.append("\n\t" + line);
						} else if (line.startsWith("E.")) {
							bufQ.append("\n\t" + line);
						} else if (line.startsWith("F.")) {
							bufQ.append("\n\t" + line);
						} else if (line.startsWith("G.")) {
							bufQ.append("\n\t" + line);
						} else if (line.startsWith("")) {
							bufQ.append("\n\t" + line);
						} else if (line.startsWith("END")) {
							bufQ.append("\\tn" + line);
						} else {
							bufQ.append(line);
						}
					} else if (isTopic && isFlashcard) {
						if (line.contains("")) {
							bufFC.append("\n\t" + line);
						} else {
							bufFC.append(line);
						}
					}  else if (isTopic && isAnswer) {
						answerList.add(line);
						bufFC.append("\n(FC) " + (ndx + 1));
						ndx++;
						isFlashcard = true;
					} else {
						// System.out.println("\n\n\nAdding Question to test bank");
						// System.out.println(buf.toString());
						bank.add(bufQ.toString());
						FCBank.add(bufFC.toString());
						bufQ = new StringBuffer();
						bufFC = new StringBuffer();
					}
					line = br.readLine();
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        } else {
            System.out.println("File DOES NOT Exists");
        }
		// System.out.println("Question bank: " + bank.size());
		// System.out.println("Answer bank: " + answerList.size());
		// System.out.println("Flashcard bank: " + FCBank.size());
		return bank;
	}
	
	public static void main(String[] args) {
		new SecPlus2();
	}
}