package server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import server.tokens.AuthenticationToken;
import server.tokens.RegistrationToken;
import server.tokens.UserUpdateToken;
import server.DBManager;

public class AuthenticationManager {
	
	private DBManager dbm;
	
	public AuthenticationManager( DBManager dbmanager ) {
		dbm = dbmanager;
	}
	
	public boolean AuthenticateUser( AuthenticationToken t ) {

		String pw = dbm.getUserPassword( t.email );
		if (pw != null) {
			System.out.println("Yes! Found password! Its: " + pw);
		}
		else
			System.out.println("No... User not found...");
		
		try {
			String hashpw = dbm.hash( t.password ).toString();
			
			System.out.println("Comparing pw to: " + hashpw);
			//System.out.println("a in has is: " + dbm.hash( "a" ).toString());
			
			if (pw != null ) {
				if ( /*hashpw*/ t.password.compareTo(pw) == 0 ) {
					System.out.println("-------------------------------");
					System.out.println("AUTHENTICATED USER SUCCESSFULLY!!");	
					System.out.println("-------------------------------");
					return true;
				}
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	public boolean RegisterUser ( RegistrationToken t ) {
		boolean result = false;
		
		dbm.CreateUser(t);
		System.out.println("-------------------------------");
		System.out.println("CREATES USER SUCCESSFULLY!!");
		System.out.println("-------------------------------");
		
		return result;
	}
	
	public boolean UpdateUser ( UserUpdateToken t ) {
		boolean result = false;
		
		dbm.UpdateUser(t);
		System.out.println("-------------------------------");
		System.out.println("UPDATED USER SUCCESSFULLY!!");
		System.out.println("-------------------------------");
		
		return result;
	}
}
