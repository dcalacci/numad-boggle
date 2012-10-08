package edu.madcourse.bloomfilter;
import java.util.BitSet;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

import android.util.Log;

public class BitVector implements Serializable {
	Integer size;
	BitSet bs;
	
	BitVector(Integer size) {
		this.size = size;
		this.bs = new BitSet(this.size);
	}
	
	BitVector(BitSet b) {
		this.size = b.size();
		this.bs = b;
	}
	
	private void clearList() {
		this.bs.clear();
	}
	
	public Boolean get(Integer pos) {
		Boolean val = (Boolean)this.bs.get(pos);
		return val;
	}
	
	public void set(Integer pos) {
		this.bs.set(pos);
	}
	
	public void flip(Integer pos) {
		this.bs.flip(pos);
	}
}