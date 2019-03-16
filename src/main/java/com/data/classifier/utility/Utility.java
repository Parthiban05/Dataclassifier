package com.data.classifier.utility;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.data.classifier.model.Confidential;
import com.data.classifier.model.Defaultdata;
import com.data.classifier.model.Highconfidential;

public class Utility
{
    private static Cipher blowfishcipher;
    private static Cipher aescipher;
    private static SecretKey blowfishsecretKey;
    private static SecretKey aesSecretKey;
    private static String[] columns = { "Emp ID", "Name Prefix", "First Name", "Middle Initial", "Last Name", "Gender", "E Mail", "Father's Name", "Mother's Name", "Mother's Maiden Name", "Date of Birth", "Time of Birth", "Age in Yrs.", "Weight in Kgs.", "Date of Joining", "Quarter of Joining", "Half of Joining", "Year of Joining", "Month of Joining", "Month Name of Joining", "Short Month", "Day of Joining", "DOW of Joining", "Short DOW", "Age in Company (Years)", "Salary", "Last % Hike", "SSN", "Phone No.", "Place Name", "County", "City", "State", "Zip", "Region", "User Name", "Password" };
    private static HashMap<String, String> columnmap = new HashMap<String, String>();
    private static HashMap<String, String> datamap = new HashMap<String, String>();
    private static HashMap<String, String> propertymap = new HashMap<String, String>();
    private static HashMap<String, List<String>> dataset = new HashMap<String, List<String>>();
    public static com.data.classifier.Classifier<String, String> bayes = new com.data.classifier.BayesClassifier<String, String>();
    public static List<Highconfidential> highconfidentials = new ArrayList<Highconfidential>();
    public static List<Highconfidential> highconfidentialsEncryptData = new ArrayList<Highconfidential>();
    public static List<Confidential> confidentials = new ArrayList<Confidential>();
    public static List<Confidential> confidentialsEncryptData = new ArrayList<Confidential>();
    public static List<Defaultdata> defaultdatas = new ArrayList<Defaultdata>();
    public static String downloadUrl = "";
    public static Map<String,String> downloadLinks = new HashMap<String,String>();
    static
    {
        columnmap.put("Emp ID", "EmployeeId");
        columnmap.put("Name Prefix", "Name");
        columnmap.put("Prefix", "Prefix");
        columnmap.put("First Name", "Firstname");
        columnmap.put("Middle Initial", "Middlename");
        columnmap.put("Last Name", "Lastname");
        columnmap.put("Gender", "Gender");
        columnmap.put("E Mail", "Email");
        columnmap.put("Father's Name", "Fathername");
        columnmap.put("Mother's Name", "Mothername");
        columnmap.put("Mother's Maiden Name", "Mother Maiden Name");
        columnmap.put("Date of Birth", "Dob");
        columnmap.put("Time of Birth", "Tob");
        columnmap.put("Age in Yrs.", "Age in Years");
        columnmap.put("Weight in Kgs.", "Weight in Kgs");
        columnmap.put("Date of Joining", "Doj");
        columnmap.put("Quarter of Joining", "Qoj");
        columnmap.put("Half of Joining", "Hoj");
        columnmap.put("Year of Joining", "Yoj");
        columnmap.put("Month of Joining", "Moj");
        columnmap.put("Month Name of Joining", "Mnoj");
        columnmap.put("Short Month", "Shortmonth");
        columnmap.put("Day of Joining", "Day of joining");
        columnmap.put("DOW of Joining", "DOW of Joining");
        columnmap.put("Short DOW", "Short DOW");
        columnmap.put("Age in Company (Years)", "Age in Company (Years)");
        columnmap.put("Salary", "Salary");
        columnmap.put("Last % Hike", "Last % Hike");
        columnmap.put("SSN", "SSN");
        columnmap.put("Phone No.", "Phone No");
        columnmap.put("Place Name", "Place Name");
        columnmap.put("County", "County");
        columnmap.put("City", "City");
        columnmap.put("State", "State");
        columnmap.put("Zip", "Zip");
        columnmap.put("Region", "Region");
        columnmap.put("User Name", "User Name");
        columnmap.put("Password", "Password");

        datamap.put("Emp ID", "highconfidential");
        datamap.put("Name Prefix", "confidential");
        datamap.put("First Name", "highconfidential");
        datamap.put("Middle Initial", "confidential");
        datamap.put("Last Name", "highconfidential");
        datamap.put("Gender", "confidential");
        datamap.put("E Mail", "highconfidential");
        datamap.put("Father's Name", "confidential");
        datamap.put("Mother's Name", "confidential");
        datamap.put("Mother's Maiden Name", "confidential");
        datamap.put("Date of Birth", "highconfidential");
        datamap.put("Time of Birth", "highconfidential");
        datamap.put("Age in Yrs.", "highconfidential");
        datamap.put("Weight in Kgs.", "confidential");
        datamap.put("Date of Joining", "highconfidential");
        datamap.put("Quarter of Joining", "confidential");
        datamap.put("Half of Joining", "confidential");
        datamap.put("Year of Joining", "highconfidential");
        datamap.put("Month of Joining", "confidential");
        datamap.put("Month Name of Joining", "confidential");
        datamap.put("Short Month", "highconfidential");
        datamap.put("Day of Joining", "confidential");
        datamap.put("DOW of Joining", "confidential");
        datamap.put("Short DOW", "confidential");
        datamap.put("Age in Company (Years)", "highconfidential");
        datamap.put("Salary", "highconfidential");
        datamap.put("Last % Hike", "highconfidential");
        datamap.put("SSN", "highconfidential");
        datamap.put("Phone No.", "highconfidential");
        datamap.put("Place Name", "confidential");
        datamap.put("County", "confidential");
        datamap.put("City", "confidential");
        datamap.put("State", "confidential");
        datamap.put("Zip", "confidential");
        datamap.put("Region", "confidential");
        datamap.put("User Name", "highconfidential");
        datamap.put("Password", "highconfidential");

        propertymap.put("Emp ID", "employeeid");
        propertymap.put("Name Prefix", "nameprefix");
        propertymap.put("First Name", "firstname");
        propertymap.put("Middle Initial", "middleinitial");
        propertymap.put("Last Name", "lastname");
        propertymap.put("Gender", "gender");
        propertymap.put("E Mail", "email");
        propertymap.put("Father's Name", "fathername");
        propertymap.put("Mother's Name", "mothername");
        propertymap.put("Mother's Maiden Name", "mothermaidenname");
        propertymap.put("Date of Birth", "dob");
        propertymap.put("Time of Birth", "tob");
        propertymap.put("Age in Yrs.", "ageinyears");
        propertymap.put("Weight in Kgs.", "weightinkgs");
        propertymap.put("Date of Joining", "dateofjoining");
        propertymap.put("Quarter of Joining", "quarterofjoining");
        propertymap.put("Half of Joining", "halfofjoining");
        propertymap.put("Year of Joining", "yearofjoining");
        propertymap.put("Month of Joining", "monthofjoining");
        propertymap.put("Month Name of Joining", "monthnameofjoining");
        propertymap.put("Short Month", "shortmonth");
        propertymap.put("Day of Joining", "dayofjoining");
        propertymap.put("DOW of Joining", "dowofjoining");
        propertymap.put("Short DOW", "shortdow");
        propertymap.put("Age in Company (Years)", "ageincompany");
        propertymap.put("Salary", "salary");
        propertymap.put("Last % Hike", "lastpercentagehike");
        propertymap.put("SSN", "ssn");
        propertymap.put("Phone No.", "phone");
        propertymap.put("Place Name", "placename");
        propertymap.put("County", "county");
        propertymap.put("City", "city");
        propertymap.put("State", "state");
        propertymap.put("Zip", "zip");
        propertymap.put("Region", "region");
        propertymap.put("User Name", "username");
        propertymap.put("Password", "password");
    }

    public static void readCsv(String filePath)
    {
        try
        {
            Reader reader = Files.newBufferedReader(Paths.get(filePath));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader("Emp ID", "Name Prefix", "First Name", "Middle Initial", "Last Name", "Gender", "E Mail", "Father's Name", "Mother's Name", "Mother's Maiden Name", "Date of Birth", "Time of Birth", "Age in Yrs.", "Weight in Kgs.", "Date of Joining", "Quarter of Joining", "Half of Joining", "Year of Joining", "Month of Joining", "Month Name of Joining", "Short Month", "Day of Joining", "DOW of Joining", "Short DOW", "Age in Company (Years)", "Salary", "Last % Hike", "SSN", "Phone No.", "Place Name", "County", "City", "State", "Zip", "Region", "User Name", "Password").withIgnoreHeaderCase().withTrim());
            int count = 0;
            for (CSVRecord csvRecord : csvParser)
            {
                if (count == 0)
                {
                    count += 1;
                    continue;
                }

                Highconfidential highconfidential = new Highconfidential();
                Highconfidential highconfidentialEncryptData = new Highconfidential();
                Confidential confidential = new Confidential();
                Confidential confidentialEncryptData = new Confidential();

                Defaultdata defaultdata = new Defaultdata();
                for (int index = 0; index < columns.length; index++)
                {
                    String columnName = columns[index].trim();
                    List<String> dataValues = dataset.get(columnName);
                    if (dataValues == null)
                    {
                        dataValues = new ArrayList<String>();
                    }
                    dataValues.add(csvRecord.get(index));
                    dataset.put(columnName, dataValues);
                    if (datamap.get(columnName).equals("highconfidential"))
                    {
                        // high confidential not encrypted
                        Map<String, String> properties = BeanUtils.describe(highconfidential);
                        properties.put(propertymap.get(columnName), csvRecord.get(index));
                        BeanUtils.populate(highconfidential, properties);

                        // high confidential encrypted
                        Map<String, String> propertiesEncrypted = BeanUtils.describe(highconfidentialEncryptData);
                        propertiesEncrypted.put(propertymap.get(columnName), aesencrypt(csvRecord.get(index)));
                        BeanUtils.populate(highconfidentialEncryptData, propertiesEncrypted);

                    }
                    else if (datamap.get(columnName).equals("confidential"))
                    {
                        Map<String, String> properties = BeanUtils.describe(confidential);
                        properties.put(propertymap.get(columnName), csvRecord.get(index));
                        BeanUtils.populate(confidential, properties);

                        // confidential encrypted
                        Map<String, String> propertiesEncrypted = BeanUtils.describe(confidentialEncryptData);
                        propertiesEncrypted.put(propertymap.get(columnName), blowfishencrypt(csvRecord.get(index)));
                        BeanUtils.populate(confidentialEncryptData, propertiesEncrypted);
                    }
                    Map<String, String> properties = BeanUtils.describe(defaultdata);
                    properties.put(propertymap.get(columnName), csvRecord.get(index));
                    BeanUtils.populate(defaultdata, properties);
                }
                highconfidentials.add(highconfidential);
                highconfidentialsEncryptData.add(highconfidentialEncryptData);
                confidentialsEncryptData.add(confidentialEncryptData);
                confidentials.add(confidential);
                defaultdatas.add(defaultdata);
            }
            Set<Entry<String, List<String>>> entrySet = dataset.entrySet();
            Iterator<Entry<String, List<String>>> entryItr = entrySet.iterator();
            while (entryItr.hasNext())
            {
                Entry<String, List<String>> entry = entryItr.next();
                if (datamap.get(entry.getKey()).equals("highconfidential"))
                {
                    bayes.learn("highconfidential", entry.getValue());
                }
                else if (datamap.get(entry.getKey()).equals("confidential"))
                {
                    bayes.learn("confidential", entry.getValue());
                }
            }
            bayes.setMemoryCapacity(500);
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
    }

    private static void blowfishinit()
    {
        try
        {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("Blowfish");
            keyGenerator.init(128);
            blowfishsecretKey = keyGenerator.generateKey();
            blowfishcipher = Cipher.getInstance("Blowfish");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private static void aesinit()
    {
        try
        {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            aesSecretKey = keyGenerator.generateKey();
            aescipher = Cipher.getInstance("AES");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static String blowfishencrypt(String plainText) throws Exception
    {
        blowfishinit();
        byte[] plainTextByte = plainText.getBytes();
        blowfishcipher.init(Cipher.ENCRYPT_MODE, blowfishsecretKey);
        byte[] encryptedByte = blowfishcipher.doFinal(plainTextByte);
        Base64.Encoder encoder = Base64.getEncoder();
        String encryptedText = encoder.encodeToString(encryptedByte);
        return encryptedText;
    }

    public static String aesencrypt(String plainText) throws Exception
    {
        aesinit();
        byte[] plainTextByte = plainText.getBytes();
        aescipher.init(Cipher.ENCRYPT_MODE, aesSecretKey);
        byte[] encryptedByte = aescipher.doFinal(plainTextByte);
        Base64.Encoder encoder = Base64.getEncoder();
        String encryptedText = encoder.encodeToString(encryptedByte);
        return encryptedText;
    }

    public static String verifyData(String input)
    {
        String category = bayes.classify(Arrays.asList(input)).getCategory();
        return category;
    }

    public static List<Highconfidential> getHighConfidentialData()
    {
        return highconfidentials;
    }

    public static List<Confidential> getConfidentialData()
    {
        return confidentials;
    }

    public static List<Defaultdata> getDefaultData()
    {
        return defaultdatas;
    }

    public static List<String> getDefaultColumns()
    {
        List<String> defaultColumns = new ArrayList<String>();
        defaultColumns.add("Employee ID");
        defaultColumns.add("Name Prefix");
        defaultColumns.add("First Name");
        defaultColumns.add("Middle Name");
        defaultColumns.add("Last Name");
        defaultColumns.add("Gender");
        defaultColumns.add("Email");
        defaultColumns.add("Father's Name");
        defaultColumns.add("Mother's Name");
        defaultColumns.add("Mother's Maiden Name");
        defaultColumns.add("Date of Birth");
        defaultColumns.add("Time of Birth");
        defaultColumns.add("Age in Years");
        defaultColumns.add("Weight in Kgs");
        defaultColumns.add("Date of Joining");
        defaultColumns.add("Quarter of Joining");
        defaultColumns.add("Half of Joining");
        defaultColumns.add("Year of Joining");
        defaultColumns.add("Month of Joining");
        defaultColumns.add("Month Name of Joining");
        defaultColumns.add("Short Month");
        defaultColumns.add("Day of Joining");
        defaultColumns.add("DOW of Joining");
        defaultColumns.add("Short DOW");
        defaultColumns.add("Age in Company");
        defaultColumns.add("Salary");
        defaultColumns.add("Last Percentage Hike");
        defaultColumns.add("SSN");
        defaultColumns.add("Phone Number");
        defaultColumns.add("Place Name");
        defaultColumns.add("Country");
        defaultColumns.add("City");
        defaultColumns.add("State");
        defaultColumns.add("Zip");
        defaultColumns.add("Region");
        defaultColumns.add("User Name");
        defaultColumns.add("Password");

        return defaultColumns;
    }

    public static List<String> getHighConfidentialColumns()
    {
        List<String> highConfidential = new ArrayList<String>();
        highConfidential.add("Employee ID");
        highConfidential.add("First Name");
        highConfidential.add("Last Name");
        highConfidential.add("Email");
        highConfidential.add("Date of Birth");
        highConfidential.add("Time of Birth");
        highConfidential.add("Age in Years");
        highConfidential.add("Date of Joining");
        highConfidential.add("Year of Joining");
        highConfidential.add("Short Month");
        highConfidential.add("Age in Company");
        highConfidential.add("Salary");
        highConfidential.add("Last Percentage Hike");
        highConfidential.add("SSN");
        highConfidential.add("Phone Number");
        highConfidential.add("User Name");
        highConfidential.add("Password");

        return highConfidential;
    }

    public static List<String> getConfidentialColumns()
    {
        List<String> confidential = new ArrayList<String>();
        confidential.add("Name Prefix");
        confidential.add("Middle Name");
        confidential.add("Gender");
        confidential.add("Father's Name");
        confidential.add("Mother's Name");
        confidential.add("Mother's Maiden Name");
        confidential.add("Weight in Kgs");
        confidential.add("Quarter of Joining");
        confidential.add("Half of Joining");
        confidential.add("Month of Joining");
        confidential.add("Month Name of Joining");
        confidential.add("Day of Joining");
        confidential.add("DOW of Joining");
        confidential.add("Short DOW");
        confidential.add("Place Name");
        confidential.add("Country");
        confidential.add("City");
        confidential.add("State");
        confidential.add("Zip");
        confidential.add("Region");

        return confidential;
    }

    public static List<Highconfidential> getHighConfidentialEncryptedData()
    {
        return highconfidentialsEncryptData;
    }

    public static List<Confidential> getConfidentialEncryptedData()
    {
        return confidentialsEncryptData;
    }
}
