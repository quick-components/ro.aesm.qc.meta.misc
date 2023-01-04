package ro.aesm.qc.meta.misc.txt.model;

import java.util.List;

public class MTxt_ReplaceBetweenRule extends MTxt_Rule {

	public static final String TAG = "replace_between";
	private String from;
	private String to;
	private String with;

	private int fromOffset;
	private int toOffset;

	private String occurence;
	private List<Integer> occurenceList;
	private int[] occurenceRange;

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

	public String getWith() {
		return with;
	}

	public void setWith(String with) {
		this.with = with;
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
