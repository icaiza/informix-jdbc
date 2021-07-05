package com.icaiza;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.HashMap;

public class ConnectionIfx {

    public Connection conn = null;
    private Statement st;
    public PreparedStatement stpr = null;
    public CallableStatement cStmt = null;

    public ConnectionIfx(boolean degub) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        this.st = null;
        Class.forName("com.informix.jdbc.IfxDriver").newInstance();
        
        if( degub ) {
        	this.conn = DriverManager.getConnection("jdbc:informix-sqli://192.168.1.2:1528/senefelder:INFORMIXSERVER=dsllo", "informix", "PersefoNE9870");
        } else {
        	// this.conn = DriverManager.getConnection("jdbc:informix-sqli://192.168.1.2:1528/senefelder:INFORMIXSERVER=dsllo", "informix", "PersefoNE9870");
        	this.conn = DriverManager.getConnection("jdbc:informix-sqli://192.168.1.201:1528/senefelder:INFORMIXSERVER=servermain;", "intranet", "ags1921");
        }

    }
    
    public ConnectionIfx() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        this.st = null;
        Class.forName("com.informix.jdbc.IfxDriver").newInstance();
        
        //com = context.getInitParameter("com");
        //Producción
        this.conn = DriverManager.getConnection("jdbc:informix-sqli://192.168.1.201:1528/senefelder:INFORMIXSERVER=servermain;", "intranet", "ags1921");
        
        //Desarrollo
        //this.conn = DriverManager.getConnection("jdbc:informix-sqli://192.168.1.2:1528/senefelder:INFORMIXSERVER=dsllo", "informix", "PersefoNE9870");
    }

    public ConnectionIfx(HashMap<String, String> Parameters) 
            throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        this.st = null;
        final String Host = Parameters.get("host");
        final String Base = Parameters.get("base");
        final String Inst = Parameters.get("inst");
        final String User = Parameters.get("user");
        final String Pass = Parameters.get("pass");

        final String UrlC = "jdbc:informix-sqli://" + Host + "/" + Base + ":INFORMIXSERVER=" + Inst;
        Class.forName("com.informix.jdbc.IfxDriver").newInstance();
        this.conn = DriverManager.getConnection(UrlC, User, Pass);
    }

    public void BeginWork() throws SQLException{        
        conn.setAutoCommit(false);      //Deshabilita el autocommit
    }
    
    public void Commit() throws SQLException{
        conn.commit();  //Guarda 
    }
    
    public void RollBack() throws SQLException{
        conn.rollback();    //Deshacer los cambios en la tablas.
    }
    
    public String getSql(String Qry, Object...values) {
    	
    	for( Object val : values ) {
    		if( val instanceof Long || val instanceof Integer || val instanceof Double || val instanceof Float ) {
    			Qry = Qry.replaceFirst("[?]", val.toString());
    		} else if( val == null ) {
    			Qry = Qry.replaceFirst("[?]", "NULL");
    		} else {
    			Qry = Qry.replaceFirst("[?]", "'" + val.toString() + "'");
    		}
    	}
    	
    	return Qry;
    }
    
    //Parsear Valores
    public void parseValues(Object... Values) throws ParseException, SQLException {
        int Index = 0;
        for (Object val : Values) {
            if(val instanceof String) {
                this.stpr.setString(++Index, (String)val);
            } else if(val instanceof Date) {
                this.stpr.setDate(++Index, (Date) val);
            }else if(val instanceof Long){
                this.stpr.setLong(++Index, (long) val);
            }else if( val instanceof Integer){
                this.stpr.setInt(++Index, (int) val);
            }else{
                //new throws IllegalThreadStateException("");
            }
        }
    }
    
	public void PrepareCallable(String Qry) throws SQLException {
    	this.cStmt = this.conn.prepareCall( Qry );    	
    }
    
	// Prepara el Statement 
    public void PrepareStatement(String Qry) throws SQLException {    	
    	this.stpr = this.conn.prepareStatement(Qry);    	
    }
	
    // Prepara el Statement 
    public void PrepareStatement(String Qry, boolean returnKey) throws SQLException {
    	if( returnKey ) {
    		this.stpr = this.conn.prepareStatement(Qry, PreparedStatement.RETURN_GENERATED_KEYS);	
    	} else {
    		this.stpr = this.conn.prepareStatement(Qry);
    	}
    }

    // Ejecuta el Qry
    public ResultSet exeQry(String Qry) throws SQLException{
        this.st = conn.createStatement();
        return this.st.executeQuery(Qry);
    }    
    // Realiza la consulta

    public ResultSet consultar() throws SQLException {
        return this.stpr.executeQuery();
    }

    // Realiza el insert y devuelve la cantidad de rows afectados

    public int insert() throws SQLException {
    	return this.stpr.executeUpdate();
    }

    // Realiza el update y devuelve la cantidad de rows afectados

    public int update() throws SQLException {
        return this.stpr.executeUpdate();
    }

    // Cierra el resultset

    public void closeconsultar() throws SQLException {
        this.consultar().close();
    }

    // Cierra el statement

    public void closeStatement() throws SQLException {
        if (this.stpr != null) {
            this.stpr.close();
        }
    }

    // Cierra la conexión

    public void closeconnection() throws SQLException {
        if (this.conn != null) {
            this.conn.close();
        }
    }

    // Cierra todo
    public void close() throws SQLException {
        //this.closeconsultar();
        //this.closeStatement();
        this.closeconnection();
    }

}
