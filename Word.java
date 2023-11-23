public class Word {

   private final String word;
   private final Pos pos;
   private final String root;
   private final Plurality number;

   public Word(String word, Pos pos, String root, Plurality number) {
      this.word = word;
      this.pos = pos;
      this.root = root;
      this.number = number;
   }

   public String getWord() {
      return word;
   }

   public Pos getPos() {
      return pos;
   }

   public String getRoot() {
      return root;
   }

   public Plurality getPlural() {
      return number;
   }

   @Override
   public String toString() {
      return "Word{" +
              "word='" + word + '\'' +
              ", pos=" + pos +
              ", root='" + root + '\'' +
              ", number=" + number +
              '}';
   }
}
