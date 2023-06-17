package com.urbots.maqueta;

import com.urbots.maqueta.models.*;

import static org.junit.Assert.assertTrue;


import org.junit.Test;



public class BBDDTest {
    @Test
    public  void TestArray(){
        Solar elemento = Solar.getsolar();
        assertTrue(elemento.getSizeElements()==2);
    }
}
