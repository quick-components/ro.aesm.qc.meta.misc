package ro.aesm.qc.meta.misc.txt;

import org.osgi.service.component.annotations.Component;
import org.w3c.dom.Node;

import ro.aesm.qc.api.meta.component.IMetaComponentModel;
import ro.aesm.qc.api.meta.component.IMetaComponentParser;
import ro.aesm.qc.base.AbstractMetaParser;
import ro.aesm.qc.meta.misc.txt.model.MTxt_KeepRule;
import ro.aesm.qc.meta.misc.txt.model.MTxt_ReplaceBetweenRule;
import ro.aesm.qc.meta.misc.txt.model.MTxt_ReplaceRule;
import ro.aesm.qc.meta.misc.txt.model.MTxt_Rule;

@Component(service = IMetaComponentParser.class)
public class TxtParser extends AbstractMetaParser {

	public TxtParser() {
		super();
		this.getNamespaceMap().put(ConstantsTxt.XMLNS_ALIAS, ConstantsTxt.XMLNS);
		this.getNamespaceMap().put("", "*");
	}

	@Override
	public String getTag() {
		return ConstantsTxt.TAG;
	}

	@Override
	public IMetaComponentModel parse(Node node) {
		String name = this.getAttr(node, "name").trim();
		TxtModel mm = new TxtModel(name);
		for (Node ruleNode : this.getNodes(node, "*")) {
			MTxt_Rule _mm = this.parseRule(ruleNode);
			if (_mm != null) {
				mm.addRule(_mm);
			}
		}
		return mm;
	}

	/**
	 * 
	 * @param node
	 * @return
	 */
	protected MTxt_Rule parseRule(Node node) {
		String tag = node.getLocalName();
		if (tag.equals(MTxt_KeepRule.TAG)) {
			return this.parseKeepRule(node);
		} else if (tag.equals(MTxt_ReplaceRule.TAG)) {
			return this.parseReplaceRule(node);
		} else if (tag.equals(MTxt_ReplaceBetweenRule.TAG)) {
			return this.parseReplaceBetweenRule(node);
		}
		return null;
	}

	/**
	 * Parse replace rule
	 * 
	 * @param node
	 * @return
	 */
	protected MTxt_ReplaceRule parseReplaceRule(Node node) {
		MTxt_ReplaceRule rule = new MTxt_ReplaceRule();
		String what = this.getValue(node, "what", null);
		if (what == null) {
			what = this.getAttr(node, "what");
		}
		rule.setWhat(what);
		String with = this.getValue(node, "with", null);
		if (with == null) {
			with = this.getAttr(node, "with");
		}
		rule.setWith(with);

		String occurrence = this.getAttr(node, "occurrence", "1");
		rule.setOccurence(occurrence);
		String index = this.getAttr(node, "index", null);
		rule.setIndex(index);
		return rule;
	}

	/**
	 * Parse replace between rule.
	 * 
	 * @param node
	 * @return
	 */
	protected MTxt_ReplaceBetweenRule parseReplaceBetweenRule(Node node) {
		MTxt_ReplaceBetweenRule rule = new MTxt_ReplaceBetweenRule();
		String from = this.getValue(node, "from", "");
		if (from.equals("")) {
			from = this.getAttr(node, "from");
		}
		rule.setFrom(from);
		String to = this.getValue(node, "to", "");
		if (to.equals("")) {
			to = this.getAttr(node, "to");
		}
		rule.setTo(to);
		String with = this.getValue(node, "with", "");
		if (with.equals("")) {
			with = this.getAttr(node, "with");
		}
		rule.setWith(with);

		rule.setFromOffset(this.getAttrAsInt(node, "from_offset"));
		rule.setToOffset(this.getAttrAsInt(node, "to_offset"));

		String occurrence = this.getAttr(node, "occurrence", "1");
		rule.setOccurence(occurrence);
		return rule;
	}

	/**
	 * Parse keep rule
	 * 
	 * @param node
	 * @return
	 */
	protected MTxt_KeepRule parseKeepRule(Node node) {
		MTxt_KeepRule rule = new MTxt_KeepRule();
		String from = this.getValue(node, "from", null);
		if (from == null) {
			from = this.getAttr(node, "from");
		}
		rule.setFrom(from);
		String to = this.getValue(node, "to", null);
		if (to == null) {
			to = this.getAttr(node, "to");
		}
		rule.setTo(to);

		rule.setFromOffset(this.getAttrAsInt(node, "from_offset"));
		rule.setToOffset(this.getAttrAsInt(node, "to_offset"));

		rule.setSeparator(this.getAttr(node, "separator", ""));

		String occurrence = this.getAttr(node, "occurrence", "1");
		rule.setOccurence(occurrence);

		String index = this.getAttr(node, "index", null);
		rule.setIndex(index);

		return rule;
	}

}
