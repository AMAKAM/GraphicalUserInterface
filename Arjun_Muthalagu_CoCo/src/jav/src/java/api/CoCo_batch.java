/*
    This file is part of RODS - the Real-time Outbreak and Disease
    Surveillance system

    RODS is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    RODS is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with RODS; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

    Copyright 2003 University of Pittsburgh

*/
/*
 *  CoCo: Complaint Coder
 *  Version 2.0
 *  Written by: Robert Olszewski
 *  Center for Biomedical Informatics
 *  University of Pittsburgh
 *  bobski@cbmi.upmc.edu
 *  This program classifies free-text chief-complaint strings into
 *  syndromes.
 *  This program functions in three different modes:
 *  Train - generates a probability file from a training file
 *  Batch - writes to an output file the classification of each
 *  complaint string in an input file
 *  Line  - prints to the screen the classification of a complaint
 *  string specified on the command line
 *  To switch between the modes, set the TRAIN, BATCH, and LINE
 *  binary variables below.
 *  Training Mode
 *  Reads in a file containing preclassified complaint strings,
 *  computes probabilities, and writes them to an output file.
 *  The output file can be subsequently used as input to this
 *  program in either Batch or Line mode.
 *  CoCo <complaint training file> <probability file>
 *  <complaint training file>
 *  name of the file containing the preclassified complaint
 *  strings; the file is in CSV format and all complaint
 *  strings should be preprocessed into a standard format
 *  (i.e., capitalization, punctuation, abbreviations,
 *  and synonyms standardized); use only whitespace to
 *  separate the words in the complaint string; for example,
 *  diff breathing,Respiratory
 *  chest pain,Other
 *  abd pain nausea vomiting,Gastrointestinal
 *  resp dist,Respiratory
 *  fever,Constitutional
 *  <probability file>
 *  name of the output file for the probabilities computed
 *  from the complaint training file; this file may
 *  be subsequently used to specify the probabilities
 *  when running in Batch or Line mode
 *  Batch Mode
 *  Reads in a file containing unclassified complaint strings,
 *  computes a posterior probability with respect to each syndrome,
 *  and writes the final classification for each string to the
 *  output file.  The probabilities file determines which values
 *  are used when computing the posterior probabilities.
 *  CoCo <complaint file> <classification file> <probability file>
 *  <complaint file>
 *  name of the file containing the complaint strings to be
 *  classified; all complaint strings should be preprocessed
 *  into a standard format (i.e., capitalization, punctuation,
 *  abbreviations, and synonyms standardized); use only
 *  whitespace to separate the words in the complaint string;
 *  for example,
 *  diff breathing
 *  chest pain
 *  abd pain nausea vomiting
 *  <classification file>
 *  the name of the output file for the classifications of
 *  the complaint strings; the classifications are written
 *  to the file in the same order as the complaint strings;
 *  for example,
 *  Respiratory
 *  Other
 *  Gastrointestinal
 *  <probability file>
 *  the name of a probability file generated in training mode
 *  Line Mode
 *  Computes a posterior probability for a complaint string specified
 *  on the command line with respect to each syndrome, and prints the
 *  final classification for the string to the screen.  The probabilities
 *  file determines which values are used when computing the posterior
 *  probabilities.
 *  CoCo <complaint string> <probability file>
 *  <complaint string>
 *  the complaint string to be classified; it should be
 *  preprocessed similarly to the complaint strings used
 *  for training (i.e., capitalization, punctuation,
 *  abbreviations, and synonyms standardized); make sure
 *  to enclose it in quotation marks; for example,
 *  "diff breathing"
 *  <probability file>
 *  the name of a probability file generated in training mode
 *  Classification
 *  The functionality of the classifier is controlled by three parameters
 *  that specify the unigram weight, bigram weight, and threshold.  Currently,
 *  those parameters are set so as to use a unigram-only model and to
 *  classify a complaint string with the one syndrome with the highest
 *  posterior probability (in the case of a tie, the syndrome that sorts
 *  first is chosen).
 *  Default Probabilities
 *  The default probabilities file, default_probs.txt, contains probability
 *  values that can be used for classification.  They were generated from
 *  28,990 complaint strings which were manually classified by a physician
 *  with one or more of 8 syndromes.
 *  The syndromes and their definitions are as follows:
 *  Gastrointestinal - pain or cramps anywhere in the abdomen, nausea,
 *  vomiting, diarrhea, and abdominal distension or
 *  swelling.
 *  Constitutional   - non-localized, systemic problems including fever,
 *  chills, body aches, flu symptoms (viral syndrome),
 *  weakness, fatigue, anorexia, malaise, lethargy,
 *  sweating (diaphoresis), light headedness, faintness
 *  and fussiness.  Shaking (not chills) is Other and
 *  not Constitutional.
 *  Respiratory      - problems of the nose (coryza) and throat
 *  (pharyngitis), as well as the lungs.  Examples
 *  of Respiratory include congestion, sore throat,
 *  tonsillitis, sinusitis, cold symptoms, bronchitis,
 *  cough, shortness of breath, asthma, chronic
 *  obstructive pulmonary disease (COPD), and
 *  pneumonia.  The presence of both cold and flu
 *  symptoms is Respiratory and not Constitutional.
 *  Rash             - any description of a rash, such as macular,
 *  papular, vesicular, petechial, purpuric, or hives.
 *  Ulcerations are not normally considered Rash unless
 *  consistent with cutaneous anthrax (an ulcer with a
 *  black eschar).
 *  Hemorrhagic      - bleeding from any site, e.g., vomiting blood
 *  (hematemesis), nose bleed (epistaxis), hematuria,
 *  gastrointestinal bleeding (site unspecified),
 *  rectal bleeding, and vaginal bleeding.  Bleeding
 *  from a site for which there is a syndrome is
 *  classified as Hemorrhagic and as the relevant
 *  syndrome (e.g., Hematochesia is Gastrointestinal
 *  and Hemorrhagic; hemoptysis is Respiratory and
 *  Hemorrhagic).
 *  Botulinic        - ocular abnormalities (diplopia, blurred vision,
 *  photophobia), difficulty speaking (dysphonia,
 *  dysarthria, slurred speech), and difficulty
 *  swallowing (dysphagia).
 *  Neurological     - non-psychiatric complaints which relate to brain
 *  function.  Included are headache, head pain,
 *  migraine, facial pain or numbness, seizure, tremor,
 *  convulsion, loss of consciousness, syncope, fainting,
 *  ataxia, confusion, disorientation, altered mental
 *  status, vertigo, concussion, meningitis, stiff neck,
 *  tingling and numbness.  (Dizziness is both
 *  Constitutional and  Neurological.)
 *  Other            - pain or process in a system or area not being
 *  monitored.  For example, flank pain most likely
 *  arising from the genitourinary system would be
 *  considered Other.  Chest pain with no mention of
 *  the source of the pain is considered Other (e.g.,
 *  chest pain (Other) versus pleuritic chest pain
 *  (Respiratory)).  Earache or ear pain is Other.
 *  Trauma is Other.
 *  The complaint strings were preprocessed as follows:
 *  - all letters were transformed to lowercase
 *  - all punctuation marks were replaced with spaces
 *  - multiple consecutive spaces were replaced with a
 *  single space
 *  When using the default probabilities, the complaint strings to be
 *  classified MUST be preprocessed in the same manner.
 *  To use a different set of syndromes and/or preprocessing scheme,
 *  assemble a complaint training file with the desired characteristics,
 *  generate a probabilities file from the complaint training file, and
 *  use that probabilities file to classify complaint strings.  Note
 *  that complaint strings can not contain commas since the input files
 *  are in CSV format and that the words in the complaint string must be
 *  separated with whitespace.
 *  Contact Information
 *  If you have any questions about this package or suggestions for
 *  modifications, please contact Bob Olszewski via email at
 *  bobski@cbmi.upmc.edu.
 */
package src.java.api;

import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 *  Description of the Class
 *
  * @created    July 18, 2003
 */
public class CoCo_batch {

	/*
	 *  Set variables that specify the functionality of the classifier.
	 */
	public static boolean isLineInit = false;
	static double weight_1_tuple = 1.0;
	static double weight_2_tuple = 0.0;
	static double threshold = -1.0;
	static double default_prob = 0.0000001;
	static double missing_prob = -1.0;
	static String train_file_name = null;
	static String prob_file_name = "default_probs.txt";
	static String complaint_string = null;
	static String complaint_file_name = "cc.txt";
	static String classification_file_name = "prod.txt";
	static int syndrome_count = 0;
	static double prior_prob[] = null;
	static double prob_1_tuple[][] = null;
	static double prob_2_tuple[][] = null;
	static String wordpair_lookup[] = null;
	static Hashtable words = new Hashtable();
	static Hashtable wordpairs = new Hashtable();
	static String syndrome_lookup[] = null;
	static Hashtable syndromes = new Hashtable();

	public CoCo_batch() {
		if (!isLineInit) {
			initFilepath();
			lineInit(prob_file_name);
			isLineInit = true;
		}
	}
	private static void initFilepath(){
		File dir1 = new File(".");
		String FilePath = "";
		try
		{
			FilePath  = dir1.getCanonicalPath() + "\\src\\jav\\src\\java\\api\\";
			 prob_file_name = FilePath + prob_file_name;			
			 complaint_file_name = FilePath + complaint_file_name;
			 classification_file_name = FilePath + classification_file_name;
		}
		catch(Exception e) {
		       e.printStackTrace();
	       }
	}
	/*
	 *  Main routine.
	 */
	/**
	 *  Description of the Method
	 *
	 * @param  args  Description of the Parameter
	 */
	public static void main(String args[]) {
		/*
		 *  Swtiches for the type of functionality.  Exactly one must
		 *  be set to 1.  If not, an error message is printed.
		 */
		boolean isTRAIN = false;
		boolean isBATCH = false;
		boolean isLINE = false;

		/*
		 *  Variables to hold command line arguments.
		 */
		/*
		 *  Check function switch settings.
		 */
		if (args.length < 1) {
			System.out.println(
				"usage: CoCo_btach [-b |-t|-l] [-Pprob_file_name] [-Ccomplaint_file_name] [-Sclassification_file_name]] [ -Rtrain_file_name : train_file_name] ");
			System.out.println("\t -b : batch mode ");
			System.out.println("\t -t : train mode. ");
			System.out.println("\t -l : line mode. ");
			System.out.println("\t -Pprob_file_name : prob_file_name.");
			System.out.println(
				"\t -Ccomplaint_file_name : complaint_file_name");
			System.out.println(
				"\t -Sclassification_file_name : classification_file_name");
			System.out.println("\t -Rtrain_file_name : train_file_name ");
			System.exit(0);
		}
		initFilepath();
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("-b")) {
				isBATCH = true;
				//logger.setLevel(Level.INFO);
			} else if (args[i].equals("-t")) {
				isTRAIN = true;
				//logger.setLevel(Level.WARNING);
			} else if (args[i].equals("-l")) {
				isLINE = true;
				//logger.setLevel(Level.WARNING);
			}

			if (args[i].startsWith("-P") || args[i].startsWith("-p")) {
				if (args[i].length() > 2) {
					prob_file_name = args[i].substring(2);
				}
			}

			if (args[i].length() > 2
				&& (args[i].startsWith("-c") || args[i].startsWith("-C"))) {
				complaint_file_name = args[i].substring(2);
				complaint_string = args[i].substring(2);
			}
			if (args[i].length() > 2
				&& (args[i].startsWith("-S") || args[i].startsWith("-s"))) {
				classification_file_name = args[i].substring(2);

			}
			if (args[i].length() > 2
				&& (args[i].startsWith("-r") || args[i].startsWith("-R"))) {
				train_file_name = args[i].substring(2);

			}
		}
		if (!(isTRAIN || isBATCH || isLINE)) {
			System.out.print(
				"\nCoCo: You have to choose a mode (TRAIN/BATCH/LINE)\n\n");
			return;
		}

		/*
		 *  Check command line args.
		 */
		if (isTRAIN) {
			if (prob_file_name == null) {
				System.out.print(
					"\nUSAGE: CoCo <complaint training file> <probability file>\n\n");
				return;
			}
			train();
		} else if (isBATCH) {
			if (prob_file_name == null || classification_file_name == null) {
				System.out.print(
					"\nUSAGE: CoCo_batch <complaint file> <classification file> <probability file>\n\n");

				return;
			}
			batch();
		} else if (isLINE) {
			if (args.length <= 1) {
				System.out.print(
					"\nUSAGE: CoCo_batch -l -C<complaint string> \n\n");

				return;
			}
			//lineInit(prob_file_name);
			CoCo_batch CoCo = new CoCo_batch();
			if (complaint_string == null) {
				complaint_string = " ";
			}
			System.out.println(CoCo_batch.line(complaint_string));
			//System.out.println(CoCo.line("bleeding"));
			//System.out.println(CoCo.line("cough"));
		}
	}

	/*
	 *  Set command line variables and print informational message.
	 *  if(TRAIN == 1)
	 *  {
	 *  train_file_name = args[0];
	 *  prob_file_name  = args[1];
	 *  System.out.print("\nCoCo: Complaint Coder (Training Mode)\n");
	 *  System.out.print("University of Pittsburgh -- Copyright 2002\n\n");
	 *  System.out.print("Complaint Training File Name = " + train_file_name + "\n");
	 *  System.out.print("Probability Output File Name = " + prob_file_name + "\n\n");
	 *  }
	 *  else if(BATCH == 1)
	 *  {
	 *  complaint_file_name      = args[0];
	 *  classification_file_name = args[1];
	 *  prob_file_name           = args[2];
	 *  System.out.print("\nCoCo: Complaint Coder (Batch Mode)\n");
	 *  System.out.print("University of Pittsburgh -- Copyright 2002\n\n");
	 *  System.out.print("Complaint File Name             = " + complaint_file_name + "\n");
	 *  System.out.print("Classification Output File Name = " + classification_file_name + "\n");
	 *  System.out.print("Probability File Name           = " + prob_file_name + "\n\n");
	 *  }
	 *  else if(LINE == 1)
	 *  {
	 *  complaint_string = args[0];
	 *  prob_file_name   = args[1];
	 *  }
	 */
	/*
	 *  Train the classifier based on preclassified chief-complaint strings.
	 *  0. Read in the training file and count the number of strings,
	 *  sydromes, words, and word pairs.
	 *  1. Read in the training file and store the strings by syndrome.
	 *  2. Compute 1-tuple and 2-tuple probabilities for each syndrome.
	 *  3. Write out the probabilities.
	 *  4. Quit.
	 */
	/**
	 *  Description of the Method
	 */
	public static void train() {
		//if(TRAIN == 1)

		int complaint_count = 0;
		int syndrome_count = 0;
		int word_count = 0;
		int wordpair_count = 0;
		int total_train = 0;
		int prob_1_tuple_count = 0;
		int prob_2_tuple_count = 0;
		int count_train[] = null;
		int count_1_tuple[][] = null;
		int count_2_tuple[][] = null;
		int total_1_tuple[] = null;
		int total_2_tuple[] = null;
		int complaint_train[][] = null;
		double prior_prob[] = null;
		double prob_1_tuple[][] = null;
		double prob_2_tuple[][] = null;
		String complaint_lookup[] = null;
		String syndrome_lookup[] = null;
		String word_lookup[] = null;
		String wordpair_lookup[] = null;
		Hashtable complaints = new Hashtable();
		Hashtable syndromes = new Hashtable();
		Hashtable words = new Hashtable();
		Hashtable wordpairs = new Hashtable();
		int loop;
		int loop1;
		int loop2;
		int loop3;

		System.out.print("Reading complaint training file...\n\n");

		try {
			String train_file_line = null;
			System.out.println("file name inside train()"+train_file_name);
			BufferedReader train_file =
				new BufferedReader(
					new InputStreamReader(
						new FileInputStream(train_file_name)));
            
			while ((train_file_line = train_file.readLine()) != null) {
				String train_file_token[] = split(train_file_line, ',');

				if (train_file_token.length == 0) {
					System.out.print(
						"WARNING: Blank line found in "
							+ train_file_name
							+ " (skipping)\n\n");
				} else if (train_file_token.length == 1) {
					System.out.print(
						"WARNING: No syndromes indicated for \""
							+ train_file_token[0]
							+ "\" in "
							+ train_file_name
							+ " (skipping)\n\n");
				} else {
					String complaint_token[] =
						split_whitespace(train_file_token[0]);
					Integer complaint_index =
						(Integer) complaints.get(train_file_token[0]);

					if (complaint_index == null) {
						complaints.put(
							train_file_token[0],
							new Integer(complaint_count++));
					}

					for (loop = 1; loop < train_file_token.length; loop++) {
						Integer syndrome_index =
							(Integer) syndromes.get(train_file_token[loop]);

						if (syndrome_index == null) {
							syndromes.put(
								train_file_token[loop],
								new Integer(syndrome_count++));
						}
					}

					for (loop = 0; loop < complaint_token.length; loop++) {
						Integer word_index =
							(Integer) words.get(complaint_token[loop]);

						if (word_index == null) {
							words.put(
								complaint_token[loop],
								new Integer(word_count++));
						}
					}

					for (loop = 0; loop < complaint_token.length - 1; loop++) {
						String wordpair =
							complaint_token[loop]
								+ ","
								+ complaint_token[loop
								+ 1];
						Integer wordpair_index =
							(Integer) wordpairs.get(wordpair);

						if (wordpair_index == null) {
							wordpairs.put(
								wordpair,
								new Integer(wordpair_count++));
						}
					}

					total_train++;
				}
			}

			train_file.close();
		} catch (Exception e) {
			System.out.print(
				"ERROR! Can not read file: " + train_file_name + "\n\n");
			System.exit(0);
		}

		complaint_lookup = new String[complaint_count];
		syndrome_lookup = new String[syndrome_count];
		word_lookup = new String[word_count];
		wordpair_lookup = new String[wordpair_count];

		for (Enumeration e = complaints.keys(); e.hasMoreElements();) {
			String next_key = (String) e.nextElement();

			complaint_lookup[((Integer) complaints.get(next_key)).intValue()] =
				next_key;
		}

		for (Enumeration e = syndromes.keys(); e.hasMoreElements();) {
			String next_key = (String) e.nextElement();

			syndrome_lookup[((Integer) syndromes.get(next_key)).intValue()] =
				next_key;
		}

		for (Enumeration e = words.keys(); e.hasMoreElements();) {
			String next_key = (String) e.nextElement();

			word_lookup[((Integer) words.get(next_key)).intValue()] = next_key;
		}

		for (Enumeration e = wordpairs.keys(); e.hasMoreElements();) {
			String next_key = (String) e.nextElement();

			wordpair_lookup[((Integer) wordpairs.get(next_key)).intValue()] =
				next_key;
		}
		/*
		 *  print_hashtable(complaints, "complaints");
		 *  print_hashtable(syndromes, "syndromes");
		 *  print_hashtable(words, "words");
		 *  print_hashtable(wordpairs, "wordpairs");
		 *  print_lookup(complaint_lookup, "complaint_lookup");
		 *  print_lookup(syndrome_lookup, "syndrome_lookup");
		 *  print_lookup(word_lookup, "word_lookup");
		 *  print_lookup(wordpair_lookup, "wordpair_lookup");
		 */
		complaint_train = new int[syndrome_count][total_train];
		count_train = new int[syndrome_count];
		count_1_tuple = new int[syndrome_count][word_count];
		count_2_tuple = new int[syndrome_count][wordpair_count];
		total_1_tuple = new int[syndrome_count];
		total_2_tuple = new int[syndrome_count];
		prior_prob = new double[syndrome_count];
		prob_1_tuple = new double[syndrome_count][word_count];
		prob_2_tuple = new double[syndrome_count][wordpair_count];

		for (loop1 = 0; loop1 < syndrome_count; loop1++) {
			count_train[loop1] = 0;
			total_1_tuple[loop1] = 0;
			total_2_tuple[loop1] = 0;

			for (loop2 = 0; loop2 < word_count; loop2++) {
				count_1_tuple[loop1][loop2] = 0;
			}

			for (loop2 = 0; loop2 < wordpair_count; loop2++) {
				count_2_tuple[loop1][loop2] = 0;
			}
		}

		try {
			String train_file_line = null;
			BufferedReader train_file =
				new BufferedReader(
					new InputStreamReader(
						new FileInputStream(train_file_name)));

			while ((train_file_line = train_file.readLine()) != null) {
				String train_file_token[] = split(train_file_line, ',');

				if (train_file_token.length > 1) {
					int complaint_index =
						((Integer) complaints.get(train_file_token[0]))
							.intValue();

					for (loop = 1; loop < train_file_token.length; loop++) {
						int syndrome_index =
							((Integer) syndromes.get(train_file_token[loop]))
								.intValue();

						complaint_train[syndrome_index][count_train[syndrome_index]++] =
							complaint_index;
					}
				}
			}

			train_file.close();
		} catch (Exception e) {
			System.out.print(
				"ERROR! Can not read file: " + train_file_name + "\n\n");
			System.exit(0);
		}

		System.out.print(
			"  Found "
				+ total_train
				+ " strings and "
				+ syndrome_count
				+ " syndromes\n\n");

		System.out.print("Computing probabilities...\n\n");

		for (loop1 = 0; loop1 < syndrome_count; loop1++) {
			for (loop2 = 0; loop2 < count_train[loop1]; loop2++) {
				String complaint_token[] =
					split_whitespace(complaint_lookup[complaint_train[loop1][loop2]]);

				for (loop3 = 0; loop3 < complaint_token.length; loop3++) {
					int word_index =
						((Integer) words.get(complaint_token[loop3]))
							.intValue();

					count_1_tuple[loop1][word_index]++;
					total_1_tuple[loop1]++;
				}

				for (loop3 = 0; loop3 < complaint_token.length - 1; loop3++) {
					String wordpair =
						complaint_token[loop3]
							+ ","
							+ complaint_token[loop3
							+ 1];
					int wordpair_index =
						((Integer) wordpairs.get(wordpair)).intValue();

					count_2_tuple[loop1][wordpair_index]++;
					total_2_tuple[loop1]++;
				}
			}

			for (loop2 = 0; loop2 < word_count; loop2++) {
				if (count_1_tuple[loop1][loop2] > 0) {
					prob_1_tuple[loop1][loop2] =
						(double) count_1_tuple[loop1][loop2]
							/ (double) total_1_tuple[loop1];

					prob_1_tuple_count++;
				} else {
					prob_1_tuple[loop1][loop2] = 0.0;
				}
			}

			for (loop2 = 0; loop2 < wordpair_count; loop2++) {
				String wordpair_token[] = split(wordpair_lookup[loop2], ',');
				int word_index =
					((Integer) words.get(wordpair_token[0])).intValue();

				if (count_2_tuple[loop1][loop2] > 0) {
					prob_2_tuple[loop1][loop2] =
						(double) count_2_tuple[loop1][loop2]
							/ (double) count_1_tuple[loop1][word_index];

					prob_2_tuple_count++;
				} else {
					prob_2_tuple[loop1][loop2] = 0.0;
				}
			}

			prior_prob[loop1] =
				(double) count_train[loop1] / (double) total_train;
		}

		System.out.print("Writing probability output file...\n\n");

		try {
			BufferedWriter prob_file =
				new BufferedWriter(
					new OutputStreamWriter(
						new FileOutputStream(prob_file_name)));

			prob_file.write(syndrome_count + "\n");

			for (loop = 0; loop < syndrome_count; loop++) {
				prob_file.write(
					syndrome_lookup[loop] + "," + prior_prob[loop] + "\n");
			}

			prob_file.write(prob_1_tuple_count + "\n");

			for (loop1 = 0; loop1 < syndrome_count; loop1++) {
				for (loop2 = 0; loop2 < word_count; loop2++) {
					if (prob_1_tuple[loop1][loop2] > 0.0) {
						prob_file.write(
							syndrome_lookup[loop1]
								+ ","
								+ word_lookup[loop2]
								+ ","
								+ prob_1_tuple[loop1][loop2]
								+ "\n");
					}
				}
			}

			prob_file.write(prob_2_tuple_count + "\n");

			for (loop1 = 0; loop1 < syndrome_count; loop1++) {
				for (loop2 = 0; loop2 < wordpair_count; loop2++) {
					if (prob_2_tuple[loop1][loop2] > 0.0) {
						prob_file.write(
							syndrome_lookup[loop1]
								+ ","
								+ wordpair_lookup[loop2]
								+ ","
								+ prob_2_tuple[loop1][loop2]
								+ "\n");
					}
				}
			}

			prob_file.close();
		} catch (Exception e) {
			System.out.print(
				"ERROR! Can not write file: " + prob_file_name + "\n\n");
			System.exit(0);
		}

		complaints.clear();
		syndromes.clear();
		words.clear();
		wordpairs.clear();

		complaint_lookup = null;
		syndrome_lookup = null;
		word_lookup = null;
		wordpair_lookup = null;

		complaint_train = null;
		count_train = null;
		count_1_tuple = null;
		count_2_tuple = null;
		total_1_tuple = null;
		total_2_tuple = null;

		prior_prob = null;
		prob_1_tuple = null;
		prob_2_tuple = null;
	}

	/**
	 *  Description of the Method
	 */
	public static void batch() {
		int syndrome_count = 0;
		int word_count = 0;
		int wordpair_count = 0;
		double prior_prob[] = null;
		double prob_1_tuple[][] = null;
		double prob_2_tuple[][] = null;
		String syndrome_lookup[] = null;
		String word_lookup[] = null;
		String wordpair_lookup[] = null;
		Hashtable syndromes = new Hashtable();
		Hashtable words = new Hashtable();
		Hashtable wordpairs = new Hashtable();
		int loop;
		int loop1;
		int loop2;
		
		/*
		 *  if(BATCH == 1)
		 *  {
		 *  System.out.print("Reading probability file...\n\n");
		 *  }
		 *  Read in the probabilities file.
		 */
		try {
			int prior_prob_count = 0;
			int prob_1_tuple_count = 0;
			int prob_2_tuple_count = 0;
			String prob_file_line = null;
			BufferedReader prob_file =
				new BufferedReader(
					new InputStreamReader(new FileInputStream(prob_file_name)));

			prior_prob_count = Integer.parseInt(prob_file.readLine());

			for (loop = 0; loop < prior_prob_count; loop++) {
				String prob_file_token[] = split(prob_file.readLine(), ',');

				syndromes.put(
					prob_file_token[0],
					new Integer(syndrome_count++));
			}

			prob_1_tuple_count = Integer.parseInt(prob_file.readLine());

			for (loop = 0; loop < prob_1_tuple_count; loop++) {
				String prob_file_token[] = split(prob_file.readLine(), ',');
				Integer word_index = (Integer) words.get(prob_file_token[1]);

				if (word_index == null) {
					words.put(prob_file_token[1], new Integer(word_count++));
				}
			}

			prob_2_tuple_count = Integer.parseInt(prob_file.readLine());

			for (loop = 0; loop < prob_2_tuple_count; loop++) {
				String prob_file_token[] = split(prob_file.readLine(), ',');
				String wordpair = prob_file_token[1] + "," + prob_file_token[2];
				Integer wordpair_index = (Integer) wordpairs.get(wordpair);

				if (wordpair_index == null) {
					wordpairs.put(wordpair, new Integer(wordpair_count++));
				}
			}

			prob_file.close();
		} catch (Exception e) {
			System.out.print(e);
			System.out.print(
				"ERROR! Can not read file: " + prob_file_name + "\n\n");
			System.exit(0);
		}

		syndrome_lookup = new String[syndrome_count];
		/*
		 *  word_lookup     = new String[word_count];
		 *  wordpair_lookup = new String[wordpair_count];
		 */
		for (Enumeration e = syndromes.keys(); e.hasMoreElements();) {
			String next_key = (String) e.nextElement();

			syndrome_lookup[((Integer) syndromes.get(next_key)).intValue()] =
				next_key;
		}
		/*
		 *  for(Enumeration e = words.keys(); e.hasMoreElements(); )
		 *  {
		 *  String next_key = (String)e.nextElement();
		 *  word_lookup[((Integer)words.get(next_key)).intValue()] = next_key;
		 *  }
		 *  for(Enumeration e = wordpairs.keys(); e.hasMoreElements(); )
		 *  {
		 *  String next_key = (String)e.nextElement();
		 *  wordpair_lookup[((Integer)wordpairs.get(next_key)).intValue()] = next_key;
		 *  }
		 */
		prior_prob = new double[syndrome_count];
		prob_1_tuple = new double[syndrome_count][word_count];
		prob_2_tuple = new double[syndrome_count][wordpair_count];

		for (loop1 = 0; loop1 < syndrome_count; loop1++) {
			for (loop2 = 0; loop2 < word_count; loop2++) {
				prob_1_tuple[loop1][loop2] = missing_prob;
			}

			for (loop2 = 0; loop2 < wordpair_count; loop2++) {
				prob_2_tuple[loop1][loop2] = missing_prob;
			}
		}

		try {
			int prior_prob_count = 0;
			int prob_1_tuple_count = 0;
			int prob_2_tuple_count = 0;
			String prob_file_line = null;
			BufferedReader prob_file =
				new BufferedReader(
					new InputStreamReader(new FileInputStream(prob_file_name)));

			prior_prob_count = Integer.parseInt(prob_file.readLine());

			for (loop = 0; loop < prior_prob_count; loop++) {
				String prob_file_token[] = split(prob_file.readLine(), ',');
				int syndrome_index =
					((Integer) syndromes.get(prob_file_token[0])).intValue();

				prior_prob[syndrome_index] =
					(Double.valueOf(prob_file_token[1])).doubleValue();
			}

			prob_1_tuple_count = Integer.parseInt(prob_file.readLine());

			for (loop = 0; loop < prob_1_tuple_count; loop++) {
				String prob_file_token[] = split(prob_file.readLine(), ',');
				int syndrome_index =
					((Integer) syndromes.get(prob_file_token[0])).intValue();
				int word_index =
					((Integer) words.get(prob_file_token[1])).intValue();

				prob_1_tuple[syndrome_index][word_index] =
					(Double.valueOf(prob_file_token[2])).doubleValue();
			}

			prob_2_tuple_count = Integer.parseInt(prob_file.readLine());

			for (loop = 0; loop < prob_2_tuple_count; loop++) {
				String prob_file_token[] = split(prob_file.readLine(), ',');
				String wordpair = prob_file_token[1] + "," + prob_file_token[2];
				int syndrome_index =
					((Integer) syndromes.get(prob_file_token[0])).intValue();
				int wordpair_index =
					((Integer) wordpairs.get(wordpair)).intValue();

				prob_2_tuple[syndrome_index][wordpair_index] =
					(Double.valueOf(prob_file_token[3])).doubleValue();
			}

			prob_file.close();
		} catch (Exception e) {
			System.out.print(
				"ERROR! Can not read file: " + prob_file_name + "\n\n");
			System.exit(0);
		}
		/*
		 *  print_hashtable(syndromes, "syndromes");
		 *  print_hashtable(words, "words");
		 *  print_hashtable(wordpairs, "wordpairs");
		 *  print_lookup(syndrome_lookup, "syndrome_lookup");
		 *  print_lookup(word_lookup, "word_lookup");
		 *  print_lookup(wordpair_lookup, "wordpair_lookup");
		 *  print_prob_array(prior_prob, "prior_prob", syndrome_lookup);
		 *  for(loop=0; loop < syndrome_count; loop++)
		 *  {
		 *  print_prob_array(prob_1_tuple[loop], "prob_1_tuple{" + syndrome_lookup[loop] + "}",
		 *  word_lookup);
		 *  }
		 *  for(loop=0; loop < syndrome_count; loop++)
		 *  {
		 *  print_prob_array(prob_2_tuple[loop], "prob_2_tuple{" + syndrome_lookup[loop] + "}",
		 *  wordpair_lookup);
		 *  }
		 */
		/*
		 *  If batch mode, classify each complaint in the complaint file
		 *  and write the classifications to the classification file.
		 */
		if (true) {
			System.out.print(
				"Reading complaint file and writing classification file...\n\n");

			try {
				String complaint_file_line = null;
				BufferedReader complaint_file =
					new BufferedReader(
						new InputStreamReader(
							new FileInputStream(complaint_file_name)));

				try {
					BufferedWriter classification_file =
						new BufferedWriter(
							new OutputStreamWriter(
								new FileOutputStream(classification_file_name)));

					while ((complaint_file_line = complaint_file.readLine())
						!= null) {
						double complaint_prob[] =
							compute_posterior_probs(
								complaint_file_line,
								syndrome_count,
								words,
								wordpairs,
								prior_prob,
								prob_1_tuple,
								prob_2_tuple);

						String syndrome_string =
							posterior_probs_to_syndrome_string(
								complaint_prob,
								syndrome_count,
								syndrome_lookup);

						classification_file.write(syndrome_string + "\n");
					}

					classification_file.close();
				} catch (Exception e) {
					System.out.print(
						"ERROR! Can not write file: "
							+ classification_file_name
							+ "\n\n");
					System.exit(0);
				}

				complaint_file.close();
			} catch (Exception e) {
				System.out.print(
					"ERROR! Can not read file: "
						+ complaint_file_name
						+ "\n\n");
				System.exit(0);
			}
		}
	}

	/*
	 *  If line mode, classify the complaint from the command line and
	 *  print the classification to STDOUT.
	 */
	/**
	 *  Description of the Method
	 *
	 * @param  prob_file_name  Description of the Parameter
	 */
	public static void lineInit(String prob_file_name) {
		int word_count = 0;
		int wordpair_count = 0;
		String word_lookup[] = null;
		String wordpair_lookup[] = null;
		int loop;
		int loop1;
		int loop2;
		try {
			int prior_prob_count = 0;
			int prob_1_tuple_count = 0;
			int prob_2_tuple_count = 0;
			String prob_file_line = null;
			BufferedReader prob_file =
				new BufferedReader(
					new InputStreamReader(new FileInputStream(prob_file_name)));

			prior_prob_count = Integer.parseInt(prob_file.readLine());

			for (loop = 0; loop < prior_prob_count; loop++) {
				String prob_file_token[] = split(prob_file.readLine(), ',');

				syndromes.put(
					prob_file_token[0],
					new Integer(syndrome_count++));
			}

			prob_1_tuple_count = Integer.parseInt(prob_file.readLine());

			for (loop = 0; loop < prob_1_tuple_count; loop++) {
				String prob_file_token[] = split(prob_file.readLine(), ',');
				Integer word_index = (Integer) words.get(prob_file_token[1]);

				if (word_index == null) {
					words.put(prob_file_token[1], new Integer(word_count++));
				}
			}

			prob_2_tuple_count = Integer.parseInt(prob_file.readLine());

			for (loop = 0; loop < prob_2_tuple_count; loop++) {
				String prob_file_token[] = split(prob_file.readLine(), ',');
				String wordpair = prob_file_token[1] + "," + prob_file_token[2];
				Integer wordpair_index = (Integer) wordpairs.get(wordpair);

				if (wordpair_index == null) {
					wordpairs.put(wordpair, new Integer(wordpair_count++));
				}
			}

			prob_file.close();
		} catch (Exception e) {
			System.out.print(
				"ERROR! Can not read file: " + prob_file_name + "\n\n");
			System.exit(0);
		}

		syndrome_lookup = new String[syndrome_count];
		/*
		 *  word_lookup     = new String[word_count];
		 *  wordpair_lookup = new String[wordpair_count];
		 */
		for (Enumeration e = syndromes.keys(); e.hasMoreElements();) {
			String next_key = (String) e.nextElement();

			syndrome_lookup[((Integer) syndromes.get(next_key)).intValue()] =
				next_key;
		}
		/*
		 *  for(Enumeration e = words.keys(); e.hasMoreElements(); )
		 *  {
		 *  String next_key = (String)e.nextElement();
		 *  word_lookup[((Integer)words.get(next_key)).intValue()] = next_key;
		 *  }
		 *  for(Enumeration e = wordpairs.keys(); e.hasMoreElements(); )
		 *  {
		 *  String next_key = (String)e.nextElement();
		 *  wordpair_lookup[((Integer)wordpairs.get(next_key)).intValue()] = next_key;
		 *  }
		 */
		prior_prob = new double[syndrome_count];
		prob_1_tuple = new double[syndrome_count][word_count];
		prob_2_tuple = new double[syndrome_count][wordpair_count];

		for (loop1 = 0; loop1 < syndrome_count; loop1++) {
			for (loop2 = 0; loop2 < word_count; loop2++) {
				prob_1_tuple[loop1][loop2] = missing_prob;
			}

			for (loop2 = 0; loop2 < wordpair_count; loop2++) {
				prob_2_tuple[loop1][loop2] = missing_prob;
			}
		}

		try {
			int prior_prob_count = 0;
			int prob_1_tuple_count = 0;
			int prob_2_tuple_count = 0;
			String prob_file_line = null;
			BufferedReader prob_file =
				new BufferedReader(
					new InputStreamReader(new FileInputStream(prob_file_name)));
//            System.out.println("Inside the intline() from defualt_prob.txt"+prob_file.readLine());
			prior_prob_count = Integer.parseInt(prob_file.readLine());

			for (loop = 0; loop < prior_prob_count; loop++) {
				String prob_file_token[] = split(prob_file.readLine(), ',');
				int syndrome_index =
					((Integer) syndromes.get(prob_file_token[0])).intValue();

				prior_prob[syndrome_index] =
					(Double.valueOf(prob_file_token[1])).doubleValue();
			}

			prob_1_tuple_count = Integer.parseInt(prob_file.readLine());

			for (loop = 0; loop < prob_1_tuple_count; loop++) {
				String prob_file_token[] = split(prob_file.readLine(), ',');
				int syndrome_index =
					((Integer) syndromes.get(prob_file_token[0])).intValue();
				int word_index =
					((Integer) words.get(prob_file_token[1])).intValue();

				prob_1_tuple[syndrome_index][word_index] =
					(Double.valueOf(prob_file_token[2])).doubleValue();
			}

			prob_2_tuple_count = Integer.parseInt(prob_file.readLine());

			for (loop = 0; loop < prob_2_tuple_count; loop++) {
				String prob_file_token[] = split(prob_file.readLine(), ',');
				String wordpair = prob_file_token[1] + "," + prob_file_token[2];
				int syndrome_index =
					((Integer) syndromes.get(prob_file_token[0])).intValue();
				int wordpair_index =
					((Integer) wordpairs.get(wordpair)).intValue();

				prob_2_tuple[syndrome_index][wordpair_index] =
					(Double.valueOf(prob_file_token[3])).doubleValue();
			}

			prob_file.close();
		} catch (Exception e) {
			System.out.print(
				"ERROR! Can not read file: " + prob_file_name + "\n\n");
			System.exit(0);
		}
	}

	/*
	 *  print_hashtable(syndromes, "syndromes");
	 *  print_hashtable(words, "words");
	 *  print_hashtable(wordpairs, "wordpairs");
	 *  print_lookup(syndrome_lookup, "syndrome_lookup");
	 *  print_lookup(word_lookup, "word_lookup");
	 *  print_lookup(wordpair_lookup, "wordpair_lookup");
	 *  print_prob_array(prior_prob, "prior_prob", syndrome_lookup);
	 *  for(loop=0; loop < syndrome_count; loop++)
	 *  {
	 *  print_prob_array(prob_1_tuple[loop], "prob_1_tuple{" + syndrome_lookup[loop] + "}",
	 *  word_lookup);
	 *  }
	 *  for(loop=0; loop < syndrome_count; loop++)/
	 *  {
	 *  print_prob_array(prob_2_tuple[loop], "prob_2_tuple{" + syndrome_lookup[loop] + "}",
	 *  wordpair_lookup);
	 *  }
	 */
	//if(LINE == 1)
	//{
	/**
	 *  Description of the Method
	 *
	 * @param  complaint_string  Description of the Parameter
	 * @return                   Description of the Return Value
	 */
	public synchronized static String line(String complaint_string)
	{
		String syndrome_string = null;
		if (!complaint_string.equals(" ")) {
			//		Create a pattern to match breaks
			Pattern p = Pattern.compile("[-\r\\s\\t\\n/&.,/?_()+:;\\\"']+");

			String[] result = p.split(complaint_string);
			if(result.length==0){
				
				int lastindex = result.length;
				
				result[lastindex+1] = prob_file_name;
//				result[2] = prob_file_name;
			}
			StringBuffer sbuff = new StringBuffer(2 * result.length);

			for (int i = 0; i < result.length; i++)
//				System.out.println(result[i]);
				complaint_string =
					sbuff
						.append(result[i])
						.append(" ")
						.toString()
						.toLowerCase()
						.trim();
           System.out.println("SBUFF VALUE INSIDE INLINE"+sbuff);
           
			double complaint_prob[] =
				compute_posterior_probs(
					complaint_string,
					syndrome_count,
					words,
					wordpairs,
					prior_prob,
					prob_1_tuple,
					prob_2_tuple);
//			for(int i=0; i < complaint_prob[]; i++){
			int test = complaint_prob.length;
			System.out.println("COMPLIANT_PROBB INSIDE INLINE"+complaint_prob);
//			}
			System.out.println("COMPLIANT_STRING INSIDE INLINE"+complaint_string);
			System.out.println("syndrome_count INSIDE INLINE"+syndrome_count);
			System.out.println("words INSIDE INLINE"+words);
			System.out.println("wordpairs INSIDE INLINE"+wordpairs);
			System.out.println("prior_prob INSIDE INLINE"+prior_prob);
			System.out.println("syndrome_lookup INSIDE INLINE"+syndrome_lookup);
			
			syndrome_string =
				posterior_probs_to_syndrome_string(
					complaint_prob,
					syndrome_count,
					syndrome_lookup);
			//System.out.print(syndrome_string + "\n");
			
		}
		if (complaint_string.equals(" ")) {
			syndrome_string = "other";
			}
		return syndrome_string;
	}

	/*
	 *  syndromes.clear();
	 *  words.clear();
	 *  wordpairs.clear();
	 *  syndrome_lookup = null;
	 *  word_lookup     = null;
	 *  wordpair_lookup = null;
	 *  prior_prob   = null;
	 *  prob_1_tuple = null;
	 *  prob_2_tuple = null;
	 *  }
	 */
	/*
	 *  Convert the posterior probabilities into a string indicating
	 *  into which syndromes the complaint string has been classified.
	 *  If the threshold is less than 0, then the string comprises
	 *  that syndrome with the highest posterior probability (in
	 *  the case of a tie, the syndrome that sorts first is chosen).
	 *  If the threshold is greater than or equal to 0, then the string
	 *  comprises those syndromes having a posterior probability greater
	 *  than or equal to the threshold.  If no syndrome has such a posterior
	 *  probability, then return an empty string.
	 */
	/**
	 *  Description of the Method
	 *
	 * @param  complaint_prob   Description of the Parameter
	 * @param  syndrome_count   Description of the Parameter
	 * @param  syndrome_lookup  Description of the Parameter
	 * @return                  Description of the Return Value
	 */
	private static String posterior_probs_to_syndrome_string(
		double[] complaint_prob,
		int syndrome_count,
		String[] syndrome_lookup) {
		String syndrome_string;
		int loop;

		System.out.println("complaint_prob"+complaint_prob);
		System.out.println("syndrome_count"+syndrome_count);
		System.out.println("syndrome_lookup"+syndrome_lookup);
		
		if (threshold < 0.0) {
			int predict = 0;
            
            
			for (loop = 1; loop < syndrome_count; loop++) {
				if (complaint_prob[loop] > complaint_prob[predict]) {
					predict = loop;
				}
			}

			if (complaint_prob[predict] == 0.0) {
				syndrome_string = "Other";
			} else {
				syndrome_string = syndrome_lookup[predict];
			}

		} else {
			int predict[] = new int[syndrome_count];
			int index = 0;

			for (loop = 0; loop < syndrome_count; loop++) {
				predict[loop] = 0;

				if (complaint_prob[loop] >= threshold) {
					predict[loop] = 1;
				}
			}

			syndrome_string = "";

			for (loop = 0; loop < syndrome_count; loop++) {
				if (predict[loop] == 1) {
					if (index > 0) {
						syndrome_string += ",";
					}

					syndrome_string += syndrome_lookup[loop];

					index++;
				}
			}

			predict = null;
		}

		return syndrome_string;
	}

	/*
	 *  Compute the posterior probability for a complaint string with
	 *  respect to each syndrome using a Bayesian classifier.  Return
	 *  the set of posterior probabilities.
	 */
	/**
	 *  Description of the Method
	 *
	 * @param  c_str           Description of the Parameter
	 * @param  syndrome_count  Description of the Parameter
	 * @param  words           Description of the Parameter
	 * @param  wordpairs       Description of the Parameter
	 * @param  prior_prob      Description of the Parameter
	 * @param  prob_1_tuple    Description of the Parameter
	 * @param  prob_2_tuple    Description of the Parameter
	 * @return                 Description of the Return Value
	 */
	private static double[] compute_posterior_probs(
		String c_str,
		int syndrome_count,
		Hashtable words,
		Hashtable wordpairs,
		double[] prior_prob,
		double[][] prob_1_tuple,
		double[][] prob_2_tuple) {
		String complaint_token[] = split_whitespace(c_str);
		for(int i=0; i < complaint_token.length; i++){
		System.out.println("complaint token inside compute_posterior_probs"+complaint_token[i]);
		}
		Integer first_word_index = (Integer) words.get(complaint_token[0]);
		double complaint_prob[] = new double[syndrome_count];
		double complaint_prob_num[] = new double[syndrome_count];
		double complaint_prob_den = 0.0;
		double first_word_prob = default_prob;
		int loop;
		int loop1;
		int loop2;

		for (loop1 = 0; loop1 < syndrome_count; loop1++) {
			boolean word_found = false;
			boolean wordpair_found = false;

			complaint_prob_num[loop1] = prior_prob[loop1];

			if (first_word_index != null) {
				first_word_prob =
					prob_1_tuple[loop1][first_word_index.intValue()];

				if (first_word_prob == missing_prob) {
					first_word_prob = default_prob;
				} else {
					word_found = true;
				}
			}

			complaint_prob_num[loop1] *= first_word_prob;

			for (loop2 = 0; loop2 < complaint_token.length - 1; loop2++) {
				String wordpair =
					complaint_token[loop2] + "," + complaint_token[loop2 + 1];
				Integer word_index =
					(Integer) words.get(complaint_token[loop2 + 1]);
				Integer wordpair_index = (Integer) wordpairs.get(wordpair);
				double word_prob = default_prob;
				double wordpair_prob = default_prob;

				if (word_index != null) {
					word_prob = prob_1_tuple[loop1][word_index.intValue()];

					if (word_prob == missing_prob) {
						word_prob = default_prob;
					} else {
						word_found = true;
					}
				}

				if (wordpair_index != null) {
					wordpair_prob =
						prob_2_tuple[loop1][wordpair_index.intValue()];

					if (wordpair_prob == missing_prob) {
						wordpair_prob = default_prob;
					} else {
						wordpair_found = true;
					}
				}

				complaint_prob_num[loop1]
					*= ((weight_2_tuple * wordpair_prob)
						+ (weight_1_tuple * word_prob));
			}

			if ((!word_found)
				|| ((!wordpair_found) && (weight_2_tuple > 0.0))) {
				complaint_prob_num[loop1] = 0.0;
			}

			complaint_prob_den += complaint_prob_num[loop1];
		}

		for (loop = 0; loop < syndrome_count; loop++) {
			if (complaint_prob_den > 0.0) {
				complaint_prob[loop] =
					complaint_prob_num[loop] / complaint_prob_den;
			} else {
				complaint_prob[loop] = 0.0;
			}
		}

		complaint_prob_num = null;
        System.out.println("Value of complaint_prob"+complaint_prob);
		return complaint_prob;
	}

	/*
	 *  Print all entries in the hashtable structure.
	 */
	/**
	 *  Description of the Method
	 *
	 * @param  hash  Description of the Parameter
	 * @param  text  Description of the Parameter
	 */
	private static void print_hashtable(Hashtable hash, String text) {
		for (Enumeration e = hash.keys(); e.hasMoreElements();) {
			String next_key = (String) e.nextElement();

			System.out.print(
				text + "[" + next_key + "] = " + hash.get(next_key) + "\n");
		}

		System.out.print("\n");

		return;
	}

	/*
	 *  Print all entries in the lookup (i.e., reverse hashtable) structure.
	 */
	/**
	 *  Description of the Method
	 *
	 * @param  lookup  Description of the Parameter
	 * @param  text    Description of the Parameter
	 */
	private static void print_lookup(String[] lookup, String text) {
		int loop;

		for (loop = 0; loop < lookup.length; loop++) {
			System.out.print(text + "[" + loop + "] = " + lookup[loop] + "\n");
		}

		System.out.print("\n");

		return;
	}

	/*
	 *  Print all entries in the probability array structure.
	 */
	/**
	 *  Description of the Method
	 *
	 * @param  prob    Description of the Parameter
	 * @param  text    Description of the Parameter
	 * @param  lookup  Description of the Parameter
	 */
	private static void print_prob_array(
		double[] prob,
		String text,
		String[] lookup) {
		int loop;

		for (loop = 0; loop < prob.length; loop++) {
			System.out.print(
				text + "{" + lookup[loop] + "} = " + prob[loop] + "\n");
		}

		System.out.print("\n");

		return;
	}

	/*
	 *  Split 'line' using 'separator' and returns an array containing
	 *  the resulting sequence of tokens.
	 */
	/**
	 *  Description of the Method
	 *
	 * @param  line       Description of the Parameter
	 * @param  separator  Description of the Parameter
	 * @return            Description of the Return Value
	 */
	private static String[] split(String line, char separator) {
		String[] token = null;

		while (true) {
			int startindex = 0;
			int endindex = 0;
			int count = 0;

			while (startindex < line.length()) {
				endindex = line.indexOf(separator, startindex);

				if (endindex >= 0) {
					if (endindex > startindex) {
						if (token != null) {
							token[count] = line.substring(startindex, endindex);
						}

						count++;
					}

					startindex = endindex + 1;
				} else {
					break;
				}
			}

			if (startindex < line.length()) {
				if (token != null) {
					token[count] = line.substring(startindex, line.length());
				}

				count++;
			}

			if (token != null) {
				return token;
			} else {
				token = new String[count];
			}
		}
	}

	/*
	 *  Split 'line' using whitespace (i.e., space, tab, newline) and
	 *  returns an array containing the resulting sequence of tokens.
	 */
	/**
	 *  Description of the Method
	 *
	 * @param  line  Description of the Parameter
	 * @return       Description of the Return Value
	 */
	private static String[] split_whitespace(String line) {
		line = line.replace('\t', ' ');
		line = line.replace('\n', ' ');

		return split(line, ' ');
	}

}
