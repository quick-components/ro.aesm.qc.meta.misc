package ro.aesm.qc.meta.misc.txt.model;

import java.util.ArrayList;
import java.util.List;

public abstract class MTxt_Rule {

	protected String type;

	protected List<Integer> decodeList(String source) {
		List<Integer> list = null;
		if (source != null && !source.isEmpty() && !source.contains(":") && !source.equals("*")) {
			list = new ArrayList<Integer>();
			if (source.contains(",")) {
				String[] tokens = source.split(",");
				for (String token : tokens) {
					list.add(Integer.valueOf(token));
				}
			} else {
				list.add(Integer.valueOf(source));
			}
		}
		return list;
	}

	protected int[] decodeRange(String source) {
		int[] range = null;

		if (source != null && source.equals("*")) {
			range = new int[] { 1, Integer.MAX_VALUE };
		}
		if (source != null && source.contains(":")) {
			String[] tokens = source.split(":");
			range = new int[] { 1, Integer.MAX_VALUE };
			range[0] = Integer.parseInt(tokens[0]);
			if (tokens.length == 2) {
				//if (tokens[1] != null && !tokens[1].isBlank()) {
				if (tokens[1] != null && !tokens[1].equals("")) {
					range[1] = Integer.parseInt(tokens[1]);
				}
			}
		}
		return range;
	}

}
