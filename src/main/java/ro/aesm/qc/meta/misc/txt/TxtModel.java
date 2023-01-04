package ro.aesm.qc.meta.misc.txt;

import java.util.ArrayList;
import java.util.List;

import ro.aesm.qc.base.AbstractMetaModel;
import ro.aesm.qc.meta.misc.txt.model.MTxt_Rule;

public class TxtModel extends AbstractMetaModel {

	public TxtModel(String name) {
		super(name);
	}

	private List<MTxt_Rule> rules = new ArrayList<MTxt_Rule>();

	public void addRule(MTxt_Rule rule) {
		this.rules.add(rule);
	}

	public List<MTxt_Rule> getRules() {
		return rules;
	}

}
