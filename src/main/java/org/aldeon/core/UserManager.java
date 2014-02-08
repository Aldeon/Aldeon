package org.aldeon.core;

import org.aldeon.core.events.*;
import org.aldeon.crypt.Key;
import org.aldeon.events.ACB;
import org.aldeon.events.AsyncCallback;
import org.aldeon.events.Callback;
import org.aldeon.model.Identity;
import org.aldeon.model.User;
import org.aldeon.utils.helpers.Callbacks;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

public class UserManager {

    private static Map<Key,Identity> identities = new HashMap<>();
    private static Map<Key,User> users = new HashMap<>();

    public static void initialize(){
        CoreModule.getInstance().getEventLoop().assign(IdentityAddedEvent.class, new ACB<IdentityAddedEvent>(CoreModule.getInstance().clientSideExecutor()) {
            @Override
            public void react(IdentityAddedEvent val) {
                identities.put(val.getIdentity().getPublicKey(), val.getIdentity());
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
                users.put(usr.getUser().getPublicKey(), usr.getUser());
            }
        });

        CoreModule.getInstance().getEventLoop().assign(UserRemovedEvent.class, new ACB<UserRemovedEvent>(CoreModule.getInstance().clientSideExecutor()) {
            @Override
            protected void react(UserRemovedEvent userRemovedEvent) {
                users.remove(userRemovedEvent.publicKey());
            }
        });

        refreshIdentities();
        refreshUsers();
    }

    public Map<Key,Identity> getAllIdentities() {
        return Collections.unmodifiableMap(identities);
    }

    public Map<Key,User> getAllUsers(){
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
                    identities.put(identity.getPublicKey(), identity);
                    users.put(identity.getPublicKey(),identity);
                }
            }
        });
    }

    public void renameUser(final User usr, final Callback<Boolean> callback){
        refreshIdentities();
        if(!identities.containsKey(usr.getPublicKey()))
        CoreModule.getInstance().getStorage().deleteUser(usr.getPublicKey(), new Callback<Boolean>() {
            @Override
            public void call(Boolean val) {
                if(val==true){
                    users.remove(usr.getPublicKey());
                    CoreModule.getInstance().getStorage().insertUser(usr,new Callback<Boolean>() {
                        @Override
                        public void call(Boolean val) {
                            if(val==true){
                                callback.call(true);
                                users.put(usr.getPublicKey(), usr);
                            }
                            else callback.call(false);
                        }
                    });
                }
            else callback.call(false);
            }
        });
        refreshIdentities();
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
                    identities.remove(identity.getPublicKey());
                    users.remove(identity.getPublicKey());
                }
            }
        });
    }

    public Identity getIdentity(Key publicKey){
        return identities.get(publicKey);
    }

    public User getUser(Key publicKey){
        return users.get(publicKey);
    }

    public static void refreshIdentities(){
        identities=new HashMap<>();
        CoreModule.getInstance().getStorage().getIdentities(new Callback <Set<Identity>>(){
            @Override
            public void call(Set<Identity> ids) {
                for(Identity id : ids)
                    if(!identities.containsValue(id)) identities.put(id.getPublicKey(),id);
            }
        });
    }

    public static void refreshUsers(){
        users=new HashMap<>();
        CoreModule.getInstance().getStorage().getUsers(new Callback <Set<User>>(){
            @Override
            public void call(Set<User> userSet) {
                for(User usr : userSet)
                    if(!users.containsValue(usr)) users.put(usr.getPublicKey(),usr);
            }
        });
    }

}
