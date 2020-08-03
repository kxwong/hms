/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

/**
 *
 * @author kxwong
 */
public class Crypto {
    
    public Crypto(){
    }
    
    public String B64Encryption(String input){
        return Base64.getEncoder().encodeToString(input.getBytes());
    }
    
    public String B64Decryption(String input){
        return new String(Base64.getDecoder().decode(input));
    }
    
    public String URLEncryption(String input){
        return URLEncoder.encode(input);
    }
    
    public String URLDecryption(String input){
        return URLDecoder.decode(input);
    }
    
    public String FPEncode(String input){
        return B64Encryption(URLEncryption(input));
    }
    
    public String FPDecode(String input){
        return URLDecryption(B64Decryption(input));
    }
    
    public String REncode(String input){
        return URLEncryption(B64Encryption(input));
    }
    
    public String RDecode(String input){
        return B64Decryption(URLDecryption(input));
    }
    
    public String HEncode(String input){
        return URLEncryption(REncode(input));
    }
    
    public String HDecode(String input){
        return RDecode(URLDecryption(input));
    }
    
    public String CEncode(String input){
        return REncode(HEncode(input));
    }
    
    public String CDecode(String input){
        return HDecode(RDecode(input));
    }
    
    public String RBEncode(String input){
        return REncode(URLEncryption(input));
    }
    
    public String RBDecode(String input){
        return URLDecryption(RDecode(input));
    }
    
    public String HTEncode(String input){
        return RBEncode(HEncode(input));
    }
    
    public String HTDecode(String input){
        return HDecode(RBDecode(input));
    }
    
    public String RREncode(String input){
        return HTEncode(CEncode(input));
    }
    
    public String RRDecode(String input){
        return CDecode(HTDecode(input));
    }
    
    public String BEncode(String input){
        return CEncode(HTEncode(input));
    }
    
    public String BDecode(String input){
        return HTDecode(CDecode(input));
    }
    
    public String PWEncode(String input){
        return FPEncode(CEncode(input));
    }
    
    public String PWDecode(String input){
        return CDecode(FPDecode(input));
    }
    
    public String UNEncode(String input){
        return RBEncode(CEncode(input));
    }
    
    public String UNDecode(String input){
        return CDecode(RBDecode(input));
    }
    
    public String FAEncode(String input){
        return BEncode(RREncode(input));
    }
    
    public String FADecode(String input){
        return RRDecode(BDecode(input));
    }
    
    public String FBEncode(String input){
        return UNEncode(RREncode(input));
    }
    
    public String FBDecode(String input){
        return RRDecode(UNDecode(input));
    }
    
    public String ANEncode(String input){
        return UNEncode(FAEncode(input));
    }
    
    public String ANDecode(String input){
        return FADecode(UNDecode(input));
    }
    
    public String CAEncode(String input){
        return ANEncode(FBEncode(input));
    }
    
    public String CADecode(String input){
        return FBDecode(ANDecode(input));
    }
    
    public String ENEncode(String input){
        return FAEncode(ANEncode(input));
    }
    
    public String ENDecode(String input){
        return ANDecode(FADecode(input));
    }
    
    public String VIEncode(String input){
        return UNEncode(HTEncode(input));
    }
    
    public String VIDecode(String input){
        return HTDecode(UNDecode(input));
    }
}
