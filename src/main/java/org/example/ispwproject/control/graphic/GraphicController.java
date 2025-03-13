package org.example.ispwproject.control.graphic;

import org.example.ispwproject.utils.bean.PokeLabBean;
import org.example.ispwproject.utils.exception.SystemException;

public abstract class GraphicController {
    public abstract void initialize(int sessionId, PokeLabBean pokeLabBean) throws SystemException;
}
