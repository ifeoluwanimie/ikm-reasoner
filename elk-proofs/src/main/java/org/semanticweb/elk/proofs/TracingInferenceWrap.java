package org.semanticweb.elk.proofs;

/*-
 * #%L
 * ELK Proofs Package
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2017 Department of Computer Science, University of Oxford
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

import java.util.List;

import org.liveontologies.puli.Delegator;
import org.liveontologies.puli.Inference;
import org.semanticweb.elk.reasoner.tracing.TracingInference;

import com.google.common.base.Functions;
import com.google.common.collect.Lists;

class TracingInferenceWrap extends Delegator<TracingInference>
		implements Inference<Object> {

	public TracingInferenceWrap(final TracingInference inference) {
		super(inference);
	}

	@Override
	public String getName() {
		return getDelegate().getName();
	}

	@Override
	public Object getConclusion() {
		return getDelegate().getConclusion();
	}

	@Override
	public List<? extends Object> getPremises() {
		return Lists.transform(getDelegate().getPremises(),
				Functions.identity());
	}

}
