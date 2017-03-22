package gee.math;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Combinatory {
	public static long factorial(long n) {
		if (n < 0)
			throw new RuntimeException("Underflow error in factorial");
		else if (n > 20)
			throw new RuntimeException("Overflow error in factorial");
		else if (n == 0)
			return 1;
		else
			return n * factorial(n - 1);
	}

	public static long A(int k, int n) {
		return factorial(n) / factorial(n - k);
	}

	/**
	 * In combinatorial mathematics, a combination is an un-ordered collection
	 * of distinct (k) elements, usually of a prescribed size and taken from a
	 * given set of (n) elements. This method returns the combination without
	 * repetition
	 * 
	 * @param k
	 * @param n
	 * @return
	 */
	public static long Combination(int k, int n) {
		return factorial(n) / (factorial(k) * factorial(n - k));
	}

	/**
	 * In combinatorial mathematics, a combination is an un-ordered collection
	 * of distinct (k) elements, usually of a prescribed size and taken from a
	 * given set of (n) elements. This method returns the combination with
	 * repetition
	 * 
	 * @param k
	 * @param n
	 * @return
	 */
	public static long CombinationWithRepetition(int k, int n) {
		return factorial(n + k - 1) / (factorial(k) * factorial(n - 1));
	}

	public static void main(String[] args) {
		System.out.println(Combination(2, 5));
		System.out.println(CombinationWithRepetition(3, 10));
		int pairs[][] = getPairs(1, 2, 3, 4, 5);
		for (int pair[] : pairs) {
			System.out.print(" - ");
			for (int val : pair) {
				System.out.print(val + " - ");
			}
			System.out.println("");
		}
	}

	public static int[] getInts(Integer... values) {
		int v2[] = new int[values.length];
		for (int i = 0; i < values.length; i++)
			v2[i] = values[i];
		return v2;
	}

	public static Object[][] getPairs(List values) {
		Object ret[][] = new Object[(int) Combinatory.Combination(2, values
				.size())][2];
		int id = 0;
		for (int i = 0; i < values.size() - 1; i++) {
			for (int j = i + 1; j < values.size(); j++) {
				ret[id][0] = values.get(i);
				ret[id++][1] = values.get(j);
			}
		}
		return ret;
	}

	public static int[][] getPairs(int... values) {
		int ret[][] = new int[(int) Combinatory.Combination(2, values.length)][2];
		int id = 0;
		for (int i = 0; i < values.length - 1; i++) {
			for (int j = i + 1; j < values.length; j++) {
				ret[id][0] = values[i];
				ret[id++][1] = values[j];
			}
		}
		return ret;
	}

	public static int[][] getTriplets(int... values) {
		int ret[][] = new int[(int) Combinatory.Combination(3, values.length)][3];
		int id = 0;
		for (int i = 0; i < values.length - 1; i++) {
			for (int j = i + 1; j < values.length; j++) {
				for (int k = j + 1; k < values.length; k++) {
					ret[id][0] = values[i];
					ret[id][1] = values[j];
					ret[id++][2] = values[k];
				}
			}
		}
		return ret;
	}

	public static int[][] getQuads(int... values) {
		int ret[][] = new int[(int) Combinatory.Combination(4, values.length)][4];
		int id = 0;
		for (int i = 0; i < values.length - 1; i++) {
			for (int j = i + 1; j < values.length; j++) {
				for (int k = j + 1; k < values.length; k++) {
					for (int l = k + 1; l < values.length; l++) {
						ret[id][0] = values[i];
						ret[id][1] = values[j];
						ret[id][2] = values[k];
						ret[id++][3] = values[l];
					}
				}
			}
		}
		return ret;
	}

	public static int[][] getQuints(int... values) {
		int ret[][] = new int[(int) Combinatory.Combination(5, values.length)][5];
		int id = 0;
		for (int i = 0; i < values.length - 1; i++) {
			for (int j = i + 1; j < values.length; j++) {
				for (int k = j + 1; k < values.length; k++) {
					for (int l = k + 1; l < values.length; l++) {
						for (int m = l + 1; m < values.length; m++) {
							ret[id][0] = values[i];
							ret[id][1] = values[j];
							ret[id][2] = values[k];
							ret[id][3] = values[l];
							ret[id++][4] = values[m];
						}
					}
				}
			}
		}
		return ret;
	}

	public static int[][] getSextes(int... values) {
		int ret[][] = new int[(int) Combinatory.Combination(6, values.length)][6];
		int id = 0;
		for (int i = 0; i < values.length - 1; i++) {
			for (int j = i + 1; j < values.length; j++) {
				for (int k = j + 1; k < values.length; k++) {
					for (int l = k + 1; l < values.length; l++) {
						for (int m = l + 1; m < values.length; m++) {
							for (int n = m + 1; n < values.length; n++) {
								ret[id][0] = values[i];
								ret[id][1] = values[j];
								ret[id][2] = values[k];
								ret[id][3] = values[l];
								ret[id][3] = values[m];
								ret[id++][5] = values[n];
							}
						}
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Returns true if all or some of subset are in master
	 * 
	 * @param master
	 * @param subset
	 * @return
	 */
	public static boolean contains(int master[], int subset[]) {
		for (int candidate : subset) {
			boolean ret = false;
			for (int source : master) {
				if (candidate == source)
					ret = true;
			}
			if (ret == false)
				return false;
		}
		return true;
	}

}
