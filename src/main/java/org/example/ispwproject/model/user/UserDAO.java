package org.example.ispwproject.model.user;

import org.example.ispwproject.utils.exception.SystemException;

public interface UserDAO {

        public void create(User user) throws SystemException;

        public User read(String Uid) throws SystemException;

        void update(User user, int plid) throws SystemException;

        public void delete(String Uid) throws SystemException;
}
