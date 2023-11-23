import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

public class Parser {
   HashMap<String, Pos> posHashMap;
   ArrayList<Rule> ruleArrayList;
   HashMap<String, Word> wordHashMap;

   ArrayList<Word> sentenceElements;

   public Parser(String rulesFile, String lexiconFile) throws FileNotFoundException, IOException {
      posHashMap = new HashMap<>();
      ruleArrayList = new ArrayList<>();
      wordHashMap = new HashMap<>();

      readRules(rulesFile);
      readLexicon(lexiconFile);
   }

   private void readRules(String fileName) throws IOException {
      FileReader fileReader = new FileReader(fileName);
      BufferedReader bufferedReader = new BufferedReader(fileReader);

      System.out.println("Reading parts of speech...");

      String line = bufferedReader.readLine();
      String[] pos = line.split("\\s");

      for (String p : pos) {
         p = p.toLowerCase();
         if (posHashMap.get(p) == null) {
            posHashMap.put(p, new Pos(p));
         }
      }

      System.out.println("Reading rules...");

      while ((line = bufferedReader.readLine()) != null) {
         String[] rule_obj = line.split("\\s");

         Pos ruleName = posHashMap.get(rule_obj[0]);

         ArrayList<String> posArrayList = new ArrayList<>();
         for (int i = 1; i < rule_obj.length; i++) {
            posArrayList.add(posHashMap.get(rule_obj[i]).getPosName());
         }

         ruleArrayList.add(new Rule(ruleName, posArrayList));
      }
   }

   private void readLexicon(String fileName) throws IOException {
      FileReader fileReader = new FileReader(fileName);
      BufferedReader bufferedReader = new BufferedReader(fileReader);

      System.out.println("Reading lexicon...");

      String line;
      while ((line = bufferedReader.readLine()) != null) {
         String[] wordObj = line.split("\\s");

         String word = wordObj[0];
         Pos pos = posHashMap.get(wordObj[1]);
         String root = wordObj[2];
         Plurality plurality;
         switch (wordObj[3]) {
            case "singular":
               plurality = Plurality.SINGULAR;
               break;
            case "plural":
               plurality = Plurality.PLURAL;
               break;
            default:
               plurality = Plurality.BOTH;
         }

         wordHashMap.put(word, new Word(word, pos, root, plurality));
      }
   }

   public String inputSentence(String sentence) {
      System.out.println("Checking if words in sentence are in the lexicon...");

      String[] words = sentence.split("\\s");

      sentenceElements = new ArrayList<>();

      for (String word : words) {
         word = word.toLowerCase();

         Word currentWord = wordHashMap.get(word);
         if (currentWord == null) {
            sentenceElements = null;
            return word;
         }

         sentenceElements.add(currentWord);
      }

      System.out.println("All words are in the lexicon.");

      return null;
   }

   public AbstractNode createTree() {
      try {
         System.out.println("Starting parsing...");
         ArrayList<ArrayList<State>> chart = earleyParse();

         System.out.println("Generating tree from parsed phrase...");
         State beginState = chart.get(chart.size() - 1).stream().filter(s -> Objects.equals(s.getRule().getMainPos().getPosName(), "s")).collect(Collectors.toList()).get(0);
         return createTreeWorker(beginState);
      }catch (Error e){
         return null;
      }

   }


   private ArrayList<ArrayList<State>> earleyParse() throws Error {
      ArrayList<ArrayList<State>> chart = new ArrayList<>();

      for (int i = 0; i <= sentenceElements.size(); i++) {
         chart.add(i, new ArrayList<>());
      }

      ArrayList<String> posArrayList = new ArrayList<>();
      posArrayList.add("s");
      chart.get(0).add(new State(new Rule(new Pos("S1"), posArrayList), 0, 0, 0, null, "Dummy"));

      for (int i = 0; i <= sentenceElements.size(); i++) {
         int j = 0;

         while (j < chart.get(i).size()) {
            State state = chart.get(i).get(j);

            if (!state.isComplete() && ruleArrayList.stream().anyMatch(r -> Objects.equals(r.getMainPos().getPosName(), state.getNextPos()))) {
               predictor(state, chart);
            } else if (!state.isComplete()) {
               scanner(state, chart, i);
            } else {
               completer(state, chart);
            }

            j++;
         }
      }

      return chart;
   }

   private void predictor(State state, ArrayList<ArrayList<State>> chart) throws Error {
      // For each rule in ruleArrayList where ruleName == pos of state at index dotPosition
      // we add a new state to chart with the rule of pos
      for (Rule rule : ruleArrayList.stream().filter(r -> Objects.equals(r.getMainPos().getPosName(), state.getNextPos())).collect(Collectors.toList())) {
         enqueue(new State(rule, state.getChartPosition(), state.getChartPosition(), 0, null, "Predictor"), chart.get(state.getChartPosition()));
      }
   }

   private void scanner(State state, ArrayList<ArrayList<State>> chart, int i) throws Error {
      if (i >= sentenceElements.size()) {
         throw new Error("Couldn't parse the tree");
      }

      Word current_word = sentenceElements.get(i);
      String stateNextPos = state.getNextPos();

      if (current_word.getPos().getPosName().equals(stateNextPos)) {
         int chartPos = state.getChartPosition();
         ArrayList<String> posArray = new ArrayList<>();
         posArray.add(current_word.getWord());

         enqueue(new State(new Rule(posHashMap.get(stateNextPos), posArray), chartPos, chartPos + 1, 1, null, "Scanner"), chart.get(chartPos + 1));
      }

   }

   private void completer(State state, ArrayList<ArrayList<State>> chart) throws Error {
      ArrayList<State> chartEntry = chart.get(state.getOrigin());
      int currentChartPosit = state.getChartPosition();

      for (State st : chartEntry.stream().filter(s -> Objects.equals(s.getNextPos(), state.getRule().getMainPos().getPosName())).collect(Collectors.toList())) {
         ArrayList<State> old_states = new ArrayList<>();
         old_states.add(state);
         if (st.getChildren() != null) {
            old_states.addAll(st.getChildren());
         }

         enqueue(new State(st.getRule(), st.getOrigin(), currentChartPosit, st.getDotPosition() + 1, old_states, "Completer"), chart.get(currentChartPosit));
      }
   }

   private void enqueue(State new_state, ArrayList<State> chartEntry) throws Error {
      if (!chartEntry.contains(new_state)) {
         chartEntry.add(new_state);
      }
   }

   public AbstractNode createTreeWorker(State s) throws Error {
      if (s.getChildren() == null) {
         Rule rule = s.getRule();
         return new SingleNode(rule.getMainPos().getPosName(), rule.getPosArrayList().get(0));
      }

      String head = s.getRule().getMainPos().getPosName();

      return new BinaryNode(
              head,
              createTreeWorker(s.getChildren().get(0)),
              createTreeWorker(s.getChildren().get(1))
      );
   }
}
