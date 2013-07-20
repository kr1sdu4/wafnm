package org.marl.wafnm.core;


import java.util.List;

import org.apache.log4j.Logger;
import org.marl.wafnm.core.api.IFrame;
import org.marl.wafnm.core.impl.MemFrameManager;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestInMemoryWorlds {

	static final Logger log = Logger.getLogger(TestInMemoryWorlds.class);
	
	public static long FRAME_TABLE_SIZE = 8;
	
	@Test
	public void testInMemoryManagerSetup()
	{
		MemFrameManager fm = new MemFrameManager();
		Assert.assertNotNull(fm);
		
		IFrame f = fm.createFrame(null);
		Assert.assertNotNull(f);
		
		log.info("created frame: " + f.getURI());
	}
	
	@Test
	public void testInMemoryManagerFind()
	{
		MemFrameManager fm = new MemFrameManager();
		Assert.assertNotNull(fm);
		log.info("initialized frame manager: " + fm.toString());
		
		IFrame f = fm.createFrame(null);
		Assert.assertNotNull(f);
		
		IFrame got =fm.getFrame(f.getURI());
		Assert.assertNotNull(got);
		Assert.assertEquals(got.getURI(), f.getURI());
		Assert.assertEquals(got, f);
		
		log.info("retrieved previously created frame: " + got.toString());
	}
	
	@Test
	public void testInMemoryManagerRemove()
	{
		MemFrameManager fm = new MemFrameManager();
		Assert.assertNotNull(fm);
		
		IFrame f = fm.createFrame(null);
		Assert.assertNotNull(f);
		
		String baseUri = f.getURI();
		
		IFrame got =fm.getFrame(f.getURI());
		Assert.assertNotNull(got);
		Assert.assertEquals(got.getURI(), baseUri);
		Assert.assertEquals(got, f);
		
		fm.removeFrame(baseUri);
		IFrame removed =fm.getFrame(baseUri);
		Assert.assertNull(removed);

		log.info("removed previously created frame: " + baseUri);
	}
	
	@Test
	public void testInMemoryManagerList()
	{
		MemFrameManager fm = new MemFrameManager();
		Assert.assertNotNull(fm);
		
		for (int k=0 ; k<FRAME_TABLE_SIZE ; k++ )
			Assert.assertNotNull( fm.createFrame(null) );
		
		List<IFrame> gotlist = fm.listFrames();
		Assert.assertEquals(gotlist.size(), FRAME_TABLE_SIZE);
		
		for (IFrame f : gotlist) {
			fm.removeFrame(f.getURI());
		}
		gotlist = fm.listFrames();
		Assert.assertEquals(gotlist.size(), 0);
	
		log.info("managing frames list seems ok for table size: " + FRAME_TABLE_SIZE);
	}
	
}
