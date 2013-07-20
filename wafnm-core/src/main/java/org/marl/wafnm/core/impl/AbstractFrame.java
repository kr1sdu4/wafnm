package org.marl.wafnm.core.impl;

import java.util.ArrayList;
import java.util.List;

import org.marl.wafnm.core.api.IFrame;
import org.marl.wafnm.core.api.IFrameManager;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/** Base implementation for frames.
 * 
 * <p>This is abstract as it does not provide any mechanism to initialize the
 * {@link IFrameManager <i>frame manager</i>} dependency. 
 * 
 * @author kr1s
 *
 */
public abstract class AbstractFrame implements IFrame {

	/////////////////////////////////////////////////////////////////////////////////////////////
	// State
	protected String frameUri;
	protected OntModel frameModel;
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	// Dependencies
	protected IFrameManager frameManager;
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	// Enforce implementation to provide a mechanism to initialize dependencies.
	protected AbstractFrame(String frameUri, OntModel frameModel)
	{
		if ( (frameUri == null) ||(frameModel == null)  )
		{
			throw new IllegalArgumentException();
		}
		
		this.frameUri = frameUri;
		this.frameModel = frameModel;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	// 															IFrame
	/////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public String getURI() {
		return frameUri;
	}

	@Override
	public Model getModel() 
	{
		return frameModel;
	}

	@Override
	public long size() {
		return frameModel.size();
	}

	@Override
	public Resource find(String semUri) {
		if (knows(semUri)) {
			return frameModel.getResource(semUri);
		}
		return null;
	}

	@Override
	public long bindKnowledge(String frameUri) 
	{
		long n0 = frameModel.size();
		IFrame otherFrame = frameManager.getFrame(frameUri);
		
		if (otherFrame != null) {
			frameModel.addSubModel(otherFrame.getModel());
		}
		
		return (frameModel.size() - n0);
	}

	@Override
	public long unbindKnowledge(String frameUri) 
	{
		long n0 = frameModel.size();
		IFrame otherFrame = frameManager.getFrame(frameUri);
		
		if (otherFrame != null) {
			frameModel.removeSubModel(otherFrame.getModel());
		}
		
		return (n0 - frameModel.size());
	}

	@Override
	public boolean knows(String semUri) {
		return frameModel.containsResource(frameModel.getResource(semUri));
	}

	@Override
	public List<Statement> resolveSem(String semUri) 
	{
		List<Statement> smallWorld = new ArrayList<Statement>();
		
		if (knows(semUri)) 
		{
			StmtIterator iterOnProps = frameModel.listStatements(
					frameModel.getResource(semUri),
					null,
					(RDFNode) null);
			smallWorld.addAll(iterOnProps.toList());
			
			iterOnProps = frameModel.listStatements(
					null,
					null,
					frameModel.getResource(semUri));
			smallWorld.addAll(iterOnProps.toList());
		}
		
		return smallWorld;
	}

	@Override
	public List<Statement> resolveSmallWorld(String query) 
	{
		//FIXME: not implemented
		List<Statement> smallWorld = new ArrayList<Statement>();
		return smallWorld;
	}

	@Override
	public String createSem(String semTypeUri, 
			String label,
			String comment, 
			String lang)
	{
		String semUri = FrameUtils.generateUri(frameUri);  
		OntResource sem = frameModel.createOntResource(semUri);
		sem.setRDFType(frameModel.getResource(semTypeUri));
		sem.addLabel(label, lang);
		sem.addComment(comment, lang);
		
		return sem.getURI();
	}

	@Override
	public long dropSem(String semUri) 
	{
		long n0 = frameModel.size();
		
		if (knows(semUri)) {
			frameModel.removeAll(frameModel.getResource(semUri), null, null);
		}
		
		return (n0 - frameModel.size());
	}

	@Override
	public long learn(Model someKnowledge) 
	{
		long n0 = frameModel.size();
		frameModel.add(someKnowledge);
		
		return (frameModel.size() - n0);
	}

	@Override
	public long learn(String subjectSemUri,
			String propositionTypeUri,
			String objectSemUri) 
	{
		long n0 = frameModel.size();
		
		frameModel.add(frameModel.getResource(subjectSemUri),
				frameModel.getProperty(propositionTypeUri),
				frameModel.getResource(objectSemUri) );
		
		return (frameModel.size() - n0);
	}

	@Override
	public long forget(Model someKnowledge) 
	{
		long n0 = frameModel.size();
		
		frameModel.removeSubModel(someKnowledge);
		
		return (n0 -frameModel.size());
	}

	@Override
	public long forget(String subjectSemUri,
			String propositionTypeUri,
			String objectSemUri) 
	{
		long n0 = frameModel.size();
		
		Resource subject = (subjectSemUri != null) && knows(subjectSemUri)  ? 
				 frameModel.getResource(subjectSemUri)
						: null;
		
		Resource object = (objectSemUri != null) && knows(objectSemUri)  ?
				 frameModel.getResource(objectSemUri)
						: null;
		
		Property predicate = propositionTypeUri != null ?  
				frameModel.getProperty(propositionTypeUri) : null;
				
		frameModel.remove(frameModel.createStatement(subject, 
				predicate, 
				object) );
		
		return (n0 - frameModel.size());
	}

	/////////////////////////////////////////////////////////////////////////////////////////////
	// 															POJO
	/////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public int hashCode() 
	{
		return getURI().hashCode();
	}

	@Override
	public boolean equals(Object obj) 
	{
		if (obj != null && obj instanceof IFrame)
		{
			return (obj.hashCode() == hashCode());
		}
		return false;
	}

	@Override
	public String toString() {
		return new StringBuffer(getURI()).append(" (").append(size()).append(")")
				.toString();
	}

}
