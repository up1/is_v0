package is.web.controller;

import is.web.model.SearchFileUpload;
import is.web.model.SearchResult;
import is.web.service.SimpleSearchService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class FileUploadController {

	@Autowired
	SimpleSearchService simpleSearchService;

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String getUploadForm(Model model) {
		model.addAttribute(new SearchFileUpload());
		return "index";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ModelAndView create(SearchFileUpload searchFileUpload, BindingResult result) {
		ModelAndView mv = new ModelAndView("/index");
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				System.err.println("Error: " + error.getCode() + " - " + error.getDefaultMessage());
			}
			return mv;
		}

		// Some type of file processing...
		System.err.println("-------------------------------------------");
		System.err.println("Test upload: " + searchFileUpload.getName());
		System.err.println("Test upload: " + searchFileUpload.getFileData().getOriginalFilename());
		System.err.println("-------------------------------------------");

		
		try {
			List<SearchResult> resultList = simpleSearchService.search(searchFileUpload.getFileData().getBytes(), searchFileUpload.getName());
			SearchResult tmp = null;
			for (SearchResult searchResult : resultList) {
				if(searchResult.getFileName().equals("spd_20120916204715_b.jpg")) {
					tmp = searchResult;
				}				
			}
			resultList.remove(tmp);
			mv.addObject("results", resultList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mv;
	}

}
