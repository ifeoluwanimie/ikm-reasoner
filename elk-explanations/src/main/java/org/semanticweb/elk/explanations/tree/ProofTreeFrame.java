/**
 * 
 */
package org.semanticweb.elk.explanations.tree;
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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.tree.TreePath;

import org.protege.editor.core.ui.util.InputVerificationStatusChangedListener;
import org.protege.editor.core.ui.util.VerifyingOptionPane;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.elk.explanations.editing.AxiomExpressionEditor;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapitools.proofs.expressions.OWLExpression;

/**
 * 
 * @author	Pavel Klinov
 * 			pavel.klinov@uni-ulm.de
 *
 */
public class ProofTreeFrame extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final Set<AxiomType<?>> EDITABLE_AXIOM_TYPES = new HashSet<AxiomType<?>>(Arrays.<AxiomType<?>>asList(AxiomType.SUBCLASS_OF, AxiomType.OBJECT_PROPERTY_DOMAIN, AxiomType.OBJECT_PROPERTY_RANGE));
	
	private final OWLEditorKit kit_;
	
	private final ProofTree tree_;
	
	private final MouseListener doubleClickListener_ = new MouseAdapter() {
		@Override
	    public void mousePressed(MouseEvent e) {
	        int selRow = tree_.getRowForLocation(e.getX(), e.getY());
	        
	        if(selRow != -1) {
	            if (e.getClickCount() == 2) {
	            	TreePath selPath = tree_.getPathForLocation(e.getX(), e.getY());
	            	Object node = selPath.getLastPathComponent();
	            	
	            	if (node instanceof OWLExpressionNode) {
	            		OWLExpressionNode exprNode = (OWLExpressionNode) node;
	            		
	            		if (editingEnabled(exprNode)) {
	            			showAxiomEditor(exprNode.getAxiom());
	            		}
	            	}
	            }
	        }
	    }

		private boolean editingEnabled(OWLExpressionNode node) {
			return node.isAsserted() && node.getAxiom().isOfType(EDITABLE_AXIOM_TYPES);
		}
	};
	
    public ProofTreeFrame(OWLEditorKit owlEditorKit, OWLExpression proofRoot) {
    	kit_ = owlEditorKit;
    	tree_ = new ProofTree(owlEditorKit, proofRoot);
        
    	setLayout(new BorderLayout());
    	tree_.setUI(new ProofTreeUI());
        add(tree_, BorderLayout.CENTER);

        tree_.setVisible(true);
        setVisible(true);
        // setting the listener for handling clicking events
        tree_.addMouseListener(doubleClickListener_);
    }
    
    private void showAxiomEditor(final OWLAxiom axiom) {
    	final AxiomExpressionEditor editor = new AxiomExpressionEditor(kit_);
        final JComponent editorComponent = editor.getEditorComponent();
        @SuppressWarnings("serial")
		final VerifyingOptionPane optionPane = new VerifyingOptionPane(editorComponent) {

            public void selectInitialValue() {
                // This is overriden so that the option pane dialog default
                // button doesn't get the focus.
            }
        };
        final InputVerificationStatusChangedListener verificationListener = new InputVerificationStatusChangedListener() {
            public void verifiedStatusChanged(boolean verified) {
                optionPane.setOKEnabled(verified);
            }
        };
        // Protege's syntax checkers only cover the class axiom's syntax
        editor.setEditedObject((OWLClassAxiom) axiom);
        // prevent the OK button from being available until the expression is syntactically valid
        editor.addStatusChangedListener(verificationListener);
        
        JDialog dlg = optionPane.createDialog(this, null);

        dlg.setModal(false);
        dlg.setResizable(true);
        dlg.pack();
        dlg.setLocationRelativeTo(this);
        dlg.addComponentListener(new ComponentAdapter() {

            public void componentHidden(ComponentEvent e) {
                Object retVal = optionPane.getValue();
                
                editorComponent.setPreferredSize(editorComponent.getSize());
                
                if (retVal != null && retVal.equals(JOptionPane.OK_OPTION)) {
                    handleEditFinished(axiom, editor.getEditedObject());
                }
                
                //setSelectedValue(frameObject, true);
                
                editor.removeStatusChangedListener(verificationListener);
                editor.dispose();
            }
        });

        dlg.setTitle("Class axiom expression editor");
        dlg.setVisible(true);
    }
    
	private void handleEditFinished(OWLAxiom oldAxiom, OWLAxiom newAxiom) {
		List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
		OWLOntology ontology = kit_.getOWLModelManager().getActiveOntology();
		// remove the old axiom
		changes.add(new RemoveAxiom(ontology, oldAxiom));
		changes.add(new AddAxiom(ontology, newAxiom));

		kit_.getOWLModelManager().applyChanges(changes);
	}
    
}
