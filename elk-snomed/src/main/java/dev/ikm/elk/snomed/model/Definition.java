package dev.ikm.elk.snomed.model;

/*-
 * #%L
 * ELK Integration with SNOMED
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

import java.util.HashSet;
import java.util.Set;

public class Definition {

	private DefinitionType definitionType;

	private Set<Concept> superConcepts = new HashSet<>();

	private RoleGroup ungroupedRoles = new RoleGroup();

	private Set<RoleGroup> roleGroups = new HashSet<>();

	public DefinitionType getDefinitionType() {
		return definitionType;
	}

	public void setDefinitionType(DefinitionType definitionType) {
		this.definitionType = definitionType;
	}

	public Set<Concept> getSuperConcepts() {
		return superConcepts;
	}

	public void addSuperConcept(Concept superConcept) {
		this.superConcepts.add(superConcept);
	}

	public Set<Role> getUngroupedRoles() {
		return ungroupedRoles.getRoles();
	}

	public void addUngroupedRole(Role role) {
		this.ungroupedRoles.addRole(role);
	}

	public Set<RoleGroup> getRoleGroups() {
		return roleGroups;
	}

	public void addRoleGroup(RoleGroup roleGroup) {
		this.roleGroups.add(roleGroup);
	}

}