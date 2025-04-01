package org.example.ispwproject.control.graphic;

import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.exception.CliException;
import org.example.ispwproject.utils.exception.PokeLabSystemException;
import org.example.ispwproject.utils.exception.SystemException;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;

public abstract class CliGraphicController {
    public abstract void init(int sessionId, PokeLabBean pokeLabBean) throws SystemException, IOException, LoginException, SQLException, CliException, PokeLabSystemException, org.example.ispwproject.utils.exception.LoginException;
}