package com.bizcreator.core.session;

import com.bizcreator.core.session.ServiceBase;
import java.util.List;

import javax.ejb.Local;

import com.bizcreator.core.entity.Sequence;

@Local
public interface SequenceManager extends ServiceBase {
	
    public final static String NAME = "sequenceManager";
    
	public String createSequence(String domain, String name, int nextValue, String description);

    public List<Sequence> findAllSequences();
    
	List findByDomain(String domain);
	
    public Sequence findByName(String domain, String name);
    
	public void destroy();
    
    /*
    public String selectSequence();
	
	public String saveSequence();
	
	public String removeSequence();
	
	public String cancel();
     
    public List<Sequence> findSequences(int pageNum, int pageSize, String sortCol, String sortDir);
	
	public String saveSequences(Sequence[] sequences);
	
	public void removeSequences(String[] ids);
     */
}
