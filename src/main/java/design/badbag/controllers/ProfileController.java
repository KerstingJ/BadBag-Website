package design.badbag.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import design.badbag.models.Post;
import design.badbag.models.SiteUser;

/*
 * 
 * Things in this Controller:
 * 	- profile (users)
 * 	- profile (guest)
 * 
 */

@Controller
public class ProfileController extends AbstractController {

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String profileView(HttpServletRequest request, Model model) {
		SiteUser siteUser = getUserFromSession(request.getSession());

		if (siteUser == null) {
			return "redirect:/login";
		}

		return "redirect:profile/" + siteUser.getUsername();
	}

	@RequestMapping(value = "/profile/{username}", method = RequestMethod.GET)
	public String userPosts(@PathVariable String username, HttpServletRequest request, Model model) {
		SiteUser activeUser = getUserFromSession(request.getSession());

		if (activeUser != null) {
			if (activeUser.getUsername().equals(username)) {
				model.addAttribute("editable", true);
			}
		}

		SiteUser author = siteUserDao.findByUsername(username);

		List<Post> posts = postDao.findAllByAuthorOrderByCreatedDesc(author);

		model.addAttribute("user", author);
		model.addAttribute("posts", posts);
		return "profile";
	}

	@RequestMapping(value = "/profile/{username}", method = RequestMethod.POST)
	public String userNewPosts(@PathVariable String username, HttpServletRequest request, Model model) {

		String postBody = request.getParameter("postBody");

		/*
		 * author on get request handler for this uri gets user object info from
		 * different source this might cause confusion in the future although
		 * seems accurate under the assumptions that: ++this will only be called
		 * when making edits to page or making a new post ++only the author of
		 * the blog can make edits or make new posts ++the author will be the
		 * activeUser
		 */
		SiteUser author = getUserFromSession(request.getSession());

		if (request.getParameter("updateBio") != null) {
			author.setBio(request.getParameter("updatedBio"));
			siteUserDao.save(author);
		}

		if (request.getParameter("updateSocial") != null) {
			// this needs to be something for social links
			author.setBio(request.getParameter("updatedSocial"));
			siteUserDao.save(author);
		}

		if (request.getParameter("updatePic") != null) {
			System.out.println("Update Pic!!!");
		}

				
		if (request.getParameter("updatePost") != null) {
			
			try {
				
				Integer upPostUID = Integer.parseInt(request.getParameter("post_ID"));
				Post updatedPost = postDao.findByUid(upPostUID);
				updatedPost.setBody(request.getParameter("updatedPost"));
				postDao.save(updatedPost);
	
			} catch (Exception e) {
				
				// does it really matter
				System.out.println("Something went terribly wrong");
			}
			
		}
		

		if (request.getParameter("newPost") != null) {
			if (author == null) {
				System.out.println("You need to be logged in to make a post");
				return "redirect:/login";
			} else if (!author.equals(siteUserDao.findByUsername(username))) {
				System.out.println("Woah, what are you trying to do here? this isn't yours");
				return "redirect:/profile";
			}

			// if everything checks out lets save this sucka
			if (exists(postBody) && author != null) {

				Post post = new Post(postBody, author);
				postDao.save(post);
				System.out.println("Saved Post: " + postBody);

			}
		}

		return "redirect:/profile";
	}

}
