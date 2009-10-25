/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.session.impl;

import com.bizcreator.core.session.impl.BasicSession;
import com.bizcreator.core.entity.Sequence;
import com.bizcreator.core.session.SequenceManager;
import java.util.List;
import javax.ejb.Stateless;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * @author Administrator
 */
@Stateless
@Transactional
public class SequenceSession extends BasicSession implements SequenceManager {
    
    public String createSequence(String domain, String name, int nextValue, String description) {
		Sequence sequence = new Sequence(domain, name, nextValue, description);
		persist(sequence);
		return sequence.getId();
	}
	
	public List<Sequence> findAllSequences() {
		List<Sequence> sequences = executeQuery("from Sequence o order by o.name");
		return sequences;
	}

    /*
	public List<Sequence> findSequences(int pageNum, int pageSize, String sortCol, String sortDir) {
		int pos = (pageNum - 1) * pageSize;
		List list = em.createQuery("from Sequence o ")
		.setMaxResults(pageSize)
		.setFirstResult(pos)
		.getResultList();
		return list;
	}
	*/
    
	public List findByDomain(String domain) {
		return executeQuery("from Sequence o where o.domain = ?", new Object[]{domain});
	}
	
    public Sequence findByName(String domain, String name) {
        return (Sequence) getSingleResult("from Sequence o where o.domain = ? and o.name = ?", new Object[]{domain, name});
    }
    
	public void destroy() {}
}
