/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.kenyacore.metadata.handler.impl;

import org.openmrs.Program;
import org.openmrs.annotation.Handler;
import org.openmrs.api.ProgramWorkflowService;
import org.openmrs.module.kenyacore.metadata.handler.ObjectDeployHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Deployment handler for programs
 */
@Handler(supports = { Program.class })
public class ProgramDeployHandler implements ObjectDeployHandler<Program> {

	@Autowired
	@Qualifier("programWorkflowService")
	private ProgramWorkflowService programService;

	/**
	 * @see ObjectDeployHandler#getIdentifier(org.openmrs.OpenmrsObject)
	 */
	@Override
	public String getIdentifier(Program obj) {
		return obj.getUuid();
	}

	/**
	 * @see org.openmrs.module.kenyacore.metadata.handler.ObjectDeployHandler#fetch(String)
	 */
	@Override
	public Program fetch(String uuid) {
		return programService.getProgramByUuid(uuid);
	}

	/**
	 * @see org.openmrs.module.kenyacore.metadata.handler.ObjectDeployHandler#save(org.openmrs.OpenmrsObject)
	 */
	@Override
	public Program save(Program obj) {
		return programService.saveProgram(obj);
	}

	/**
	 * @see org.openmrs.module.kenyacore.metadata.handler.ObjectDeployHandler#findAlternateMatch(org.openmrs.OpenmrsObject)
	 */
	@Override
	public Program findAlternateMatch(Program incoming) {
		// In 1.9.x getProgramByName incorrectly looks at concept name (TRUNK-3504)
		for (Program p : programService.getAllPrograms(true)) {
			if (p.getName().equals(incoming.getName())) {
				return p;
			}
		}
		return null;
	}

	/**
	 * @see org.openmrs.module.kenyacore.metadata.handler.ObjectDeployHandler#remove(org.openmrs.OpenmrsObject, String)
	 * @param obj the object to remove
	 */
	@Override
	public void remove(Program obj, String reason) {
		programService.retireProgram(obj);
	}
}