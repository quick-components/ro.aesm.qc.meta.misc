package ro.aesm.qc.meta.misc.txt.model;

import java.util.List;

public class MTxt_KeepRule extends MTxt_Rule {

	public static final String TAG = "keep";

	private String from;
	private String to;

	private int fromOffset;
	private int toOffset;

	private String index;
	private List<Integer> indexList;
	private int[] indexRange;

	private String occurence;
	private List<Integer> occurenceList;
	private int[] occurenceRange;

	private String separator;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public int getFromOffset() {
		return fromOffset;
	}

	public void setFromOffset(int fromOffset) {
		this.fromOffset = fromOffset;
	}

	public int getToOffset() {
		return toOffset;
	}

	public void setToOffset(int toOffset) {
		this.toOffset = toOffset;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
		this.indexList = this.decodeList(this.index);
		this.indexRange = this.decodeRange(this.index);
	}

	public String getOccurence() {
		return occurence;
	}

	public void setOccurence(String occurence) {
		this.occurence = occurence;
		this.occurenceList = this.decodeList(this.occurence);
		this.occurenceRange = this.decodeRange(this.occurence);
	}

	public List<Integer> getIndexList() {
		return indexList;
	}

	public int[] getIndexRange() {
		return indexRange;
	}

	public List<Integer> getOccurenceList() {
		return occurenceList;
	}

	public int[] getOccurenceRange() {
		return occurenceRange;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

}
