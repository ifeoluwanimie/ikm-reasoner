/*
 * #%L
 * ELK Reasoner
 * 
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2011 Department of Computer Science, University of Oxford
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
package org.semanticweb.elk.reasoner.indexing.hierarchy;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.semanticweb.elk.owl.interfaces.ElkClass;
import org.semanticweb.elk.owl.interfaces.ElkNamedIndividual;
import org.semanticweb.elk.owl.predefined.PredefinedElkClass;
import org.semanticweb.elk.reasoner.indexing.OntologyIndex;
import org.semanticweb.elk.reasoner.saturation.rules.contextinit.ChainableContextInitRule;
import org.semanticweb.elk.reasoner.saturation.rules.contextinit.LinkedContextInitRule;
import org.semanticweb.elk.reasoner.saturation.rules.contextinit.RootContextInitializationRule;
import org.semanticweb.elk.reasoner.saturation.rules.subsumers.ChainableSubsumerRule;
import org.semanticweb.elk.util.collections.ArrayHashSet;
import org.semanticweb.elk.util.collections.Operations;
import org.semanticweb.elk.util.collections.chains.AbstractChain;
import org.semanticweb.elk.util.collections.chains.Chain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 */
public class DirectIndex implements ModifiableOntologyIndex {

	protected static final Logger LOGGER_ = LoggerFactory
			.getLogger(DirectIndex.class);

	final IndexedClass indexedOwlThing, indexedOwlNothing;

	final IndexedObjectCache objectCache;
	private ChainableContextInitRule contextInitRules_;

	private final Set<IndexedObjectProperty> reflexiveObjectProperties_;

	public DirectIndex(IndexedObjectCache objectCache) {
		this.objectCache = objectCache;

		// the context root initialization rule is always registered
		RootContextInitializationRule.addRuleFor(this);

		// index predefined entities
		MainAxiomIndexerVisitor tmpAxiomInserter = new MainAxiomIndexerVisitor(
				this, true);
		// TODO: what to do if someone tries to delete them?
		this.indexedOwlThing = tmpAxiomInserter
				.indexClassDeclaration(PredefinedElkClass.OWL_THING);
		this.indexedOwlNothing = tmpAxiomInserter
				.indexClassDeclaration(PredefinedElkClass.OWL_NOTHING);

		this.reflexiveObjectProperties_ = new ArrayHashSet<IndexedObjectProperty>(
				64);
	}

	public DirectIndex() {
		this(new IndexedObjectCache());
	}

	/* read-only methods required by the interface */

	@Override
	public LinkedContextInitRule getContextInitRuleHead() {
		return contextInitRules_;
	}

	@Override
	public Collection<IndexedClassExpression> getIndexedClassExpressions() {
		return objectCache.indexedClassExpressionLookup;
	}

	@Override
	public Collection<IndexedClass> getIndexedClasses() {
		return new AbstractCollection<IndexedClass>() {
			@Override
			public Iterator<IndexedClass> iterator() {
				return Operations.filter(getIndexedClassExpressions(),
						IndexedClass.class).iterator();
			}

			@Override
			public int size() {
				return objectCache.indexedClassCount;
			}
		};
	}

	@Override
	public Collection<IndexedIndividual> getIndexedIndividuals() {
		return new AbstractCollection<IndexedIndividual>() {

			@Override
			public Iterator<IndexedIndividual> iterator() {
				return Operations.filter(getIndexedClassExpressions(),
						IndexedIndividual.class).iterator();
			}

			@Override
			public int size() {
				return objectCache.indexedIndividualCount;
			}

		};
	}

	@Override
	public Collection<IndexedPropertyChain> getIndexedPropertyChains() {
		return objectCache.indexedPropertyChainLookup;
	}

	@Override
	public Collection<IndexedObjectProperty> getIndexedObjectProperties() {
		return new AbstractCollection<IndexedObjectProperty>() {

			@Override
			public Iterator<IndexedObjectProperty> iterator() {
				return Operations.filter(getIndexedPropertyChains(),
						IndexedObjectProperty.class).iterator();
			}

			@Override
			public int size() {
				return objectCache.indexedObjectPropertyCount;
			}
		};
	}

	@Override
	public Collection<IndexedObjectProperty> getReflexiveObjectProperties() {
		return Collections.unmodifiableCollection(reflexiveObjectProperties_);
	}

	@Override
	public IndexedClass getIndexedOwlThing() {
		return indexedOwlThing;
	}

	@Override
	public IndexedClass getIndexedOwlNothing() {
		return indexedOwlNothing;
	}

	/* read-write methods required by the interface */

	@Override
	public IndexedObjectCache getIndexedObjectCache() {
		return this.objectCache;
	}

	@Override
	public void addClass(ElkClass newClass) {
		// we do not rack signature changes
	}

	@Override
	public void removeClass(ElkClass oldClass) {
		// we do not rack signature changes
	}

	@Override
	public void addNamedIndividual(ElkNamedIndividual newIndividual) {
		// we do not rack signature changes
	}

	@Override
	public void removeNamedIndividual(ElkNamedIndividual oldIndividual) {
		// we do not rack signature changes
	}

	@Override
	public void addContextInitRule(ChainableContextInitRule newRule) {
		newRule.addTo(getContextInitRuleChain());
	}

	@Override
	public void removeContextInitRule(ChainableContextInitRule oldRule) {
		if (!oldRule.removeFrom(getContextInitRuleChain()))
			throw new ElkUnexpectedIndexingException(
					"Cannot remove context initialization rule "
							+ oldRule.getName());
	}

	@Override
	public void add(IndexedClassExpression target, ChainableSubsumerRule rule) {
		rule.addTo(target.getCompositionRuleChain());
	}

	@Override
	public void remove(IndexedClassExpression target, ChainableSubsumerRule rule) {
		if (!rule.removeFrom(target.getCompositionRuleChain()))
			throw new ElkUnexpectedIndexingException(
					"Cannot remove composition rule " + rule.getName()
							+ " for " + target);
	}

	@Override
	public void add(IndexedObject newObject) {
		newObject.accept(objectCache.inserter);
	}

	@Override
	public void remove(IndexedObject oldObject) {
		if (!oldObject.accept(objectCache.deletor))
			throw new ElkUnexpectedIndexingException(
					"Cannot remove indexed object from the cache " + oldObject);
//		if (oldObject instanceof IndexedClassExpression) {
//			IndexedClassExpression ice = (IndexedClassExpression) oldObject;
//			Context context = ice.getContext();
//			if (context != null)
//				context.removeLinks();
//		}
	}

	@Override
	public void addReflexiveProperty(IndexedObjectProperty property) {
		reflexiveObjectProperties_.add(property);
	}

	@Override
	public void removeReflexiveProperty(IndexedObjectProperty property) {
		if (!reflexiveObjectProperties_.remove(property))
			throw new ElkUnexpectedIndexingException(
					"Cannot remove reflexivity of object property " + property);
	}

	/* class-specific methods */

	/**
	 * @return a {@link Chain} view of context initialization rules assigned to
	 *         this {@link OntologyIndex}; it can be used for inserting new
	 *         rules or deleting existing ones
	 */
	public Chain<ChainableContextInitRule> getContextInitRuleChain() {
		return new AbstractChain<ChainableContextInitRule>() {

			@Override
			public ChainableContextInitRule next() {
				return contextInitRules_;
			}

			@Override
			public void setNext(ChainableContextInitRule tail) {
				contextInitRules_ = tail;
			}
		};
	}

}
