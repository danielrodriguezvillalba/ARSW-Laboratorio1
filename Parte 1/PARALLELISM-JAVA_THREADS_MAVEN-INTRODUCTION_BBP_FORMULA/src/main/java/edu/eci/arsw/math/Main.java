/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.math;

import java.lang.Runtime;

/**
 *
 * @author hcadavid
 */
public class Main {

    public static void main(String a[]) {
    	Runtime ru = Runtime.getRuntime();
        //System.out.println(bytesToHex(PiDigits.getDigits(0, 100000)));
        //System.out.println(bytesToHex(PiDigits.getDigits(20, 100000)));
        //System.out.println(bytesToHex(PiDigits.getDigits(1, 1000000)));
        //System.out.println(bytesToHex(PiDigits.getDigits(0, 20,10)));
    	System.out.println(ru.availableProcessors());
    	System.out.println(bytesToHex(PiDigits.getDigits(0, 10000,200)));	
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<hexChars.length;i=i+2){
            //sb.append(hexChars[i]);
            sb.append(hexChars[i+1]);            
        }
        return sb.toString();
    }

}
