package com.data.classifier.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.data.classifier.model.Defaultdata;
import com.data.classifier.model.Highconfidential;
import com.data.classifier.model.MailEntity;
import com.data.classifier.model.OtpEntity;
import com.data.classifier.service.DataclassifierService;
import com.data.classifier.utility.Utility;

@Controller
@RequestMapping("/classifier")
public class ClassifierController
{

    @Autowired private DataclassifierService dataclassifierService;

    @GetMapping("/ping")
    public @ResponseBody String ping()
    {
        return "Classifier is pinging -" + System.currentTimeMillis();
    }

    @GetMapping("/verifydata/{input}")
    public @ResponseBody String verifydata(@PathVariable("input") String input)
    {
        return dataclassifierService.verifyData(input);
    }

    @GetMapping("/{type}")
    public String index(@PathVariable("type") String type,
                        Model model,
                        HttpServletRequest request,
                        HttpServletResponse response)
    {
        model.addAttribute("columnmaps", Utility.getDefaultColumns());
        List<Defaultdata> defaultDataList = Utility.getDefaultData();
        model.addAttribute("datasets", defaultDataList);
        if (type.equals("highconfidential") && defaultDataList.size() > 0)
        {
            model.addAttribute("message", "High Confidential data");
            model.addAttribute("columnmaps", Utility.getHighConfidentialColumns());
            model.addAttribute("datasets", Utility.getHighConfidentialData());
            model.addAttribute("showdownloadurl", true);
        }
        else if (type.equals("confidential") && defaultDataList.size() > 0)
        {
            model.addAttribute("message", "Confidential data");
            model.addAttribute("columnmaps", Utility.getConfidentialColumns());
            model.addAttribute("datasets", Utility.getConfidentialData());
            model.addAttribute("showdownloadurl", true);
        }
        else if (type.equals("encryptdata") && defaultDataList.size() > 0)
        {
            model.addAttribute("message", "High Confidential Encrypted Data");
            model.addAttribute("columnmaps", Utility.getHighConfidentialColumns());
            model.addAttribute("datasets", Utility.getHighConfidentialEncryptedData());
            model.addAttribute("showdownloadurl", true);
        }
        else if (type.equals("encryptconfidentialdata") && defaultDataList.size() > 0)
        {
            model.addAttribute("message", "Confidential Encrypted Data");
            model.addAttribute("columnmaps", Utility.getConfidentialColumns());
            model.addAttribute("datasets", Utility.getConfidentialEncryptedData());
            model.addAttribute("showdownloadurl", true);
        }
        else if (defaultDataList.size() > 0)
        {
            model.addAttribute("message", "Default data");
            model.addAttribute("showdownloadurl", true);
        }
        model.addAttribute("type", type);
        return "upload";
    }

    @PostMapping("/upload") // //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   Model model,
                                   HttpServletRequest request,
                                   HttpServletResponse response)
    {
        //Save the uploaded file to this folder

        String UPLOADED_FOLDER = "D:\\Eclipse\\Workspace\\Dataclassifier\\src\\main\\resources\\static\\upload\\";
        String downloadfolder = "/upload/";

        if (file.isEmpty())
        {
            model.addAttribute("message", "Please select a file to upload");
            return "upload";
        }

        try
        {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            String filePath = UPLOADED_FOLDER + file.getOriginalFilename();
            Path path = Paths.get(filePath);
            Files.write(path, bytes);
            Utility.readCsv(filePath);

            model.addAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");
            model.addAttribute("columnmaps", Utility.getDefaultColumns());
            model.addAttribute("datasets", Utility.getDefaultData());
            Utility.downloadUrl = downloadfolder + file.getOriginalFilename();
            model.addAttribute("showdownloadurl", true);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return "upload";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus()
    {
        return "uploadStatus";
    }

    @GetMapping("/download/{type}")
    public String download(Model model, @PathVariable("type") String type)
    {
        model.addAttribute("mailEntity", new MailEntity());
        model.addAttribute("otpEntity", new OtpEntity());
        model.addAttribute("showemailform", true);
        if (type.equals("emailsuccess"))
        {
            model.addAttribute("showotpform", true);
            model.addAttribute("emailSuccess", "Email sent successfully, please check your inbox and enter the OTP to download the file");
            model.addAttribute("showemailform", false);
        }
        else if (type.equals("emailfailed"))
        {
            model.addAttribute("emailSuccess", "Email sent failed, please try agian later");
        }
        else if (type.equals("otpsuccess"))
        {
            model.addAttribute("emailSuccess", "Your OTP has been validated successfully, please click here to download");
            model.addAttribute("downloadurl", Utility.downloadUrl);
        }
        else if (type.equals("otpfail"))
        {
            model.addAttribute("showotpform", true);
            model.addAttribute("emailSuccess", "Your OTP is wrong, please enter the valid OTP");
            model.addAttribute("showemailform", false);
        }
        return "download";
    }

    @GetMapping("/highconfidentials")
    public @ResponseBody List<Highconfidential> highconfidentials()
    {
        return dataclassifierService.highconfidentials();
    }

    @GetMapping("/getDefaultData")
    public @ResponseBody List<Defaultdata> getDefaultData()
    {
        return Utility.getDefaultData();
    }

    @PostMapping("/sendmail")
    public String sendmail(@ModelAttribute MailEntity mailEntity)
    {
        System.out.println("Email:" + mailEntity.getEmail());
        boolean result = dataclassifierService.sendEmail(mailEntity.getEmail());
        if (result) { return "redirect:" + "/classifier/download/" + "emailsuccess"; }
        return "redirect:" + "/classifier/download/" + "emailfailed";
    }

    @PostMapping("/validateotp")
    public String sendmail(@ModelAttribute OtpEntity otpEntity)
    {
        System.out.println("OTP:" + otpEntity.getOtp());
        if (Utility.downloadLinks.get(otpEntity.getOtp()) != null)
        {
            Utility.downloadUrl = Utility.downloadLinks.get(otpEntity.getOtp());
            return "redirect:" + "/classifier/download/" + "otpsuccess";
        }
        return "redirect:" + "/classifier/download/" + "otpfail";
    }
}
