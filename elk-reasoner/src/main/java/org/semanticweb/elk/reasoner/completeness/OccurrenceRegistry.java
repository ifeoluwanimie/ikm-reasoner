/*
 * This product is dual-licensed under Apache 2.0 License for two organizations due to forking.
 *
 * Copyright © 2023 Integrated Knowledge Management (support@ikm.dev)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ======================================================================
 *
 * Copyright © 2011 - 2023 Department of Computer Science, University of Oxford
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.semanticweb.elk.reasoner.completeness;



import java.util.EnumMap;

/**
 * An object that keeps track of the number occurrences of {@link Feature}s
 * 
 * @author Yevgeny Kazakov
 */
public class OccurrenceRegistry extends EnumMap<Feature, Integer>
		implements OccurrenceCounter, OccurrenceListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8085426088043106502L;

	public OccurrenceRegistry() {
		super(Feature.class);
	}

	@Override
	public int getOccurrenceCount(Feature occurrence) {
		Integer result = get(occurrence);
		if (result == null) {
			return 0;
		}
		// else
		return result;
	}

	@Override
	public void occurrenceChanged(Feature occurrence, int increment) {
		Integer noOccurrences = getOccurrenceCount(occurrence);
		noOccurrences += increment;
		if (noOccurrences == 0) {
			remove(occurrence);
		} else {
			put(occurrence, noOccurrences);
		}
	}

}