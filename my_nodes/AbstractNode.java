package my_nodes;

public abstract class AbstractNode {
   private String head;

   public AbstractNode(String head) {
      this.head = head;
   }

   public String getHead() {
      return head;
   }

   public String getTreeDisplay(String indent) {
      return indent + head;
   }

   public String getBracketedStructDisplay() {
      return head.toUpperCase();
   }
}
