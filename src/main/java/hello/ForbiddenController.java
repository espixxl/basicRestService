package hello;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenController extends RuntimeException {

    //@RequestMapping(value="/forbidden", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }
    
//    public ResponseEntity<ResponseStatus> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex){
//
//        ResponseStatus responseStatus = new ResponseStatus("400", "Bad Request. " + ex);
//        responseStatus.setResponseStatusTime(timestampService.createTimestamp());
//        HttpStatus status = HttpStatus.BAD_REQUEST;
//
//        ResponseEntity<ResponseStatus> response = new ResponseEntity<ResponseStatus>(responseStatus, status);
//    }
    
    @RequestMapping(value="/forbidden", method=RequestMethod.GET)
    public ResponseEntity handleException() {
        // log exception
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("Error Message");
    }  
}
