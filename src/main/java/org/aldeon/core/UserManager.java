package org.aldeon.core;

import org.aldeon.core.events.IdentityAddedEvent;
import org.aldeon.core.events.IdentityRemovedEvent;
import org.aldeon.core.events.MessageAddedEvent;
import org.aldeon.core.events.MessageRemovedEvent;
import org.aldeon.core.events.UserAddedEvent;
import org.aldeon.crypt.Key;
import org.aldeon.events.ACB;
import org.aldeon.events.Callback;
import org.aldeon.model.Identity;
import org.aldeon.model.User;
import org.aldeon.utils.helpers.Callbacks;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserManager {

    private static Map<Integer,Identity> identities = new HashMap<>();
    private static Map<Integer,User> users = new HashMap<>();

    public static void initialize(){
        CoreModule.getInstance().getEventLoop().assign(IdentityAddedEvent.class, new ACB<IdentityAddedEvent>(CoreModule.getInstance().clientSideExecutor()) {
            @Override
            public void react(IdentityAddedEvent val) {
                identities.put(val.getIdentity().getPublicKey().hashCode(), val.getIdentity());
            }
        });

        CoreModule.getInstance().getEventLoop().assign(IdentityRemovedEvent.class, new ACB<IdentityRemovedEvent>(CoreModule.getInstance().clientSideExecutor()) {
            @Override
            protected void react(IdentityRemovedEvent val) {
                identities.remove(val.getPublicKey());
            }
        });

        CoreModule.getInstance().getEventLoop().assign(UserAddedEvent.class , new ACB<UserAddedEvent>(CoreModule.getInstance().clientSideExecutor()) {
            @Override
            protected void react(UserAddedEvent usr) {
                addUser(usr.getUser());
            }
        });

        refreshIdentities();
        refreshUsers();
    }

    public Map<Integer,Identity> getAllIdentities() {
        return Collections.unmodifiableMap(identities);
    }

    public Map<Integer,User> getAllUsers(){
        return Collections.unmodifiableMap(users);
    }

    public void addIdentity(final Identity identity) {
        final boolean[] success = {false};
        CoreModule.getInstance().getStorage().insertIdentity(identity, new Callback<Boolean>() {
            @Override
            public void call(Boolean identityInserted) {
                if (identityInserted) {
                    success[0] =true;
                }
            }
        });
        CoreModule.getInstance().getStorage().insertUser(identity,new Callback<Boolean>() {
            @Override
            public void call(Boolean userInserted) {
                if (userInserted&&success[0]) {
                    identities.put(identity.getPublicKey().hashCode(), identity);
                    users.put(identity.getPublicKey().hashCode(),identity);
                }
            }
        });
    }

    public static void addUser(final User user){
        CoreModule.getInstance().getStorage().insertUser(user, new Callback<Boolean>() {
            @Override
            public void call(Boolean userInserted) {
                if (userInserted) {
                    users.put(user.getPublicKey().hashCode(), user);
                }
            }
        });
    }

    public void renameUser(final User usr, final Callback<Boolean> callback){
        refreshIdentities();
        if(!identities.containsKey(usr.getPublicKey().hashCode()))
        CoreModule.getInstance().getStorage().deleteUser(usr.getPublicKey(), new Callback<Boolean>() {
            @Override
            public void call(Boolean val) {
                if(val==true) CoreModule.getInstance().getStorage().insertUser(usr,new Callback<Boolean>() {
                    @Override
                    public void call(Boolean val) {
                        if(val==true) callback.call(true);
                        else callback.call(false);
                    }
                });
            else callback.call(false);
            }
        });
    }

    public void delIdentity(final Identity identity) {
        final boolean[] success = {false};
        CoreModule.getInstance().getStorage().deleteIdentity(identity.getPublicKey(), new Callback<Boolean>() {
            @Override
            public void call(Boolean identityRemoved) {
                if (identityRemoved) {
                    success[0] =true;
                }
            }
        });
        CoreModule.getInstance().getStorage().deleteUser(identity.getPublicKey(), new Callback<Boolean>() {
            @Override
            public void call(Boolean userRemoved) {
                if (userRemoved&&success[0]) {
                    identities.remove(identity.getPublicKey().hashCode());
                    users.remove(identity.getPublicKey().hashCode());
                }
            }
        });
    }

    public Identity getIdentity(Key publicKey){
        return identities.get(publicKey.hashCode());
    }

    public User getUser(Key publicKey){
        return users.get(publicKey.hashCode());
    }

    public static void refreshIdentities(){
        identities=new HashMap<>();
        CoreModule.getInstance().getStorage().getIdentities(new Callback <Set<Identity>>(){
            @Override
            public void call(Set<Identity> ids) {
                for(Identity id : ids)
                    if(!identities.containsValue(id)) identities.put(id.getPublicKey().hashCode(),id);
            }
        });
    }

    public static void refreshUsers(){
        users=new HashMap<>();
        CoreModule.getInstance().getStorage().getUsers(new Callback <Set<User>>(){
            @Override
            public void call(Set<User> userSet) {
                for(User usr : userSet)
                    if(!users.containsValue(usr)) users.put(usr.getPublicKey().hashCode(),usr);
            }
        });
    }

}
