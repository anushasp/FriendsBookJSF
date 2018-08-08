/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;


/**
 *
 * @author rama
 */
@ManagedBean
@SessionScoped
public class Login implements Serializable 
{

    /**
     * Creates a new instance of Login
     */
    private String id;
    private String password;
    private String msg;
    private FriendBookAccount theLoginAccount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public FriendBookAccount getTheLoginAccount() {
        return theLoginAccount;
    }

    public void setTheLoginAccount(FriendBookAccount theLoginAccount) 
    {
        this.theLoginAccount = theLoginAccount;
    }
   
   
   
   
    
    public Login()
    {
        //at the biginning, there is no login account
        theLoginAccount = null;
       
    }
    
    public String login() throws IOException 
    {
        
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (Exception e)
        {
           return ("Internal Error! Please try again later.123");
        }
        
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/saripellaa6306";
        
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try
        {
            //connect to the databse
            connection = DriverManager.getConnection(DATABASE_URL, 
                  "saripellaa6306", "1517381");
            //create statement
            statement = connection.createStatement();
            //search the accountID in the onlineAccount table
            resultSet = statement.executeQuery("Select * from friendbookaccount "
                    + "where userID = '" + id + "'");
            
            if(resultSet.next())
            {
                //the id is found, check the password
                if(password.equals(resultSet.getString(4)))
                {
                    //password is good
                   theLoginAccount = new FriendBookAccount(resultSet.getString(1),
                                      resultSet.getString(2),resultSet.getString(3),
                                      resultSet.getString(4),resultSet.getString(5),
                                      resultSet.getString(6));
                  
                    return "home";
                }
                else
                {
                    //password is not correct
                    id="";
                password="";
                   return "loginNotOk";
                }
            }
            else
            {
                 id="";
                password="";
                 return "loginNotOk";
            }
            
        }
        catch (SQLException e)
        {
            e.printStackTrace();
             return ("internalError");
           
        }
        finally
        {
            try
             {
                 statement.close();
                 connection.close();
                 resultSet.close();
             }
             catch(Exception e)
             {
                 e.printStackTrace();
             }
          
        }      
    }
   

}

