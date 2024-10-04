package com.e_learning.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.e_learning.payloads.ApiResponse;
import com.e_learning.payloads.CategoryDto;

import com.e_learning.services.CategoryService;
import com.e_learning.services.FileService;



@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	

	@Autowired
	private FileService fileService;

	@Value("${project.image}")
	private String path;
	// create

	@PostMapping("/")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
	    CategoryDto createCategory = this.categoryService.createCategory(categoryDto);
	    return new ResponseEntity<>(createCategory, HttpStatus.CREATED);
	}


	// update

	@PutMapping("/{catId}")
	public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto,
			@PathVariable Integer catId) {
		CategoryDto updatedCategory = this.categoryService.updateCategory(categoryDto, catId);
		return new ResponseEntity<CategoryDto>(updatedCategory, HttpStatus.OK);
	}

	// delete

	@DeleteMapping("/{catId}")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer catId) {
		this.categoryService.deleteCategory(catId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("category is deleted successfully !!", true),
				HttpStatus.OK);
	}
	// get

	@GetMapping("/{catId}")
	public ResponseEntity<CategoryDto> getCategory(@PathVariable Integer catId) {

		CategoryDto categoryDto = this.categoryService.getCategory(catId);

		return new ResponseEntity<CategoryDto>(categoryDto, HttpStatus.OK);

	}

	// get all
	@GetMapping("/")
	public ResponseEntity<List<CategoryDto>> getCategories() {
		List<CategoryDto> categories = this.categoryService.getCategories();
		return ResponseEntity.ok(categories);
	}

	@GetMapping("/latest")
    public ResponseEntity<List<CategoryDto>> getLatestCategories() {
        List<CategoryDto> latestCategories = categoryService.getLatestCategories();
        return ResponseEntity.ok(latestCategories);
    }
	// Search by category title
    @GetMapping("/search/title/{categoryTitle}")
    public ResponseEntity<List<CategoryDto>> searchByCategoryTitle(@PathVariable String categoryTitle) {
        List<CategoryDto> categories = categoryService.searchByCategoryTitle(categoryTitle);
        return ResponseEntity.ok(categories);
    }

    // Search by main category
    @GetMapping("/search/main/{mainCategory}")
    public ResponseEntity<List<CategoryDto>> searchByMainCategory(@PathVariable String mainCategory) {
        List<CategoryDto> categories = categoryService.searchByMainCategory(mainCategory);
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<CategoryDto>> searchCategories(@PathVariable String keyword) {
        List<CategoryDto> categories = categoryService.searchCategories(keyword);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
    
  //-------------Image upload-------------------
  	// Post method for file upload
      @PostMapping("/file/upload/{categoryId}")
      public ResponseEntity<CategoryDto> uploadCategoryFile(@RequestParam("file") MultipartFile file,
                                                    @PathVariable Integer categoryId) throws IOException {
          CategoryDto categoryDto = this.categoryService.getCategory(categoryId);
          String fileName = this.fileService.uploadFile(path, file);
          categoryDto.setImageName(fileName);// Assuming you want to set the uploaded file name as imageName
          CategoryDto updatedcategory = this.categoryService.updateCategory(categoryDto, categoryId);
          return new ResponseEntity<>(updatedcategory, HttpStatus.OK);
      }
    //-------------method to serve files------------------
    @GetMapping(value = "/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable("imageName") String imageName,
            HttpServletResponse response
    ) throws IOException {
    	String fileExtension = FilenameUtils.getExtension(imageName).toLowerCase();
        MediaType mediaType = MediaType.IMAGE_JPEG;  // Default

        if (fileExtension.equals("png")) {
            mediaType = MediaType.IMAGE_PNG;
        } else if (fileExtension.equals("jpg") || fileExtension.equals("jpeg")) {
            mediaType = MediaType.IMAGE_JPEG;
        }

        response.setContentType(mediaType.toString());
        try (InputStream resource = this.fileService.getResource(path, imageName)) {
            StreamUtils.copy(resource, response.getOutputStream());
        }
    }
//        InputStream resource = this.fileService.getResource(path, imageName);
//        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
//        StreamUtils.copy(resource,response.getOutputStream())   ;
//
//    }
}
