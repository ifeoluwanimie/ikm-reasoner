package org.semanticweb.elk.matching.inferences;

import org.semanticweb.elk.matching.ElkMatchException;

/*
 * #%L
 * ELK Proofs Package
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2016 Department of Computer Science, University of Oxford
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.semanticweb.elk.matching.conclusions.ConclusionMatchExpressionFactory;
import org.semanticweb.elk.matching.conclusions.ForwardLinkMatch2;
import org.semanticweb.elk.matching.conclusions.ForwardLinkMatch2Watch;
import org.semanticweb.elk.matching.conclusions.SubClassInclusionDecomposedMatch2;
import org.semanticweb.elk.matching.root.IndexedContextRootMatch;
import org.semanticweb.elk.owl.interfaces.ElkObjectHasSelf;

public class ForwardLinkOfObjectHasSelfMatch2
		extends AbstractInferenceMatch<ForwardLinkOfObjectHasSelfMatch1>
		implements ForwardLinkMatch2Watch {

	private final IndexedContextRootMatch extendedOriginMatch_;

	ForwardLinkOfObjectHasSelfMatch2(ForwardLinkOfObjectHasSelfMatch1 parent,
			SubClassInclusionDecomposedMatch2 premiseMatch) {
		super(parent);
		ElkObjectHasSelf existential = premiseMatch
				.getSubsumerIndexedObjectHasSelfMatch();
		if (!parent.getForwardRelationMatch()
				.equals(existential.getProperty())) {
			throw new ElkMatchException(
					parent.getParent().getDecomposedExistential(), existential);
		}
		this.extendedOriginMatch_ = premiseMatch.getExtendedDestinationMatch();
		checkEquals(premiseMatch, getPremiseMatch(DEBUG_FACTORY));
	}

	public IndexedContextRootMatch getExtendedOriginMatch() {
		return extendedOriginMatch_;
	}

	SubClassInclusionDecomposedMatch2 getPremiseMatch(
			ConclusionMatchExpressionFactory factory) {
		return factory.getSubClassInclusionDecomposedMatch2(
				getParent().getPremiseMatch(factory), getExtendedOriginMatch(),
				factory.getObjectHasSelf(
						getParent().getForwardRelationMatch()));
	}

	public ForwardLinkMatch2 getConclusionMatch(
			ConclusionMatchExpressionFactory factory) {
		return factory.getForwardLinkMatch2(
				getParent().getConclusionMatch(factory),
				getExtendedOriginMatch());
	}

	@Override
	public <O> O accept(InferenceMatch.Visitor<O> visitor) {
		return visitor.visit(this);
	}

	@Override
	public <O> O accept(ForwardLinkMatch2Watch.Visitor<O> visitor) {
		return visitor.visit(this);
	}

	/**
	 * The visitor pattern for instances
	 * 
	 * @author Yevgeny Kazakov
	 *
	 * @param <O>
	 *            the type of the output
	 */
	public interface Visitor<O> {

		O visit(ForwardLinkOfObjectHasSelfMatch2 inferenceMatch2);

	}

	/**
	 * A factory for creating instances
	 * 
	 * @author Yevgeny Kazakov
	 *
	 */
	public interface Factory {

		ForwardLinkOfObjectHasSelfMatch2 getForwardLinkOfObjectHasSelfMatch2(
				ForwardLinkOfObjectHasSelfMatch1 parent,
				SubClassInclusionDecomposedMatch2 premiseMatch);

	}

}
