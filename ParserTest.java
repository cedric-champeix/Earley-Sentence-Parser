import my_nodes.AbstractNode;

import java.util.Scanner;

public class ParserTest {

   public static final String RULES_FILE = "rules.txt";
   public static final String LEXICON_FILE = "lexicon.txt";


   public static void main(String[] args) {
      try {
         Parser parser = new Parser(RULES_FILE, LEXICON_FILE);

         Scanner scanner = new Scanner(System.in);

         System.out.println("Write the sentence to parse: ");
         String sentence = scanner.nextLine();

         String unknownWord = parser.inputSentence(sentence);
         if (unknownWord != null) {
            System.out.println("The word: '" + unknownWord + "' is unknown.");
            return;
         }

         AbstractNode tree = parser.createTree();

         if (tree == null) {
            System.out.println("Couldn't parse sentence.");
            return;
         }

         System.out.println(tree.getTreeDisplay("-----"));
         System.out.println();
         System.out.println(tree.getBracketedStructDisplay());

      } catch (Exception e) {
         e.printStackTrace();
      }
   }

}
