package com.urbots.maqueta;

import static org.junit.Assert.assertTrue;

import com.urbots.maqueta.models.Solar;

import org.junit.Test;

public class UDPTest {
    @Test
    public  void TestFrame(){
       //Creem un objecte Solar
        Solar element = Solar.getsolar();
        //Activem el 1 i el 2
        element.setStatus(true,0);
        element.setStatus(true,1);
        //La resta desactivem
        element.setStatus(false,2);
        element.setStatus(false,3);
        element.setPosicio(123);
        //Miramos que se ponga 1100 0000 en el frame enabled y sea d e8
        assertTrue(element.getFrameEnabled().length()==8);
        assertTrue(element.getFrameEnabled().equals("11000000"));
        //Miramos que el frame sea S|11000000|123|123|123|123|123|
        assertTrue(element.generateFrame().equals("S|11000000|123|123|123|123|123|"));
    }
}
