package evaluator;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

import request.DocumentRelevance;

public class Evaluation {

	private static String PATH_TO_QREL = "/Users/cecilemanset/Documents/INSA/5IL/WebSem/qrels/";
	private static int DOC_NB = 138;
	private static int REQ_NB = 9;
	
	private String[] requestTab;

//	private ArrayList<DocumentRelevance> qrels;

	public Evaluation() {
//		qrels = new ArrayList<DocumentRelevance>();
//		for (int qrelId = 1; qrelId <= 9; qrelId++) {
//			File input = new File(PATH_TO_QREL + "qrelQ" + qrelId + ".txt");
//			Scanner sc = null;
//			try {
//				sc = new Scanner(input);
//				while (sc.hasNextLine()) {
//					String[] qrel = sc.next().split(" ");
//					qrels.add(new DocumentRelevance(qrel[0], Float.parseFloat(qrel[1])));
//				}
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} finally {
//				sc.close();
//			}
//		}
		
		
	}

//	private ArrayList<DocumentRelevance> createQrel(int qrelId) {
//		ArrayList<DocumentRelevance> qrels = new ArrayList<DocumentRelevance>();
//		File input = new File(PATH_TO_QREL + "qrelQ" + qrelId + ".txt");
//		Scanner sc = null;
//		try {
//			sc = new Scanner(input);
//			while (sc.hasNextLine()) {
////				String[] qrel = sc.next().split("[ ]+|\t");
////				System.out.println("toto " + qrel.length);
//				qrels.add(new DocumentRelevance(sc.next(), Float.parseFloat(sc.next())));
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} finally {
//			sc.close();
//		}
//		return qrels;
//	}
	
	// Renvoie un tableau qui fait correspondre l'indice de pertinence établi par un humain à chaque document du corpus, 
	// pour une requête donnée. Attention, le tableau commence en 0 et les docs en 1 !!
	private float[] readQrel(int qrelId) {
		float[] docRelevanceTab = new float[DOC_NB];
		int ind = 0;
		String relevance;
		File input = new File(PATH_TO_QREL + "qrelQ" + qrelId + ".txt");
		Scanner sc = null;
		
		try {
			sc = new Scanner(input);
			while (sc.hasNext()) {
				sc.next(); // Nom du document
				relevance = sc.next();
				if (relevance.compareTo("0,5") == 0)
					relevance = "0.5";
				docRelevanceTab[ind] = Float.parseFloat(relevance);
				ind++;
			}
			if (ind != DOC_NB) {
				System.out.println("WARNING : an error might have occurred. The Qrel file was weirdly parsed.");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			sc.close();
		}
		return docRelevanceTab;
	}
	
	private float computePerformance(ArrayList<DocumentRelevance> docRelevanceList, float[] docRelevanceTab, float nbRelevantDocs) {
		float performance = 0;
		String docName;
		int docNb;
		
		float factor = 1/nbRelevantDocs;
		
		for (int i = 0 ; i < nbRelevantDocs ; i++) {
			docName = docRelevanceList.get(i).getName();
			docNb = Integer.parseInt(docName.substring(1).split("\\.")[0]); // Testé 
			
			performance += docRelevanceTab[docNb-1] * factor;
		}
		
		return performance;
	}

	// run all the requests and check against Qrel file to establish accuracy
	public void evaluate(ArrayList<DocumentRelevance> docRelevanceList, int qrelId) {
		DecimalFormat df = new DecimalFormat("#.##");
		float[] docRelevanceTab = readQrel(qrelId);
//		System.out.println("Qrels list: ");
//		for (int i = 0 ; i < doc_nb ; i++) {
//			System.out.println("doc " + i + " " + docRelevanceTab[i]);
//		}
//
//		System.out.println("Our list: ");
//		for (DocumentRelevance docRelevance : docRelevanceList) {
//			System.out.println(docRelevance.getName() + " " + docRelevance.getScore());
//		}
		
		System.out.println("PA 5 : " + df.format(computePerformance(docRelevanceList, docRelevanceTab, 5)));
		System.out.println("PA 10 : " + df.format(computePerformance(docRelevanceList, docRelevanceTab, 10)));
		System.out.println("PA 25 : " + df.format(computePerformance(docRelevanceList, docRelevanceTab, 25)));
	}

}