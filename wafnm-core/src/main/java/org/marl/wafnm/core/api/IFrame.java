package org.marl.wafnm.core.api;

import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

/** A <i>frame</i> is a graph of relations between semantic individuals 
 * (<i>sem</i>).
 * 
 * <p>A relation between two sem is known as a <i>proposition</i>.
 * 
 * <p> A frame may contain:
 * <ul>
 * <li>sem defined through their taxonomy groups, and identified by their
 * URIs</li>
 * <li>included propositions, which this frame manages</li>
 * <li>referenced propositions, which this frame knows about, but do
 * not manage</li>
 * </ul>
 * 
 * <p>The last point above states that all referenced frames content should be
 * immutable through this interface.
 * 
 * <p>A frame is identified through an URI.
 * 
 * <p>A frame is responsible for managing a namespace in which each sem
 * has a unique URI.
 * 
 * <p>This interface does not define any checked exception, but implementation 
 * of most of these API calls my throw:
 * <ul>
 * <li>an <code>IllegalArgumentException</code>, when the processing fails
 * due to invalid arguments</li>
 * <li>an <code>IllegalStateException</code>, when the frame belongs to an
 * internal state that is incompatible with the requested operation</li>
 * </ul>
 * 
 * @author kr1s
 */
public interface IFrame {

	/** Answers the unique identifier for this frame.
	 * 
	 * @return This frame URI.
	 */
	public String getURI() ;
	
	/** Answers this frame graph.
	 * 
	 * <p>The frame client should privilege the rest of the API whenever possible.
	 *
	 * @return This frame model as an Apache Jena ontology model.
	 */
	public Model getModel() ;
	
	/** Answers the count of all propositions known by this frame, including
	 * taxonomy statements and referenced frames.
	 * 
	 * @return The number of statements in this graph.
	 */
	public long size() ;
	
	/** Answers a sem identified by its URI.
	 * 
	 * @param semUri The sem URI.
	 * 
	 * @return The sem as an Apache Jena ontology resource, or
	 * <code>null</code> if this frame is unaware of this sem.
	 */
	public Resource find(String semUri) ;

	/** Let this frame know about propositions managed by another frame.
	 * 
	 * <p>The frame may later be  unbound via the
	 * {@link #unbindKnowledge(String)} call.
	 * 
	 * @param frameUri  The URI of the referenced frame.
	 * 
	 * @return The count of learned propositions.
	 */
	public long bindKnowledge(String frameUri) ;

	/** Let this frame forget some external propositions.
	 * 
	 * <p>The corresponding frame might have been previously bound via the
	 * {@link #bindKnowledge(String)} call.
	 * 
	 * @param frameUri The URI of the referenced frame.
	 * 
	 * @return The count of forgotten statements.
	 */
	public long unbindKnowledge(String frameUri) ;

	/** Answers whether this frame "knows" a given sem.
	 * 
	 * @param semUri The sem URI.
	 * 
	 * @return <code>true</code> when this frame defines a resource with the
	 * given URI, and that belongs to some taxonomy group, <code>false</code>
	 * otherwise. 
	 */
	public boolean knows(String semUri);
	
	/** Answers all known propositions  that accept a given sem as subject 
	 * or object.
	 * 
	 * @param semUri The sem URI.
	 * 
	 * @return A mini-graph centered on a the requested sem.
	 */
	public List<Statement> resolveSem(String semUri);
	
	/** Answers all known propositions  that belongs to the "small world"
	 * defined by the requested query.
	 * 
	 * <p>This interface neither define the query syntax, nor the small world
	 * construction semantic.
	 * 
	 * @param query A query describing a small world.
	 * 
	 * @return The resolved small world.
	 */
	public List<Statement> resolveSmallWorld(String query);
	
	/** Creates a new sem within this frame.
	 * 
	 * <p>The frame is responsible for generating the sem URI.
	 * 
	 * @param typeUri The URI of the privileged taxonomy group of this sem.
	 * @param label The base name of this sem.
	 * @param comment A brief description.
	 * @param lang The language for the above properties. 
	 * If <code>null</code>, defaults to <code>en</code>.
	 * 
	 * @return The created sem URI.
	 */
	public String createSem(String typeUri,
			String label,
			String comment, 
			String lang) ;

	/** Removes all propositions that accept a given sem as either subject or object.
	 * 
	 * @param semUri The requested sem URI.
	 * 
	 * @return The count of forgotten statements.
	 */
	public long dropSem(String semUri);

	/** Let this frame learn a set of propositions.
	 * 
	 * <p>The propositions are not referenced, but imported into this
	 * frame, which then manages the copies.
	 * 
	 * @param someKnowledge An Apache Jena model containing the propositions
	 * to learn.
	 * 
	 * @return The count of learned statements.
	 */
	public long learn(Model someKnowledge);
	
	/** Let this frame learn a proposition.
	 * 
	 * @param subjectSemUri The subject URI.
	 * @param propositionTypeUri The proposition kind.
	 * @param objectSemUri The object URI.
	 * 
	 * @return The count of learned propositions, which may be greater than one
	 * when using inference.
	 */
	public long learn(String subjectSemUri,
			String propositionTypeUri,
			String objectSemUri);
	
	/** Let this model forget a set of propositions.
	 * 
	 * @param someKnowledge An Apache Jena model containing the propositions
	 * to forget.
	 * 
	 * @return The count of forgotten statements.
	 */
	public long forget(Model someKnowledge);
	
	/** Let this model forget a set of propositions.
	 * 
	 * <p>Any parameter may be <code>null</code>.
	 * 
	 * @param subjectSemUri The subject URI.
	 * @param propositionTypeUri The proposition kind.
	 * @param objectSemUri The object URI.
	 * 
	 * @return The count of forgotten statements.
	 */
	public long forget(String subjectSemUri,
			String propositionTypeUri,
			String objectSemUri);
}
