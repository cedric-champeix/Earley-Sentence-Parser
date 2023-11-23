import java.util.Objects;

public class Pos {

   private String pos;

   public Pos(String pos) {
      this.pos = pos;
   }

   public String getPosName() {
      return pos;
   }

   @Override
   public String toString() {
      return pos;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Pos pos1 = (Pos) o;
      return Objects.equals(pos, pos1.pos);
   }
}
