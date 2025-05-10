package org.example.ispwprogect;

import org.example.ispwproject.utils.database.DBConnection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestConnectionDB {

    @Test
    void testGetConnection() throws org.example.ispwproject.utils.exception.SystemException {

        int value = 0;

        if (DBConnection.getDBConnection() != null) {
            value = 1;
        }

        Assertions.assertEquals(1, value);

    }

}
