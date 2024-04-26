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

public class SecPlus {
	
	ArrayList<String> testBank;
	ArrayList<String> acronymList;
	
	String acronymTopic = "Acronyms";
	String questionTopic = "Questions";
	
	boolean isPlaying;
	String userInput;
	int questionCount = 0;
	int questionCorrect = 0;
	int tickCount = 0;
	int tickCorrect = 0;
	
	public SecPlus() {
		
		testBank = generateTestQuestionBank(questionTopic);
		acronymList = generateAcronymBank(acronymTopic);
		
		displayTitle();
		displayMenu();
		
	}
	
	private void displayMenu() {
		StringBuffer buf = new StringBuffer();
		buf.append("\t| 1. Take Test | 2. Note Cards | 3. Exit |\n");
		System.out.println(buf.toString());
		
		String input = getUserInput();
		
		if (input.equals("1") || input.equals("test") || input.equals("take test")) {
			
			List<Integer> testQ = new ArrayList<>();
			
			testQ = genRandQuestions(5);
		
			isPlaying = true;
			runQ(testQ);
		} else if (input.equals("2") || input.equals("notes") || input.equals("note cards")) {
			displayMenu();
		} else if (input.equals("3") || input.equals("exit")) {
			// do Nothing and let the program end
		}
	}
	
	private List<Integer> genRandQuestions(int num) {
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
		long timeStart = System.currentTimeMillis();
        for (int i = 0; i < testQ.size(); i++) {
			System.out.println("");
			//int qNdx = pickRandomQuestionIndex();
			
			String answers = setUpQuestionDisplay(testQ.get(i));
			String[] ans = answers.split(",");
			
			userInput = getUserInput();
			if (userInput.equals("exit")) {
				isPlaying = false;
				resetMetrics();
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
		
			checkAnswers(userInput, ans);
			
			// Display Answers
			StringBuffer buf = new StringBuffer();
			if (ans.length > 1) {
				for (String a : ans) {
					buf.append(a).append(",");
					tickCount++;
				}
				
				StringBuffer sb = new StringBuffer(buf.toString());
				sb.deleteCharAt(sb.length() - 1);
				System.out.print(sb.toString());
			} else {
				System.out.print(ans[0]);
				tickCount++;
			}
			
			displayMetrics();
        }
		long timeStop = System.currentTimeMillis();
		long timeSec = (timeStop - timeStart) / 1000;
		System.out.print("\n\n\tYou Have Finished in " + timeSec + " seconds\n\n");
		System.out.print("\n\n\t============ TRY AGAIN? =============\n\n");
		// playAgain
		displayTitle();
		displayMenu();
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
			System.out.println(sResult);
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
	
	private void checkAnswers(String ui, String[] ans) {
		questionCount++;
		questionCount++;
		String[] uia;
		if (ui.length() > 1) {
			uia = ui.split(",");
		} else {
			uia = ui.split("");
		}
		int qTick = 0;
		// for (String a : uia) {System.out.print("answer " + a + " ");};
		for (int i = 0; i < ans.length; i++) {
			for (int j = 0; j < uia.length; j++) {
				if (ans[i].equals(uia[j])) {
					tickCorrect++;
					qTick++;
				}
			}				
		}
		if (qTick == ans.length) {
			System.out.println("Correct");
			questionCorrect++;
		} else if (qTick >= 1 && qTick < ans.length) {
			System.out.println("Partial Correct");
		} else {
			System.out.println("Incorrect!!!");
		}
	}
	
	private String setUpQuestionDisplay(int qNdx) {
		String[] qa = testBank.get(qNdx).split("%,");
		StringBuffer buf = new StringBuffer();
		System.out.println("");
		for (int i = 0; i < qa.length; i++) {
			if (i == 0) {
				System.out.println("\t" + qa[i]);
				System.out.println("");
			} else {
				if (qa[i].contains("(Correct)")) {
					String temp = qa[i].replace("(Correct)", "");
					buf.append(i + ",");
					System.out.println("\t\t" + i + ": " + temp);
				} else {
					System.out.println("\t\t" + i + ": " + qa[i]);
				}
			}
		}
		StringBuffer sb = new StringBuffer(buf.toString());
		sb.deleteCharAt(sb.length() - 1);
		System.out.println("\n\n\n");
		return sb.toString();
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
	
	private int pickRandomQuestionIndex() {
        int ndx = -1;
        Random rand = new Random();
        ndx = rand.nextInt(testBank.size());
        return ndx;
    }
	
	private ArrayList<String> generateAcronymBank(String topic) {
		ArrayList<String> bank = new ArrayList<>();
		File testFile = new File(".//SecPlus.txt");
		if(testFile.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(testFile));
				String line = br.readLine();
				boolean isTopic = false;
				while(line != null) {
					// System.out.println(acronyms);
					if (line.equals("=====" + topic + " Start=====")) {
						isTopic = true;
						line = br.readLine();
					} else if (line.equals("=====" + topic + " End=====")) {
						isTopic = false;
					}
					if (isTopic) {
						bank.add(line);
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
		return bank;
	}
	
	private ArrayList<String> generateTestQuestionBank(String topic) {
		ArrayList<String> bank = new ArrayList<>();
		
		File testFile = new File(".//SecPlus.txt");
		if(testFile.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(testFile));
				String line = br.readLine();
				boolean isTopic = false;
				int blankLines = 0;
				StringBuffer buf = new StringBuffer();
				while(line != null) {
					// System.out.println(acronyms);
					if (line.equals("=====" + topic + " Start=====")) {
						isTopic = true;
						line = br.readLine();
					} else if (line.equals("=====" + topic + " End=====")) {
						isTopic = false;
					}
					if (isTopic) {
						if (line.contains("===== Bank")) {
							line = br.readLine();
						} else if (line.equals("")) {
							buf.append("%;");
						} else {
							//bank.add(line);
							buf.append(line);
							buf.append("%,");
						}
					}
					
					line = br.readLine();
				}
				String[] question = buf.toString().split("%;");
				
				for (int i = 0; i < question.length; i++) {
				
					StringBuffer sb = new StringBuffer(question[i]);
					sb.deleteCharAt(sb.length() - 1);
					sb.deleteCharAt(sb.length() - 1);
					bank.add(sb.toString());
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
	
	public static void main(String[] args) {
		new SecPlus();
	}
}