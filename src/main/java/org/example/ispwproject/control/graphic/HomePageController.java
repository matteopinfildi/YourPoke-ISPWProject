package org.example.ispwproject.control.graphic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.ispwproject.ChangePage;
import org.example.ispwproject.Session;
import org.example.ispwproject.SessionManager;
import org.example.ispwproject.control.application.BuyPokeLabAppController;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.bean.SaveBean;


public class HomePageController extends GraphicController{
    @FXML
    private ImageView bannerImage;

    @FXML
    private ImageView pwImage;

    @FXML
    private ImageView buyCPImage;

    @FXML
    private ImageView buyPLImage;


    public void initialize(int id, PokeLabBean pokeLabBean) {
            Image banner = new Image(getClass().getResource("/org/example/ispwproject/image/banner.JPG").toExternalForm());
            bannerImage.setImage(banner);

            Image pokeWall = new Image(getClass().getResource("/org/example/ispwproject/image/pokeWall.JPG").toExternalForm());
            pwImage.setImage(pokeWall);

            Image classicPoke = new Image(getClass().getResource("/org/example/ispwproject/image/buyClassicPoke.JPG").toExternalForm());
            buyCPImage.setImage(classicPoke);

            Image pokeLab = new Image(getClass().getResource("/org/example/ispwproject/image/buyPokeLab.JPG").toExternalForm());
            buyPLImage.setImage(pokeLab);
    }

    private int id = -1;

    @FXML
    public void handleBuyPokeLab(ActionEvent event) {

        BuyPokeLabAppController buyPokeLabAppController = new BuyPokeLabAppController();
        PokeLabBean pokeLabBean = buyPokeLabAppController.newPokeLab();

//        SessionManager sessionManager = SessionManager.getSessionManager();
//        Session session = sessionManager.getSessionFromId(id);

//        SaveBean saveBean = new SaveBean(session.getUserId());
//        boolean value = buyPokeLabAppController.checkPokeLab(saveBean);
       // BuyDreamGuitarControllerStart.setToRecover(value);
        // devo aggiungere un pulsante save da qualche parte!!!

        ChangePage.changeScene((Node) event.getSource(), "/org/example/ispwproject/view/buyPokeLab.fxml", pokeLabBean, id);
    }

    @FXML
    public void handlePokeWall(ActionEvent event) {
        System.out.println("Pokè wall");
    }

}
