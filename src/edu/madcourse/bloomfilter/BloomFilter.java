package edu.madcourse.bloomfilter;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
//import java.util.Set;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;
import java.util.BitSet;


//-----------------------------------------------------------------------------
// A BloomFilter<T> is a
//   new BloomFilter<T>(Integer k, Integer m)
//
// and implements Set<T>
// add and contains
public class BloomFilter<T> {
	Integer k;
	BitVector bit;
	Integer m;
	MurmurHash2 gen = new MurmurHash2();
	public BloomFilter(Integer k, Integer m) {
		this.k = k;
		this.m = m;
		bit = new BitVector(m);
	}
	
	// if we're given just a bitset, it's being initialized with a saved
	// bit vector - 
	public BloomFilter(Integer k, BitSet b) {
		this.k = k;
		this.m = b.size();
		this.bit = new BitVector(b);
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