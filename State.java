import java.util.List;
import java.util.Objects;

public class State {

   private final Rule rule;
   private final int origin;
   private final int chartPosition;
   private final int dotPosition;
   private final List<State> children;
   private final String action;

   public State(Rule rule, int origin, int chartPosition, int dotPosition, List<State> children, String action) {
      this.rule = rule;
      this.origin = origin;
      this.chartPosition = chartPosition;
      this.dotPosition = dotPosition;
      this.children = children;
      this.action = action;
   }

   public boolean isComplete() {
      return dotPosition >= rule.getNbChildren();
   }

   public String getNextPos() {
      return rule.getPosAt(dotPosition);
   }

   public Rule getRule() {
      return rule;
   }

   public int getOrigin() {
      return origin;
   }

   public int getChartPosition() {
      return chartPosition;
   }

   public int getDotPosition() {
      return dotPosition;
   }

   public List<State> getChildren() {
      return children;
   }

   public void addChildren(List<State> st_list) {
      for (State st : st_list) {
         if (!children.contains(st)) {
            children.add(st);
         }
      }
   }

   public String getAction() {
      return action;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      State state = (State) o;
      return Objects.equals(rule, state.rule) && origin == state.origin && chartPosition == state.chartPosition && dotPosition == state.dotPosition;
   }


   @Override
   public String toString() {
      return "State{" +
              "rule=" + rule +
              ", [" + origin +
              ", " + chartPosition +
              ", " + dotPosition +
              "] , action=" + action +
              '}';
   }
}
