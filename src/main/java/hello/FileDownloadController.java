package hello;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ConfigurationProperties
public class FileDownloadController {
	
	@Value("{test}")
	private String test;
//	
//	@Value("{repository.folder}")
//	private String repositoryFolder;
	
	@Autowired
	private FileRepository fileRepository;
	 
//    @RequestMapping(value="/download", method=RequestMethod.GET)
//    public @ResponseBody String provideUploadInfo() {
//        return "You can upload a file by posting to this same URL.";
//    }

//    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/pdf")
    //@RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> download(@RequestParam("name") String name)
            throws IOException {

    	FileInputStream fileInputStream = new FileInputStream(fileRepository.getFolder() + File.separator + name);

        return ResponseEntity
                .ok()
                .contentLength(fileInputStream.getChannel().size())
                .contentType(
                        MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(fileInputStream));
    }
    
    private static final int BUFFER_SIZE = 4096;
    
    @RequestMapping(value = "/{fileName:.+}", method = RequestMethod.GET)
    public void doDownloadFile(@PathVariable String fileName, HttpServletResponse response) throws IOException {
    	doDownload(fileName, response);
    }
    
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void doDownload(@RequestParam("name") String name, HttpServletResponse response) throws IOException {
 
        // get absolute path of the application
//        ServletContext context = request.getServletContext();
//        String appPath = context.getRealPath("");
//        System.out.println("appPath = " + appPath);
 
    	
        // construct the complete absolute path of the file
        String fullPath = fileRepository.getFolder() + File.separator + name;     
        String mimeType = Files.probeContentType(Paths.get(fullPath));  //yeahhhhhhhh
        File downloadFile = new File(fullPath);
        FileInputStream inputStream = new FileInputStream(downloadFile);
         
        // get MIME type of the file
        //String mimeType = context.getMimeType(fullPath);
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }
        // Response header
//        response.setHeader("Content-Disposition", "attachment; filename=\""
//                + downloadFile.getName() + "\"");
        System.out.println("MIME type: " + mimeType);
 
        // set content attributes for the response
        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());
 
        // set headers for the response
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                downloadFile.getName());
        response.setHeader(headerKey, headerValue);
 
        // get output stream of the response
        OutputStream outStream = response.getOutputStream();
 
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;
 
        // write bytes read from the input stream into the output stream
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
 
        inputStream.close();
        outStream.close();
 
    }
    
    //@RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(@RequestParam("name") String name, HttpServletResponse response) throws IOException {
 
    	String fullPath = fileRepository.getFolder() + File.separator + name; 
        File file = new File(fullPath);
        InputStream is = new FileInputStream(file);
 
        // MIME type of the file
        response.setContentType("application/octet-stream");
        // Response header
        response.setHeader("Content-Disposition", "attachment; filename=\""
                + file.getName() + "\"");
        // Read from the file and write into the response
        OutputStream os = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        os.flush();
        os.close();
        is.close();
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