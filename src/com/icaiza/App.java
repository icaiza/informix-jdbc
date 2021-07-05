package com.icaiza;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;


public class App {

	public static void main(String[] args) {
		try {
			
			final HashMap<String, String> Parameters = new HashMap<>();
			Parameters.put("host", "");
			Parameters.put("base", "");
			Parameters.put("inst", "");
			Parameters.put("user", "");
			Parameters.put("pass", "");
	        
	        final ConnectionIfx conn = new ConnectionIfx(Parameters);
	        
	        // SELECT ---------------------------------------------------------------------------------------------------------
	        final String Qry = "SELECT * FROM yourtable WHERE stringField = ? AND IntegerField = ? AND stringField LIKE ?  ";
            conn.PrepareStatement(Qry);
            conn.stpr.clearParameters();
            conn.stpr.setString(1, "valueString");
            conn.stpr.setInt(2, 0);
            conn.stpr.setString(3, "%valueSearch");
            
            System.out.println( "GetSql " + conn.getSql(Qry, "valueString", 0,"%valueSearch" ) );
            
            final ResultSet rs = conn.consultar();
            while( rs.next() ){
            	System.out.println("Datos: " + rs.getString("referencia"));
            }
            
            
            
            rs.close();
            
            
            
            // UPDATE -----------------------------------------------------------------------------------------------------
            conn.PrepareStatement(" UPDATE yourTable SET stringField = ? WHERE integerField = ? AND stringField = 'A'");
            conn.stpr.clearParameters();
            conn.stpr.setString(1, "stringValue");
            conn.stpr.setInt(2, 0);
            final int update = conn.update(); //rows Affects
            
            
            // Option Transaction
            conn.BeginWork();
            // INSERT -----------------------------------------------------------------------------------------------------
			conn.PrepareStatement( " INSERT INTO yourTable VALUES(50, ?, ? 'A') " );
			conn.stpr.clearParameters();
			conn.stpr.setString(1, "stringValue");
            conn.stpr.setInt(2, 0);
            final int insert = conn.insert(); //rows Affects
            
            
            	// RETURN_GENERATED_KEYS
            final String insertHead = " INSERT INTO yourTable VALUES(0, ?) ";
			conn.PrepareStatement( insertHead, true );
			conn.stpr.clearParameters();
			conn.stpr.setString(1, "stringValue");
			final int rowsAffect = conn.stpr.executeUpdate();
			System.out.println( "Rows Affects: " + rowsAffect );
			
			final ResultSet rsGen = conn.stpr.getGeneratedKeys();
			final int serialNumber = rs.next() ? rs.getInt(1) : 0;

			// Option Transaction
			conn.Commit();
		        
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
			e.printStackTrace();
			e.printStackTrace();
		}

	}

}
