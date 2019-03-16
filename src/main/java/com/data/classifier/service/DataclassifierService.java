package com.data.classifier.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.data.classifier.model.Highconfidential;
import com.data.classifier.utility.MailUtil;
import com.data.classifier.utility.Utility;

@Service
public class DataclassifierService
{
    
    public List<Highconfidential> highconfidentials()
    {
        return Utility.getHighConfidentialData();
    }

    public String verifyData(String input)
    {
        return Utility.verifyData(input);
    }

    public boolean sendEmail(String toaddress)
    {
        int otp = MailUtil.getOTP();
        String bodyContent = "Your OTP is " + MailUtil.getOTP();
        boolean result = MailUtil.sendmail(toaddress, bodyContent);
        Utility.downloadLinks.put(String.valueOf(otp), Utility.downloadUrl);
        return result;
    }
}
