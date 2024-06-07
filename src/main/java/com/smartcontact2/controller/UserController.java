package com.smartcontact2.controller;

//import org.springframework.security.access.prepost.PreAuthorize;
import com.smartcontact2.dao.ContactRepository;
import com.smartcontact2.dao.UserRepository;
import com.smartcontact2.entities.Contact;
import com.smartcontact2.entities.User;
import com.smartcontact2.helper.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import services.SessionHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //method for adding common data
    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userName = principal.getName();

        User user = userRepository.getUserByUserName(userName);
        model.addAttribute("user", user);
    }

    @RequestMapping("/index")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String dashboard(Model model, Principal principal) {
//        String userName = principal.getName();
//
//        User user = userRepository.getUserByUserName(userName);
//        model.addAttribute("user",user);

        //get the user using username
        model.addAttribute("title", "User DashBoard");
        String s = "normal/user_dashboard";
        return s;
    }

    //open add form handler
    @GetMapping("/add-contact")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String openAddContatForm(Model model) {
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());
        return "normal/add_contact_form";
    }

    //processing add contact form
    @PostMapping("/process-contact")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String processContact(@Valid @ModelAttribute Contact contact, BindingResult result, Model model, @RequestParam("profileImage") MultipartFile file, Principal principal, HttpSession session) {
        try {
            SessionHelper sh = new SessionHelper();
            if (result.hasErrors()) {
                model.addAttribute("contact", contact);
                return "normal/add_contact_form";
            } else {
                String name = principal.getName();
                User user = this.userRepository.getUserByUserName(name);
                user.getContacts().add(contact);
                contact.setUser(user);

                //processing and uploading file
                if (file.isEmpty()) {
                    //if the file is empty then try our message
                    contact.setImage("contact.jpg");
                } else {
                    //file the file to folder and update the name to contact

                    LocalTime currentTime = LocalTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH,mm,ss");
                    String formattedTime = currentTime.format(formatter);
                    contact.setImage(formattedTime + file.getOriginalFilename());
                    File saveFile = new ClassPathResource("static/img").getFile();
                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + formattedTime + file.getOriginalFilename());
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                }

                this.userRepository.save(user);

                //success message
                session.setAttribute("message",new Message("Your Contact is Added!!"," alert-success "));
//                model.addAttribute("successes", "Your Contact is Added!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message",new Message("Something went wrong!! Try again.."," alert-danger "));
//            model.addAttribute("errors", "Something went wrong!! Try again..");
        }
        return "normal/add_contact_form";
    }

    //show Contacts Handler
    //per page 5 contact
    //current page = 0[page
    @GetMapping("/show-contacts/{page}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String showContact(@PathVariable("page") Integer page, Model m, Principal principal) {
        m.addAttribute("title", "Show Contacts");
        //to pass contact list

//        String userName = principal.getName();
//
//        User user = this.userRepository.getUserByUserName(userName);
//        List<Contact> contacts = user.getContacts();

        String userName = principal.getName();
        User user = this.userRepository.getUserByUserName(userName);

        //currentPage-page
        //contact Per-page -5
        Pageable pageable = PageRequest.of(page, 8);

        Page<Contact> contacts = this.contactRepository.findContactByUser(user.getId(), pageable);

        m.addAttribute("contacts", contacts);
        m.addAttribute("currentPage", page);
        m.addAttribute("totalPages", contacts.getTotalPages());

        return "normal/show_contacts";
    }

    //showing particuler contact details
    @GetMapping("/{cid}/contact")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String showContactDetail(@PathVariable("cid") Integer cid, Model model, Principal principal) {

        try {
            Optional<Contact> contactOptional = this.contactRepository.findById(cid);

            if (contactOptional != null) {
                Contact contact = contactOptional.get();
                String userName = principal.getName();
                User user = this.userRepository.getUserByUserName(userName);
                //this is to provide security no other user can see other users contacts
                if (user.getId() == contact.getUser().getId()) {
                    model.addAttribute("contact", contact);
                    model.addAttribute("title", contact.getName());
                }
            } else {
                model.addAttribute("emptyy", "The user with this contact does not exist");
                return "normal/contact_detail";

            }
        } catch (Exception e) {
            model.addAttribute("error", "Something Went Wrong!!!");
            return "normal/contact_detail";

        }
        return "normal/contact_detail";
    }

    //delete contact handler
    @GetMapping("/delete/{cid}")
    public String deleteContact(@PathVariable("cid") Integer cId, Principal principal, Model model, RedirectAttributes redirectAttributes,HttpSession session){

        try{
            Optional<Contact> contactOptional = this.contactRepository.findById(cId);
            if (contactOptional.isPresent()) {
                Contact contact = contactOptional.get();
                String userName = principal.getName();
                User user = this.userRepository.getUserByUserName(userName);
                if (user.getId() == contact.getUser().getId()) {
                    contact.setUser(null);
                    //remove photo
                    deleteImage(contact.getImage());
                    this.contactRepository.delete(contact);
//                    model.addAttribute("deleted"," Contact Successfully Deleted!!");
                    session.setAttribute("message",new Message("Contact Successfully Deleted!!"," alert-success "));


                }
                else {
                    session.setAttribute("message",new Message("Error While Deleting Contact!!"," alert-danger "));

//                    model.addAttribute("undeleted","Error While Deleting Contact!!");
                }
            }else {
                model.addAttribute("undeleted", "Contact not found!!");
            }

        }catch (Exception e){
            model.addAttribute("undeleted","Error While Deleting Contact!!");
        }
        return "redirect:/user/show-contacts/0";
//        return "normal/show_contacts/0";
    }

    //open update form handler
    @PostMapping("/update-contact/{cid}")
    public String updateForm(@PathVariable("cid") Integer cid, Model m){

        m.addAttribute("title","Update Contact");
        Contact contact = this.contactRepository.findById(cid).get();
        m.addAttribute("contact",contact);
        return "normal/update_form";
    }

    //update contact handler
    @PostMapping("/process-update")
    public String update(@ModelAttribute Contact contact,@RequestParam("profileImage") MultipartFile file,Model m,HttpSession session,Principal principal,@RequestParam("cid") Integer cid){
        try {
            contact.setcId(cid);
            //old contact details
            Contact oldContactDetail = this.contactRepository.findById(contact.getcId()).get();

            //image
            if(!file.isEmpty()){
                //file work
                //rewrite


                //delete old photo
                deleteImage(oldContactDetail.getImage());


                //update new photo
                File saveFile = new ClassPathResource("static/img").getFile();
                LocalTime currentTime = LocalTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH,mm,ss");
                String formattedTime = currentTime.format(formatter);
                contact.setImage(formattedTime + file.getOriginalFilename());
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + formattedTime + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                contact.setImage(formattedTime + file.getOriginalFilename());
            }else{
                contact.setImage(oldContactDetail.getImage());
            }

                User user = this.userRepository.getUserByUserName(principal.getName());
                contact.setUser(user);
                this.contactRepository.save(contact);
//                m.addAttribute("successes", "Your Contact has been modified!!");
                session.setAttribute("message",new Message("Yous contact has been modified!!!"," alert-success "));


        }catch (Exception e){
            m.addAttribute("error", "Something Went Wrong!!!");
            session.setAttribute("message",new Message("Something Went Wrong!!!"," alert-danger "));


        }
//        System.out.println(contact);
        return "redirect:/user/"+contact.getcId()+"/contact";
    }

    private void deleteImage(String imageName) {
        try {
            File imageFile = new ClassPathResource("static/img/" + imageName).getFile();
            Files.deleteIfExists(imageFile.toPath());
        } catch (IOException e) {
            // Handle exceptions related to file deletion
        }
    }

    //your profile Handler
    @GetMapping("/profile")
    public String yourProfile(Model model){
        model.addAttribute("title","Profile Page");
        return "normal/profile";
    }

    //open setting handler
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/settings")
    public String openSettings(){
        return "normal/settings";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldPassword")String oldPassword,@RequestParam("newPassword")String newPassword,Principal principal,HttpSession session){
        System.out.println(oldPassword);
        System.out.println(newPassword);
        try {
            String userName = principal.getName();
            User currentUser = this.userRepository.getUserByUserName(userName);
            System.out.println(currentUser.getPassword());
            if (this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {
                //change the user's password
                currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
                this.userRepository.save(currentUser);
                session.setAttribute("message", new Message("Successfully Changed Password!!", " alert-success "));
//                session.setAttribute("message", "Your password has been successfully changed!");
//                session.setAttribute("alertType", "alert-success");
            } else {
//            session.setAttribute(,);
                session.setAttribute("message", new Message("Enter correct old password!!", " alert-danger "));
//                session.setAttribute("message", "Enter correct old password!!");
//                session.setAttribute("alertType", "alert-danger");
                return "redirect:/user/settings";
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return "redirect:/user/index";
    }
    @GetMapping("/remove-message-from-session")
    @ResponseBody
    public void removeMessageFromSession(HttpSession session) {
        session.removeAttribute("message");
    }
}






