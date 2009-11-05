package com.bizcreator.core.webean;

import com.bizcreator.core.LoginContext;
import com.bizcreator.core.RhSessionContext;
import com.bizcreator.core.BizContext;
import com.bizcreator.core.entity.User;
import com.bizcreator.core.session.UserManager;
import com.bizcreator.util.ObjectUtil;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Transient;

/**
 *
 * @author lgh
 */
@ManagedBean(name="userBean")
@SessionScoped
public class UserBean extends User {

    private UserManager userMgr;

    public UserBean() {
        userMgr = (UserManager) BizContext.getBean(UserManager.NAME);
    }

    @Transient
    public UserManager getUserMgr() {
        return userMgr;
    }

    public void setUserMgr(UserManager userMgr) {
        this.userMgr = userMgr;
    }

    private String selectedId;

    public String getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }
    
    public String login() {///
        LoginContext loginCtx = userMgr.authenticate(name, password);
        FacesContext facesCtx = FacesContext.getCurrentInstance();
        if (loginCtx != null && facesCtx != null) {
            Map<String,Object> sessionMap = facesCtx.getExternalContext().getSessionMap();
            sessionMap.put(User.USER_KEY, loginCtx.getUser());
            sessionMap.put(RhSessionContext.CLIENT_ID, loginCtx.getClientId());
            sessionMap.put(RhSessionContext.ORG_ID, loginCtx.getOrgId());
            sessionMap.put(RhSessionContext.DUTY_ID, loginCtx.getDutyId());
            sessionMap.putAll(loginCtx.getAttributes());
            return "UserList";
        }
        return null;
    }

    public List<User> getUsers() {
    	return userMgr.listAll();
    }

    public String editUser() throws IllegalArgumentException, IllegalAccessException {
        if (selectedId != null) {
            User user = userMgr.findById(selectedId);
            if (user != null) {
                ObjectUtil.assignObject(user, this);
                return "UserEdit";
            }
        }
        return null;
    }

    public String addUser() throws IllegalArgumentException, IllegalAccessException  {
        User user = new User();
        ObjectUtil.assignObject(user, this);
        return "UserEdit";
    }

    public String save() throws IllegalArgumentException, IllegalAccessException, InstantiationException {
        User user = (User) ObjectUtil.extractObject(this, User.class);
        userMgr.save(user);
        return "UserList";
    }
}

