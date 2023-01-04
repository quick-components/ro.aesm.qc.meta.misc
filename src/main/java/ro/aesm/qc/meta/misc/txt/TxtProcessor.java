package ro.aesm.qc.meta.misc.txt;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ro.aesm.qc.base.AbstractMetaProcessor;
import ro.aesm.qc.base.util.IOUtils;
import ro.aesm.qc.meta.misc.txt.model.MTxt_KeepRule;
import ro.aesm.qc.meta.misc.txt.model.MTxt_ReplaceBetweenRule;
import ro.aesm.qc.meta.misc.txt.model.MTxt_ReplaceRule;
import ro.aesm.qc.meta.misc.txt.model.MTxt_Rule;

public class TxtProcessor extends AbstractMetaProcessor {

	public String execute(TxtModel txtMetaModel, InputStream dataInputStream) throws Exception {
		String txt = IOUtils.toString(dataInputStream);
		return this.execute(txtMetaModel, txt);
	}

	/**
	 * 
	 * @param mm
	 * @param content
	 * @return
	 */

	public String execute(TxtModel txtMetaModel, String dataString) {
		String txt = dataString;
		for (MTxt_Rule rule : ((TxtModel) txtMetaModel).getRules()) {
			if (rule instanceof MTxt_KeepRule) {
				txt = this.executeKeepRule((MTxt_KeepRule) rule, txt);
			} else if (rule instanceof MTxt_ReplaceRule) {
				txt = this.executeReplaceRule((MTxt_ReplaceRule) rule, txt);
			} else if (rule instanceof MTxt_ReplaceBetweenRule) {
				txt = this.executeReplaceBetweenRule((MTxt_ReplaceBetweenRule) rule, txt);
			}
		}
		return txt;
	}

	/**
	 * 
	 * @param rule
	 * @param content
	 * @return
	 */
	protected String executeKeepRule(MTxt_KeepRule rule, String content) {
		String txt = content;
		String from = rule.getFrom();
		String to = rule.getTo();
		String separator = rule.getSeparator();

		if (rule.getIndex() != null) {
			if (rule.getIndexList() != null) {
				StringBuilder sb = new StringBuilder();
				int k = 0;
				for (int i = rule.getIndexList().size() - 1; i >= 0; i--) {
					int occ = rule.getIndexList().get(i);
					if (occ < txt.length()) {
						if (k > 0) {
							sb.append(separator);
						}
						sb.append(txt.substring(occ, occ + 1));
						k++;
					}
				}
				txt = sb.toString();
			} else if (rule.getIndexRange() != null) {
				int[] range = rule.getIndexRange();
				if (range[1] > txt.length()) {
					range[1] = txt.length();
				}
				txt = txt.substring(range[0], range[1]);
			}
		} else if (!from.isEmpty() || !to.isEmpty()) {

			if (from.isEmpty() && !to.isEmpty()) {
				int idx = txt.indexOf(to);
				if (idx >= 0) {
					idx = idx + rule.getToOffset();
					if (idx < 0) {
						idx = 0;
					}
					if (idx > txt.length()) {
						idx = txt.length();
					}
					txt = txt.substring(0, idx);
				}
				return txt;
			}
			if (!from.isEmpty() && to.isEmpty()) {
				int idx = txt.indexOf(from);
				if (idx >= 0) {
					idx = idx + rule.getFromOffset();
					if (idx < 0) {
						idx = 0;
					}
					if (idx > txt.length()) {
						idx = txt.length();
					}
					txt = txt.substring(idx);
				}
				return txt;
			}

			List<int[]> occurences = new ArrayList<int[]>();
			List<Integer> list = rule.getOccurenceList();
			int[] range = rule.getOccurenceRange();
			boolean isRangeOccurence = true;
			int lastOcc = 0;
			if (list != null && !list.isEmpty()) {
				lastOcc = rule.getOccurenceList().get(rule.getOccurenceList().size() - 1);
				isRangeOccurence = false;
			} else {
				lastOcc = range[1];
			}
			int cnt = 0;
			int searchIdx = 0;
			StringBuilder sb = new StringBuilder();
			while (cnt < lastOcc) {
				int startIdx = 0;

				if (!from.isEmpty()) {
					startIdx = txt.indexOf(from, searchIdx);
				}
				if (startIdx < 0) {
					break;
				}
				int endIdx = txt.length() - 1;
				if (!to.isEmpty()) {
					endIdx = txt.indexOf(to, startIdx + from.length());
				}
				if (endIdx > txt.length() || endIdx < 0) {
					endIdx = txt.length() - 1;
				}
				cnt++;
				// TODO: Evaluate if the next search should start from endIndex + to.length
				searchIdx = endIdx + rule.getToOffset();
				if ((isRangeOccurence && (cnt >= range[0] && cnt <= range[1]))
						|| (rule.getOccurenceList() != null && rule.getOccurenceList().contains(cnt))) {
					int _from = startIdx + rule.getFromOffset();
					if (_from < 0) {
						_from = 0;
					}
					int _to = endIdx + rule.getToOffset();
					if (_to > txt.length() - 1) {
						_to = txt.length();
					}
					occurences.add(new int[] { cnt, _from, _to });
				}
			}
			int k = 0;
			for (int i = 0; i < occurences.size(); i++) {
				int[] occ = occurences.get(i);
				if (k > 0) {
					sb.append(separator);
				}
				sb.append(txt.substring(occ[1], occ[2]));
				k++;
			}
			txt = sb.toString();
		}

		return txt;
	}

	// ===================== ReplaceRule =========================

	/**
	 * 
	 * @param rule
	 * @param content
	 * @return
	 */
	protected String executeReplaceRule(MTxt_ReplaceRule rule, String content) {
		String txt = content;
		String what = rule.getWhat();
		String with = rule.getWith();
		if (what != null) {
			if (rule.getOccurence().equals("*")) {
				txt = txt.replaceAll(what, with);
			} else if (rule.getOccurenceList() != null || rule.getOccurenceRange() != null) {
				txt = this.executeReplaceRuleByOccurence(rule, txt);
			}
		} else if (rule.getIndex() != null) {
			if (rule.getIndexList() != null) {
				for (int i = rule.getIndexList().size() - 1; i >= 0; i--) {
					int occ = rule.getIndexList().get(i);
					if (occ < txt.length()) {
						txt = txt.substring(0, occ) + with + txt.substring(occ + 1);
					}
				}
			} else if (rule.getIndexRange() != null) {
				int[] range = rule.getIndexRange();
				if (range[1] > txt.length()) {
					range[1] = txt.length();
				}
				txt = txt.substring(0, range[0]) + with + txt.substring(range[1]);
			}
		}
		return txt;
	}

	/**
	 * 
	 * @param rule
	 * @param content
	 * @return
	 */
	protected String executeReplaceRuleByOccurence(MTxt_ReplaceRule rule, String content) {
		String txt = content;
		String what = rule.getWhat();
		String with = rule.getWith();
		List<int[]> occurences = new ArrayList<int[]>();
		List<Integer> list = rule.getOccurenceList();
		int[] range = rule.getOccurenceRange();
		boolean isRangeOccurence = true;
		int lastOcc = 0;
		if (list != null && !list.isEmpty()) {
			lastOcc = rule.getOccurenceList().get(rule.getOccurenceList().size() - 1);
			isRangeOccurence = false;
		} else {
			lastOcc = range[1];
		}
		int cnt = 0;
		int searchIdx = 0;
		while (cnt < lastOcc) {
			int idx = txt.indexOf(what, searchIdx);
			if (idx < 0) {
				break;
			}
			cnt++;
			searchIdx = idx + what.length();

			if ((isRangeOccurence && (cnt >= range[0] && cnt <= range[1]))
					|| (rule.getOccurenceList() != null && rule.getOccurenceList().contains(cnt))) {
				occurences.add(new int[] { cnt, idx });
			}
		}
		for (int i = occurences.size() - 1; i >= 0; i--) {
			int[] occ = occurences.get(i);
			txt = txt.substring(0, occ[1]) + with + txt.substring(occ[1] + what.length());
			// System.out.println(txt);
		}
		return txt;
	}

	/**
	 * 
	 * @param rule
	 * @param content
	 * @return
	 */
	protected String executeReplaceBetweenRule(MTxt_ReplaceBetweenRule rule, String content) {

		String txt = content;
		String from = rule.getFrom();
		String to = rule.getTo();
		String with = rule.getWith();

		if (from.isEmpty() && to.isEmpty()) {
			return txt;
		}

		if (from.isEmpty() && !to.isEmpty()) {
			int idx = txt.indexOf(to);
			if (idx >= 0) {
				idx = idx + rule.getToOffset();
				if (idx < 0) {
					idx = 0;
				}
				if (idx > txt.length()) {
					idx = txt.length();
				}
				txt = txt.substring(0, idx) + with;
			}
			return txt;
		}
		if (!from.isEmpty() && to.isEmpty()) {
			int idx = txt.indexOf(from);
			if (idx >= 0) {
				idx = idx + rule.getFromOffset();
				if (idx < 0) {
					idx = 0;
				}
				if (idx > txt.length()) {
					idx = txt.length();
				}
				txt = with + txt.substring(idx);
			}
			return txt;
		}

		List<int[]> occurences = new ArrayList<int[]>();
		List<Integer> list = rule.getOccurenceList();
		int[] range = rule.getOccurenceRange();
		boolean isRangeOccurence = true;
		int lastOcc = 0;
		if (list != null && !list.isEmpty()) {
			lastOcc = rule.getOccurenceList().get(rule.getOccurenceList().size() - 1);
			isRangeOccurence = false;
		} else {
			lastOcc = range[1];
		}

		int cnt = 0;
		int searchIdx = 0;
		while (cnt < lastOcc) {
			int startIdx = 0;

			if (!from.isEmpty()) {
				startIdx = txt.indexOf(from, searchIdx);
			}
			if (startIdx < 0) {
				break;
			}
			int endIdx = txt.length() - 1;
			if (!to.isEmpty()) {
				endIdx = txt.indexOf(to, startIdx + from.length());
			}
			if (endIdx > txt.length() || endIdx < 0) {
				endIdx = txt.length() - 1;
			}
			cnt++;
			// TODO: Evaluate if the next search should start from endIndex + to.length
			searchIdx = endIdx + rule.getToOffset();
			if ((isRangeOccurence && (cnt >= range[0] && cnt <= range[1]))
					|| (rule.getOccurenceList() != null && rule.getOccurenceList().contains(cnt))) {
				int _from = startIdx + rule.getFromOffset();
				if (_from < 0) {
					_from = 0;
				}
				int _to = endIdx + rule.getToOffset();
				if (_to > txt.length() - 1) {
					_to = txt.length();
				}
				occurences.add(new int[] { cnt, _from, _to });
			}
		}
		for (int i = occurences.size() - 1; i >= 0; i--) {
			int[] occ = occurences.get(i);
			txt = txt.substring(0, occ[1]) + with + txt.substring(occ[2]);
			// System.out.println(txt);
		}
		return txt;
	}

}
