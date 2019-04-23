/*
 * This class implements the queries needed for the CountryNameService.  The class uses Oracle spatial calls.  
 * The data must be stored in the Oracle Spatial database.   The spatial objects must be geodetic SRID = 8307.
 *
 */
package mil.nga.to6;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import mil.nga.country_name.v2.BoundaryInfoBean;
import mil.nga.country_name.v2.CountryNameBean;
import mil.nga.country_name.v2.GetCountryNamesResponse;
import mil.nga.country_name.v2.GetParameterInfoResponse;
import mil.nga.security.SecurityElement;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;
import us.gov.ic.ism.v2.ClassificationType;

/**
 * Provides the backend for the CountryNameService using Oracle Spatial Calls
 * to do the spatial queries.
 * @author David Jennings
 * 
 */
public class QueryWorldSDO {
    
    static final Category log = Category.getInstance(QueryWorldSDO.class);
    
    // Configuration Properties with several configurable paramters deployed with the application
    final static String STR_CONFIG_FILE = "/Configuration.properties";
    final static String STR_LOG4J_FILE = "/log4j.properties";
    
    // Variable loaded from the configuration file; while I set them to default values 
    // these are overwritten by the values in the Configuration.properties file
    
    String m_connectionURL = "jdbc:oracle:thin:@oracle1:1521:SDE";
    String m_userID = "USERNAME";
    String m_userPassword = "PASSWORD";
    String m_codeLookupTable = "CCODES";
    String m_parameterTable = "CPARAMS";
    String m_layerInfoTable = "LAYERINFO";
    String m_JNDI_Name = "jdbs/ws";
    String m_SDO_GEOMETRY = "";
    boolean m_useWKT = false; 
    int MAX_POINTS = 200;
    double MAX_BUFFER = 500;
    
    // Other class wide scoped variables 
    Connection m_con = null;
    Statement m_stmt = null;
    String m_strCodes[] = null;

    /**
     * Default Constructor doesn't do a thing.
     */
    public QueryWorldSDO() {

    }

    /**
     * 
     * Turns the stack trace into a string.
     * @param t An Exeception Type
     * @return String
     */
    private String getStackTrace(Throwable t) {
      
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      t.printStackTrace(pw);
      pw.flush();
      return sw.toString();
    }    
    
    /**
     * Loads parametes from an Inputstream  
     * @param pis
     * @return true if the load was successful; otherwise false
     */
    private boolean loadPropertiesFromStream(InputStream pis) {

        // Loads the properties from the configuration file 
        boolean ok = true;

        // Set some null strings to hold the properties for validation

        String strCodeLookupTable = null;
        String strParameterTable = null;
        String strLayerInfoTable = null;
        String strMaxPoints = null;
        String strMaxBuffer = null;
        
        String strJNDI_Name = null;
        String strUseWKT = null;
        
        String strServer = null;
        String strUser = null;
        String strPassword = null;
        String strSID = null;
        String strPort = null;
        
        try {
            // Setup the logger
            InputStream logger_is = getClass().getResourceAsStream(STR_LOG4J_FILE);
            Properties logger_p = new Properties();
            logger_p.load(logger_is);            
            PropertyConfigurator.configure(logger_p);            
            logger_is.close();            
            log.info("loadPropertiesFromStream Begin");
            
            // Get the properties
            Properties p = new Properties();
            p.load(pis);

            // Values read from the config file
            strCodeLookupTable = p.getProperty("LookupTable");
            strParameterTable = p.getProperty("ParamsTable");
            strLayerInfoTable = p.getProperty("LayerIntoTable");
            strMaxPoints = p.getProperty("MAX_POINTS");
            strMaxBuffer = p.getProperty("MAX_BUFFER");

            strJNDI_Name = p.getProperty("JNDI_Name");
            strUseWKT = p.getProperty("useWKT");
            
            strServer = p.getProperty("Server");
            strUser = p.getProperty("User");
            strPassword = p.getProperty("Password");
            strSID = p.getProperty("SID");
            strPort = p.getProperty("Port");

            if (strJNDI_Name == null) {
                if (strServer == null || strUser == null || strPassword == null || 
                        strSID == null || strPort == null || strCodeLookupTable == null || 
                        strParameterTable == null || strMaxPoints == null || strMaxBuffer == null ||
                        strLayerInfoTable == null) {
                    ok = false;
                } else {
                    m_connectionURL = "jdbc:oracle:thin:@" + strServer.trim() + ":" + 
                            strPort.trim() + ":" + strSID.trim();
                    m_userID = strUser.trim();
                    m_userPassword = strPassword.trim();
                    m_codeLookupTable = strCodeLookupTable.trim();
                    m_parameterTable = strParameterTable.trim();
                    m_layerInfoTable = strLayerInfoTable.trim();
                    MAX_BUFFER = Double.valueOf(strMaxBuffer.trim());
                    MAX_POINTS = Integer.valueOf(strMaxPoints.trim());           
                    m_JNDI_Name = null;                    
                }
                
            } else {
                if (strCodeLookupTable == null || strParameterTable == null || 
                        strMaxPoints == null || strMaxBuffer == null ||
                        strLayerInfoTable == null) {
                    ok = false;
                } else {
                    m_JNDI_Name = strJNDI_Name.trim();
                    m_codeLookupTable = strCodeLookupTable.trim();
                    m_parameterTable = strParameterTable.trim();
                    m_layerInfoTable = strLayerInfoTable.trim();
                    MAX_BUFFER = Double.valueOf(strMaxBuffer.trim());
                    MAX_POINTS = Integer.valueOf(strMaxPoints.trim());                                        
                }                
            }

            // optional Parameter (default to true)
            m_useWKT = true;
            if (strUseWKT != null) {
                if (strUseWKT.equalsIgnoreCase("true")) {
                    m_useWKT = true;
                } else if (strUseWKT.equalsIgnoreCase("false")) {
                    m_useWKT = false;
                } else {
                    throw new Exception("Unrecognized useWKT value in Configuration File. Must be either True or False");
                }
            }
            
            
        } catch (Exception e) {
            m_connectionURL = "Error in loadPropertiesFromStream: " + e.getMessage();
            ok = false;
            log.debug(getStackTrace(e));
        }

        log.info("loadPropertiesFromStream End");
        return ok;
    }

    
    /**
     * Get's Parameter information from the Parameter table.  User would 
     * use this to learn more about what parameters are valid.  The valid
     * values are configurable post deployment.  
     * @param parameter should be one of the parameters for GetCountryNames
     * @return ParameterInfoBean includes the Description and values.
     * 
     */
    public GetParameterInfoResponse getParameterInfo(String parameter) {

        // I used an object to return the values (Simple Bean with setters and getters)
        GetParameterInfoResponse rslt = new GetParameterInfoResponse();

        // We'll need a statment and resultset
        Statement stmt = null;
        ResultSet rset = null;
            
        try {
            // Setup the logger
            InputStream logger_is = getClass().getResourceAsStream(STR_LOG4J_FILE);
            Properties logger_p = new Properties();
            logger_p.load(logger_is);            
            PropertyConfigurator.configure(logger_p);            
            logger_is.close();            
            log.info("getParameterInfo Begin");
            
            // Open a connection to the database
            // open DB Connection            
            if (!openConnection()) {
                throw new Exception("Failed to open database connection.  If problem persists contact the service provider");
            }


            
            // Here's the SQL we'll send to the database
            // Using UPPER allows the paramter to be case insensitive
            String strSQL = "SELECT * from ";
            strSQL += m_parameterTable;
            strSQL += " where UPPER(name) = ";
            strSQL += "UPPER('" + parameter + "')";

            log.info("getParameterInfo: " + strSQL);
            
            // Query the database
            stmt = m_con.createStatement();
            rset = stmt.executeQuery(strSQL);

            // Get the first result (There should only be one)


            if (rset.next()) {                
                rslt.setDescription(rset.getString("DESCRIPTION"));
                rslt.setValues(rset.getString("VALS"));
            } else {
                // Then parameter was not found
                rslt.setDescription("ERROR");
                rslt.setValues("Parameter: " + parameter + " was not found");

            }

            rset.close();
            stmt.close();
            
            // The parameters are all unlcassified no dissementation controls
            SecurityElement sec = new SecurityElement();
            sec.setClassification(ClassificationType.U);
            sec.getOwnerProducer().add("USA");
            
            rslt.setSecurity(sec);
            
        } catch (Exception e) {
            
            // Then parameter was not found
            rslt.setDescription("ERROR");
            rslt.setValues("Unexpected Error: " + e.getMessage());
            log.error(getStackTrace(e));
        } finally {
            try {                
                rset.close();
            } catch (Exception e) {
                // ignore e
            }
            try {                
                stmt.close();
            } catch (Exception e) {
                // ignore e
            }            
            log.info("Closing Connection 1");
            closeConnection();
            
        }

        log.info("getParameterInfo End");

        return rslt;
    }

    /**
     * This function opens a connection to the database.  
     * @return true if successful
     */
    private boolean openConnection() {
                
        
        boolean blnOK = false;
        try {            
            // Setup the logger
            InputStream logger_is = getClass().getResourceAsStream(STR_LOG4J_FILE);
            Properties logger_p = new Properties();
            logger_p.load(logger_is);            
            PropertyConfigurator.configure(logger_p);            
            logger_is.close();            
            log.info("openConnection Begin");
            
            if (m_con == null || m_con.isClosed()) {
                // connect if needed
                InputStream pis = getClass().getResourceAsStream(STR_CONFIG_FILE);
                blnOK = loadPropertiesFromStream(pis);

                if (m_JNDI_Name == null) {
                    // using the properties read from configuration create a connection to the database
                    Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                    m_con = DriverManager.getConnection(m_connectionURL, m_userID, m_userPassword);
                    log.info("Using Direct Database Connection.");
                    
                } else {
                    /* Use a Connection Pool on the Server */
                    DataSource ds = getJdbcWs();               
                    m_con = ds.getConnection();                    
                    log.info("Using Connection Pool.");
                }
                
                
                m_stmt = m_con.createStatement();
                m_stmt.setQueryTimeout(59);
                
                
                
            } else {
                // Connection is already open
                blnOK = true;
                log.info("openConnection Connection Already Open");
            }    
                       
        } catch (Exception e) {
            // Something didn't work 
            blnOK = false;
            log.debug(getStackTrace(e));
        }
        log.info("openConnection End");
        return blnOK;
    }

    /**
     * Close the connection to the database.
     * @return true if close is successful.
     */
    private boolean closeConnection() {
        boolean blnOK = false;
        try {
            // Set up the Logger if needed
            InputStream logger_is = getClass().getResourceAsStream(STR_LOG4J_FILE);
            Properties logger_p = new Properties();
            logger_p.load(logger_is);            
            PropertyConfigurator.configure(logger_p);            
            logger_is.close();            
            log.info("closeConnection Begin");

            // Just try and close the connection 
            
            try {
                m_stmt.close();
                
            } catch (Exception e) {
                // ignore e
            }
                      
            m_con.close();
           
            m_con = null;
            
            
           
            log.info("closeConnection End");
        } catch (Exception e) {
            // Connection close fails when connection is already closed
            blnOK = false;
            log.debug(getStackTrace(e));
        }
        return blnOK;
    }

    /**
     * Checks the CountryLayerName provided by the user to GetCountryNames call
     * @param CountryLayerName
     * @return CountryNameBean sets NAME = "ERROR" if something goes wrong.
     */
    private CountryNameBean checkCountryLayerName(String CountryLayerName) throws InvalidParameterException {

        CountryNameBean b = new CountryNameBean();

        int i;

        // Need a Statment and ResultSet  (m_con must already be done)
        ResultSet rset = null;

        try {            
            // Set up the logger
            InputStream logger_is = getClass().getResourceAsStream(STR_LOG4J_FILE);
            Properties logger_p = new Properties();
            logger_p.load(logger_is);            
            PropertyConfigurator.configure(logger_p);            
            logger_is.close();            
            log.info("checkCountryLayerName Begin");
            


            // Create the SQL
            String strSQL = "SELECT * from ";
            strSQL += m_parameterTable;
            strSQL += " where UPPER(name) = ";
            strSQL += "UPPER('CountryBoundaryLayer')";

            log.info("checkCountryLayerName: " + strSQL);
            
            // Execute the Query
            rset = m_stmt.executeQuery(strSQL);

            // Get the results. There must be a record in m_parameterTable for CountryBoundaryLayer
            rset.next();

            // The list of Country Names is comma delimited in the VALS column
            String countryLayersArray[] = rset.getString("VALS").split(",");           
            i = 0;
            while (i < countryLayersArray.length) {
                countryLayersArray[i] = countryLayersArray[i].trim();
                i++;
            }

            rset.close();
            
            // Set Default first element in VALS
            String strCountryLayerName = countryLayersArray[0];

            if (CountryLayerName != null) {
                // if not null then try to find the name specified in the list
                strCountryLayerName = null;
                i = 0;
                // If the input if one of the valid values set otherwise use default
                while (i < countryLayersArray.length) {
                    if (CountryLayerName.equalsIgnoreCase(countryLayersArray[i])) {
                        strCountryLayerName = countryLayersArray[i];
                    }
                    i++;
                }
                
                if (strCountryLayerName == null) {
                    // Didn't find the name specified in the list
                    throw new InvalidParameterException("Invalid CountryBoundaryLayer: " + CountryLayerName);
                } 
//                 else { 
//                    // I did find the name in the list
//                    b.setName(strCountryLayerName);
//                    b.setAltNames("");
//                    b.setCodes("");
//                    
//                }
//            } else {
//                // User want's default
//                b.setName(strCountryLayerName);
//                b.setAltNames("");
//                b.setCodes("");                
            }
            
            // Create the SQL
            strSQL = "SELECT * from ";
            strSQL += m_layerInfoTable;
            strSQL += " where UPPER(LAYER_NAME) = ";
            strSQL += "UPPER('" + strCountryLayerName + "')";
            
            // Execute the Query
            rset = m_stmt.executeQuery(strSQL);

            // Get the results. There must be a record in m_layerInfoTable for strCountryLayerName
            rset.next();
            
            // The list of Country Names is comma delimited in the VALS column
            strCountryLayerName = rset.getString("TABLE_NAME").trim();           
            
            
            
            b.setName(strCountryLayerName);
            b.setAltNames("");
            b.setCodes("");                
            
            
        } catch (Exception e) {
            log.debug(getStackTrace(e));
            throw new InvalidParameterException(e.getMessage());
        } finally {
            try {                
                rset.close();
            } catch (Exception e) {
                // ignore e
            }
        }
        
        log.info("checkCountryLayerName End");
        return b;

    }
  
    
    /**
     * Checks the OceanLayerName user input
     * @param OceanLayerName
     * @return CountryNameBean sets NAME = "ERROR" if something goes wrong.
     */
    private CountryNameBean checkOceanLayerName(String OceanLayerName) throws InvalidParameterException {
        CountryNameBean b = new CountryNameBean();

        int i;

        // Statement and Rset needed
        ResultSet rset = null;
      
        try {
            // Setup logger
            InputStream logger_is = getClass().getResourceAsStream(STR_LOG4J_FILE);
            Properties logger_p = new Properties();
            logger_p.load(logger_is);            
            PropertyConfigurator.configure(logger_p);            
            logger_is.close();            
            log.info("checkOceanLayerName Begin");            


            // Build the Query 
            String strSQL = "SELECT * from ";
            strSQL += m_parameterTable;
            strSQL += " where UPPER(name) = ";
            strSQL += "UPPER('OceanBoundaryLayer')";

            log.info("checkOceanLayerName: " + strSQL);
            // Execute the Query
            rset = m_stmt.executeQuery(strSQL);

            // There must be a OceanBoundaryLayer in m_parameterTable
            rset.next();

            // Get the comma delimited values
            String oceanLayersArray[] = rset.getString("VALS").split(",");

            i = 0;
            while (i < oceanLayersArray.length) {
                oceanLayersArray[i] = oceanLayersArray[i].trim();
                i++;
            }

            rset.close();
            
            // Set Default (null means just return World Oceans)
            String strOceanLayerName = null;

            if (OceanLayerName != null) {
                // if not null then see if the layer specified is in list
                strOceanLayerName = null;
                i = 0;
                // If the input if one of the valid values set otherwise use default
                while (i < oceanLayersArray.length) {
                    if (OceanLayerName.equalsIgnoreCase(oceanLayersArray[i])) {
                        strOceanLayerName = oceanLayersArray[i];
                    }
                    i++;
                }
                if (strOceanLayerName == null) {
                    throw new InvalidParameterException("Invalid OceanBoundaryLayer: " + OceanLayerName);

                } 
//                else {
//                    // All is well return the CountryLayerName
//                    b.setName(strOceanLayerName);
//                    b.setAltNames("");
//                    b.setCodes("");
//                    
//                }
//            } else {
//                // User wants default Ocean which is no ocean layer 
//                b.setName(strOceanLayerName); // sets name to null
//                b.setAltNames("");
//                b.setCodes("");
            }

            if (strOceanLayerName != null) {
                // Create the SQL
                strSQL = "SELECT * from ";
                strSQL += m_layerInfoTable;
                strSQL += " where UPPER(LAYER_NAME) = ";
                strSQL += "UPPER('" + strOceanLayerName + "')";

                // Execute the Query
                rset = m_stmt.executeQuery(strSQL);

                // Get the results. There must be a record in m_layerInfoTable for strCountryLayerName
                rset.next();

                // The list of Country Names is comma delimited in the VALS column
                strOceanLayerName = rset.getString("TABLE_NAME").trim();                          
                
                
            }
            b.setName(strOceanLayerName);
            b.setAltNames("");
            b.setCodes("");             
            
            
        } catch (Exception e) {
            log.debug(getStackTrace(e));
            throw new InvalidParameterException(e.getMessage());

        } finally {
            try {                
                rset.close();
            } catch (Exception e) {
                // ignore e
            }
        }

        log.info("checkOceanLayerName End");            
        return b;

    }

    /**
     * Checks the user specified Codes and set m_strCodes
     * @param codes
     * @return 
     */
    private CountryNameBean checkCodes(String codes) throws InvalidParameterException {
        CountryNameBean b = new CountryNameBean();

        int i;

        // Need a statement and result set
        ResultSet rset = null;
        try {
            // Setup logger
            InputStream logger_is = getClass().getResourceAsStream(STR_LOG4J_FILE);
            Properties logger_p = new Properties();
            logger_p.load(logger_is);            
            PropertyConfigurator.configure(logger_p);            
            logger_is.close();            
            log.info("checkCodes Begin");              



            // Load list of valid Codes from Parameter Table
            String strSQL = "SELECT * from ";
            strSQL += m_parameterTable;
            strSQL += " where UPPER(name) = ";
            strSQL += "UPPER('CodeList')";

            // Execute the Query
            rset = m_stmt.executeQuery(strSQL);

            log.info("checkCodes: " + strSQL);
            
            // There must be a parameter name CodeList in m_parameterTable
            rset.next();

            // Get the values for CodeList comma delimited
            String strAllCodes = rset.getString("VALS");
            String CodeListArray[] = strAllCodes.split(",");

            rset.close();            

            i = 0;
            while (i < CodeListArray.length) {
                CodeListArray[i] = CodeListArray[i].trim();
                i++;
            }


            String strCodeList = "";

            String strInvalidCodes = "";
            // If no codes specified; default is all codes
            if (codes == null || codes.trim().equalsIgnoreCase("") || codes.equalsIgnoreCase("ALL")) {
                strCodeList = strAllCodes;
                m_strCodes = strCodeList.split(",");
            } else {
                // Check the codes specified against the list of valid codes
                strCodeList = codes;
                // parse and trim user input codes
                m_strCodes = strCodeList.split(",");

                i = 0;
                while (i < m_strCodes.length) {
                    boolean invalidCode = true;
                    m_strCodes[i] = m_strCodes[i].trim();
                    int j = 0;
                    while (j < CodeListArray.length) {
                        if (m_strCodes[i].equalsIgnoreCase(CodeListArray[j])) {
                            invalidCode = false;
                        }
                        j++;
                    }
                    if (invalidCode) {
                        strInvalidCodes += m_strCodes[i] + ",";
                    }

                    i++;
                }
            }


            if (!strInvalidCodes.equalsIgnoreCase("")) {
                // Take off last comma
                strInvalidCodes = strInvalidCodes.substring(0, strInvalidCodes.length() - 1);
                throw new InvalidParameterException("Invalid Code(s): " + strInvalidCodes);
            } else {
                // OK
                b.setName(strCodeList);
                b.setAltNames("");
                b.setCodes("");

            }

        } catch (Exception e) {
            log.debug(getStackTrace(e));
            throw new InvalidParameterException(e.getMessage());
        } finally {
            try {                
                rset.close();
            } catch (Exception e) {
                // ignore e
            }
        }
        log.info("checkCodes End");              

        return b;

    }
   
   /**
     * Checks the geometry Well Known Text to ensure it's valid.
     * NOTE: I've had some problems with this Oracle Spatial function.  
     * Invalid input like: POLYGON((0 0,10 0,10 10,10 0,0 0) << Missing last parenthesis
     * causes the database to go into an endless query.  I set timout to 5 seconds.
     * I'm also seeing sporatic false results.  I don't know why.
     * @param geometryWKT
     * @return
     */
    private CountryNameBean checkWKT(String geometryWKT) throws InvalidParameterException {

        CountryNameBean b = new CountryNameBean();

        try {            
            // Setup logger
            InputStream logger_is = getClass().getResourceAsStream(STR_LOG4J_FILE);
            Properties logger_p = new Properties();
            logger_p.load(logger_is);            
            PropertyConfigurator.configure(logger_p);            
            logger_is.close();            
            log.info("checkWKT Begin");     
            
            // Oracle limits the WKT to 4000 characters when constructing with a string
            if (geometryWKT.length() > 4000) {
                throw new InvalidParameterException("Invalid WKT: Maxium length of 4000 exceeded.");
            }
            
            int index = 0;
            // Split on first open parenthesis (
            index = geometryWKT.indexOf('(');
            
            if (index == -1) {
                throw new InvalidParameterException("Invalid WKT: No open parenthesis");
            }
                        
            // Found an open parenthesis continue
            String strGeomType = geometryWKT.substring(0,index).trim();
            String strPoints = geometryWKT.substring(index).trim();
                        
            // First char and last char of Points should be a parenthesis
            if (strPoints.charAt(0) == '(' && strPoints.charAt(strPoints.length()-1) == ')') {
                // Strip off the parenthesis
                strPoints = strPoints.substring(1,strPoints.length()-1);
            } else {             
                throw new InvalidParameterException("Invalid WKT: Point(s) must be inside a set of parenthesis");
            }
            
            // strCoords are the pairs of LON LAT values
            // strLonLat splits the Coord on space(s)
            String strCoords[] = null;
            String strLonLat[] = null;
            int intGeomNum = 0;

            // Uses Oracle Spatial Geom Check to check valid lines and polys
            boolean needToCheckSDO = false;

            // Extending WKT to support MBR
            if (strGeomType.equalsIgnoreCase("MBR")) {
                // Here I just turn the MBR to a four point Polygon
                // For large polygons there are errors associated Geodetic Coords
                
                log.info("MBR Checking");
                
                // Check for Two Points
                strCoords = strPoints.split(",");
                                
                // There should be just two parts
                if (strCoords.length != 2) {
                    throw new InvalidParameterException("Invalid WKT: MBR must have two points, lower left and upper right: " + strPoints);                                 
                }
                
                // Split on one or more spaces                
                strLonLat = strCoords[0].split(" +");
                
                // There should be just two parts
                if (strLonLat.length != 2) {
                    throw new InvalidParameterException("Invalid WKT: Longitude and latitude should be space delimted: " + strPoints);                                 
                }
                
                // Both points should be doubles
                double dblLLLon = 0.0;
                double dblLLLat = 0.0;
                try { 
                    dblLLLon = Double.parseDouble(strLonLat[0]);                                                            
                    dblLLLat = Double.parseDouble(strLonLat[1]);
                } catch (NumberFormatException e) {
                    throw new InvalidParameterException("Invalid WKT: Must be decimal degree values: " + strPoints);                                                      
                }
                                
                // Split on one or more spaces                
                strLonLat = strCoords[1].split(" +");
                
                // There should be just two parts
                if (strLonLat.length != 2) {
                    throw new InvalidParameterException("Invalid WKT: Longitude and latitude should be space delimted: " + strPoints);                                 
                }
             
                // Both points should be doubles
                double dblURLon = 0.0;
                double dblURLat = 0.0;
                try { 
                    dblURLon = Double.parseDouble(strLonLat[0]);                                                            
                    dblURLat = Double.parseDouble(strLonLat[1]);
                } catch (NumberFormatException e) {
                    throw new InvalidParameterException("Invalid WKT: Must be decimal degree values: " + strPoints);                                                      
                }
                
                // Check UR > LL
                if (dblURLat <= dblLLLat) {
                    throw new InvalidParameterException("Invalid WKT: Upper Right Latitude must be greater than Lower Left Latitude: " + strPoints);                    
                }                
                
                // Latitudes of 90 or -90 are problematic in GEODETIC
                if (dblURLat > 89.99999 || dblURLat < -89.99999 || dblLLLat > 89.99999 || dblLLLat < -89.99999) {
                    throw new InvalidParameterException("Invalid WKT: Latitudes must be between -89.99999 and 89.99999: " + strPoints);                                        
                }
                
                double lonDiff = Math.abs(dblURLon-dblLLLon);
                if (lonDiff >= 180.0 || lonDiff == 0) {
                    throw new InvalidParameterException("Invalid WKT: Difference between upper right and lower left longitude must be between 0 and 180: " + strPoints);                                        
                }
                                
                strCoords = new String[5];
                strCoords[0] = String.valueOf(dblLLLon) + " " + String.valueOf(dblLLLat);                
                strCoords[1] = String.valueOf(dblURLon) + " " + String.valueOf(dblLLLat);
                strCoords[2] = String.valueOf(dblURLon) + " " + String.valueOf(dblURLat);                
                strCoords[3] = String.valueOf(dblLLLon) + " " + String.valueOf(dblURLat);
                strCoords[4] = String.valueOf(dblLLLon) + " " + String.valueOf(dblLLLat);
                                
                needToCheckSDO = true;
                intGeomNum = 2003;                 
                
                log.info("MBR OK");
                
            } else if (strGeomType.equalsIgnoreCase("Point")) {
                // *******  POINT *************************
                log.info("Point Checking");
                strCoords = strPoints.split(",");
                
                // One Point only 
                if (strCoords.length > 1) {
                    throw new InvalidParameterException("Invalid WKT: Only one point should be specified: " + strPoints);
                }
                
                // Split on one or more spaces                
                strLonLat = strCoords[0].split(" +");
                
                // There should be just two parts
                if (strLonLat.length != 2) {
                    throw new InvalidParameterException("Invalid WKT: Longitude and latitude should be space delimted: " + strPoints);                                 
                }
                                
                // Both points should be doubles
                double dblLon = 0.0;
                double dblLat = 0.0;
                try { 
                    dblLon = Double.parseDouble(strLonLat[0]);                                                            
                    dblLat = Double.parseDouble(strLonLat[1]);
                } catch (NumberFormatException e) {
                    throw new InvalidParameterException("Invalid WKT: Must be decimal degree values: " + strPoints);                                                      
                }
                
                // Check the Latitude is Valid
                if (!isValidLat(dblLat)) {
                    throw new InvalidParameterException("Invalid WKT: Invalid Latitude: " + strPoints);                                                                            
                }
                
                // Check the Longitude if valid
                if (!isValidLon(dblLon)) {
                    throw new InvalidParameterException("Invalid WKT: Invalid Longitude: " + strPoints);                                                                            
                }
                
                needToCheckSDO = true;
                
                // Point is OK
                log.info("Point is OK");
                
                intGeomNum = 2001;
                
                
                
            } else if (strGeomType.equalsIgnoreCase("LineString")) {
                // *******  LINESTRING *************************
                log.info("LineString Checking");
                strCoords = strPoints.split(",");
                                
                // Check number of coords 
                int numCoords = strCoords.length;
                if (numCoords < 2 ) {                   
                    throw new InvalidParameterException("Invalid WKT: Linestring must have at least two points.");                                                                                            
                }
                if (numCoords > MAX_POINTS ) {
                    throw new InvalidParameterException("Invalid WKT: Linestring must have less than " + MAX_POINTS + " points.");                                                                                             
                }
                
                // Loop through the coords and check each one
                int i = 0;
                while (i < strCoords.length) {
                    // Trim off any extra spaces
                    String strPoint = strCoords[i].trim();

                    // Split on one or more spaces                
                    strLonLat = strPoint.split(" +");

                    // There should be just two parts
                    if (strLonLat.length != 2) {
                        throw new InvalidParameterException("Invalid WKT: Invalid Point: " + strPoint);                                
                    }
           
                    // Both points should be doubles
                    double dblLon = 0.0;
                    double dblLat = 0.0;
                    try { 
                        dblLon = Double.parseDouble(strLonLat[0]);                                                            
                        dblLat = Double.parseDouble(strLonLat[1]);
                    } catch (NumberFormatException e) {
                        throw new InvalidParameterException("Invalid WKT: Invalid Point: " + strPoint);                        
                    }

                    // Check for Valid Lat
                    if (!isValidLat(dblLat)) {
                        throw new InvalidParameterException("Invalid WKT: Invalid Latitude: " + strPoint);                        
                    }

                    // Check for Valid lon
                    if (!isValidLon(dblLon)) {
                        throw new InvalidParameterException("Invalid WKT: Invalid Longitude: " + strPoint);                                                                         
                    }
                    i++;
                }
                
                needToCheckSDO = true;
                
                intGeomNum = 2002;

            } else if (strGeomType.equalsIgnoreCase("Polygon")) {
                // *******  POLYGON *************************
                log.info("Polygon Checking");
                // First char and last char of Polygon must be another set of parenthesis
                if (strPoints.charAt(0) == '(' && strPoints.charAt(strPoints.length()-1) == ')') {
                    // Strip off the parenthesis
                    strPoints = strPoints.substring(1,strPoints.length()-1);
                } else {
                    throw new InvalidParameterException("Invalid WKT: Polygons points must be inside two sets of parenthesis.");
                }
                
                // Check the number of coords
                strCoords = strPoints.split(",");                
                int numCoords = strCoords.length;
                if (numCoords < 4 ) {
                    throw new InvalidParameterException("Invalid WKT: Polygon must have at least four points.");                                                                                               
                }
                if (numCoords > MAX_POINTS ) {
                    throw new InvalidParameterException("Invalid WKT: Polygon must have less than " + MAX_POINTS + " points.");                                                                                             
                }
                
                // Check each of the coords
                int i = 0;
                while (i < strCoords.length) {
                    String strPoint = strCoords[i].trim();

                    // Split on one or more spaces                
                    strLonLat = strPoint.split(" +");

                    // There should be just two parts
                    if (strLonLat.length != 2) {
                        throw new InvalidParameterException("Invalid WKT: Invalid Point: " + strPoint);                                 
                    }

                    // Both points should be doubles
                    double dblLon = 0.0;
                    double dblLat = 0.0;
                    try { 
                        dblLon = Double.parseDouble(strLonLat[0]);                                                            
                        dblLat = Double.parseDouble(strLonLat[1]);
                    } catch (NumberFormatException e) {
                        throw new InvalidParameterException("Invalid WKT: Invalid Point: " + strPoint);                                                      
                    }

                    // Latitude must be valid
                    if (!isValidLat(dblLat)) {
                        throw new InvalidParameterException("Invalid WKT: Invalid Latitude: " + strPoint);                                                                           
                    }

                    // Longitude must be valid
                    if (!isValidLon(dblLon)) {
                        throw new InvalidParameterException("Invalid WKT: Invalid Longitude: " + strPoint);                                                                           
                    }
                    i++;
                }               
                
                needToCheckSDO = true;
                
                intGeomNum = 2003;                    



            } else {
                throw new InvalidParameterException("Invalid WKT: Unsupported Geometry Type: " + strGeomType +
                    ". Supported Types: POINT, LINESTRING, or POLYGON");
            }
            
            
            
            if (needToCheckSDO) {
                                               
                ResultSet rset = null;

                try {
                    /* 
                     * Discovered VALIDATE_WKT_GEOMETRY has many problems
                     * - Hangs on some invalid inputs
                     * - Returns valid on some invalid Geometries
                    select SDO_UTIL.VALIDATE_WKTGEOMETRY(
                    'POLYGON ((0.0 0.0,30.0 0.0,30.0 30.0,0.0 30.0,0.0 0.0))') 
                    ISVALID FROM DUAL;                 
                     *
                     */

                    /* Using SDO_GEOM.VALIDATE_GEOMETRY_WITH_CONTEXT provide
                     * better geometry checking and also returns error messages
                     * to give the user a clue as to what is wrong.
                     */

                    
                    if (m_useWKT) {
                        m_SDO_GEOMETRY = "MDSYS.SDO_GEOMETRY('";
                        m_SDO_GEOMETRY += geometryWKT;
                        m_SDO_GEOMETRY += "',8307)";
                    } else {
                        String SDO_GTYPE = "";
                        String SDO_SRID = "8307";
                        String SDO_POINT = "";
                        String SDO_ELEM_INFO = "";
                        String SDO_ORDINATES = "";
                        int i = 0;                       
                        while (i < strCoords.length - 1) {
                            SDO_ORDINATES += strCoords[i].trim().replaceAll(" +",",") + ",";
                            i++;
                        }                        
                        SDO_ORDINATES += strCoords[i].trim().replaceAll(" +",",");                                                                        
                                                
                        switch (intGeomNum) {
                            case (2001): {
                                SDO_GTYPE = "2001";
                                SDO_POINT = "MDSYS.SDO_POINT_TYPE(" + SDO_ORDINATES + ",NULL)";
                                SDO_ELEM_INFO = "null";
                                SDO_ORDINATES = "null";                                
                                break;
                            }
                            case (2002): {
                                SDO_GTYPE = "2002";
                                SDO_POINT = "null";
                                SDO_ELEM_INFO = "MDSYS.SDO_ELEM_INFO_ARRAY(1,2,1)";
                                SDO_ORDINATES = "MDSYS.SDO_ORDINATE_ARRAY(" + SDO_ORDINATES + ")";
                                break;
                            }
                            case (2003): {
                                SDO_GTYPE = "2003";
                                SDO_POINT = "null";
                                SDO_ELEM_INFO = "MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1)";                                
                                SDO_ORDINATES = "MDSYS.SDO_ORDINATE_ARRAY(" + SDO_ORDINATES + ")";
                                break;
                            }                            
                            default:  {
                                m_SDO_GEOMETRY += "null";
                            }
                        }
                        m_SDO_GEOMETRY = "MDSYS.SDO_GEOMETRY(";
                        m_SDO_GEOMETRY += SDO_GTYPE + ",";
                        m_SDO_GEOMETRY += SDO_SRID + ",";
                        m_SDO_GEOMETRY += SDO_POINT + ",";
                        m_SDO_GEOMETRY += SDO_ELEM_INFO + ",";
                        m_SDO_GEOMETRY += SDO_ORDINATES + ")";
                                                
                    }
                                        
                    String strSQL = "";

                    // Check to see if the geometryWKT is valid
                    strSQL = "select SDO_GEOM.VALIDATE_GEOMETRY_WITH_CONTEXT(";
                    strSQL += m_SDO_GEOMETRY;
                    strSQL += ",1) ISVALID FROM DUAL";                        

                    log.info("checkWKT: " + strSQL);

                    String strValidWKT = "FALSE";
                    b.setCodes("");

                    rset = m_stmt.executeQuery(strSQL);
                    rset.next();
                    strValidWKT = rset.getString("ISVALID");
                    log.info("strValidWKT = " + strValidWKT);

                    rset.close();

                    // if geometryWKT is invalid return an error
                    if (strValidWKT.equalsIgnoreCase("TRUE")) {
                        b.setName(geometryWKT);
                        b.setAltNames("");
                        b.setCodes("");
                    } else {
                        String errnum = "";
                        if (m_useWKT) {
                            errnum  = strValidWKT.substring(0, strValidWKT.indexOf(' '));                                
                        } else {
                            errnum = strValidWKT.substring(0,5);
                        }
                        
                        // There was an error detected 
                        strSQL = "select GET_ERR_MESSAGE(-" + errnum + ") MSG from dual";
                        String strMsg = "";
                        rset = m_stmt.executeQuery(strSQL);
                        if (rset.next()) {
                            strMsg = rset.getString("MSG");
                        }
                        rset.close();

                        throw new InvalidParameterException("Invalid WKT: " + strValidWKT + " : " + strMsg);
                    }

                    
                } catch (Exception e) {
                    log.debug(getStackTrace(e));
                    throw new InvalidParameterException(e.getMessage());
                } finally {
                    try {                
                        rset.close();
                    } catch (Exception e) {
                        // ignore e
                    }
                    
                }
            }
                   
        } catch (Exception e) {
            
            log.debug(getStackTrace(e));
            throw new InvalidParameterException(e.getMessage());
        }

        log.info("checkWKT End");              
        return b;

    }
    
    /**
     * Checks for valid latitude ranges -90 to 90 degrees
     * @param lat
     * @return true or false
     */
    private boolean isValidLat(double lat) {
        
        boolean blnOK = false;
        
        if (lat >= -90.00 && lat <= 90.00) {
            blnOK = true;
        }
        
        return blnOK;
    }
    
    /**
     * Checks for valid longitude ranges -180 to 360 degrees
     * @param lon
     * @return true or false
     */
    private boolean isValidLon(double lat) {
        
        boolean blnOK = false;
        
        if (lat >= -180.00 && lat <= 360.00) {
            blnOK = true;
        }
        
        return blnOK;
    }    
    
    /**
     * Returns Country Names based on the parameters provided.
     * @param CountryLayerName Country Boundary Layer to use
     * @param OceanLayerName Ocean Boundary Layer to use
     * @param codes Codes to return
     * @param geometryWKT Well Know Test for Point, LineString, or Polygon
     * @param nearest Set to true to return nearest country; false oceans
     * @param buffer Add a buffer around the geometry 
     * @return Returns a single CountryNameBean with Name = "ERROR" if something 
     * goes wrong.  
     */
    public GetCountryNamesResponse getCountryNames(String CountryLayerName,
            String OceanLayerName, String codes,
            String geometryWKT, Boolean nearest, Double buffer) {

        GetCountryNamesResponse gcnr = new GetCountryNamesResponse();
        ArrayList<CountryNameBean> rslt = new ArrayList<CountryNameBean>();
        SecurityElement sec = new SecurityElement();
        GetCountryNamesResponse temp = new GetCountryNamesResponse();
        String strGeometrySQL = "";
        // Initialize to default Layers
        String strCountryLayerName = "";
        String strOceanLayerName = "";
        String strCodes = "";

        int i = 0;
            
        ResultSet rset = null;
        
        try {
            InputStream logger_is = getClass().getResourceAsStream(STR_LOG4J_FILE);
            Properties logger_p = new Properties();
            logger_p.load(logger_is);            
            PropertyConfigurator.configure(logger_p);            
            logger_is.close();            
            log.info("getCountryNames Begin");              

            // open DB Connection            
            if (!openConnection()) {
                throw new Exception("Failed to open database connection.  If problem persists contact the service provider");
            }

            // Check CountryLayerName
            CountryNameBean c = new CountryNameBean();
            c = checkCountryLayerName(CountryLayerName);
            strCountryLayerName = c.getName();
                                   
            // Check OceanLayerName
            c = new CountryNameBean();
            c = checkOceanLayerName(OceanLayerName);
            strOceanLayerName = c.getName();

            // Check codes set m_strCodes array
            c = new CountryNameBean();
            c = checkCodes(codes);

            // Set default buffer 
            if (buffer == null) {
                buffer = new Double(0.0);
            }                        
            
            // Check Buffer 
            if (buffer.doubleValue() > MAX_BUFFER || buffer.doubleValue() < 0) {
                throw new InvalidParameterException("Invalid Buffer: " + buffer.toString() +
                        ".  Buffer must be between 0 and " + MAX_BUFFER);
            }            
            


            String strCountryNameQuery = "";

            if (geometryWKT == null) {
                // if geometryWKT is null then return all the country codes for specified Country
                strCountryNameQuery = "SELECT unique b.* from (select FIPS from " +  
                        strCountryLayerName + ") A, " + m_codeLookupTable + 
                        " B where a.fips = b.fips(+) order by name";
            } else {
                geometryWKT = geometryWKT.trim();
                c = new CountryNameBean();
                c = checkWKT(geometryWKT);

                // geometryWKT must be OK continue                               

                if (buffer.doubleValue() <= 0) {
                    // Create geometry SQL without Buffer
                    /*
                    SELECT UNIQUE B.*
                    FROM (SELECT FIPS FROM CNTRY2006M
                    WHERE SDO_RELATE(SHAPE,  -- Stuff below here
                    MDSYS.SDO_GEOMETRY('POLYGON ((0.0 0.0,30.0 0.0,30.0 30.0,0.0 30.0,0.0 0.0))',8307)
                    ,'mask=ANYINTERACT') = 'TRUE') A, CLOOK B -- Stuff above here
                    WHERE TRIM(A.FIPS) = TRIM(B.FIPS(+));	
                     */

                    // Don't use buffer                    
                    strGeometrySQL = m_SDO_GEOMETRY;
                    
                } else {
                    // Create geometry SQL with Buffer
                    /*
                    SELECT UNIQUE B.*
                    FROM (SELECT FIPS FROM CNTRY2006M
                    WHERE SDO_RELATE(SHAPE,  -- Stuff below here
                    SDO_GEOM.SDO_BUFFER(
                    MDSYS.SDO_GEOMETRY('POLYGON ((0.0 0.0,30.0 0.0,30.0 30.0,0.0 30.0,0.0 0.0))',8307), 
                    100, 0.05,'unit=km')
                    ,'mask=ANYINTERACT') = 'TRUE') A, CLOOK B  -- Stuff above here
                    WHERE TRIM(A.FIPS) = TRIM(B.FIPS(+));
                     */

                    // Use Buffer 

                    strGeometrySQL = "SDO_GEOM.SDO_BUFFER(";
                    strGeometrySQL += m_SDO_GEOMETRY;
                    strGeometrySQL += "," + buffer.toString() + ", 1,'unit=km arc_tolerance=0.05')";
                }

                // Create the SQL to search for for features 
                strCountryNameQuery = "SELECT UNIQUE B.* ";
                strCountryNameQuery += "FROM (SELECT FIPS FROM " + strCountryLayerName + " ";
                strCountryNameQuery += "WHERE SDO_RELATE(SHAPE,";
                strCountryNameQuery += strGeometrySQL;
                strCountryNameQuery += ",'mask=ANYINTERACT') = 'TRUE') A, " + m_codeLookupTable + " B ";
                strCountryNameQuery += "WHERE TRIM(A.FIPS) = TRIM(B.FIPS(+)) ORDER BY B.NAME ";

            }

            // Time to execute the strCountryNameQuery
            //System.out.println(strCountryNameQuery);
            log.info("getCountryNames CountryNameQuery: " + strCountryNameQuery);
            
            // Execute the spatial query
            rset = m_stmt.executeQuery(strCountryNameQuery);


            if (rset.next()) {
                // if number of results > 0 then populate results and return
                do {
                    CountryNameBean b = new CountryNameBean();
                    b.setName(rset.getString("NAME"));
                    b.setAltNames(rset.getString("ALTNAMES"));



                    String strCodeVals = "";
                    i = 0;
                    while (i < m_strCodes.length) {
                        log.debug("Code: " + m_strCodes[i]);
                        strCodeVals += rset.getString(m_strCodes[i]);

                        i = i + 1;
                        if (i < m_strCodes.length) {
                            strCodeVals += ",";
                        }
                    }

                    b.setCodes(strCodeVals);
                    rslt.add(b);
                } while (rset.next());

                rset.close();
                
                log.info("getCountryNames End. Query returned " + rslt.size() + " results");                
                gcnr.getCountryNames().addAll(rslt);
                temp = getLayerInfo(strCountryLayerName);
                gcnr.setSecurity(temp.getSecurity());
                gcnr.setBoundaryLayer(temp.getBoundaryLayer());
                


            } else {
                //  No results found for the spatial query
                rset.close();
                
                if (nearest == null) {
                    // If null then set to false
                    nearest = new Boolean(false);
                }

                if (nearest) {
                    // User want the nearest result
                    /*
                    SELECT UNIQUE B.*,SDO_NN_DISTANCE(1) dist
                    FROM (SELECT FIPS FROM CNTRY2006M
                    WHERE SDO_NN(SHAPE, -- Stuff below here
                    SDO_GEOM.SDO_BUFFER(
                    MDSYS.SDO_GEOMETRY('POLYGON ((0.0 0.0,3.0 0.0,3.0 3.0,0.0 3.0,0.0 0.0))',8307)
                    ,100, 0.05,'unit=km')
                    ,'sdo_num_res=2 unit=km',1) = 'TRUE') A, CLOOK B -- Stuff above here
                    WHERE TRIM(A.FIPS) = TRIM(B.FIPS(+))
                    ORDER BY dist;
                    SELECT UNIQUE B.*,SDO_NN_DISTANCE(1) dist
                    FROM (SELECT FIPS FROM CNTRY2006M
                    WHERE SDO_NN(SHAPE, -- Stuff below here
                    MDSYS.SDO_GEOMETRY('POLYGON ((0.0 0.0,3.0 0.0,3.0 3.0,0.0 3.0,0.0 0.0))',8307)
                    ,'sdo_num_res=2 unit=km',1) = 'TRUE') A, CLOOK B -- Stuff above here
                    WHERE TRIM(A.FIPS) = TRIM(B.FIPS(+))
                    ORDER BY dist;                     */
                    // Use the SDO_NN and return the results
                    String strNearestSQL = "SELECT UNIQUE B.*";
                    strNearestSQL += "FROM (SELECT FIPS FROM " + strCountryLayerName + " ";
                    strNearestSQL += "WHERE SDO_NN(SHAPE, ";
                    strNearestSQL += strGeometrySQL;
                    strNearestSQL += ",'sdo_num_res=1 unit=km',1) = 'TRUE') A, " + m_codeLookupTable + " B ";
                    strNearestSQL += "WHERE TRIM(A.FIPS) = TRIM(B.FIPS(+)) ORDER BY B.NAME ";

                    //System.out.println(strNearestSQL);
                    log.info("getCountryNames End. Nearest Query: " + strNearestSQL);
                    
                    // Execute the query Nearest Neighbor

                    rset = m_stmt.executeQuery(strNearestSQL);

                    if (rset.next()) {
                        // if number of results > 0 then populate results and return
                        do {
                            CountryNameBean b = new CountryNameBean();
                            b.setName(rset.getString("NAME"));
                            b.setAltNames(rset.getString("ALTNAMES"));

                            String strCodeVals = "";
                            i = 0;
                            while (i < m_strCodes.length) {
                                strCodeVals += rset.getString(m_strCodes[i]);

                                i = i + 1;
                                if (i < m_strCodes.length) {
                                    strCodeVals += ",";
                                }
                            }

                            b.setCodes(strCodeVals);
                            rslt.add(b);
                        } while (rset.next());
                        
                        rset.close();
                        
                        log.info("getCountryNames End. Nearest neighbor returned " + rslt.size() + " results." );

                        temp = getLayerInfo(strCountryLayerName);
                        gcnr.setSecurity(temp.getSecurity());
                        gcnr.setBoundaryLayer(temp.getBoundaryLayer());
                        gcnr.getCountryNames().addAll(rslt);

                        
                    } else {
                        try {
                            rset.close();
                        } catch (Exception e) {
                            // ignore e
                        }
                        

                        // Should never get here; there should always be one nearest
                        throw new Exception("Unexpected Error. No Nearest Neighbor. In problem persists contact service provider.");                        
                    }


                } else {
                    // User didn't request nearest therefore return oceans
                    /*
                    -- Query OceanLayer
                    Select UNIQUE NAME from WORLDOCEANS90
                    WHERE SDO_RELATE(SHAPE,  -- Stuff below here
                    SDO_GEOM.SDO_BUFFER(
                    MDSYS.SDO_GEOMETRY('POLYGON ((0.0 0.0,3.0 0.0,3.0 3.0,0.0 3.0,0.0 0.0))',8307)
                    ,100, 0.05,'unit=km')
                    ,'mask=ANYINTERACT') = 'TRUE';   -- Stuff above here
                    -- or 
                    Select UNIQUE NAME from WORLDOCEANS90
                    WHERE SDO_RELATE(SHAPE,  -- Stuff below here
                    MDSYS.SDO_GEOMETRY('POLYGON ((0.0 0.0,30.0 0.0,30.0 30.0,0.0 30.0,0.0 0.0))',8307)
                    ,'mask=ANYINTERACT') = 'TRUE';   -- Stuff above here
                     */
                    
                    // Nearest was false so return Ocean Name
                    if (strOceanLayerName == null) {
                        // Just return World Oceans
                        CountryNameBean b = new CountryNameBean();
                        b.setName("World Oceans");
                        b.setAltNames("");
                        b.setCodes("");
                        rslt.add(b);
                        log.info("getCountryNames End.  No Ocean Layer specified.");

                        gcnr.getCountryNames().addAll(rslt);
                        sec.setClassification(ClassificationType.U);                        
                        sec.getOwnerProducer().add("USA");
                        gcnr.setSecurity(sec);
                    } else {
                        // OK then query the Ocean Layer to get an Ocean Name

                        
                        
                        // Build the SQL
                        String strOceanSQL = "Select UNIQUE NAME from " + strOceanLayerName + " ";
                        strOceanSQL += "WHERE SDO_RELATE(SHAPE, ";
                        strOceanSQL += strGeometrySQL;
                        strOceanSQL += ",'mask=ANYINTERACT') = 'TRUE' ORDER BY NAME ";

                        //System.out.println(strOceanSQL);
                        log.info("getCountryNames OceanQuery " + strOceanSQL);


                        // Execute SQL
                        rset = m_stmt.executeQuery(strOceanSQL);

                        if (rset.next()) {
                            // if number of results > 0 then populate results and return

                            do {
                                CountryNameBean b = new CountryNameBean();
                                b.setName(rset.getString("NAME"));
                                b.setAltNames("");
                                b.setCodes("");

                                log.info("getCountryNames End.  Ocean found.");
                                rslt.add(b);
                            } while (rset.next());


                            gcnr.getCountryNames().addAll(rslt);
                            temp = getLayerInfo(strOceanLayerName);
                            gcnr.setSecurity(temp.getSecurity());
                            gcnr.setBoundaryLayer(temp.getBoundaryLayer());
                        
                            rset.close();

                        } else {
                            // Should never get here; there should always be an Ocean
                            throw new Exception("Unexpected Error. No Oceans Name found in " + strOceanLayerName +
                                    "If problem persists contact service provider.");                        
                        } // End rset.next from Ocean Query (Trap no ocean results)
                        
                    } // End of else for (strOceanLayerName == null) (Ocean Specified)

                } // End of else for nearest? (Nearest not requested)

            } // End of else from rset.next World Query (No Countries Returned)

                    
//            closeConnection();
            
        } catch (Exception e) {
            log.debug(getStackTrace(e));
            rslt.clear();
            CountryNameBean b = new CountryNameBean();
            b.setName("ERROR");
            b.setAltNames(e.getMessage());
            b.setCodes("");
            rslt.add(b);
            sec.setClassification(ClassificationType.U);
            sec.getOwnerProducer().add("USA");
            gcnr.setSecurity(sec);
            gcnr.getCountryNames().addAll(rslt);
        }  finally {           
            try {                
                rset.close();
            } catch (Exception e) {
                // ignore e
            }
            
            log.info("Closing Connection 2");
            closeConnection();
        } 
                

        
        log.info("getCountryNames End");              
        return gcnr;
    }

    /**
     * Reads Layer Information from the LayerInfo table.  In the table is 
     * not found the values are set to null.  
     *
     */
    private GetCountryNamesResponse getLayerInfo(String CountryTableName) {
        
        SecurityElement sec = new SecurityElement();
        GetCountryNamesResponse rslt = new GetCountryNamesResponse();
        
        BoundaryInfoBean bib = new BoundaryInfoBean();

        ResultSet rset = null;
        
        try {
            InputStream logger_is = getClass().getResourceAsStream(STR_LOG4J_FILE);
            Properties logger_p = new Properties();
            logger_p.load(logger_is);            
            PropertyConfigurator.configure(logger_p);            
            logger_is.close();            
            log.info("getSecurity Begin");              
            
            //openConnection();


            String strSQL = "";
            
            String strCountryLayerName = CountryTableName;
            
            // if the CountryLayerName is null the get the default
            if (CountryTableName == null) { 
                CountryNameBean cnb = checkCountryLayerName(null);
                strCountryLayerName = cnb.getName();
            }
            
            
            strSQL = "SELECT * from ";
            strSQL += m_layerInfoTable;
            strSQL += " where UPPER(table_name) = ";
            strSQL += "UPPER('" + strCountryLayerName + "')";

            log.info("getSecurity " + strSQL);
            
            rset = m_stmt.executeQuery(strSQL);


            if (rset.next()) {
                String strClassification = rset.getString("classification").trim();
                String strDisseminationControls = rset.getString("Dissemination_Controls");
                strCountryLayerName = rset.getString("LAYER_NAME");
                
                sec.getOwnerProducer().add("USA");
                                
                
                if (strClassification.equals("U")) {
                    sec.setClassification(ClassificationType.U);                    
                } else if (strClassification.equals("S")) {
                    sec.setClassification(ClassificationType.S);                    
                } else if (strClassification.equals("TS")) {
                    sec.setClassification(ClassificationType.TS);                    
                }
                
                if (strDisseminationControls == null) {
                    // add nothing
                } else {                
                    sec.getDisseminationControls().add(strDisseminationControls);
                }
                
                String strLastUpdated = rset.getString("LAST_UPDATED");
                
                bib.setName(strCountryLayerName);
                bib.setLastupdated(strLastUpdated);
                
                
            } else {            
                // table not found
                sec = null;
                bib= null;
                
            }

            rset.close();
            
            
        } catch (Exception e) {
            bib = null;
            sec = null; // Unrecognized layer
            log.debug(getStackTrace(e));
        }  finally {           
            try {                
                rset.close();
            } catch (Exception e) {
                // ignore e
            }
        }
              
        log.info("getSecurity End");              
        
        
        rslt.setSecurity(sec);
        rslt.setBoundaryLayer(bib);
        
        return rslt;
        
    }
    
    
    /**
     * 
     * @param args
     */
    public static void main(String args[]) {

        try {
            QueryWorldSDO qws = new QueryWorldSDO();

            GetParameterInfoResponse p = qws.getParameterInfo("CountryBoundaryLayer");
            System.out.println(p.getDescription());
            System.out.println(p.getValues());


            //String strWKT = "POLYGON ((0.0 0.0,3.0 0.0,3.0 3.0,0.0 3.0,0.0 0.0))";
            String strWKT = "POLYGON ((0.0 0.0,30.0 0.0,30.0 30.0,0.0 30.0,0.0 0.0))";
            //String strWKT = "POINT(3600000000000010 10)";
            //String strWKT = "LINESTRING (0.0 0.0,10.0 10.0, 20.0 10.0)";

            ArrayList<CountryNameBean> rslt = new ArrayList<CountryNameBean>();

            Double buffer = new Double(500);


            Boolean nearest = new Boolean(true);

            GetCountryNamesResponse gcnr = new GetCountryNamesResponse();
            //gcnr = qws.getCountryNames(null, null, "ISO3CHR,ISO3DIG", null, null, null);  // if strWKT is null all others are ignored
            //gcnr = qws.getCountryNames(null, null, null, strWKT, null, null);
            gcnr = qws.getCountryNames(null, null, null, strWKT, null, buffer);
            //gcnr = qws.getCountryNames("CNTRY06SP", null, "ISO3CHR, ISO3DIG, FIPS, ISO2CHR", strWKT, nearest, buffer);
            //gcnr = qws.getCountryNames(null, null, "ISO3CHR, ISO3DIG, FIPS, ISO2CHR", strWKT, nearest, buffer);


            rslt = (ArrayList<CountryNameBean>) gcnr.getCountryNames();

            CountryNameBean b = new CountryNameBean();


            if (rslt.isEmpty()) {
                System.out.println("No Beans Returned");
            } else {

                int i = 0;

                while (i < rslt.size()) {
                    b = rslt.get(i);
                    System.out.println(i + 1);
                    System.out.println(b.getName());
                    System.out.println(b.getAltNames());
                    System.out.println(b.getCodes());
                    i++;
                }
            }


            SecurityElement sec = gcnr.getSecurity();
            System.out.println(sec);

            BoundaryInfoBean bib = gcnr.getBoundaryLayer();
            System.out.println(bib);

            qws.closeConnection();


            //QueryWorldSDO qws = new QueryWorldSDO();

//            if (!qws.openConnection()) {
//                System.out.println("Failed to open database connection");
//            }        
//
//            CountryNameBean cnb = new CountryNameBean();
//            cnb = qws.checkWKT("Polygon((3 3,4 3,4 4,5 5))");
//            System.out.println(cnb.getName());
//            System.out.println(cnb.getAltNames());
//            System.out.println(cnb.getCodes());

    //        cnb = qws.checkWKT("LineString(0 0,0.01 0.01,2 2,3 3)");
    //        System.out.println(cnb.getName());
    //        System.out.println(cnb.getAltNames());
    //        System.out.println(cnb.getCodes());
    //
    //        cnb = qws.checkWKT("Polygon((0 0,0.01 0.01,2 2,0 0))");
    //        System.out.println(cnb.getName());
    //        System.out.println(cnb.getAltNames());
    //        System.out.println(cnb.getCodes());
    //
    //        cnb = qws.checkWKT("Point(300 300)");
    //        System.out.println(cnb.getName());
    //        System.out.println(cnb.getAltNames());
    //        System.out.println(cnb.getCodes());
    //
    //        cnb = qws.checkWKT("LineString(0 0,0.01 0.01,200 200,3 3)");
    //        System.out.println(cnb.getName());
    //        System.out.println(cnb.getAltNames());
    //        System.out.println(cnb.getCodes());
    //
    //        cnb = qws.checkWKT("Polygon((360 0,361 0,361 30,360 0))");
    //        System.out.println(cnb.getName());
    //        System.out.println(cnb.getAltNames());
    //        System.out.println(cnb.getCodes());        

            qws.closeConnection();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DataSource getJdbcWs() throws NamingException {
        Context c = new InitialContext();
        //return (DataSource) c.lookup("java:comp/env/" + m_JNDI_Name);
        return (DataSource) c.lookup(m_JNDI_Name);
    }

}
