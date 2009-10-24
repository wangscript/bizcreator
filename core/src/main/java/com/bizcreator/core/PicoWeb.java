/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bizcreator.core;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Administrator
 */
public class PicoWeb extends HttpServlet {
    
    /*private DefaultPicoContainer requestContainer;
    private HttpSessionStoring sessionStoring;    
    private HttpSessionStoring requestStoring;    
     */
    public void init(ServletConfig cfg) throws ServletException {
        /*
        PicoContainer appContainer = new DefaultPicoContainer(new Caching()); 
        
        // app scoped components are cached for all users/sessions
        Storing storingBehavior1 = new Storing();        
        PicoContainer sessionContainer = new DefaultPicoContainer(storingBehavior1, appContainer);
        sessionStoringAdapter = new HttpSessionStoringAdapter(storingBehavior1, "sessionStore");
        Storing storingBehavior2 = new Storing();
        requestContainer = new DefaultPicoContainer(storingBehavior2, sessionContainer);
        requestStoringAdapter = new HttpSessionStoringAdapter(storingBehavior2, "requestStore");
        // populate app, session and request scoped containers.<BR>        
        appContainer.addComponent(HibernateManager.class, MyHibernateManager.class); 
        // all sessions share one HibernateManager        
        sessionContainer.addComponent(ShoppingCart.class, FifoCart.class); 
        // a new cart per user
        requestContainer.addComponent("/addToCart.do", AddToCart.class); 
        // key crudely maps to URL        
        requestContainer.addComponent("/removeFromCart.do", RemoveFromCart.class);        
         */
    }
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
        sessionStoringAdapter.retrieveOrCreateStore(req.getSession()); 
        // associate thread with session, for caching (session scoped components)        
        requestStoringAdapter.resetStore(); 
        // cache components per request (request scoped components)        
        Action action = (Action) requestContainer.getComponent(req.getPathTranslated());
        try {
            action.execute(req, resp); 
            // yeah yeah, this is pretty basic
        } finally {
            sessionStoringAdapter.invalidateStore();            
            requestStoringAdapter.invalidateStore();
        }
        // trying to retrieve components from at session or request scopes from here on will result in an UnsupportedOperationException<BR>    
         */
    }
}
