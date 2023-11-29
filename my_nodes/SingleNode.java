package my_nodes;

public class SingleNode extends AbstractNode {

   public String word;

   public SingleNode(String head, String word) {
      super(head);
      this.word = word;
   }

   public String getWord() {
      return word;
   }

   @Override
   public String getTreeDisplay(String indent) {
      return super.getTreeDisplay(indent) + " ----- " + word + "\n";
   }

   @Override
   public String getBracketedStructDisplay() {
      return "[" +
              super.getBracketedStructDisplay() +
              " [ " + word + " ] " +
              "]";
   }
}
