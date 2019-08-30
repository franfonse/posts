package com.franfonse.app.controller;

import java.sql.Date;
import java.util.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import com.franfonse.app.model.DaoPost;
import com.franfonse.app.model.DaoUser;
import com.franfonse.app.model.Post;
import com.franfonse.app.model.User;

@SessionAttributes("iduser")
@Controller
public class MainController {
	
	
	//Daos
	
	@Autowired
	private PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	DaoUser daoUser;
	
	@Autowired
	DaoPost daoPost;
	
	
	// Mappings
	
	@GetMapping ("/")
	public String home() {
		
		return "homepage";
		
	}
		
	
	@GetMapping ("/login")
	public String loginUser(Model model, ModelMap model2, @RequestParam String username, @RequestParam String password) {
		
		User user = daoUser.findByUsername(username);
			
		if (user == null || !passwordEncoder().matches(password, user.getPassword())) { 
			
			// Username or password invalid
			
			model.addAttribute("failLogIn", "Failed to log-in. Please, try again.");
			
			return "homepage";
				
		} else {
			
			// Log in succeeded!
			
			model2.put("iduser", user.getIduser());
			model.addAttribute("posts", daoPost.findAll());
			
			return "posts";
		}
	}
	
	@GetMapping ("/goRegister")
	public String registerUser() {
		
		return "newuser";
	}
	
	@GetMapping ("/goHome")
	public String goBackHome() {
		
		return "homepage";
	}
	
	@GetMapping ("/goPosts")
	public String goToPosts(Model model, @SessionAttribute("iduser") long iduser) {
		
		model.addAttribute("hi", "Hi, " + daoUser.findById(iduser).get().getUsername() + "! :)");
		
		model.addAttribute("posts", daoPost.findAll());
		
		return "posts";
	}
	
	
	@PostMapping ("/createUser")
	public String newUser(Model model, @RequestParam String username, @RequestParam String password, 
			@RequestParam String email) {
		
		Date date = new Date(Calendar.getInstance().getTimeInMillis());
		User user = new User(username, passwordEncoder().encode(password), email, date);
		
		model.addAttribute("user", user);
		
		
		if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null) {
			
			// Paramenters are required.
			
			return "newuser";
			
		} else if (daoUser.findByUsername(user.getUsername()) != null) {
			
			// Username already taken.
			
			return "newuser";
			
		} else {
			
			// User saved in database.
			
			daoUser.save(user);
			
			return "homepage";
			
		}
		
	}
	
	@GetMapping ("/createPost")
	public String goCreatePost(Model model, @SessionAttribute("iduser") long iduser) {
		
		User loggedUser = daoUser.findById(iduser).get();
		
		model.addAttribute("loggedUser", loggedUser);
		
		return "newpost";
		
	}
	
	@PostMapping ("/post")
	public String savePost(Model model, @SessionAttribute("iduser") long iduser, @RequestParam String comment) {
		
		User loggedUser = daoUser.findById(iduser).get();
		
		if (comment == null || comment == "") {
			
			//Must containt text to post.
			
			return "newpost";
			
		} else {
			
			// Save post
			
			Date date = new Date(Calendar.getInstance().getTimeInMillis());
			Post post = new Post(loggedUser, date, comment);
			daoPost.save(post);
			
			model.addAttribute("posts", daoPost.findAll());
			
			return "posts";
			
		}
		
	}
	
	@GetMapping ("/logOff")
	public String loggingOff(SessionStatus status) {
		
		status.setComplete();
		
		return "homepage";
		
	}

}