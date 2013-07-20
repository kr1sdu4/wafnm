package org.marl.wafnm.core.api;

import com.hp.hpl.jena.rdf.model.Model;

/** Creates frames.
 * 
 * <p>This is intended to encapsulate default/configured choices for the 
 * Apache Jena underlying models, including:
 * <ul>
 * <li>the graph back-end: in memory, TDB repository, database</li>
 * <li>the inference level</li>
 * </ul>
 * 
 * <p>This interface does not define any checked exception, but implementation 
 * of most of these API calls my throw:
 * <ul>
 * <li>an <code>IllegalArgumentException</code>, when the processing fails
 * due to invalid arguments</li>
 * <li>an <code>IllegalStateException</code>, when this factory internal state 
 * does not permit to create new frames (no memory, broken connection with 
 * underlying back-end, ...)</li>
 * </ul>
 *  
 * @author kr1s
 *
 */
public interface IFrameFactory {

	/** Creates a frame.
	 * 
	 * @param uri An URI identifier. This should not be <code>null</code>
	 * and should stand as a unique identifier for the intended scope.
	 * @param baseOntology The ontology that represent the initial knowledge 
	 * (vocabulary, corpus, ...) for this frame. May be <code>null</code>.
	 * 
	 * @return The created frame.
	 */
	public IFrame createFrame(String uri, Model baseOntology);

}
