package server;

import static jooqdb.Tables.USER;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Collection;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.tools.json.JSONObject;
import org.jooq.tools.json.JSONParser;
import org.jooq.util.derby.sys.Sys;

import server.tokens.AuthenticationToken;
import server.tokens.RegistrationToken;
import server.tokens.Token;
import server.tokens.UserUpdateToken;
import server.AuthenticationManager;
import server.DBManager;

import com.google.gson.*;

public class Server extends WebSocketServer
{

	TokenHandler tokenhandler;
	
    public Server( int port ) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }


    public Server( InetSocketAddress address ) {
        super(address);
    }


    @Override
    public void onOpen( WebSocket conn, ClientHandshake handshake ) {
        this.sendToAll( "new connection: " + handshake.getResourceDescriptor() );
        System.out.println( conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!" );
    }

    @Override
    public void onClose( WebSocket conn, int code, String reason, boolean remote ) {
        this.sendToAll( conn + " has disconnected!" );
        System.out.println( conn + " has disconnected!" );
    }

    // Received a string from a client
    @Override
    public void onMessage( WebSocket conn, String message ) {
        //this.sendToAll( message );
        System.out.println( conn + ": " + message );

    	JSONReader reader = new JSONReader<Token>();
    	Token t = (Token)reader.readJson( message , Token.class );
    	System.out.println( "Recieved Token tag: " + t.sTag );
    	
    	TokenHandler tokenhandler = new TokenHandler( t, message, conn );
    	tokenhandler.run();
    }


    public void onFragment( WebSocket conn, Framedata fragment ) {
        System.out.println( "received fragment: " + fragment );
    }

    @Override
    public void onError( WebSocket conn, Exception ex ) {
        ex.printStackTrace();
        if( conn != null ) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    public void sendToAll( String text ) {
        Collection<WebSocket> con = connections();
        synchronized ( con ) {
            for( WebSocket c : con ) {
                c.send( text );
            }
        }
    }

    
}
