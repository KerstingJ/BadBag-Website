package design.badbag.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

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

	public static String uploadImage(MultipartFile imageFile, int parentID) {
		
		/*
		 *  uploads image to directory declared by environment variable
		 *  
		 *  takes a spring MultipartFile and the int uid of the parent element it belongs to
		 *  creates unique image name from parent id and image name
		 *  
		 *  returns a String with new file name if upload was a success
		 *  
		 *  otherwise returns a String starting with "Error: " followed by a short indication of what happened
		 *  
		 */
		
		if (!imageFile.isEmpty() && imageFile.getContentType().contains("image")) {

			System.out.println(imageFile.getContentType());

			String newFileName = parentID + imageFile.getOriginalFilename();
			String filePath = "src/main/resources/static/uploads/";

			try {
				byte[] bytes = imageFile.getBytes();

				// TODO: this should't save to my local directory
				BufferedOutputStream buffStream = new BufferedOutputStream(
						new FileOutputStream(new File(filePath + newFileName)));

				buffStream.write(bytes);
				buffStream.close();

				System.out.println("Image Saved Successfully as " + newFileName);
				return newFileName;

			} catch (IOException e) {

				e.printStackTrace();
				
				return "Error: Couldn't save image";
			}

		} else {

			return "Error: Couldn't find iamge";

		}

	}

}
