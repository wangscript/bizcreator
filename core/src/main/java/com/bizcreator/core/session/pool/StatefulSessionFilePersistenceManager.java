/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bizcreator.core.session.pool;

import com.bizcreator.core.session.pool.ServiceContext;
import com.bizcreator.core.session.StatefulService;
import java.io.File;
import java.rmi.RemoteException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.util.id.UID;

/**
 *
 * @author lgh
 */
public class StatefulSessionFilePersistenceManager implements StatefulSessionPersistenceManager {

    Log log = LogFactory.getLog(StatefulSessionFilePersistenceManager.class);
    /** The default store directory name ("<tt>sessions</tt>"). */
    public static final String DEFAULT_STORE_DIRECTORY_NAME = "sessions";
    /** Our container. */
    //private StatefulSessionContainer con;
    /**
     * The sub-directory name under the server data directory where
     * session data is stored.
     *
     * @see #DEFAULT_STORE_DIRECTORY_NAME
     * @see #setStoreDirectoryName
     */
    private String storeDirName = DEFAULT_STORE_DIRECTORY_NAME;
    /** The base directory where sessions state files are stored for our container. */
    private File storeDir;
    /**
     * Enable purging leftover state files at create and destroy
     * time (default is true).
     */
    private boolean purgeEnabled = true;
    private String serviceName;
    private Class serviceClass;

    public StatefulSessionFilePersistenceManager(String serviceName, Class serviceClass) {
        this.serviceName = serviceName;
        this.serviceClass = serviceClass;
    }
    //
    // jason: these properties are intended to be used when plugins/interceptors 
    //        can take configuration values (need to update xml schema and processors).
    //
    /**
     * Set the sub-directory name under the server data directory
     * where session data will be stored.
     *
     * <p>
     * This value will be appened to the value of
     * <tt><em>jboss-server-data-dir</em></tt>.
     *
     * <p>
     * This value is only used during creation and will not dynamically
     * change the store directory when set after the create step has finished.
     *
     * @jmx:managed-attribute
     *
     * @param dirName   A sub-directory name.
     */
    public void setStoreDirectoryName(final String dirName) {
        this.storeDirName = dirName;
    }

    /**
     * Get the sub-directory name under the server data directory
     * where session data is stored.
     *
     * jmx:managed-attribute
     *
     * @see #setStoreDirectoryName
     *
     * @return A sub-directory name.
     */
    public String getStoreDirectoryName() {
        return storeDirName;
    }

    /**
     * Set the stale session state purge enabled flag.
     *
     * jmx:managed-attribute
     *
     * @param flag   The toggle flag to enable or disable purging.
     */
    public void setPurgeEnabled(final boolean flag) {
        this.purgeEnabled = flag;
    }

    /**
     * Get the stale session state purge enabled flag.
     *
     * jmx:managed-attribute
     *
     * @return  True if purge is enabled.
     */
    public boolean getPurgeEnabled() {
        return purgeEnabled;
    }

    /**
     * Returns the directory used to store session passivation state files.
     *
     * jmx:managed-attribute
     * 
     * @return The directory used to store session passivation state files.
     */
    public File getStoreDirectory() {
        return storeDir;
    }

    /**
     * Setup the session data storage directory.
     *
     * <p>Purges any existing session data found.
     */
    protected void createService() throws Exception {
        /*
        // Initialize the dataStore
        String ejbName = con.getBeanMetaData().getEjbName();
        
        // Get the system data directory
        File dir = ServerConfigLocator.locate().getServerTempDir();
        
        // Setup the reference to the session data store directory
        dir = new File(dir, storeDirName);
        // ejbName is not unique across all deployments, so use a unique token
        dir = new File(dir, ejbName + "-" + new UID().toString());
        storeDir = dir;
        
        log.debug("Storing sessions for '" + ejbName + "' in: " + storeDir);
        
        // if the directory does not exist then try to create it
        if( !storeDir.exists() )
        {
        if( MkdirsFileAction.mkdirs(storeDir) == false )
        {
        throw new IOException("Failed to create directory: " + storeDir);
        }
        }
        
        // make sure we have a directory
        if( !storeDir.isDirectory() )
        {
        throw new IOException("File exists where directory expected: " + storeDir);
        }
        
        // make sure we can read and write to it
        if( !storeDir.canWrite() || !storeDir.canRead() )
        {
        throw new IOException("Directory must be readable and writable: " + storeDir);
        }
        
        // Purge state session state files, should be none, due to unique directory
        purgeAllSessionData();
         */
    }

    /**
     * Removes any state files left in the storgage directory.
     */
    private void purgeAllSessionData() {
        if (!purgeEnabled) {
            return;
        }
        log.debug("Purging all session data in: " + storeDir);

        File[] sessions = storeDir.listFiles();
        for (int i = 0; i < sessions.length; i++) {
            if (!sessions[i].delete()) {
                log.warn("Failed to delete session state file: " + sessions[i]);
            } else {
                log.debug("Removed stale session state: " + sessions[i]);
            }
        }
    }

    /**
     * Purge any data in the store, and then the store directory too.
     */
    protected void destroyService() throws Exception {
        // Purge data and attempt to delete directory
        purgeAllSessionData();

        // Nuke the directory too if purge is enabled
        if (purgeEnabled && !storeDir.delete()) {
            log.warn("Failed to delete session state storage directory: " + storeDir);
        }
    }

    /**
     * Make a session state file for the given instance id.
     */
    private File getFile(final Object id) {
        //
        // jason: may have to translate id into a os-safe string, though
        //        the format of UID is safe on Unix and win32 already...
        //

        return new File(storeDir, String.valueOf(id) + ".ser");
    }

    /**
     * @return  A {@link UID}.
     */
    public Object createId(ServiceContext ctx)
            throws Exception {
        return new UID();
    }

    /**
     * Non-operation.
     */
    public void createdSession(ServiceContext ctx)
            throws Exception {
        // nothing
    }

    /**
     * Restores session state from the serialized file & invokes
     * {@link SessionBean#ejbActivate} on the target bean.
     */
    public void activateSession(final ServiceContext ctx)
            throws RemoteException {
        /*
        boolean trace = log.isTraceEnabled();
        if (trace) {
        log.trace("Attempting to activate; ctx=" + ctx);
        }
        
        Object id = ctx.getId();
        
        // Load state
        File file = getFile(id);
        if (trace) {
        log.trace("Reading session state from: " + file);
        }
        
        try {
        FileInputStream fis = FISAction.open(file);
        SessionObjectInputStream in = new SessionObjectInputStream(ctx,
        new BufferedInputStream(fis));
        
        try {
        Object obj = in.readObject();
        if (trace) {
        log.trace("Session state: " + obj);
        }
        ctx.setInstance(obj);
        } finally {
        in.close();
        }
        } catch (Exception e) {
        throw new EJBException("Could not activate; failed to " +
        "restore state", e);
        }
        
        removePassivated(id);
        
        try {
        // Instruct the bean to perform activation logic         
        //AllowedOperationsAssociation.pushInMethodFlag(IN_EJB_ACTIVATE);
        SessionBean bean = (SessionBean) ctx.getInstance();
        bean.ejbActivate();
        } finally {
        //AllowedOperationsAssociation.popInMethodFlag();
        }
        
        if (trace) {
        log.trace("Activation complete; ctx=" + ctx);
        }*/
    }

    /**
     * Invokes {@link SessionBean#ejbPassivate} on the target bean and saves the
     * state of the session to a file.
     */
    public void passivateSession(final ServiceContext ctx)
            throws RemoteException {

        boolean trace = log.isTraceEnabled();
        if (trace) {
            log.trace("Attempting to passivate; ctx=" + ctx);
        }
    /*
    try {
    // Instruct the bean to perform passivation logic    
    AllowedOperationsAssociation.pushInMethodFlag(IN_EJB_PASSIVATE);
    SessionBean bean = (SessionBean) ctx.getInstance();
    bean.ejbPassivate();
    } finally {
    AllowedOperationsAssociation.popInMethodFlag();
    }
    
    // Store state
    
    File file = getFile(ctx.getId());
    if (trace) {
    log.trace("Saving session state to: " + file);
    }
    
    try {
    FileOutputStream fos = FOSAction.open(file);
    SessionObjectOutputStream out = new SessionObjectOutputStream(
    new BufferedOutputStream(fos));
    
    Object obj = ctx.getInstance();
    if (trace) {
    log.trace("Writing session state: " + obj);
    }
    
    try {
    out.writeObject(obj);
    } finally {
    out.close();
    }
    } catch (Exception e) {
    throw new EJBException("Could not passivate; failed to save state", e);
    }
    
    if (trace) {
    log.trace("Passivation complete; ctx=" + ctx);
    }*/
    }

    /**
     * Invokes {@link SessionBean#ejbRemove} on the target bean.
     */
    public void removeSession(final ServiceContext ctx)
            throws RemoteException//, RemoveException 
    {

        boolean trace = log.isTraceEnabled();
        if (trace) {
            log.trace("Attempting to remove; ctx=" + ctx);
        }

        // Instruct the bean to perform removal logic
        StatefulService bean = (StatefulService) ctx.getInstance();
        bean.destroy();

        if (trace) {
            log.trace("Removal complete; ctx=" + ctx);
        }
    }

    /**
     * Removes the saved state file (if any) for the given session id.
     */
    public void removePassivated(final Object id) {
        boolean trace = log.isTraceEnabled();
    /*
    File file = getFile(id);
    
    // only attempt to delete if the file exists
    if (file.exists()) {
    if (trace) {
    log.trace("Removing passivated state file: " + file);
    }
    
    if (DeleteFileAction.delete(file) == false) {
    log.warn("Failed to delete passivated state file: " + file);
    }
    }*/
    }
}
