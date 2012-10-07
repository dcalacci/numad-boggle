package edu.madcourse.dancalacci.boggle;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
//import java.util.Set;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;


class MurmurHash2 {
	
	public static List<Integer> hashCodes(Object o, Integer k, Integer m) {
		List<Integer> result = new ArrayList<Integer>();
		byte[] codes = BigInteger.valueOf(o.hashCode()).toByteArray();
		
		/* hash using previous hash as seed with
		 * initial seed as hashCode */
		result.add(Math.abs(hash(codes, o.hashCode())) % m);
		for (int i = 1; i < k; i++) {
			result.add(Math.abs(hash(codes, result.get(i - 1))) % m);
		}
		
		return result;
	}

    @SuppressWarnings("fallthrough")
        public static int hash(byte[] data, int seed) {
        // 'm' and 'r' are mixing constants generated offline.
        // They're not really 'magic', they just happen to work well.
        int m = 0x5bd1e995;
        int r = 24;

        // Initialize the hash to a 'random' value
        int len = data.length;
        int h = seed ^ len;

        int i = 0;
        while (len  >= 4) {
            int k = data[i + 0] & 0xFF;
            k |= (data[i + 1] & 0xFF) << 8;
            k |= (data[i + 2] & 0xFF) << 16;
            k |= (data[i + 3] & 0xFF) << 24;

            k *= m;
            k ^= k >>> r;
            k *= m;

            h *= m;
            h ^= k;

            i += 4;
            len -= 4;
        }

        switch (len) {
        case 3: h ^= (data[i + 2] & 0xFF) << 16;
        case 2: h ^= (data[i + 1] & 0xFF) << 8;
        case 1: h ^= (data[i + 0] & 0xFF);
                h *= m;
        }

        h ^= h >>> 13;
        h *= m;
        h ^= h >>> 15;

        return h;
    }
}



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
//-----------------------------------------------------------------------------
// A BloomFilter<T> is a
//   new BloomFilter<T>(Integer k, Integer m)
//
// and implements Set<T>
// add and contains
class BloomFilter<T> {
	Integer k;
	BitVector bit;
	Integer m;
	MurmurHash2 gen = new MurmurHash2();
	BloomFilter(Integer k, Integer m) {
		this.k = k;
		this.m = m;
		bit = new BitVector(m);
	}
	public void add(T obj) {
		List<Integer> keys = gen.hashCodes(obj, this.k, this.m);
		for (Integer i : keys) {
			this.bit.set(i);
		}
	}
	public Boolean contains(T obj) {
		List<Integer> keys = gen.hashCodes(obj, this.k, this.m);
		for (Integer i : keys) {
			if (!this.bit.get(i)) {
				return false;
			}
		}
		return true;
	}
}

//-----------------------------------------------------------------------------

class BitVector {
	Integer size;
	ArrayList<Boolean> list;
	//Constructor
	BitVector(Integer size) {
		this.size = size;
		this.list = new ArrayList<Boolean>(this.size);
		this.clearList();
	}
	private void clearList() {
		this.list.clear();
		for (Integer i = 0; i < this.size; i++) {
			this.list.add(false);
		}
	}
	//Produce bit at given position
	public Boolean get(Integer pos) {
		if (pos >= this.size) {
			throw new RuntimeException("too big");
		} else {
			return this.list.get(pos);
		}
	}
	//Set bit to true at given position
	public void set(Integer pos) {
		this.list.set(pos, true);
	}
	//Flip a bit at the position
	public void flip(Integer pos) {
		if (pos >= this.size) {
			throw new RuntimeException("too big");
		} else {
			this.list.set(pos, !this.list.get(pos));
		}
	}
}



//class Examples {
//	Examples() {}
//
//  public void tests(Tester t) {
//    this.testBloomFilterRando(t);
//    this.testBloomFilterRando2(t);
//  }
//
//	public Boolean testClearList(Tester t) {
//		return t.checkExpect(new BitVector(5).get(3), false);
//	}
//	public Boolean testSet(Tester t) {
//		BitVector bit = new BitVector(5);
//		bit.set(3);
//		return t.checkExpect(bit.get(3), true)
//		&& t.checkExpect(bit.get(4), false);
//	}
//	public void testFlip(Tester t) {
//		BitVector bit = new BitVector(5);
//		bit.flip(4);
//		t.checkExpect(bit.get(4), true);
//	}
//	public void testBloomFilter(Tester t) {
//		BloomFilter<String> bloom = new BloomFilter<String>(5, 10);
//		bloom.add("Dan");
//		t.checkExpect(bloom.contains("Dan"), true);
//		t.checkExpect(bloom.contains("Todd"), false);
//	}
//	public void testBloomFilterRando(Tester t) {
//		BloomFilter<Integer> bloom = new BloomFilter<Integer>(10,5000);
//		Random rand = new Random(bloom.hashCode());
//		Integer random = rand.nextInt(5000);
//		for (Integer i = 0; i < 10; i++) {
//			bloom.add(random);
//			t.checkExpect(bloom.contains(random));
//			random = rand.nextInt(5000);
//		}
//		for (Integer i = 0; i < 100; i++) {
//			t.checkExpect(bloom.contains(random));
//			random = rand.nextInt(5000);
//		}
//	}
//
//  public void testBloomFilterRando2(Tester t) {
//    try {
//    File wordlist = new File("/Users/Dan/Dropbox/work/semesters/s2012/cs2510/labs/wordlist.txt");
//
//    BloomFilter<String> bloom = new BloomFilter<String>(66, 41439467);
//    FileReader input = new FileReader(wordlist);
//    BufferedReader bufRead = new BufferedReader(input);
//
//    String line;
//    int count = 0;
//
//    line = bufRead.readLine();
//    bloom.add(line);
//
//    while (line != null) {
//      line = bufRead.readLine();
//      bloom.add(line);
//      t.checkExpect(bloom.contains(line));
//    }
//
//    bufRead.close();
//  }
//    catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//
//}
