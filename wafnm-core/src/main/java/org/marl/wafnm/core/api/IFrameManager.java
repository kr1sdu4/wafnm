package org.marl.wafnm.core.api;


import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;

/** Creates and manage frames.
 * 
 * <p>A <i>frame manager</i> is responsible for:
 * <ul>
 * <li>managing a frame collection, optimizing resources through sharing
 * nodes and subgraph when possible</li>
 * <li>managing a namespace within which each frame has a unique URI, and
 * may reference an other frame within the same manager; this should
 * be enforced by associating the namespace to the manager identity</li>
 * </ul>
 * 
 * <p>A <i>frame manager</i> depends upon a 
 * {@link IFrameFactory <i>frame factory</i>} for actual frame creation.
 * 
 * <p>This interface does not define any checked exception, but implementation 
 * my throw:
 * <ul>
 * <li>an <code>IllegalArgumentException</code>, when the processing fails
 * due to invalid arguments</li>
 * <li>an <code>IllegalStateException</code>, when the frame belongs to an
 * internal state that is incompatible with the requested operation</li>
 * </ul>
 *  
 * @author kr1s
 *
 */
public interface IFrameManager {

	/** Retrieves a frame by its URI.
	 * 
	 * @param uri The requested frame URI.
	 * 
	 * @return The corresponding frame, or <code>null</code> if this manager
	 * is unaware of this frame.
	 */
	public IFrame getFrame(String uri) ;
	
	/** Asks this manager to create a new frame.
	 * 
	 * <p>The manager is responsible for generating the frame URI.
	 * 
	 * @param baseOntology The ontology that represents the initial knowledge 
	 * (vocabulary, corpus, ...) for this frame. May be <code>null</code>.
	 * 
	 * @return The created frame.
	 */
	public IFrame createFrame(Model baseOntology);
	
	/** Asks a this manager to forget a frame forever.
	 * 
	 * <p>Once a frame is removed, any associated graph or node may be released
	 * by the underlying back-end, and should not be reused.
	 * 
	 * @param uri The frame to remove.
	 */
	public void removeFrame(String uri);

	/** Answers a list containing the frames managed by this container.
	 *   
	 * @return The managed frames list.
	 */
	public List<IFrame> listFrames();

	/** Answers the number of managed frames.
	 * 
	 * @return The managed frames' count.
	 */
	public long size();
}
