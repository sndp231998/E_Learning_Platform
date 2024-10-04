package com.e_learning.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

import com.e_learning.config.AppConstants;
import com.e_learning.entities.User;
import com.e_learning.payloads.ApiResponse;
import com.e_learning.payloads.ExamDto;
import com.e_learning.payloads.PostDto;
import com.e_learning.payloads.PostResponse;
import com.e_learning.services.FileService;
import com.e_learning.services.PostService;



@RestController
@RequestMapping("/api/v1/")
public class PostController {

	@Autowired
	private PostService postService;

	@Autowired
	private FileService fileService;

	@Value("${project.image}")
	private String path;
	
//	create
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @PathVariable Integer userId,
			@PathVariable Integer categoryId) {
		
		// Extract the user ID from the authentication context
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    User userDetails = (User) authentication.getPrincipal();
	    Integer tokenUserId = userDetails.getId(); // Get the user ID from the token


	    // Compare the user ID from the token with the user ID from the path variable
	    if (!tokenUserId.equals(userId)) {
	        return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 403 Forbidden
	    }
		PostDto createPost = this.postService.createPost(postDto, userId, categoryId);
		return new ResponseEntity<PostDto>(createPost, HttpStatus.CREATED);
	}

	// get by user

	@GetMapping("/user/{userId}/posts")
	public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable Integer userId) {

		List<PostDto> posts = this.postService.getPostsByUser(userId);
		return new ResponseEntity<List<PostDto>>(posts, HttpStatus.OK);

	}

	// get by category
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/category/{categoryId}/posts")
	public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable Integer categoryId) {

		List<PostDto> posts = this.postService.getPostsByCategory(categoryId);
		return new ResponseEntity<List<PostDto>>(posts, HttpStatus.OK);

	}
//	get Posts By userFaculty
	@PreAuthorize("hasRole('ADMIN') or hasRole('SUBSCRIBED')")
	 @GetMapping("posts/user/{userId}")
	    public ResponseEntity<List<PostDto>> getPostsByUserFaculty(@PathVariable Integer userId) {
	        List<PostDto> posts = this.postService.getPostsByUserFaculty(userId);
	        return new ResponseEntity<>(posts, HttpStatus.OK);
	    }
	

	// get all posts
	//@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/posts")
	public ResponseEntity<PostResponse> getAllPost(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {

		PostResponse postResponse = this.postService.getAllPost(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<PostResponse>(postResponse, HttpStatus.OK);
	}

	// get post details by id
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId) {

		PostDto postDto = this.postService.getPostById(postId);
		return new ResponseEntity<PostDto>(postDto, HttpStatus.OK);

	}

	// delete post
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/posts/{postId}")
	public ApiResponse deletePost(@PathVariable Integer postId) {
		this.postService.deletePost(postId);
		return new ApiResponse("Post is successfully deleted !!", true);
	}

	// update post
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/posts/{postId}")
	public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable Integer postId) {

		PostDto updatePost = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);

	}

	// search
	@GetMapping("/posts/search/{keywords}")
	public ResponseEntity<List<PostDto>> searchPostByTitle(@PathVariable("keywords") String keywords) {
		List<PostDto> result = this.postService.searchPosts(keywords);
		return new ResponseEntity<List<PostDto>>(result, HttpStatus.OK);
	}

	// post image upload
	//@PreAuthorize("hasRole('ADMIN')")
//	@PostMapping("/post/file/upload/{postId}")
//	public ResponseEntity<PostDto> uploadPostImage(@RequestParam("image") MultipartFile image,
//			@PathVariable Integer postId) throws IOException {
//
//		PostDto postDto = this.postService.getPostById(postId);
//		
//		String fileName = this.fileService.uploadImage(path, image);
//		postDto.setImageName(fileName);
//		PostDto updatePost = this.postService.updatePost(postDto, postId);
//		return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);
//
//	}
//	
	// Post method for file upload
    @PostMapping("/post/file/upload/{postId}")
    public ResponseEntity<PostDto> uploadExamFile(@RequestParam("file") MultipartFile file,
                                                  @PathVariable Integer postId) throws IOException {
        PostDto postDto = this.postService.getPostById(postId);
        String fileName = this.fileService.uploadFile(path, file);
        postDto.setImageName(fileName);  // Assuming you want to set the uploaded file name as imageName
        PostDto updatedPost = this.postService.updatePost(postDto, postId);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    //method to serve files
    @GetMapping(value = "/post/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
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
