package dev.ikm.elk.snomed.reasoner;

/*-
 * #%L
 * ELK Reasoner for SNOMED
 * %%
 * Copyright (C) 2023 Integrated Knowledge Management
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

import java.util.Set;

import org.semanticweb.elk.owl.interfaces.ElkAxiom;
import org.semanticweb.elk.owl.interfaces.ElkObject;

public class OwlConverter {

	public static OwlConverter getInstance() {
		// TODO Auto-generated method stub
		return null;
	}

	public <C extends ElkObject> C convert(C obj) {
		return obj;
	}

	public Iterable<? extends ElkAxiom> convertAxiomSet(Set<? extends ElkAxiom> owlAxioms) {
		return owlAxioms;
	}

	public boolean isRelevantAxiom(ElkAxiom axiom) {
		return true;
	}

}