package org.marl.wafnm.core.impl;

import org.marl.wafnm.core.api.IFrame;
import org.marl.wafnm.core.api.IFrameManager;

import com.hp.hpl.jena.ontology.OntModel;

/** Implements a simple frame.
 * 
 *  <p>The {@link IFrameManager <i>frame manager</i>} dependency
 *  should later be initialized via {@link #setFrameManager(IFrameManager)}.
 * 
 * @author kr1s
 *
 */
public class BaseFrame extends AbstractFrame implements IFrame {

	/** Constructor that initialize state.
	 * 
	 * @param frameUri
	 * @param frameModel
	 */
	BaseFrame(String frameUri, OntModel frameModel)
	{
		super(frameUri, frameModel);
	}

	/** Initialize the frame manager dependency.
	 * 
	 * @param frameManager A frame manager.
	 * 
	 * @throws IllegalStateException if the frame manager has already been 
	 * initialized.
	 */
	public void setFrameManager(IFrameManager frameManager) 
	{
		if (this.frameManager != null) {
			throw new IllegalStateException("FrameManager should be set only once");
		}
		
		this.frameManager = frameManager;
	}
}
