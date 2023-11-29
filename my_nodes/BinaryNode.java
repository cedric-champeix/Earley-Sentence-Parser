package my_nodes;

public class BinaryNode extends AbstractNode {

   private AbstractNode node1;
   private AbstractNode node2;

   public BinaryNode(String head, AbstractNode node1, AbstractNode node2) {
      super(head);
      this.node1 = node1;
      this.node2 = node2;
   }

   public AbstractNode getNode1() {
      return node1;
   }

   public AbstractNode getNode2() {
      return node2;
   }

   @Override
   public String getTreeDisplay(String indent) {
      return node2.getTreeDisplay("        " + indent) +
              super.getTreeDisplay(indent) + "\n" +
              node1.getTreeDisplay("        " + indent);
   }

   @Override
   public String getBracketedStructDisplay() {
      return "[" +
              super.getBracketedStructDisplay() +
              " " +
              node2.getBracketedStructDisplay() +
              " " +
              node1.getBracketedStructDisplay() +
              "]";
   }
}
