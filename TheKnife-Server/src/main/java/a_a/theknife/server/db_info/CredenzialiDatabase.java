package a_a.theknife.server.db_info;
public class CredenzialiDatabase{
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/TheKnife";
    private static final String DB_USER = "AndreaHo67";
    private static final String DB_PASSWORD = "Montecristo_04";
    
    public static final String getDbUrl(){
        return(DB_URL);
    }
    
    public static final String getDbUser(){
        return(DB_USER);
    }
    
    public static final String getDbPassword(){
        return(DB_PASSWORD);
    }
}