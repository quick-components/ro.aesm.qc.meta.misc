package ro.aesm.qc.meta.misc.txt.model;

import java.util.List;

public class MTxt_ReplaceRule extends MTxt_Rule {

	public static final String TAG = "replace";

	private String what;
	private String with;
	private String index;
	private List<Integer> indexList;
	private int[] indexRange;
	private String occurence;
	private List<Integer> occurenceList;
	private int[] occurenceRange;

	public String getWhat() {
		return what;
	}

	public void setWhat(String what) {
		this.what = what;
	}

	public String getWith() {
		return with;
	}

	public void setWith(String with) {
		this.with = with;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
		this.indexList = this.decodeList(this.index);
		this.indexRange = this.decodeRange(this.index);
	}

	public List<Integer> getIndexList() {
		return indexList;
	}

	public int[] getIndexRange() {
		return indexRange;
	}

	public String getOccurence() {
		return occurence;
	}

	public void setOccurence(String occurence) {
		this.occurence = occurence;
		this.occurenceList = this.decodeList(this.occurence);
		this.occurenceRange = this.decodeRange(this.occurence);
	}

	public List<Integer> getOccurenceList() {
		return occurenceList;
	}

	public int[] getOccurenceRange() {
		return occurenceRange;
	}

}
