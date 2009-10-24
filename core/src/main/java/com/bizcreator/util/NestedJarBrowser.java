package com.bizcreator.util;


import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


import org.jboss.util.file.ArchiveBrowser;


public class NestedJarBrowser implements Iterator{
	
	private String prefixPath = null;
	JarFile jarFile;
	   Enumeration entries;
	   JarEntry next;
	   ArchiveBrowser.Filter filter;
	   
	public NestedJarBrowser(URL url, ArchiveBrowser.Filter filter) {
		
		this.filter = filter;
		String urlStr = url.toString();
		int index = urlStr.indexOf("!");
		
		if (index>=0) {
			prefixPath = urlStr.substring(index+2);
		}
		
		try {
			URLConnection urlConn = url.openConnection();
			JarURLConnection jarConn = (JarURLConnection) urlConn;
			jarFile = jarConn.getJarFile();
			 entries = jarFile.entries();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		setNext();
	}
	
	public boolean hasNext() {
	      return next != null;
	   }


	public Object next() {
		JarEntry entry = next;
		setNext();
		try {
			return jarFile.getInputStream(entry);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void remove() {
		throw new RuntimeException("Illegal operation on ArchiveBrowser");
	}
	
	private void setNext() {
		next = null;
		while (entries.hasMoreElements() && next == null) {
			do {
				next = (JarEntry) entries.nextElement();
			} while (entries.hasMoreElements() && !next.getName().startsWith(prefixPath));
			if (next.isDirectory())
				next = null;
			
			if (next != null && !filter.accept(next.getName())) {
				next = null;
			}
		}
	}
}
