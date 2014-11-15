import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException; 
import javax.crypto.BadPaddingException;
import javax.crypto.NoSuchPaddingException;
public class EncodeDecode {
   static KeyPair keypair;
   static Cipher cipher;
   public static void main(String[] sa) {
      try {
  	 	   String text = "imagedata:imagedata:imagedata:imagedata MIDIICONMARKER[0:576:Ludovico Enaudi][813:318:oiugb]"
      	   	   + "URLLINKMARKER[123:111:www.vk.com][222:333:www.mail.ru]"
      		   + "IMAGEMARKER[20:30:765:456:pict.][40:50:432:123:Picture]"
      		   + "WHITEBOARDLINKMARKER[54:32:mena][98:23:NAME,.]"
      		   + "LINEMARKER[24:64:234:876:123:981:234:120][23:63:233:875:122:980:233:119]"
      		   + "RECTANGLEMARKER[2:30:20:345:854:0:0:0:0:0:0][2:31:21:346:855:1:1:1:1:1:0]"
      		   + "CIRCLEMARKER[20:2:1000:1200:123:123:123:123:123:123]"
      		   + "TEXTBOXMARKER[14:2:10:162:238:23:16:150:New Roman:::::TeXt text tex:t:]"
      		   + "[16:20:30:162:238:239:167:43:Helvatica::.prbn::iwbn]";
  	 	   KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
  	  	   keyGen.initialize(1024);
  	  	   KeyPair keyPair = keyGen.genKeyPair();
   	  	   PrivateKey privateKey = keyPair.getPrivate();
       	   PublicKey publicKey = keyPair.getPublic();
    	   //System.out.println(privateKey + "\n" + publicKey);    	   
		   Cipher cipher = Cipher.getInstance("RSA");
		   byte[] encriptedData = new EncodeDecode().encript(text, cipher, publicKey);
		   //System.out.println(new String(encriptedData));
		   String decriptedData = new EncodeDecode().decript(encriptedData, cipher, privateKey);
		   System.out.println(decriptedData);
	  } catch (InvalidKeyException e) {
      	e.printStackTrace();
      } catch (IllegalBlockSizeException e) {
      	e.printStackTrace();
      } catch (BadPaddingException e) {
      	e.printStackTrace();
      } catch (NoSuchAlgorithmException e) {
      	e.printStackTrace();
      } catch (NoSuchPaddingException e) {
      	e.printStackTrace();
      }	
   }	     
   public byte[] encript(String text, Cipher cipher, PublicKey publicKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException{	   
   	  cipher.init(Cipher.ENCRYPT_MODE, publicKey);
      byte[] textToBytes = text.getBytes();
      byte[] interm = new byte[100];
      byte[] toReturn = new byte[0];
      byte[] arr;
      byte[] cipherData;
      int a = textToBytes.length - (textToBytes.length % interm.length);
      int i = 0;
      while (i != a) {
      	  for (int x = 0; x < interm.length; x++) {
              interm[x] = textToBytes[i];
              i++;
          }
          cipherData = cipher.doFinal(interm);
          toReturn = append(toReturn, cipherData);
      }
      arr = new byte[textToBytes.length - a];	
      int r = 0;
      for (int b = a; b < textToBytes.length; b++) {
          arr[r] = textToBytes[b];
          r++;
      }
      cipherData = cipher.doFinal(arr);
      toReturn = append(toReturn, cipherData);
      return toReturn;
   }                  
   public String decript(byte[] encriptedData, Cipher cipher, PrivateKey privateKey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException{		  
	  cipher.init(Cipher.DECRYPT_MODE, privateKey);
      byte[] interm = new byte[128];
      byte[] returnTo = new byte[0];
      byte[] arr;
      byte[] cipherData;
      int a = encriptedData.length - (encriptedData.length % interm.length);
      int i = 0;
      while (i != a) {
      	  for (int x = 0; x < interm.length; x++) {
              interm[x] = encriptedData[i];
              i++;
          }
          cipherData = cipher.doFinal(interm);
          returnTo = append(returnTo, cipherData); 
      } 
	  return new String(returnTo);
    }  
    private byte[] append(byte[] pre, byte[] post) {
    	byte[] toReturn = new byte[pre.length + post.length];
        for (int i = 0; i < pre.length; i++) {
        	toReturn[i] = pre[i];
        }
        for(int i = 0; i < post.length; i++) {
            toReturn[i + pre.length] = post[i];
        }
        return toReturn;
    }
}     
