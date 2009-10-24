package com.bizcreator.core.security;

public interface PrincipalManager {
    /**
     * The principal is set only for the current thread.
     * Normally used in a web server where each thread can
     * represent a different user.
     */
    void setThreadPrincipal(String login, String passWord);

    /**
     * The principal is set only one time for all the thread
     * created in the JVM.
     */
    void setJVMPrincipal (String login, String password);
}

