import java.util.ArrayList;
import java.util.Objects;

public class Rule {

   private final Pos rulePos;
   private final ArrayList<String> posArrayList;

   public Rule(Pos ruleName, ArrayList<String> pos) {
      this.rulePos = ruleName;
      this.posArrayList = pos != null ? pos : new ArrayList<>();
   }

   public Pos getMainPos() {
      return rulePos;
   }

   public ArrayList<String> getPosArrayList() {
      return posArrayList;
   }

   public String getPosAt(int i) {
      if (posArrayList.size() <= i) {
         return null;
      }
      return posArrayList.get(i);
   }

   public int getNbChildren() {
      return posArrayList.size();
   }

   @Override
   public String toString() {
      StringBuilder str = new StringBuilder();

      for (String pos : posArrayList) {
         str.append(pos).append(" ");
      }

      return "Rule{" + rulePos +
              " => " + str +
              '}';
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Rule rule = (Rule) o;
      return Objects.equals(rulePos, rule.rulePos) && Objects.equals(posArrayList, rule.posArrayList);
   }
}
