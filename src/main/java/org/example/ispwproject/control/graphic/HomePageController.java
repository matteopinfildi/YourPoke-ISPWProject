package org.example.ispwproject.control.graphic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.ispwproject.NavigationHelper;
import org.example.ispwproject.Session;
import org.example.ispwproject.SessionManager;
import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.exception.PokeLabSystemException;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;


public class HomePageController extends GraphicController{
    @FXML
    private ImageView bannerImage;

    @FXML
    private ImageView pwImage;

    @FXML
    private ImageView buyCPImage;

    @FXML
    private ImageView buyPLImage;


    private Session session;

    public void  initialize(){
        Image banner = new Image(getClass().getResource("/org/example/ispwproject/image/banner.JPG").toExternalForm());
        bannerImage.setImage(banner);

        Image pokeWall = new Image(getClass().getResource("/org/example/ispwproject/image/pokeWall.JPG").toExternalForm());
        pwImage.setImage(pokeWall);

        Image classicPoke = new Image(getClass().getResource("/org/example/ispwproject/image/buyClassicPoke.JPG").toExternalForm());
        buyCPImage.setImage(classicPoke);

        Image pokeLab = new Image(getClass().getResource("/org/example/ispwproject/image/buyPokeLab.JPG").toExternalForm());
        buyPLImage.setImage(pokeLab);
    }


    public void init(int id, PokeLabBean pokeLabBean)  throws SystemException, IOException, LoginException, SQLException {

        this.id = id;
        SessionManager sessionManager = SessionManager.getSessionManager();
        this.id = sessionManager.getCurrentId();
        session = sessionManager.getSessionFromId(id);

    }

    private int id = -1;

    @FXML
    public void handleBuyPokeLab(ActionEvent event) throws PokeLabSystemException, org.example.ispwproject.utils.exception.LoginException {
        NavigationHelper.handleSceneChange(event, session, "/org/example/ispwproject/view/buyPokeLab.fxml", id);
    }

    @FXML
    public void handlePokeWall(ActionEvent event) throws PokeLabSystemException, org.example.ispwproject.utils.exception.LoginException {
        NavigationHelper.handleSceneChange(event, session, "/org/example/ispwproject/view/pokeWall.fxml", id);
    }

}
