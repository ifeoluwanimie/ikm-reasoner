package org.semanticweb.elk.explanations;
/*
 * #%L
 * Explanation Workbench
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2011 - 2014 Department of Computer Science, University of Oxford
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

import java.awt.BorderLayout;

import org.protege.editor.owl.ui.explanation.ExplanationResult;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/03/2012
 */
public class WorkbenchPanelExplanationResult extends ExplanationResult {

    private ProofWorkbenchPanel workbenchPanel;

    public WorkbenchPanelExplanationResult(ProofWorkbenchPanel workbenchPanel) {
        this.workbenchPanel = workbenchPanel;
        setLayout(new BorderLayout());
        add(workbenchPanel);
    }

    @Override
    public void dispose() {
        workbenchPanel.dispose();
    }
}
