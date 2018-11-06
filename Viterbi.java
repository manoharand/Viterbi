/**
	* Run the viterbi algorithm to find most probable path and posterior probability.
	* To run: java Viterbi [observed string] [index of posterior probability] [state of posterior probability]
	* For hw3. this would be: java Viterbi HHHHHTTTTT 7 B;
	*/
public class Viterbi {

	/** hidden states */
	private char[] states = {'F', 'B'};
	/** observable states */
	private char[] observations = {'H', 'T'};
	/** emission probabilities */
	private double[][] emission = {{0.5, 0.75}, {0.5, 0.25}};
	/** transition probabilities */
	private double[][] transition = {{0.9, 0.1}, {0.1, 0.9}};
	/** start probabilities */
	private double[] start = {0.5, 0.5};
	/** the string to be decoded */
	private String input;
	/** the dynamic programming table for viterbi */
	private double[][] table;
	/** forward table */
	private double[][] forward;
	/** backward table */
	private double[][] backward;
	/** the traceback table */
	private char[][] traceback;
	/** most probable path */
	StringBuilder str = new StringBuilder();

	public Viterbi() {}

	public void initializeTable(String input) {
		this.input = input;
		table = new double[input.length()][states.length];
		forward = new double[input.length()][states.length];
		backward = new double[input.length()][states.length];
		traceback = new char[input.length()][states.length];
		/*for (int i = 0; i < input.length(); i++) {
			for (int j = 0; j < states.length; j++) {
				traceback[i][j] = 0;
			}
		}*/
	}

	public void viterbi() {
		/** initialize */
		if (input.charAt(0) == 'H') {
			table[0][0] = start[0]*emission[0][0];
			table[0][1] = start[1]*emission[0][1];
		}
		else {
			table[0][0] = start[0]*emission[1][0];
			table[0][1] = start[1]*emission[1][1];
		}
		/** fill */
		for (int i = 1; i < input.length(); i++) {
			for (int j = 0; j < states.length; j++) {
				/** if input == H */
				if (input.charAt(i) == observations[0]) {
					/** prev full * P(H|F) * P(F|F) */
					double temp1 = table[i-1][0]*emission[0][j]*transition[j][0];
					/** prev biased * P(H|F) * P(F|B) */
					double temp2 = table[i-1][1]*emission[0][j]*transition[j][1];
					if (temp1 > temp2) {
						table[i][j] = temp1;
						traceback[i][j] = 'F';
					}
					else if (temp2 > temp1) {
						table[i][j] = temp2;
						traceback[i][j] = 'L';
					}
				}
				/** if input == T */
				else if (input.charAt(i) == observations[1]) {
					/** prev full * P(T|F) * P(F|F) */
					double temp1 = table[i-1][0]*emission[1][j]*transition[j][0];
					/** prev biased * P(T|F) * P(F|B) */
					double temp2 = table[i-1][1]*emission[1][j]*transition[j][1];
					if (temp1 > temp2) {
						table[i][j] = temp1;
						traceback[i][j] = 'F';
					}
					else if (temp2 > temp1) {
						table[i][j] = temp2;
						traceback[i][j] = 'L';
					}
				}
			}
		}
		if (table[input.length() - 1][0] > table[input.length() - 1][1]) {
			deprecatedtraceback(0, input.length() - 1);
		}
		else {
			deprecatedtraceback(1, input.length() - 1);
		}
		if (table[0][0] > table[0][1])
			str.append("F");
		else
			str.append("B");
		System.out.println("Most probable sequence: " + reverse(str.toString()));
	}

	public void forward() {
		/** initialize */
		if (input.charAt(0) == 'H') {
			forward[0][0] = start[0]*emission[0][0];
			forward[0][1] = start[1]*emission[0][1];
		}
		else {
			forward[0][0] = start[0]*emission[1][0];
			forward[0][1] = start[1]*emission[1][1];
		}
		/** fill */
		for (int i = 1; i < input.length(); i++) {
			for (int j = 0; j < states.length; j++) {
				/** if input == H */
				if (input.charAt(i) == observations[0]) {
					/** prev full * P(H|F) * P(F|F) */
					double temp1 = /**emission[0][j]**/forward[i-1][0]*transition[j][0];
					/** prev biased * P(H|F) * P(F|B) */
					double temp2 = /**emission[0][j]**/forward[i-1][1]*transition[j][1];
					forward[i][j] = (temp1 + temp2) * emission[0][j];
				}
				/** if input == T */
				else if (input.charAt(i) == observations[1]) {
					/** prev full * P(T|F) * P(F|F) */
					double temp1 = /**emission[1][j]**/forward[i-1][0]*transition[j][0];
					/** prev biased * P(T|F) * P(F|B) */
					double temp2 = /**emission[1][j]**/forward[i-1][1]*transition[j][1];
					forward[i][j] = (temp2 + temp1) * emission[1][j];
				}
			}
		}
	}

	public void backward() {
		/** initialize */
		if (input.charAt(input.length() - 1) == 'H') {
			backward[input.length() - 1][0] = emission[0][0];
			backward[input.length() - 1][1] = emission[0][1];
		}
		else {
			backward[input.length() - 1][0] = emission[1][0];
			backward[input.length() - 1][1] = emission[1][1];
		}
		/** fill */
		for (int i = input.length() - 2; i >= 0 ; i--) {
			for (int j = 0; j < states.length; j++) {
				/** if input == H */
				if (input.charAt(i) == observations[0]) {
					/** prev full * P(H|F) * P(F|F) */
					double temp1 = emission[0][j]*backward[i+1][0]*transition[j][0];
					/** prev biased * P(H|F) * P(F|B) */
					double temp2 = emission[0][j]*backward[i+1][1]*transition[j][1];
					backward[i][j] = temp1 + temp2;
				}
				/** if input == T */
				else if (input.charAt(i) == observations[1]) {
					/** prev full * P(T|F) * P(F|F) */
					double temp1 = emission[1][j]*backward[i+1][0]*transition[j][0];
					/** prev biased * P(T|F) * P(F|B) */
					double temp2 = emission[1][j]*backward[i+1][1]*transition[j][1];
					backward[i][j] = temp1 + temp2;
				}
			}
		}
	}

	public void posProb(int i, char s) {
		if (s == states[0])
			System.out.println("Posterior probability: " + backward[i-1][0]*forward[i-1][0]/forward[input.length()-1][0]);
		else
			System.out.println("Posterior probability: " + backward[i-1][1]*forward[i-1][1]/forward[input.length()-1][1]);
	}

	public void posterior(int i, char s) {
		/** fair coin */
		if (s == states[0])
			System.out.println("Posterior probability: " + table[i][0]);
		else
			System.out.println("Posterior probability: " + table[i][1]);
	}

	public String deprecatedtraceback(int state, int i) {
		if (i == 0)
			return str.toString();
		double temp1;
		double temp2;
		if (input.charAt(i) == 'H') {
			temp1 = table[i - 1][0]*emission[0][state]*transition[state][0];
			temp2 = table[i-1][1]*emission[0][state]*transition[state][1];
		}
		else {
			temp1 = table[i-1][0]*emission[1][state]*transition[state][0];
			temp2 = table[i-1][1]*emission[1][state]*transition[state][1];
		}
		if (temp1 > temp2) {
			str.append("F");
			deprecatedtraceback(0, i-1);
		}
		else {
			str.append("B");
			deprecatedtraceback(1, i-1);
		}

		return str.toString();

	}

	/** deprecated */
	public String traceback(int i) {
		if (i == 0)
			return str.toString();
		if (table[i][0] > table[i][1])
			str.append("F");
		else
			str.append("B");
		traceback(i-1);
		return str.toString();
	}

	/**
    * Reverse a given string.
    * @param str the String to be reversed
    */
  public String reverse(String str) {
    String reverse = "";
    for(int i = str.length() - 1; i >= 0; i--){
      reverse = reverse + str.charAt(i);
    }
    return reverse;
  }

	public void printTable() {
		System.out.println("Viterbi table:");
		for (int i = 0; i < input.length(); i++) {
			System.out.printf(" %f", table[i][0]);
		}
		System.out.println();
		for (int i = 0; i < input.length(); i++) {
			System.out.printf(" %f", table[i][1]);
		}
		System.out.println();
		System.out.println("Forward table:");
		for (int i = 0; i < input.length(); i++) {
			System.out.printf(" %f", forward[i][0]);
		}
		System.out.println();
		for (int i = 0; i < input.length(); i++) {
			System.out.printf(" %f", forward[i][1]);
		}
		System.out.println();
		System.out.println("Backward table:");
		for (int i = 0; i < input.length(); i++) {
			System.out.printf(" %f", backward[i][0]);
		}
		System.out.println();
		for (int i = 0; i < input.length(); i++) {
			System.out.printf(" %f", backward[i][1]);
		}
		System.out.println();
	}

	public static void main(String[] args) {
		Viterbi v = new Viterbi();
		v.initializeTable(args[0]);
		v.viterbi();
		v.forward();
		v.backward();
		v.printTable();
		v.posProb(Integer.parseInt(args[1]), args[2].charAt(0));
		//v.posterior(Integer.parseInt(args[1]), args[2].charAt(0));
	}

}
