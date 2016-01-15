package hello;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileRepositoryController {

	@RequestMapping(value = "/hi")
	public String hi(){
		return "hi";
	}
	
	@RequestMapping(value = "/bye")
	public String bye(){
		return "bye";
	}
}
