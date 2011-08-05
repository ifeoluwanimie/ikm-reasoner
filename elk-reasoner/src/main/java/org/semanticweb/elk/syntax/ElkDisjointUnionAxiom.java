package org.semanticweb.elk.syntax;

import java.util.ArrayList;
import java.util.List;

/**
 * Corresponds to an <a href=
 * "http://www.w3.org/TR/owl2-syntax/#Disjoint_Union_of_Class_Expressions"
 * >Disjoint Union of Class Expressions Axiom<a> in the OWL 2 specification.
 * 
 * @author Markus Kroetzsch
 */
public class ElkDisjointUnionAxiom extends ElkClassAxiom {

	private static final int constructorHash_ = "ElkDisjointUnionAxiom"
			.hashCode();

	protected final List<? extends ElkClassExpression> disjointClassExpressions;

	private ElkDisjointUnionAxiom(
			List<? extends ElkClassExpression> disjointClassExpressions) {
		this.disjointClassExpressions = disjointClassExpressions;
		this.structuralHashCode = ElkObject.computeCompositeHash(
				constructorHash_, disjointClassExpressions);
	}

	public static ElkDisjointUnionAxiom create(
			List<? extends ElkClassExpression> disjointClassExpressions) {
		return (ElkDisjointUnionAxiom) factory.put(new ElkDisjointUnionAxiom(
				disjointClassExpressions));
	}

	public static ElkDisjointUnionAxiom create(
			ElkClassExpression firstClassExpression,
			ElkClassExpression secondClassExpression,
			ElkClassExpression... otherClassExpressions) {
		List<ElkClassExpression> classExpressions = new ArrayList<ElkClassExpression>(
				2 + otherClassExpressions.length);
		classExpressions.add(firstClassExpression);
		classExpressions.add(secondClassExpression);
		for (int i = 0; i < otherClassExpressions.length; i++)
			classExpressions.add(otherClassExpressions[i]);
		return (ElkDisjointUnionAxiom) factory.put(new ElkDisjointUnionAxiom(
				classExpressions));
	}

	public List<? extends ElkClassExpression> getDisjointClassExpressions() {
		return disjointClassExpressions;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("DisjointUnion(");
		for (ElkClassExpression ce : disjointClassExpressions) {
			result.append(ce.toString());
			result.append(" ");
		}
		result.setCharAt(result.length() - 1, ')');
		return result.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.elk.reasoner.ElkObject#structuralEquals(java.lang.Object)
	 */
	@Override
	public boolean structuralEquals(ElkObject object) {
		if (this == object) {
			return true;
		} else if (object instanceof ElkDisjointUnionAxiom) {
			return disjointClassExpressions
					.equals(((ElkDisjointUnionAxiom) object).disjointClassExpressions);
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.semanticweb.elk.reasoner.ELKClassAxiom#accept(org.semanticweb.elk
	 * .reasoner.ELKClassAxiomVisitor)
	 */
	@Override
	public <O> O accept(ElkClassAxiomVisitor<O> visitor) {
		return visitor.visit(this);
	}
}