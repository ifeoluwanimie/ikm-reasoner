/*
 * #%L
 * ELK OWL API Binding
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
package org.semanticweb.elk.owlapi.wrapper;

import java.util.List;

import org.semanticweb.elk.owl.interfaces.ElkObjectPropertyExpression;
import org.semanticweb.elk.owl.interfaces.ElkSubObjectPropertyExpression;
import org.semanticweb.elk.owl.interfaces.ElkSubObjectPropertyOfAxiom;
import org.semanticweb.elk.owl.visitors.ElkObjectPropertyAxiomVisitor;
import org.semanticweb.elk.owl.visitors.ElkSubObjectPropertyOfAxiomVisitor;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLSubPropertyChainOfAxiom;

/**
 * Implements the {@link ElkSubObjectPropertyOfAxiom} interface by wrapping
 * instances of {@link OWLSubPropertyChainOfAxiom}.
 * 
 * @author Yevgeny Kazakov
 * 
 * @param <T>
 *            the type of the wrapped object
 */
public class ElkSubObjectPropertyChainOfAxiomWrap<T extends OWLSubPropertyChainOfAxiom>
		extends ElkObjectPropertyAxiomWrap<T>
		implements ElkSubObjectPropertyOfAxiom {

	public ElkSubObjectPropertyChainOfAxiomWrap(T owlSubPropertyChainOfAxiom) {
		super(owlSubPropertyChainOfAxiom);
	}

	@Override
	public ElkSubObjectPropertyExpression getSubObjectPropertyExpression() {
		return new ElkObjectPropertyChainWrap<List<? extends OWLObjectPropertyExpression>>(
				this.owlObject.getPropertyChain());
	}

	@Override
	public ElkObjectPropertyExpression getSuperObjectPropertyExpression() {
		return converter.convert(this.owlObject.getSuperProperty());
	}

	@Override
	public <O> O accept(ElkObjectPropertyAxiomVisitor<O> visitor) {
		return accept((ElkSubObjectPropertyOfAxiomVisitor<O>) visitor);
	}

	@Override
	public <O> O accept(ElkSubObjectPropertyOfAxiomVisitor<O> visitor) {
		return visitor.visit(this);
	}

}
