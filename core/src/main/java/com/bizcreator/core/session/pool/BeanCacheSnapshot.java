/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core.session.pool;

/**
 *
 * @see Monitorable
 * @author <a href="mailto:simone.bordet@compaq.com">Simone Bordet</a>
 * @version $Revision: 1.1 $
 */
public class BeanCacheSnapshot
	implements java.io.Serializable
{
   static final long serialVersionUID = 691475591030550490L;

	// Constants ----------------------------------------------------
	
	// Attributes ---------------------------------------------------
	public String m_application;
	public String m_container;
	public int m_passivatingBeans;
	public int m_cacheMinCapacity;
	public int m_cacheMaxCapacity;
	public int m_cacheCapacity;
	public int m_cacheSize;
	private StringBuffer m_buffer = new StringBuffer();
	
	// Static -------------------------------------------------------

	// Constructors -------------------------------------------------
	public BeanCacheSnapshot() {}
	
	// Public -------------------------------------------------------
	public String toString()
	{
		m_buffer.setLength(0);
		m_buffer.append("Cache Snapshot for application '");
		m_buffer.append(m_application);
		m_buffer.append("', container for bean '");
		m_buffer.append(m_container);
		m_buffer.append("':\nmin capacity: ");
		m_buffer.append(m_cacheMinCapacity);
		m_buffer.append("\nmax capacity: ");
		m_buffer.append(m_cacheMaxCapacity);
		m_buffer.append("\ncapacity: ");
		m_buffer.append(m_cacheCapacity);
		m_buffer.append("\nsize: ");
		m_buffer.append(m_cacheSize);
		m_buffer.append("\nnumber of beans scheduled for passivation: ");
		m_buffer.append(m_passivatingBeans);
		return m_buffer.toString();
	}
}