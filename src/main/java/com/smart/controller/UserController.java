package com.smart.controller;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.UserRepository;
import com.smart.dao.contactRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private contactRepository cR;
	
	@ModelAttribute
	public void addCommonData(Model m,Principal p) {
		
		String userName = p.getName();
		System.out.println(userName);
		
		//get the user using username(email)
		
			
		User user = userRepository.getUserByUserName(userName);
		
		m.addAttribute("user",user);
	}
	
@GetMapping("/index")
	public String dashbord(Model model,Principal p) {
	
	
		
		return "normal/user-dashboard";
	}

	//open add form handler
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {
		model.addAttribute("tittle","Add contact");
		model.addAttribute("contact",new Contact());
		return "normal/add_contact_form";
	}
	
	@PostMapping("/process-contact")
	public String addContactForm(
			@ModelAttribute Contact contact ,
			Model model,
			@RequestParam("profileImage") MultipartFile file ,
			Principal principal) {
		
		try {
		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);
		
		//processing and uploding file
		if(file.isEmpty()) {
			System.out.println("No file avalaible");
			contact.setImage("contact.png");
		}else {
			contact.setImage(file.getOriginalFilename());
			 File saveFile = new ClassPathResource("static/image").getFile();
			 
			 Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			 
			 Files.copy(file.getInputStream(),path , StandardCopyOption.REPLACE_EXISTING);
			 System.out.println("Image is uploaded");
		}
		
		contact.setUser(user);
		user.getContacts().add(contact);
		this.userRepository.save(user);
		
		
		
		 model.addAttribute("contact", new Contact());  // isse form reset ho jayega
		    model.addAttribute("message", "Contact added successfully!");
		    model.addAttribute("messageType","success");
		}catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message","Something Went Wrong");
			model.addAttribute("messageType","error");
		}
		return "normal/add_contact_form";
	}
	
	//show contact handler
	@GetMapping("/show-contact")
	public String ShowContact(Model m,Principal principal) {
		m.addAttribute("tittle","View Contact");
		//contact ki list bhejni hai
		
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		List<Contact> contacts = this.cR.findContactsByUser(user.getId());
		m.addAttribute("contacts",contacts);
		return "normal/show-contact";
		
		
	}
	
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") int cid, Principal principal, Model model) {

	    User user = userRepository.getUserByUserName(principal.getName());
	    Optional<Contact> contactOptional = cR.findById(cid);
	    Contact contact = contactOptional.get();

	    if (user.getId() == contact.getUser().getId()) {
	        cR.delete(contact);
	        model.addAttribute("message", "Contact deleted successfully!");
	        model.addAttribute("messageType", "success");
	    } else {
	        model.addAttribute("message", "Unauthorized access!");
	        model.addAttribute("messageType", "danger");
	    }

	    // yahan direct view return karna hoga, redirect mat karna
	    List<Contact> contacts = this.cR.findContactsByUser(user.getId());
	    model.addAttribute("contacts", contacts);
	    model.addAttribute("tittle", "View Contact");

	    return "normal/show-contact";  // directly view return
	}

//	@PostMapping("/update/{cid}")
//	public String updateContact(@PathVariable("cid") int cid,Principal principal,Model model) {
//		
//		
//		
//		model.addAttribute("tittle","Update Contact");
//		Contact contact = this.cR.findById(cid).get();
//		model.addAttribute("contact",contact);
//		return "normal/updateform";
//	}
	
	//Your profile handler
	@GetMapping("/profile")
	public String yourProfile(Model m) {
		m.addAttribute("tittle"+"Profile Page");
		return "normal/profile";
	}
	
	@GetMapping("/search")
	public String searchContact(@RequestParam("query") String query, Principal principal, Model model) {
	    String userName = principal.getName();
	    User user = this.userRepository.getUserByUserName(userName);
	    
	    List<Contact> contacts = this.cR.findByNameContainingIgnoreCaseAndUser(query, user);
	    
	    System.out.println("CONTACTS FOUND: " + contacts.size());
	    
	    model.addAttribute("contacts", contacts);  
	    model.addAttribute("title", "Search Results");
	    
	    return "normal/show-contact";
	}


}
