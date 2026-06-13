package a_a.theknife.common.models;

import java.io.Serializable;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import a_a.theknife.common.exceptions.InvalidPasswordException;

/**
 * La classe <code>GestorePassword</code> contiene i metodi necessari a stabilire se una password inserita
 * dall'utente soddisfa i <strong>requisiti minimi di sicurezza</strong>:<br>
 * <ul>
 *  <li>lunghezza minima di 8 caratteri;<br></li>
 *  <li>contiene almeno una lettera maiuscola;<br></li>
 *  <li>contiene almeno una lettera minuscola;<br></li>
 *  <li>contiene almeno una cifra;<br></li>
 *  <li>contiene almeno un carattere speciale , ; . : - _ \ \ | / ! ? ' \ " £ $ € % ( ) [ ] { } @ # °;<br></li>
 *  <li>non contiene 3 o più caratteri ripetuti in sequenza;<br></li>
 * </ul><br>
 * I requisiti di sicurezza sopraelencati vengono valutati
 * nella sequenza specificata dall'elenco.
 * @author Andrea Olmo
 * @author Andrea Napolitano
 */
public class GestorePassword implements Serializable{
    private static final int ITERATIONS = 600_000;
    private static final int KEY_LENGTH = 256;
    private static final SecureRandom RANDOM = new SecureRandom();
    
    /**
     * Il metodo stabilisce se la password inserita è valida, controllando tutti
     * i requisiti di sicurezza specificati.
     * @param password la stringa da validare
     * @return <code>true</code> se la password inserita rispetta tutti i requisiti, <code>false</code> altrimenti
     */
    public static boolean validate(String password){
        if(password.length() < 8){
            return(false);
        }
        if(password.equals(password.toLowerCase())){
            return(false);
        }
        if(password.equals(password.toUpperCase())){
            return(false);
        }
        if(!containsDigits(password)){
            return(false);
        }
        if(!containsSpecialChars(password)){
            return(false);
        }
        if(containsRepetitions(password)){
            return(false);
        }
        return(true);
    }
        
    /**
     * Questo metodo stabilisce se l'oggetto <code>GestorePassword</code> che esegue il
     * metodo contiene o no cifre numeriche.
     * @param password la stringa da validare
     * @return <code>true</code> se la password contiene almeno una cifra, <code>false</code> altrimenti
     */
    private static boolean containsDigits(String password){
        for(int i = 0; i < password.length(); i++){
            if(Character.isDigit(password.charAt(i))){
                return(true);
            }
        }
        return(false);
    }
    
    /**
     * Stabilisce se la stringa passatagli come parametro contiene almeno uno dei seguenti
     * caratteri speciali:<strong> , ; . : - _ \ | / ! ? ' " £ $ € % ( ) [ ] { } @ # °</strong>.
     * @param password la stringa da validare
     * @return <code>true</code> se la password inserita contiene almeno un carattere speciale, <code>false</code> altrimenti
     */
    private static boolean containsSpecialChars(String password){
        String specialChars = ",;.:+=-_\\|/*!?'\"£$€%()[]{}@#°";
        for(int i = 0; i < password.length(); i++){
            for(int j = 0; j < specialChars.length(); j++){
                if((password.charAt(i)) == (specialChars.charAt(j))){
                    return(true);
                }
            }
        }
        return(false);
    }
    
    /**
     * Stabilisce se la stringa passatagli come parametro contiene 3 o più caratteri
     * ripetuti in sequenza.
     * @param password la stringa da validare
     * @return <code>true</code> se la password inserita contiene almeno una sequenza di caratteri ripetuti tre volte o più, <code>false</code> altrimenti
     */
    private static boolean containsRepetitions(String password){
        for(int i = 0; i < (password.length() - 2); i++){
            if(password.charAt(i) == password.charAt(i + 1) && password.charAt(i) == password.charAt(i + 2)){
                return(true);
            }
        }
        return(false);
    }
    
    /**
     * Genera una password casuale in uno degli 8 formati possibili specificati
     * nell'implementazione del metodo.
     * La password generata soddisfa tutti i requisiti di sicurezza.
     * @return una password sicura, su cui applicare l'hashing.
     */
    public static String securePasswordGenerator(){
        
        final String LOWER_LETTERS = "abcdefghijklmnopqrstuvwxyz";
        final String UPPER_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String DIGITS = "0123456789";
        final String SPEC_CHARS = ",;.:-_\\|/!?\'\"£$€%()[]{}@#° ";
        
        String password = "";
        while(!validate(password)){
            switch(RANDOM.nextInt(1, 9)){
                case 1 -> { // genera una password nel formato Xxxxxxx*00
                    password += UPPER_LETTERS.charAt(RANDOM.nextInt(26));
                    for(int j = 0; j < 6; j++){
                        password += LOWER_LETTERS.charAt(RANDOM.nextInt(26));
                    }
                    password += SPEC_CHARS.charAt(RANDOM.nextInt(27));
                    for(int j = 0; j < 2; j++){
                        password += DIGITS.charAt(RANDOM.nextInt(10));
                    }
                }
                case 2 -> { // genera una password nel formato xXxxxxx0*0
                    password += LOWER_LETTERS.charAt(RANDOM.nextInt(26));
                    password += UPPER_LETTERS.charAt(RANDOM.nextInt(26));
                    for(int j = 0; j < 5; j++){
                        password += LOWER_LETTERS.charAt(RANDOM.nextInt(26));
                    }
                    password += DIGITS.charAt(RANDOM.nextInt(10));
                    password += SPEC_CHARS.charAt(RANDOM.nextInt(27));
                    password += DIGITS.charAt(RANDOM.nextInt(10));
                }
                case 3 -> { // genera una passowrd nel formato xx0X*xxx0*
                    for(int j = 0; j < 2; j++){
                        password += LOWER_LETTERS.charAt(RANDOM.nextInt(26));
                    }
                    password += DIGITS.charAt(RANDOM.nextInt(10));
                    password += UPPER_LETTERS.charAt(RANDOM.nextInt(26));
                    password += SPEC_CHARS.charAt(RANDOM.nextInt(27));
                    for(int j = 0; j < 3; j++){
                        password += LOWER_LETTERS.charAt(RANDOM.nextInt(26));
                    }
                    password += DIGITS.charAt(RANDOM.nextInt(10));
                    password += SPEC_CHARS.charAt(RANDOM.nextInt(27));
                }
                case 4 -> { // genera una password nel formato *Xxxxx00x*
                    password += SPEC_CHARS.charAt(RANDOM.nextInt(27));
                    password += UPPER_LETTERS.charAt(RANDOM.nextInt(26));
                    for(int j = 0; j < 4; j++){
                        password += LOWER_LETTERS.charAt(RANDOM.nextInt(26));
                    }
                    for(int j = 0; j < 2; j++){
                        password += DIGITS.charAt(RANDOM.nextInt(10));
                    }
                    password += LOWER_LETTERS.charAt(RANDOM.nextInt(26));
                    password += SPEC_CHARS.charAt(RANDOM.nextInt(27));
                }
                case 5 -> { // genera una password nel formato Xx*xxxx*00xx
                    password += UPPER_LETTERS.charAt(RANDOM.nextInt(26));
                    password += LOWER_LETTERS.charAt(RANDOM.nextInt(26));
                    password += SPEC_CHARS.charAt(RANDOM.nextInt(27));
                    for(int j = 0; j < 4; j++){
                        password += LOWER_LETTERS.charAt(RANDOM.nextInt(26));
                    }
                    password += SPEC_CHARS.charAt(RANDOM.nextInt(27));
                    for(int j = 0; j < 2; j++){
                        password += DIGITS.charAt(RANDOM.nextInt(10));
                    }
                    for(int j = 0; j < 2; j++){
                        password += LOWER_LETTERS.charAt(RANDOM.nextInt(26));
                    }
                }
                case 6 -> { // genera una password nel formato xXx*xxx0*0xx
                    password += LOWER_LETTERS.charAt(RANDOM.nextInt(26));
                    password += UPPER_LETTERS.charAt(RANDOM.nextInt(26));
                    password += LOWER_LETTERS.charAt(RANDOM.nextInt(26));
                    password += SPEC_CHARS.charAt(RANDOM.nextInt(27));
                    for(int j = 0; j < 3; j++){
                        password += LOWER_LETTERS.charAt(RANDOM.nextInt(26));
                    }
                    password += DIGITS.charAt(RANDOM.nextInt(10));
                    password += SPEC_CHARS.charAt(RANDOM.nextInt(27));
                    password += DIGITS.charAt(RANDOM.nextInt(10));
                }
                case 7 -> { // genera una passowrd nel formato xx0X*xxx0*Xx
                    for(int j = 0; j < 2; j++){
                        password += LOWER_LETTERS.charAt(RANDOM.nextInt(26));
                    }
                    password += DIGITS.charAt(RANDOM.nextInt(10));
                    password += UPPER_LETTERS.charAt(RANDOM.nextInt(26));
                    password += SPEC_CHARS.charAt(RANDOM.nextInt(27));
                    for(int j = 0; j < 3; j++){
                        password += LOWER_LETTERS.charAt(RANDOM.nextInt(26));
                    }
                    password += DIGITS.charAt(RANDOM.nextInt(10));
                    password += SPEC_CHARS.charAt(RANDOM.nextInt(27));
                    password += UPPER_LETTERS.charAt(RANDOM.nextInt(26));
                    password += LOWER_LETTERS.charAt(RANDOM.nextInt(26));
                }
                case 8 -> { // genera una password nel formato *Xx*xx00x*Xx
                    password += SPEC_CHARS.charAt(RANDOM.nextInt(27));
                    password += UPPER_LETTERS.charAt(RANDOM.nextInt(26));
                    password += LOWER_LETTERS.charAt(RANDOM.nextInt(26));
                    password += SPEC_CHARS.charAt(RANDOM.nextInt(27));
                    for(int j = 0; j < 2; j++){
                        password += LOWER_LETTERS.charAt(RANDOM.nextInt(26));
                    }
                    for(int j = 0; j < 2; j++){
                        password += DIGITS.charAt(RANDOM.nextInt(10));
                    }
                    password += LOWER_LETTERS.charAt(RANDOM.nextInt(26));
                    password += SPEC_CHARS.charAt(RANDOM.nextInt(27));
                    password += UPPER_LETTERS.charAt(RANDOM.nextInt(26));
                    password += LOWER_LETTERS.charAt(RANDOM.nextInt(26));
                }
            }
        }
        return(password);
    }
    
    /**
     * Il metodo calcola l'hash della stringa passatagli come parametro.
     * @param password la password inserita
     * @return l'hash della password in chiaro inserita
     * @throws <code>Exception</code>
     */
    public static String hashPassword(String password) throws InvalidPasswordException{
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        byte[] hash;
        try{
            hash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        }
        catch(NoSuchAlgorithmException | InvalidKeySpecException e){
            throw(new InvalidPasswordException());
        }

        return(ITERATIONS + ":" + Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash));
    }

    /**
     * Verifica che la password inserita dall'utente (ad esempio in fase di login)
     * corrisponda alla password precedentemente salvata nel database, calcolando
     * l'hash della password inserita con l'hash già memorizzato nel database.
     * @param password la password inserita
     * @param storedHash la password (hashata) salvata nel database
     * @return <code>true</code> se gli hash delle password corrispondono, <code>false</code> altrimenti
     * @throws <code>Exception</code>
     */
    public static boolean verifyPassword(String password, String storedHash) throws InvalidPasswordException{
        String[] parts = storedHash.split(":");

        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = Base64.getDecoder().decode(parts[1]);
        byte[] expectedHash = Base64.getDecoder().decode(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, expectedHash.length * 8);

        byte[] actualHash;
        try{
            actualHash = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        }
        catch(NoSuchAlgorithmException | InvalidKeySpecException e){
            throw(new InvalidPasswordException());
        }

        return(java.security.MessageDigest.isEqual(expectedHash, actualHash));
    }
}