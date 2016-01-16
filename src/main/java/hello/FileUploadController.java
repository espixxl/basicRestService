package hello;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@ConfigurationProperties
public class FileUploadController {
	
	@Value("{test}")
	private String test;
//	
//	@Value("{repository.folder}")
//	private String repositoryFolder;
	
	@Autowired
	private FileRepository fileRepository;
	 
    @RequestMapping(value="/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("name") String name,
            @RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                
                String uploadFileName = fileRepository.getFolder() + File.separator;
                System.out.println(test);
                if (name != null && name.length()>0) {
                	uploadFileName += name;
                } else {
                	uploadFileName += file.getOriginalFilename();
                }
                
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(uploadFileName)));
                stream.write(bytes);
                stream.close();
                return "You successfully uploaded " + name + "!";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

//	public String getRepositoryFolder() {
//		return repositoryFolder;
//	}
//
//	public void setRepositoryFolder(String repositoryFolder) {
//		this.repositoryFolder = repositoryFolder;
//	}

    
    
}