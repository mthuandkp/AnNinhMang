/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.sql.Timestamp;

/**
 *
 * @author mthuan
 */
public class DateUtils {
    public static String getLocalTimeEpoch(){
        return String.valueOf(new Timestamp(System.currentTimeMillis()).getTime());
    }
    
}
