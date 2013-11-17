package org.aldeon.gui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 */
public class TopicMsgsController extends ScrollPane implements Initializable{

    public FlowPane fpane;

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

    private Parent constructResponse(String text, int nestingLevel) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("../Resp.fxml"));
        Parent parent=null;
        try {
            parent = (Parent) loader.load(getClass().getResource("../Resp.fxml").openStream());
        } catch (IOException e) {
        }
        ResponseController rc = (ResponseController) loader.<ResponseController>getController();
        rc.setMessage(text, nestingLevel);

        return parent;
    }

    public void appendMsg(String content, int nestingLevel) {
        fpane.getChildren().add(constructResponse(content, nestingLevel));
    }

    //or parent hash instead of nestingLevel
    public void insertMsg(String content, int nestingLevel) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Node msg = constructResponse(weirdText);
//        fpane.getChildren().add(constructResponse(weirdText, 0));
//        fpane.getChildren().add(constructResponse(weirdText, 1));
//        fpane.getChildren().add(constructResponse(weirdText, 2));
//        fpane.getChildren().add(constructResponse(weirdText, 3));
//        fpane.getChildren().add(constructResponse(weirdText, 4));
//        fpane.getChildren().add(constructResponse(weirdText, 5));
//        fpane.getChildren().add(constructResponse(weirdText, 6));
//        fpane.getChildren().add(constructResponse(weirdText, 7));
//        fpane.getChildren().add(constructResponse(weirdText, 8));
    }
}
