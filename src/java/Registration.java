/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author rama
 */
@ManagedBean
@RequestScoped
public class Registration {

    /**
     * Creates a new instance of Registration
     */
    private String name;
    private String gender;
    private String userID;
    private String password;
    private String school;
    private String bday;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getBday() {
        return bday;
    }

    public void setBday(String bday) {
        this.bday = bday;
    }
    
    public String register()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (Exception e)
        {
            return ("Internal Error! Please try again later.");
        }
        //declare varaibles
            String str = userID;
            boolean isnum = false;
            boolean isspec = false;
            boolean ischar = false;
            for (int i = 0, n = str.length(); i < n; i++)
            {
                char c = str.charAt(i);
                if (isnum == false) 
                {
                    if ((Character.getNumericValue(c)) >= 0 && (Character.getNumericValue(c)) <= 9) 
                    {
                        isnum = true;
                        continue;
                    }
                }
                if (isspec == false)
                {
                    if (c == '#' || c == '?' || c == '*' || c == '!')
                    {
                        isspec = true;
                        continue;
                    }
                }
                if (ischar == false) 
                {
                    if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') 
                    {
                        ischar = true;
                        continue;
                    }
                }
            }
            if (!ischar || !isspec || !isnum) 
            {
               return("userID din't meet the requirements! Try again");
            }
        
        
         if (password.equals(userID)) 
         {
            return("userID and  password are same! Enter a new password");
         }
       
        //Complete the database part below
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/saripellaa6306";
        
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try
        {
            //connect to the databse
            connection = DriverManager.getConnection(DATABASE_URL, 
                    "saripellaa6306", "1517381");
            //crate the statement
            statement = connection.createStatement();
            
            //do a query
            resultSet = statement.executeQuery("Select * from friendbookaccount"
                    + " where userID = '" + userID + "'");
            if(resultSet.next())
            {
                //either the ssn is used or the id is used
                 return("Either you have an online account already "
                        + "or your online ID is not available to register");
            }
            else
            {
                //insert a record into onlineAccount
              int r = statement.executeUpdate("insert into friendbookaccount values "
                        + "('" + name + "', '" + gender + "', '"+ userID + "', '"+ password + "', '"+ bday + "', '"
                        + school +"')");
               return ("Registration Successful! Please "
                         + "return to login your account.");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return ("Internal Error! Please try again later.");
        }
        finally
        {
             //close the database
             try
             {
                 resultSet.close();
                 statement.close();
                 connection.close();
             }
             catch(Exception e)
             {
                 e.printStackTrace();
                 return ("Internal Error! Please try again later.");
             }
        }
    }
}
