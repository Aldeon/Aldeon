package org.aldeon.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import org.aldeon.core.CoreModule;
import org.aldeon.crypt.rsa.RsaKeyGen;
import org.aldeon.events.ACB;
import org.aldeon.events.Callback;
import org.aldeon.model.Identifier;
import org.aldeon.model.Identity;
import org.aldeon.model.Message;
import org.aldeon.utils.helpers.Callbacks;
import org.aldeon.utils.helpers.Messages;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;

/**
 *
 */
public class TopicMsgsController implements Initializable, ResponseControlListener, WriteResponseControlListener {

    public FlowPane fpane;
    public HBox hbox;
    private ArrayList<MsgWithInt> msgs = new ArrayList<>();
    private Message topicMessage;

    private class MsgWithInt {
        public Parent node;
        public Message msg;
        public int indent;

        public MsgWithInt(Parent node, int indent, Message message) {
            this.indent = indent;
            this.msg = message;
            this.node = node;
        }
    }

    private String weirdText = "1. Przecz skrżytało pogaństwo a ludzie myślili są proz? \n" +
            "2. Przystajali są krolowie ziemszczy a książęta seszli są sie na gromadę przeciwo Gospodnu i przeciwo \n" +
            "jego pomazańcu. \n" +
            "3. Roztargajmy jich przekowy i srzucimy s nas jarzmo jich. \n" +
            "4. Jen przebywa na niebiesiech, pośmieje sie jim, i Gospodzin zwala śmiech w nich. \n" +
            "5. Tegdy mołwić będzie k nim w gniewie swojem i w rosierdziu swojem zamąci je.  2\n" +
            "6. Ale ja postawion jeśm krol od niego na Syjon, gorze świętej jego, \n" +
            "przepowiadaję kaźń jego. \n" +
            "7. Gospodzin rzekł ku mnie: Syn moj jeś ty, ja dzisia porodził jeśm cie. \n" +
            "8. Pożędaj ote mnie, i dam ci pogany w dziedzicstwo twoje i w trzymanie twoje kraje ziemskie. \n" +
            "9. Włodać będziesz nad nimi w mietle żelaznej a jako ssąd zdunowy \n" +
            "rozbijesz je. \n" +
            "10. A już, krolowie, rozumiejcie, nauczcie sie, cso sądzicie ziemię. \n" +
            "11. Służycie Bogu w strasze i wiesielcie sie jemu se drżenim. \n" +
            "12. Przyjmicie pokaźnienie, bo snadź rozgniewa sie Gospodzin, i \n" +
            "zginiecie z drogi prawej. \n" +
            "13. Gdy rozżgą na krotce gniew jego, błogosławieni wszystcy, jiż imają w niem pwę";

    private Parent constructResponse(Message message, int nestingLevel) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/gui/fxml/Resp.fxml"));
        if(message==null) System.out.println("DOSTAŁEM NULLA");
        Parent parent=null;
        try {
            parent = (Parent) loader.load(getClass().getResource("/gui/fxml/Resp.fxml").openStream());
        } catch (IOException e) {
        }
        ResponseController rc = loader.getController();
        rc.registerListener(this);
        rc.toPass = parent;
        rc.setMessage(message, nestingLevel);

        final ResponseController rcF = rc;

        if (nestingLevel != 0)
        CoreModule.getInstance().getStorage().getMessagesByParentId(message.getIdentifier(),
                new Callback<Set<Message>>() {
                    @Override
                    public void call(Set<Message> val) {
                        if (val != null && !val.isEmpty()) {
                            rcF.setHasChildren(false);
                        }
                    }
                });
        if(parent==null) System.out.println("ZWRACAM NULLA");
        return parent;
    }

    public void setRootMsg(Message root) {
        msgs.clear();
        fpane.getChildren().clear();
        Parent rootNode = constructResponse(root, 0);
        fpane.getChildren().add(rootNode);
        msgs.add(new MsgWithInt(rootNode, 0, root));
    }

    public void addChildMsg(Message child) {
        int nesting = 0;
        int index = 0;

        for (int i = 0; i < msgs.size(); i++) {
            if (msgs.get(i).msg.getIdentifier().equals(child.getParentMessageIdentifier())) {
                nesting = msgs.get(i).indent;
                index = i;
                break;
            }
        }

        Parent childNode = constructResponse(child, nesting+1);
        fpane.getChildren().add(index+1, childNode);
        msgs.add(index+1, new MsgWithInt(childNode, nesting+1, child));

    }

    public void setTopicMessage(Message topicMessage) {
        this.topicMessage = topicMessage;
        setRootMsg(topicMessage);

        CoreModule.getInstance().getStorage().getMessagesByParentId(topicMessage.getIdentifier(),
                new Callback<Set<Message>>() {
                    @Override
                    public void call(Set<Message> val) {
                        for (Message message : val) {

                            final Message m = message;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    addChildMsg(m);
                                }
                            });
                        }
                    }
                });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @Override
    public void responseHideClicked(Parent rcNode, ResponseController rc) {
        Iterator<MsgWithInt> it = msgs.iterator();
        boolean delete = false;
        int nesting = 0;

        while (it.hasNext()) {
            MsgWithInt curr = it.next();
            if (curr.node == rcNode) {
                delete = true;
                nesting = curr.indent;
                //fpane.getChildren().remove(curr.node);
                //it.remove();
                continue;
            }

            if (delete == true && curr.indent > nesting) {
                fpane.getChildren().remove(curr.node);
                it.remove();
            } else if (delete == true && curr.indent <= nesting) {
                break;
            }
        }
    }

    @Override
    public void responseShowClicked(Parent rcNode, ResponseController rc) {
        CoreModule.getInstance().getStorage().getMessagesByParentId(rc.getMsg().getIdentifier(),
                new ACB<Set<Message>>(CoreModule.getInstance().clientSideExecutor()) {
                    @Override
                    protected void react(Set<Message> val) {
                        for (Message message : val) {
                            final Message m = message;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    addChildMsg(m);
                                }
                            });
                        }
                    }
                });
    }

    @Override
    public void responseClicked(ResponseController rc, String text) {

    }

    @Override
    public void responseRespondClicked(Parent rcNode, ResponseController rc,  int nestingLevel) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/gui/fxml/WriteResponse.fxml"));
        Parent parent=null;
        try {
            parent = (Parent) loader.load(getClass().getResource("/gui/fxml/WriteResponse.fxml").openStream());
        } catch (IOException e) {
        }

        WriteResponseController wrc = loader.getController();
        wrc.setNestingLevel(nestingLevel);
        wrc.setNode(parent);
        wrc.setParentIdentifier(rc.getMsg().getIdentifier());
        wrc.setParentController(rc);
        wrc.registerListener(this);

        //TODO @down - move to synchronized block
        fpane.getChildren().add(fpane.getChildren().indexOf(rcNode) + 1, parent);
    }

    @Override
    public void responseDeleteClicked(Parent responseNode, ResponseController rc) {

        CoreModule.getInstance().getStorage().deleteMessage(rc.getMsg().getIdentifier(), Callbacks.<Boolean>emptyCallback());

        Iterator<MsgWithInt> it = msgs.iterator();
        boolean delete = false;
        int nesting = 0;

        while (it.hasNext()) {
            MsgWithInt curr = it.next();
            if (curr.node == responseNode) {
                delete = true;
                nesting = curr.indent;
                fpane.getChildren().remove(curr.node);
                it.remove();
                continue;
            }

            if (delete && curr.indent > nesting) {
                fpane.getChildren().remove(curr.node);
                it.remove();
            } else if (delete && curr.indent <= nesting) {
                break;
            }
        }
    }

    @Override
    public void createdResponse(Parent wrcNode, WriteResponseController wrc, String responseText, Identity author, Identifier parentIdentifier,
                                int nestingLevel) {

        //TODO @down - move to synchronized block

        //Identity currId = Identity.create("Anon", new RsaKeyGen());
        Message newMsg = Messages.createAndSign(parentIdentifier, author.getPublicKey(), author.getPrivateKey(), responseText);
        CoreModule.getInstance().getStorage().insertMessage(newMsg, Callbacks.<Boolean>emptyCallback());
        int creationIndex = fpane.getChildren().indexOf(wrcNode);
        Parent msg = constructResponse(newMsg, nestingLevel+1);
        wrc.getParentController().setHasChildren(true);
        msgs.add(creationIndex, new MsgWithInt(msg, nestingLevel+1, newMsg));
        fpane.getChildren().add(creationIndex,msg);
        fpane.getChildren().remove(wrcNode);
    }
}
