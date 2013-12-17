package org.aldeon.core;

import org.aldeon.core.events.IdentityAddedEvent;
import org.aldeon.core.events.IdentityRemovedEvent;
import org.aldeon.core.events.MessageAddedEvent;
import org.aldeon.core.events.MessageRemovedEvent;
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

    private static Map<String,Identity> identities = new HashMap<>();
    private static Map<String,User> users = new HashMap<>();

    public static void initialize(){
        CoreModule.getInstance().getEventLoop().assign(IdentityAddedEvent.class, new ACB<IdentityAddedEvent>(CoreModule.getInstance().clientSideExecutor()) {
            @Override
            public void react(IdentityAddedEvent val) {
                identities.put(val.getIdentity().getPublicKey().toString(),val.getIdentity());
            }
        });

        CoreModule.getInstance().getEventLoop().assign(IdentityRemovedEvent.class, new ACB<IdentityRemovedEvent>(CoreModule.getInstance().clientSideExecutor()) {
            @Override
            protected void react(IdentityRemovedEvent val) {
                identities.remove(val.getPublicKey());
            }
        });
    }

    public Map<String,Identity> getAllIdentities() {
        return Collections.unmodifiableMap(identities);
    }

    public Map<String,User> getAllUsers(){
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
                    identities.put(identity.getPublicKey().toString(), identity);
                    users.put(identity.getPublicKey().toString(),identity);
                }
            }
        });
    }

    public void addUser(final User user){
        CoreModule.getInstance().getStorage().insertUser(user,new Callback<Boolean>() {
            @Override
            public void call(Boolean userInserted) {
                if (userInserted) {
                    users.put(user.getPublicKey().toString(),user);
                }
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
                    identities.remove(identity.getPublicKey().toString());
                    users.remove(identity.getPublicKey().toString());
                }
            }
        });
    }

    public Identity getIdentity(Key publicKey){
        return identities.get(publicKey.toString());
    }


    public void refreshIdentities(){
        identities=new HashMap<>();
        CoreModule.getInstance().getStorage().getIdentities(new Callback <Set<Identity>>(){
            @Override
            public void call(Set<Identity> ids) {
                for(Identity id : ids)
                    if(!identities.containsValue(id)) identities.put(id.getPublicKey().toString(),id);
            }
        });
    }

    public void refreshUsers(){
        users=new HashMap<>();
        CoreModule.getInstance().getStorage().getUsers(new Callback <Set<User>>(){
            @Override
            public void call(Set<User> userSet) {
                for(User usr : userSet)
                    if(!users.containsValue(usr)) users.put(usr.getPublicKey().toString(),usr);
            }
        });
    }

}
