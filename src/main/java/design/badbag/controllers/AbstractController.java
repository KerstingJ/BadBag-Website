package design.badbag.controllers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import design.badbag.models.SiteUser;
import design.badbag.models.dao.CartItemDao;
import design.badbag.models.dao.ItemDao;
import design.badbag.models.dao.PostDao;
import design.badbag.models.dao.SiteUserDao;

public abstract class AbstractController {

	@Autowired
	protected SiteUserDao siteUserDao;

	@Autowired
	protected PostDao postDao;

	@Autowired
	protected ItemDao itemDao;

	@Autowired
	protected CartItemDao cartItemDao;

	public static final String userSessionKey = "user_id";
	
	private static String AWS_ID = System.getenv("AWS_ID");
	private static String AWS_KEY = System.getenv("AWS_KEY");
	private static String AWS_BUCKET = System.getenv("AWS_BUCKET");

	protected boolean isLoggedIn(HttpSession session) {

		Integer userId = (Integer) session.getAttribute(userSessionKey);
		return userId == null ? false : true;

	}

	protected SiteUser getUserFromSession(HttpSession session) {

		Integer userId = (Integer) session.getAttribute(userSessionKey);
		return userId == null ? null : siteUserDao.findByUid(userId);

	}

	protected void setUserInSession(HttpSession session, SiteUser siteUser) {
		session.setAttribute(userSessionKey, siteUser.getUid());
	}

	public static boolean exists(String value) {
		/*
		 * Static method to ensure Strings are not null or made of spaces
		 */

		if (value.equals("") || value == null) {
			return false;
		}

		Pattern validString = Pattern.compile("^ *$");
		Matcher matcher = validString.matcher(value);

		return !matcher.matches();
	}

	protected static String uploadImage(MultipartFile imageFile, int parentID) {
		
		/*
		 *  uploads image to s3 bucket set by env variable
		 *  
		 *  takes a spring MultipartFile and the int uid of the parent element it belongs to
		 *  creates unique image name from parent id and image name
		 *  
		 *  returns a String with public url to file if upload was a success
		 *  
		 *  otherwise returns a String starting with "Error: " followed by a short indication of what happened
		 *  
		 */
		
		//TODO: update this so it doesn't return error messages as a string
		
		if (!imageFile.isEmpty() && imageFile.getContentType().contains("image")) {

			System.out.println(imageFile.getContentType());
			
			String newFileName = parentID + imageFile.getOriginalFilename();
			
			//set my credentials
			AWSCredentials awsCredentials = new BasicAWSCredentials(AWS_ID, AWS_KEY);
			
			//set up client
			AmazonS3Client s3Client = new AmazonS3Client(awsCredentials);
	

			try {
				byte[] bytes = imageFile.getBytes();
				InputStream stream = new ByteArrayInputStream(bytes);
				
				//set object meta data
				ObjectMetadata metadata = new ObjectMetadata();
				metadata.setContentLength(imageFile.getSize());
				metadata.setContentType(imageFile.getContentType());
				
				//add image to s3 with public read permissions
				s3Client.putObject(new PutObjectRequest(
						AWS_BUCKET, newFileName, stream, metadata)
						.withCannedAcl(CannedAccessControlList.PublicRead));
				
				stream.close();

				System.out.println("Image Saved Successfully as " + newFileName);
				
				return s3Client.getResourceUrl(AWS_BUCKET, newFileName);

			} catch (Exception e) {

				e.printStackTrace();
				
				return "Error: Couldn't save image";
			}

		} else {

			return "Error: Couldn't find iamge";

		}

	}

}
