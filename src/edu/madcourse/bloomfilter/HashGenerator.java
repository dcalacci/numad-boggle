package edu.madcourse.bloomfilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class HashGenerator {
  // hashCode : Object Integer Integer -> List<Integer>
  // produces k hashCodes for the given object
  public static List<Integer> hashCodes(Object o, Integer k, Integer m) {
        Random rand = new Random(o.hashCode());
    ArrayList<Integer> keys = new ArrayList<Integer>();
 
    return hashCodesAccum(o, k, m, keys, rand);
  }
 
  // hashCodesAccum : Object Integer Integer List<Integer> -> List<Integer>
  // accumulator helper for the function above
  // Invariant: lst is the list of keys generated so far
  private static List<Integer> hashCodesAccum(Object o, Integer k, Integer m,
                  List<Integer> keys, Random rand) {
    Integer next = rand.nextInt(m);
 
    if (k == 0) {
      return keys;
    }
    else {
      keys.add(next);
      return hashCodesAccum(o, k - 1, m, keys, rand);
    }
  }
}