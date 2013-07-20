package org.marl.wafnm.core.impl;

import org.marl.wafnm.core.api.IFrame;
import org.marl.wafnm.core.api.IFrameFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/** Produces simple in-memory frames, with
 * base inference capabilities.
 * 
 * @author kr1s
 *
 */
public class MemFrameFactory implements IFrameFactory {
	
	/** The Jena ontology specification corresponding to the default
	 * inference level for models created by this factory.
	 * 
	 */
	public static final OntModelSpec DEFAULT_INFERENCE 
		= OntModelSpec.OWL_MEM_RDFS_INF;
	
	MemFrameFactory() {}
	
	@Override
	public IFrame createFrame(String uri, Model baseOntology)
	{
		OntModel frameModel = ModelFactory.createOntologyModel(
				DEFAULT_INFERENCE, 
				baseOntology);
		
		return new BaseFrame(uri, frameModel);
	}

}
