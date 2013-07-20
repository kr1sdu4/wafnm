package org.marl.wafnm.core.impl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.marl.wafnm.core.api.IFrame;
import org.marl.wafnm.core.api.IFrameFactory;
import org.marl.wafnm.core.api.IFrameManager;

import com.hp.hpl.jena.rdf.model.Model;

/** Implements an in-memory frame manager.
 * 
 * <p>All nodes and graph are volatile.
 * 
 * <p>This implementation should be thread-safe.
 * 
 * <p>This manager expects a factory that produces {@link BaseFrame} frames.
 * 
 * @author kr1s
 *
 */
public class MemFrameManager implements IFrameManager {

	/////////////////////////////////////////////////////////////////////////////////////////////
	// Internal state
	protected Hashtable<String, IFrame> framesTable;
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	// Dependencies
	protected IFrameFactory frameFactory;
	
	public static final String MEM_FRAME_URI_PREFIX 
		= "http://wafnm.marl.org/vocabulary/1/memframe"; 
	
	/** Initialize a new frame manager with a default
	 * {@link MemFrameFactory}.
	 * 
	 */
	public MemFrameManager() 
	{
		this.framesTable = new Hashtable<String, IFrame>();
		this.frameFactory = new MemFrameFactory();
	}

	
	/////////////////////////////////////////////////////////////////////////////////////////////
	// 													IFrameManager
	/////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public IFrame getFrame(String uri) 
	{
		return framesTable.get(uri) ;
	}

	@Override
	public IFrame createFrame(Model baseOntology) 
	{
		String frameUri = FrameUtils.generateUri(MEM_FRAME_URI_PREFIX);
		IFrame frame = frameFactory.createFrame(frameUri, baseOntology);
		
		if (! (frame instanceof BaseFrame) ) 
		{
			throw new IllegalStateException("Invalid factory: " 
					+ frame.getClass().getName());
		}
		
		((BaseFrame) frame).setFrameManager(this);
		framesTable.put(frameUri, frame);
		
		return frame; 
	}
	
	@Override
	public void removeFrame(String uri) 
	{
		if (getFrame(uri) == null) {
			throw new IllegalStateException("Frame is undefined: " + uri);
		}
		
		IFrame f = framesTable.get(uri) ;
		f.getModel().close();
		framesTable.remove(uri);
	}


	@Override
	public List<IFrame> listFrames() 
	{
		ArrayList<IFrame> frames = new ArrayList<IFrame>();
		frames.addAll(framesTable.values());
		
		return frames;
	}

	@Override
	public long size() {
		return framesTable.size();
	}

	
	/////////////////////////////////////////////////////////////////////////////////////////////
	// 													POJO
	/////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public int hashCode() {
		return MEM_FRAME_URI_PREFIX.hashCode();
	}

	@Override
	public boolean equals(Object obj) 
	{
		if (obj != null && obj instanceof IFrameManager)
		{
			return (obj.hashCode() == hashCode());
		}
		return false;
	}
	
	@Override
	public String toString() {
		return new StringBuffer(MEM_FRAME_URI_PREFIX)
			.append(" (")
			.append(size()).append(")").toString();
	}
}
